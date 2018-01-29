package neo4j;

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
    public INode getNode(String key) throws Uncoercible {
        try {
            return new Neo4jNode(record.get(key).asNode());
        } catch (Uncoercible e) {
            System.out.println("The retrieved Value is not a Path");
            return null;
        }
    }

    @Override
    public IPath getPath(String key) throws Uncoercible {
        try {
            return new Neo4jPath(record.get(key).asPath());
        } catch (Uncoercible e) {
            System.out.println("The retrieved Value is not a Path");
            return null;
        }
    }

    @Override
    public IRelationship getRelationship(String key) throws Uncoercible {
        try {
            return new Neo4jRelationship(record.get(key).asRelationship());
        } catch (Uncoercible e) {
            System.out.println("The retrieved Value is not a Path");
            return null;
        }
    }
}
