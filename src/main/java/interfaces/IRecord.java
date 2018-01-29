package interfaces;

/**
 * Interface for a single record, the result of a query, in a graph database.
 * */
public interface IRecord {
    /**
     * Gets the {@link INode}  associated with a given key in the record.
     * If the key is invalid or not a {@link INode}  an exception is thrown.
     * @return The requested {@link INode}
     * */
    INode getNode(String key);

    /**
     * Gets the {@link IPath}  associated with a given key in the record.
     * If the key is invalid or not a {@link IPath}  an exception is thrown.
     * @return The requested {@link IPath}
     * */
    IPath getPath(String key);

    /**
     * Gets the {@link IRelationship}  associated with a given key in the record.
     * If the key is invalid or not a {@link IRelationship}  an exception is thrown.
     * @return The requested {@link IRelationship}
     * */
    IRelationship getRelationship(String key);
}
