package neo4j;

import static org.neo4j.driver.v1.Values.parameters;

import interfaces.*;
import java.util.ArrayList;
import java.util.List;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Relationship;



public class Neo4jDriverAdapter implements IDriverAdapter {

    private Driver driver;
    private final String ID = "id";
    private final String VALUE1 = "value1";
    private final String VALUE2 = "value2";
    private final String FROM = "from";
    private final String TO = "to";
    private final String N = "n";
    private final String M = "m";
    private final String P = "p";
    private final String R = "r";
    
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
        try (Session session = driver.session()) {
            StatementResult res = session.run("MATCH (n {name:{value1}}) RETURN n",
                            parameters(VALUE1, value));

            if (!res.hasNext()) {
                return null;
            }
            Node n = res.single().get(N).asNode();
            return new Neo4jNode(n);
        }
    }

    @Override
    public INode getNode(long id) {
        try (Session session = driver.session()) {
            StatementResult res = session.run("MATCH (n) WHERE id(n)={id} RETURN n",
                    parameters(ID, id));

            if (!res.hasNext()) {
                return null;
            }
            Node n = res.single().get(0).asNode();
            return new Neo4jNode(n);
        }
    }

    @Override
    public List<INode> getRelatedNodes(String value) {
        String query = "MATCH (n {name:{value1}})--(m) RETURN n,m";
        try (Session session = driver.session()) {
            StatementResult res = session.run(query, parameters(VALUE1, value));

            List<INode> nodeSet = new ArrayList<>();
            if (!res.hasNext()) {
                return nodeSet;
            }

            nodeSet.add(new Neo4jNode(res.peek().get(N).asNode()));
            while (res.hasNext()) {
                Record record = res.next();
                nodeSet.add(new Neo4jNode(record.get(M).asNode()));
            }
            return nodeSet;
        }
    }

    @Override
    public List<IRelationship> getRelationshipsToNode(String value) {
        String query = "MATCH (n {name:{value1}})-[r]-(m) RETURN n,m,r;";

        try (Session session = driver.session()) {
            StatementResult res = session.run(query, parameters(VALUE1, value));

            List<IRelationship> relations = new ArrayList<>();
            while (res.hasNext()) {
                relations.add(new Neo4jRelationship(res.next().get(R).asRelationship()));
            }
            return relations;
        }
    }

    @Override
    public List<IRelationship> getRelationshipsBetween(String value1, String value2) {
        String query = "MATCH (n {name:{value1}})-[r]-(m {name:{value2}}) RETURN n,m,r;";

        try (Session session = driver.session()) {
            StatementResult res = session.run(query,
                    parameters(VALUE1, value1, VALUE2, value2));


            List<IRelationship> relations = new ArrayList<>();
            while (res.hasNext()) {
                Relationship r = res.next().get(R).asRelationship();
                relations.add(new Neo4jRelationship(r));
            }
            return relations;
        }
    }

    @Override
    public IRelationship getRelationship(long id) {

        try (Session session = driver.session()) {
            StatementResult res = session.run("MATCH ()-[r]-() WHERE id(r)={id} RETURN r",
                    parameters(ID, id));

            if (!res.hasNext()) {
                return null;
            }
            return new Neo4jRelationship(res.next().get(R).asRelationship());
        }
    }

    /* This method will generalize the node entities by traversing "BE" relationships up until it
     finds the desired relationship in between two of the generalized concepts.
     */
    @Override
    public List<IPath> checkGeneralizedConnection(String from, String to, String rel) {
        String query = "MATCH p = ({name:{value1}})-"
                + "[:BE*0..7]->()-[:%s]->()<-[:BE*0..7]-({name:{value2}}) RETURN p;";
        query = String.format(query, rel);

        try (Session session = driver.session()) {
            StatementResult res = session.run(query,
                    parameters(VALUE1, from, VALUE2, to));
            List<IPath> paths = new ArrayList<>();

            while (res.hasNext()) {
                Record record = res.next();
                paths.add(new Neo4jPath(record.get(P).asPath()));

            }
            return paths;
        }
    }

    @Override
    public void updateRelationships(String oldRelationship, String newRelationship) {
        String query = "MATCH (n)-[r:%s]->(m) CREATE (n)-[r2:%s]->(m) SET r2 = r WITH r DELETE r;";

        try (Session session = driver.session()) {
            session.run(String.format(query, oldRelationship, newRelationship));
        }
    }

    @Override
    public Neo4jStatementResult runQuery(String query) {
        try (Session session = driver.session()) {
            StatementResult res = session.run(query);
            return new Neo4jStatementResult(res);
        }
    }

    @Override
    public Neo4jNode createNode(String value) {
        String query = "CREATE (n {name:{value1}}) return n";

        try (Session session = driver.session()) {
            StatementResult res = session.run(query, parameters(VALUE1, value));
            return new Neo4jNode(res.single().get(0).asNode());
        }
    }

    @Override
    public Neo4jRelationship createRelationship(String fromNode, String toNode, String rel) {
        String query = "MATCH (n {name:{from}}), (m {name:{to}})\n"
                + "CREATE (n)-[r:%s]->(m)\n"
                + "RETURN r";
        query = String.format(query, rel);

        try (Session session = driver.session()) {
            StatementResult res = session.run(query,
                    parameters(FROM, fromNode, TO, toNode));

            return new Neo4jRelationship(res.single().get(0).asRelationship());
        }
    }

    @Override
    public int deleteNode(String value) {
        String query = "MATCH (n {name:{value1}}) "
                + "OPTIONAL MATCH (n)-[r]-() "
                + "DELETE r, n RETURN COUNT(n);";

        try (Session session = driver.session()) {
            StatementResult res = session.run(query,
                    parameters(VALUE1, value));
            return res.single().get(0).asInt();
        }
    }

    @Override
    public int deleteRelationship(String fromNode, String toNode, String rel) {
        String query = "MATCH (n {name:{from}})-[r:%s]->(m {name:{to}}) "
                     + "DELETE r RETURN COUNT(r);";
        query = String.format(query, rel);

        try (Session session = driver.session()) {
            StatementResult res = session.run(query,
                    parameters(FROM, fromNode, TO, toNode));
            return res.single().get(0).asInt();
        }
    }

    @Override
    public void cleanLabel(String label){
        String query = "MATCH (n:%s) "
                     + "OPTIONAL MATCH (n)-[r]-() "
                     + "DELETE n,r";
        String fullQuery = String.format(query, label);

        try (Session session = driver.session()) {
            session.run(fullQuery);
        }
    }
}

