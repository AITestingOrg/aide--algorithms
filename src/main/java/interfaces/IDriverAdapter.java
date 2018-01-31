package interfaces;

import java.util.List;

public interface IDriverAdapter {

    /**
     * Closes the current driver connection.
     **/
    void close();

    /**
     * Gets an {@link INode} based on its value.
     *
     * @param value The value of assigned to the desired node.
     * @return The {@link INode} retrieved.
     **/
    INode getNode(String value);

    /**
     * Gets an {@link INode} based on its id.
     *
     * @param id The id of the node to be retrieved.
     * @return The {@link INode} retrieved.
     **/
    INode getNode(long id);

    /**
     * Gets an {@link IRelationship} based on its id.
     *
     * @param id The id of the relationship to be retrieved.
     * @return The {@link IRelationship} retrieved.
     **/
    IRelationship getRelationship(long id);

    /**
     * Gets all of the {@link INode} directly related to a given node.
     *
     * @param value The value of assigned to the desired node.
     * @return The {@link List} of INode retrieved.
     **/
    List<INode> getRelatedNodes(String value);

    /**
     * Gets all of the {@link IRelationship} directly related to a given node.
     *
     * @param value The value of assigned to the desired node.
     * @return The {@link List} of IRelationship retrieved.
     **/
    List<IRelationship> getRelationshipsToNode(String value);

    /**
     * Gets all of the {@link IRelationship} between two given {@link INode}.
     *
     * @param value1 The value of assigned to the first node.
     * @param value2 The value of assigned to the second node.
     * @return The {@link List}of IRelationship retrieved.
     **/
    List<IRelationship> getRelationshipsBetween(
            String value1, String value2);

    /**
     * Gets all of the generalized {@link IPath} between two given {@link INode}
     * containing a given relationship.
     *
     * @param from The value of assigned to the first node.
     * @param to   The value of assigned to the second node.
     * @param rel  The value of the relationship to be present.
     * @return The {@link List} of IPath retrieved.
     **/
    List<IPath> checkGeneralizedConnection(
            String from, String to, String rel);

    /**
     * Updates a relationship across the whole database.
     *
     * @param oldRel The value of the old relationship to be replaced.
     * @param newRel The value of the new relationship.
     **/
    void updateRelationships(String oldRel, String newRel);

    /**
     * Runs a query against the database.
     *
     * @param query The query to be run.
     * @return A {@link IStatementResult} with the results from running the query.
     **/
    IStatementResult runQuery(String query);

    /**
     * Creates a new node.
     *
     * @param value The value to be assigned to the node.
     * @return the Node created
     **/
    INode createNode(String value);

    /**
     * Creates a relationship between two nodes.
     *
     * @param startNode The value of assigned to the first node.
     * @param endNode   The value of assigned to the second node.
     * @param rel       The value of the relationship to be created.
     * @return The relationship created.
     **/
    IRelationship createRelationship(String startNode, String endNode, String rel);

    /**
     * Deletes nodes with given value.
     *
     * @param value The value of assigned to the node.
     * @return The number of entries deleted.
     **/
    int deleteNode(String value);

    /**
     * Deletes a relationship between two nodes.
     *
     * @param startNode The value of assigned to the first node.
     * @param endNode   The value of assigned to the second node.
     * @param rel       The value of the relationship to be deleted.
     * @return The number of entries deleted.
     **/
    int deleteRelationship(String startNode, String endNode, String rel);

    /**
     * Deletes all nodes with given label.
     *
     * @param label The value of assigned to the node.
     * @return The number of entries deleted.
     **/
    void cleanLabel(String label);
}
