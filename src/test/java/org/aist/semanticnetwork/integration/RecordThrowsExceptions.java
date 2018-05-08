package integration;

import exceptions.UncoercibleTypeException;
import neo4j.Neo4jDriverAdapter;
import neo4j.Neo4jRecord;
import neo4j.Neo4jStatementResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RecordThrowsExceptions {
    private Neo4jDriverAdapter neoDriver;

    private final String firstNode = "test node 1";
    private final String secondNode = "test node 2";
    private final String relation = "connected";

    @Before
    public void setUp(){
        neoDriver = new Neo4jDriverAdapter("bolt://localhost:7687");
        neoDriver.runQuery("MATCH (n) DETACH DELETE n");
    }

    @After
    public void tearDown(){
        neoDriver.runQuery("MATCH (n) DETACH DELETE n");
        neoDriver.close();
    }

    @Test(expected = UncoercibleTypeException.class)
    public void wrongTypePathRetrievalThrowsUncoercibleException() throws UncoercibleTypeException {
        //arrange
        neoDriver.createNode(firstNode);

        //act
        Neo4jStatementResult stat = neoDriver.runQuery("MATCH (n) RETURN n");
        Neo4jRecord rec = stat.next();
        rec.getPath("n");
    }

    @Test(expected = UncoercibleTypeException.class)
    public void wrongTypeRelationshipRetrievalThrowsUncoercibleException() throws UncoercibleTypeException {
        //arrange
        neoDriver.createNode(firstNode);

        //act
        Neo4jStatementResult stat = neoDriver.runQuery("MATCH (n) RETURN n");
        Neo4jRecord rec = stat.next();
        rec.getRelationship("n");
    }

    @Test(expected = UncoercibleTypeException.class)
    public void wrongTypeNodeRetrievalThrowsUncoercibleException() throws UncoercibleTypeException {
        //arrange
        neoDriver.createNode(firstNode);
        neoDriver.createNode(secondNode);
        neoDriver.createRelationship(firstNode, secondNode, relation);

        //act
        Neo4jStatementResult stat = neoDriver.runQuery("MATCH ()-[r]-() RETURN r");
        Neo4jRecord rec = stat.next();
        rec.getNode("r");
    }

}
