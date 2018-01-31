package integration;

import interfaces.INode;
import interfaces.IRelationship;
import java.util.List;

import neo4j.Neo4jDriverAdapter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NeoDriverTest {
    private Neo4jDriverAdapter neoDriver;

    private final String firstNode = "test node 1";
    private final String secondNode = "test node 2";
    private final String relation = "connected";

    @Before
    public void setUp(){
        neoDriver = new Neo4jDriverAdapter("bolt://localhost:7687");
    }

    @After
    public void tearDown(){
        neoDriver.runQuery("MATCH (n {name:\"test node 1\"}) "
                + "OPTIONAL MATCH (n)-[r]-() "
                + "DELETE r, n RETURN COUNT(n)");
        neoDriver.runQuery("MATCH (n {name:\"test node 2\"}) "
                + "OPTIONAL MATCH (n)-[r]-() "
                + "DELETE r, n RETURN COUNT(n)");
        neoDriver.close();
    }

    @Test
    public void nodesAreCreatedAndReturned() {
        //act
        INode node1 = neoDriver.createNode(firstNode);
        INode node2 = neoDriver.createNode(secondNode);

        //assert
        assert node1.get("name").equals(firstNode);
        assert node2.get("name").equals(secondNode);
    }

    @Test
    public void getNodeReturnsNode(){
        //arrange
        neoDriver.createNode(firstNode);

        //act
        INode node1 = neoDriver.getNode(firstNode);

        //assert
        assert node1.get("name").equals(firstNode);
    }

    @Test
    public void relationshipsAreCreatedAndReturned(){
        //arrange
        neoDriver.createNode(firstNode);
        neoDriver.createNode(secondNode);

        //act
        IRelationship rel = neoDriver.createRelationship(firstNode, secondNode, relation);

        //assert
        assert rel.type().equals(relation);
        assert rel.hasType(relation);
    }

    @Test
    public void getRelationshipReturnsRelationship(){
        //arrange
        neoDriver.createNode(firstNode);
        neoDriver.createNode(secondNode);
        neoDriver.createRelationship(firstNode, secondNode, relation);

        //act
        List<IRelationship> rel = neoDriver.getRelationshipsBetween(firstNode, secondNode);

        //assert
        assert rel.get(0).hasType(relation);
    }

    @Test
    public void relationshipsAreDeleted(){
        //arrange
        neoDriver.createNode(firstNode);
        neoDriver.createNode(secondNode);
        neoDriver.createRelationship(firstNode, secondNode, relation);

        //act
        int count = neoDriver.deleteRelationship(firstNode, secondNode, relation);

        //assert
        assert count == 1;
    }

    @Test
    public void nodesAreDeleted(){
        //arrange
        neoDriver.createNode(firstNode);

        //act
        int count = neoDriver.deleteNode(firstNode);

        //assert
        assert count == 1;
    }
}
