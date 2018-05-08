package org.aist.semanticnetwork.neo4j;

import org.aist.semanticnetwork.interfaces.INode;
import org.neo4j.driver.v1.types.Node;

public class Neo4jNode implements INode {
    private Node node;

    Neo4jNode(Node n) {
        node = n;
    }

    @Override
    public Iterable<String> labels() {
        return node.labels();
    }

    @Override
    public Iterable<String> keys() {
        return node.keys();
    }

    @Override
    public String get(String key) {
        return node.get(key).asString();
    }

    @Override
    public long id() {
        return node.id();
    }

    @Override
    public String toString() {
        return node.asMap().toString();
    }
}
