package neo4j;

import interfaces.INode;
import interfaces.IPath;
import interfaces.IRecord;
import interfaces.IRelationship;

import org.neo4j.driver.v1.Record;

public class Neo4jRecord implements IRecord {
    private Record record;

    public Neo4jRecord(Record record) {
        this.record = record;
    }

    public INode getNode(String key) {
        return new Neo4jNode(record.get(key).asNode());
    }

    public IPath getPath(String key) {
        return new Neo4jPath(record.get(key).asPath());
    }

    public IRelationship getRelationship(String key) {
        return new Neo4jRelationship(record.get(key).asRelationship());
    }
}
