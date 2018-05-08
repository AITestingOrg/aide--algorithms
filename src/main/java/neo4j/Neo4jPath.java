package neo4j;

import interfaces.INode;
import interfaces.IPath;
import interfaces.IRelationship;

import java.util.ArrayList;
import java.util.List;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Path;
import org.neo4j.driver.v1.types.Relationship;

public class Neo4jPath implements IPath {
    private Path path;

    public Neo4jPath(Path path) {
        this.path = path;
    }

    @Override
    public boolean contains(INode node) {
        return path.contains((Node) node);
    }

    @Override
    public int length() {
        return path.length();
    }

    @Override
    public Iterable<INode> nodes() {
        List<INode> iter = new ArrayList<>();
        for (Node n : path.nodes()) {
            iter.add(new Neo4jNode(n));
        }
        return iter;
    }

    @Override
    public Iterable<IRelationship> relationships() {
        List<IRelationship> iter = new ArrayList<>();
        for (Relationship r : path.relationships()) {
            iter.add(new Neo4jRelationship(r));
        }
        return iter;
    }
}
