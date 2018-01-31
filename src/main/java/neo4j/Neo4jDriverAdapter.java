package neo4j;

import interfaces.*;

import java.util.ArrayList;
import java.util.List;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Relationship;

import static org.neo4j.driver.v1.Values.parameters;

public class Neo4jDriverAdapter implements IDriverAdapter {

    private Driver driver;
    private Session session;

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
    public INode getNode(String value) {
        StatementResult res = driver.session()
                .run("MATCH (n {name:{value}}) RETURN n",
                        parameters("value", value));

        if (!res.hasNext()) {
            return null;
        }
        driver.session().close();
        Node n = res.single().get("n").asNode();
        return new Neo4jNode(n);
    }

    @Override
    public INode getNode(long id) {
        StatementResult res = driver.session().run(
                "MATCH (n) WHERE id(n)={id} RETURN n",
                parameters("id", id));

        if (!res.hasNext()) {
            return null;
        }
        driver.session().close();
        Node n = res.single().get(0).asNode();
        return new Neo4jNode(n);
    }

    @Override
    public List<INode> getRelatedNodes(String value) {
        String query = "MATCH (n {name:{value}})--(m) RETURN n,m";
        StatementResult res = driver.session().run(query,
                parameters("value", value));

        List<INode> nodeSet = new ArrayList<>();
        if (!res.hasNext()) {
            return nodeSet;
        }

        nodeSet.add(new Neo4jNode(res.peek().get("n").asNode()));
        while (res.hasNext()) {
            Record record = res.next();
            nodeSet.add(new Neo4jNode(record.get("m").asNode()));
        }
        driver.session().close();
        return nodeSet;
    }

    @Override
    public List<IRelationship> getRelationshipsToNode(String value) {
        String query = "MATCH (n {name:{value}})-[r]-(m) RETURN n,m,r;";
        StatementResult res = driver.session().run(query,
                parameters("value", value));

        List<IRelationship> relations = new ArrayList<>();
        while (res.hasNext()) {
            relations.add(new Neo4jRelationship(res.next().get("r").asRelationship()));
        }
        driver.session().close();
        return relations;
    }

    @Override
    public List<IRelationship> getRelationshipsBetween(String value1, String value2) {
        String query = "MATCH (n {name:{value1}})-[r]-(m {name:{value2}}) RETURN n,m,r;";
        StatementResult res = driver.session().run(query,
                parameters("value1", value1, "value2", value2));

        List<IRelationship> relations = new ArrayList<>();
        while (res.hasNext()) {
            Relationship r = res.next().get("r").asRelationship();
            relations.add(new Neo4jRelationship(r));
        }
        return relations;
    }

    @Override
    public IRelationship getRelationship(long id) {
        StatementResult res = driver.session().run("MATCH ()-[r]-() WHERE id(r)={id} RETURN r",
                parameters("id", id));

        if (!res.hasNext()) {
            return null;
        }
        driver.session().close();
        return new Neo4jRelationship(res.next().get("r").asRelationship());
    }


    @Override
    public List<IPath> checkGeneralizedConnection(String from, String to, String rel) {
        String query = "MATCH p = ({name:{value1}})-"
                + "[:BE*0..7]->()-[:%s]->()<-[:BE*0..7]-({name:{value2}}) RETURN p;";
        query = String.format(query, rel);
        StatementResult res = driver.session().run(query,
                parameters("value1", from, "value2", to));
        List<IPath> paths = new ArrayList<>();

        while (res.hasNext()) {
            Record record = res.next();
            paths.add(new Neo4jPath(record.get("p").asPath()));

        }
        return paths;
    }

    @Override
    public void updateRelationships(String oldRel, String newRel) {
        String query = "MATCH (n)-[r:%s]->(m) CREATE (n)-[r2:%s]->(m) SET r2 = r WITH r DELETE r;";
        driver.session().run(String.format(query, oldRel, newRel));
        driver.session().close();
    }

    @Override
    public Neo4jStatementResult runQuery(String query) {
        System.out.println("Running query:" + query);
        StatementResult res = driver.session().run(query);
        driver.session().close();
        return new Neo4jStatementResult(res);
    }

    @Override
    public Neo4jNode createNode(String value) {
        String query = "CREATE (n {name:{value}}) return n";
        StatementResult res = driver.session().run(query, parameters("value", value));
        driver.session().close();
        return new Neo4jNode(res.single().get(0).asNode());
    }

    @Override
    public Neo4jRelationship createRelationship(String startNode, String endNode, String rel) {
        String query = "MATCH (n {name:{start}}), (m {name:{end}})\n"
                + "CREATE (n)-[r:%s]->(m)\n"
                + "RETURN r";
        query = String.format(query, rel);
        System.out.println(query);
        StatementResult res = driver.session().run(query,
                parameters("start", startNode, "end", endNode));

        driver.session().close();
        return new Neo4jRelationship(res.single().get(0).asRelationship());
    }

    @Override
    public int deleteNode(String value) {
        String query = "MATCH (n {name:{value}}) "
                + "OPTIONAL MATCH (n)-[r]-() "
                + "DELETE r, n RETURN COUNT(n);";
        System.out.println(query);
        StatementResult res = driver.session().run(query,
                parameters("value", value));
        driver.session().close();
        return res.single().get(0).asInt();
    }

    @Override
    public int deleteRelationship(String startNode, String endNode, String rel) {
        String query = "MATCH (n {name:{start}})-[r:%s]->(m {name:{end}}) "
                     + "DELETE r RETURN COUNT(r);";
        query = String.format(query, rel);
        System.out.println(query);
        StatementResult res = driver.session().run(query,
                parameters("start", startNode, "end", endNode));
        driver.session().close();
        System.out.println(res.keys());
        return res.single().get(0).asInt();
    }

    @Override
    public void cleanLabel(String label){
        String query = "MATCH (n:%s) "
                     + "OPTIONAL MATCH (n)-[r]-() "
                     + "DELETE n,r";
        String fullQuery = String.format(query, label);
        System.out.println(fullQuery);
        driver.session().run(fullQuery);
        driver.session().close();
    }
}

