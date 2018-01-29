package aist.incremental.learning;

public interface IGeneralizationStrategy {

    /**
     * Uses a generalization strategy to check for posible connections between
     * two Nodes that contain a given relationship. Order of the nodes determines
     * the order of the relationship.
     *
     * @return The {@link String} value of the type of the Relationship.
     **/
    boolean findIfRelationExists(String key1, String value1, String key2, String value2, String rel);

    /**
     * Closes the driver connection.
     **/
    void close();
}
