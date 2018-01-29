package neo4j;

import interfaces.IRelationship;
import org.neo4j.driver.v1.types.Relationship;

public class Neo4jRelationship implements IRelationship {

    private Relationship relationship;

    Neo4jRelationship(Relationship relationship) {
        this.relationship = relationship;
    }

    @Override
    public long id() {
        return relationship.id();
    }

    @Override
    public long startNodeId() {
        return relationship.startNodeId();
    }

    @Override
    public long endNodeId() {
        return relationship.endNodeId();
    }

    @Override
    public String type() {
        return relationship.type();
    }

    @Override
    public boolean hasType(String type) {
        return relationship.hasType(type);
    }

    @Override
    public String toString() {
        return startNodeId() + "-" + type() + "->" + endNodeId();
    }
}
