package integration;

import interfaces.INode;
import interfaces.IRelationship;
import neo4j.Neo4jDriverAdapter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class NeoDriverTest {
    private final String firstNode = "testnode1";
    private final String secondNode = "testnode2";
    private final String relation = "connected";
    private Neo4jDriverAdapter neoDriver;

    @Before
    public void setUp() {
        neoDriver = new Neo4jDriverAdapter("bolt://localhost:7687");
        neoDriver.runQuery("match (n) detach delete n");
    }

    @After
    public void tearDown() {
        neoDriver.runQuery("match (n) detach delete n");
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
    public void getNodeReturnsNode() {
        //arrange
        neoDriver.createNode(firstNode);

        //act
        INode node1 = neoDriver.getNode(firstNode);

        //assert
        assert node1.get("name").equals(firstNode);
    }

    @Test
    public void relationshipsAreCreatedAndReturned() {
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
    public void getRelationshipReturnsRelationship() {
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
    public void relationshipsAreDeleted() {
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
    public void nodesAreDeleted() {
        //arrange
        neoDriver.createNode(firstNode);

        //act
        int count = neoDriver.deleteNode(firstNode);

        //assert
        assert count == 1;
    }
}
