package org.aist.semanticnetwork.incremental.learning;

public interface IGeneralizationStrategy {

    /**
     * Uses a generalization strategy to check for posible connections between
     * two Nodes that contain a given relationship. Order of the nodes determines
     * the order of the relationship.
     *
     * @param node1Value the value of the first node.
     * @param node2Value the value of the second node.
     *
     * @return The {@link String} value of the type of the Relationship.
     **/
    boolean findIfRelationExists(String node1Value, String node2Value, String rel);

    /**
     * Closes the driver connection.
     **/
    void close();
}
