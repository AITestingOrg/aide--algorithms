package neo4j;

import interfaces.*;

import java.util.ArrayList;
import java.util.List;
import org.neo4j.driver.v1.*;

public class Neo4jDriverAdapter implements IDriverAdapter {
    private final Driver driver;

    public Neo4jDriverAdapter(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    public Neo4jDriverAdapter(String uri) {
        driver = GraphDatabase.driver(uri);
    }

    @Override
    public void close() {
        driver.close();
    }

    @Override
    public INode getNode(String key, String value) {
        StatementResult res = driver.session().beginTransaction()
                .run("MATCH (n {" + key + ":\"" + value + "\"})RETURN n");

        if (!res.hasNext()) {
            return null;
        }
        return new Neo4jNode(res.single().get(0).asNode());
    }

    @Override
    public INode getNode(long id) {
        String query = "MATCH (n) WHERE id(n)= %s RETURN n";
        StatementResult res = driver.session().beginTransaction().run(String.format(query, id));

        if (!res.hasNext()) {
            return null;
        }
        return new Neo4jNode(res.single().get(0).asNode());
    }

    @Override
    public List<INode> getRelatedNodes(String key, String value) {
        String query = "MATCH (n {%s:\"%s\"})--(m) RETURN n,m";
        String fullQuery = String.format(query, key, value);
        System.out.println("Getting related nodes:\n" + fullQuery);
        StatementResult res = driver.session().beginTransaction().run(fullQuery);

        List<INode> nodeSet = new ArrayList<>();
        if (!res.hasNext()) {
            return nodeSet;
        }

        nodeSet.add(new Neo4jNode(res.peek().get("n").asNode()));
        while (res.hasNext()) {
            Record record = res.next();
            nodeSet.add(new Neo4jNode(record.get("m").asNode()));
        }
        return nodeSet;
    }

    @Override
    public List<IRelationship> getRelationshipsToNode(String key, String value) {
        String query = "MATCH (n {%s:\"%s\"})-[r]-(m) RETURN n,m,r;";
        String fullQuery = String.format(query, key, value);
        System.out.println("Getting relationship:\n" + fullQuery);
        StatementResult res = driver.session().beginTransaction().run(fullQuery);

        List<IRelationship> relations = new ArrayList<>();
        while (res.hasNext()) {
            relations.add(new Neo4jRelationship(res.next().get("r").asRelationship()));
        }
        return relations;
    }

    @Override
    public List<IRelationship> getRelationshipsBetween(
        String key1, String value1, String key2, String value2) {
        String query = "MATCH (n {%s:\"%s\"})-[r]-(m {%s:\"%s\" }) RETURN n,m,r;";
        String fullQuery = String.format(query, key1, value1, key2, value2);
        System.out.println("Getting relationships:\n" + fullQuery);
        StatementResult res = driver.session().beginTransaction().run(fullQuery);

        List<IRelationship> relations = new ArrayList<>();
        while (res.hasNext()) {
            relations.add(new Neo4jRelationship(res.next().get("r").asRelationship()));
        }
        return relations;
    }

    @Override
    public List<IPath> checkGeneralizedConnection(
        String key1, String value1, String key2, String value2, String rel) {
        String query = "MATCH p = ({%s:\"%s\"})-"
                + "[:BE*0..7]->()-[:%s]->()<-[:BE*0..7]-({%s:\"%s\"}) RETURN p;";
        System.out.println(String.format(query, key1, value1, rel, key2, value2));
        StatementResult res = driver.session().beginTransaction().run(
                String.format(query, key1, value1, rel, key2, value2));
        List<IPath> paths = new ArrayList<>();
        System.out.println(res);

        while (res.hasNext()) {
            Record record = res.next();
            paths.add(new Neo4jPath(record.get("p").asPath()));

        }
        return paths;
    }

    @Override
    public void updateRelationships(String oldRel, String newRel) {
        String query = "MATCH (n)-[r:%s]->(m) CREATE (n)-[r2:%s]->(m) SET r2 = r WITH r DELETE r;";
        driver.session().beginTransaction().run(String.format(query, oldRel, newRel));
    }

    @Override
    public int deleteRelationship(
        String key1, String value1, String key2, String value2, String rel) {
        String query = "MATCH (n {%s:\"%s\"})-[r:%s]->(m {%s:\"%s\" }) DELETE r RETURN COUNT(r);";
        String fullQuery = String.format(query, key1, value1, rel, key2, value2);
        System.out.println("Deleting relationship\n" + fullQuery);
        StatementResult res = driver.session().beginTransaction().run(fullQuery);
        return res.single().get(0).asInt();
    }

    @Override
    public IStatementResult runQuery(String query) {
        StatementResult res = driver.session().beginTransaction().run(query);
        return new Neo4jStatementResult(res);
    }

    @Override
    public void createRelationship(
        String key1, String value1, String key2, String value2, String rel) {
        String query = "MATCH (n {%s:\"%s\"}), (m {%s:\"%s\"})\n"
                + "CREATE (n)-[:%s]->(m)";
        String fullQuery = String.format(query, key1, value1, key2, value2, rel);
        System.out.println("Creating new relationship\n" + fullQuery);
        driver.session().beginTransaction().run(fullQuery);
    }
}

