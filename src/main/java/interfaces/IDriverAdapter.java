package interfaces;

import java.util.List;

public interface IDriverAdapter {

    /**
     * Closes the current driver connection.
     **/
    void close();

    /**
     * Gets an {@link INode} based on a pair of key and value.
     *
     * @param key   The key to check against.
     * @param value The value of assigned to key in the desired node.
     * @return The {@link INode} retrieved.
     **/
    INode getNode(String key, String value);

    /**
     * Gets an {@link INode} based on its id.
     *
     * @param id The id of the node to be retrieved.
     * @return The {@link INode} retrieved.
     **/
    INode getNode(long id);

    /**
     * Gets all of the {@link INode} directly related to a given node.
     *
     * @param key   The key to check against.
     * @param value The value of assigned to key in the desired node.
     * @return The {@link List} of INode retrieved.
     **/
    List<INode> getRelatedNodes(String key, String value);

    /**
     * Gets all of the {@link IRelationship} directly related to a given node.
     *
     * @param key   The key to check against.
     * @param value The value of assigned to key in the desired node.
     * @return The {@link List} of IRelationship retrieved.
     **/
    List<IRelationship> getRelationshipsToNode(String key, String value);

    /**
     * Gets all of the {@link IRelationship} between two given {@link INode}.
     *
     * @param key1   The key to check against for the first node.
     * @param value1 The value of assigned to key in the first node.
     * @param key2   The key to check against for the second node.
     * @param value2 The value of assigned to key in the second node.
     * @return The {@link List}of IRelationship retrieved.
     **/
    List<IRelationship> getRelationshipsBetween(
            String key1, String value1, String key2, String value2);

    /**
     * Gets all of the generalized {@link IPath} between two given {@link INode}
     * containing a given relationship.
     *
     * @param key1   The key to check against for the first node.
     * @param value1 The value of assigned to key in the first node.
     * @param key2   The key to check against for the second node.
     * @param value2 The value of assigned to key in the second node.
     * @param rel    The value of the relationship to be present.
     * @return       The {@link List} of IPath retrieved.
     **/
    List<IPath> checkGeneralizedConnection(
            String key1, String value1, String key2, String value2, String rel);

    /**
     * Updates a relationship across the whole database.
     *
     * @param oldRel The value of the old relationship to be replaced.
     * @param newRel The value of the new relationship to be replaced.
     **/
    void updateRelationships(String oldRel, String newRel);

    /**
     * Deletes a relationship between two nodes.
     *
     * @param key1   The key to check against for the first node.
     * @param value1 The value of assigned to key in the first node.
     * @param key2   The key to check against for the second node.
     * @param value2 The value of assigned to key in the second node.
     * @param rel    The value of the relationship to be deleted.
     **/
    int deleteRelationship(String key1, String value1, String key2, String value2, String rel);

    /**
     * Runs a query against the database.
     *
     * @param query The query to be run.
     * @return A {@link IStatementResult} with the results from running the query.
     **/
    IStatementResult runQuery(String query);

    /**
     * Creates a relationship between two nodes.
     *
     * @param key1   The key to check against for the first node.
     * @param value1 The value of assigned to key in the first node.
     * @param key2   The key to check against for the second node.
     * @param value2 The value of assigned to key in the second node.
     * @param rel    The value of the relationship to be created.
     **/
    void createRelationship(String key1, String value1, String key2, String value2, String rel);
}
