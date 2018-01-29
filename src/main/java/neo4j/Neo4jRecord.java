package neo4j;

import exceptions.UncoercibleTypeException;
import interfaces.INode;
import interfaces.IPath;
import interfaces.IRecord;
import interfaces.IRelationship;

import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.exceptions.value.Uncoercible;

public class Neo4jRecord implements IRecord {
    private Record record;

    Neo4jRecord(Record record) {
        this.record = record;
    }

    @Override
    public INode getNode(String key) throws UncoercibleTypeException {
        try {
            return new Neo4jNode(record.get(key).asNode());
        } catch (Uncoercible e) {
            System.out.println("The retrieved Value is not a Node");
            throw new UncoercibleTypeException("The retrieved Value is not a Node", e.getStackTrace());
        }
    }

    @Override
    public IPath getPath(String key) throws UncoercibleTypeException {
        try {
            return new Neo4jPath(record.get(key).asPath());
        } catch (Uncoercible e) {
            System.out.println("The retrieved Value is not a Path");
            throw new UncoercibleTypeException("The retrieved Value is not a Path", e.getStackTrace());
        }
    }

    @Override
    public IRelationship getRelationship(String key) throws UncoercibleTypeException {
        try {
            return new Neo4jRelationship(record.get(key).asRelationship());
        } catch (Uncoercible e) {
            System.out.println("The retrieved value is not a Relationship");
            throw new UncoercibleTypeException("The retrieved value is not a Relationship", e.getStackTrace());
        }
    }
}
