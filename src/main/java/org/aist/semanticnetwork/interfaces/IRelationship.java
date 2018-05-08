package org.aist.semanticnetwork.interfaces;

public interface IRelationship {

    /**
     * Gets the id for this Relationship.
     *
     * @return The {@link Long} id value of the Relationship.
     **/
    long id();

    /**
     * Gets the id for the starting Node of this Relationship.
     *
     * @return The {@link Long} id value of the start node of the Relationship.
     **/
    long startNodeId();

    /**
     * Gets the id for the ending Node of this Relationship.
     *
     * @return The {@link Long} id value of the end node of the Relationship.
     **/
    long endNodeId();

    /**
     * Gets the type of the Relationship.
     *
     * @return The {@link String} value of the type of the Relationship.
     **/
    String type();


    /**
     * Checks if the type of the Relationship is a given type.
     *
     * @param type The type to check against.
     * @return True if the types are the same.
     **/
    boolean hasType(String type);
}
