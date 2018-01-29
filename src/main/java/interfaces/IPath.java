package interfaces;

/**
 * Interface for a Graph path in a graph database.
 * */
public interface IPath {
    /**
     * Checks if an INode is present on the path.
     *
     * @return True if the INode is present in the path.
     **/
    boolean contains(INode node);


    /**
     * Checks length of the path.
     *
     * @return The length of the current path.
     **/
    int length();

    /**
     * Gets an iterable of all the {@link INode} present in the path.
     *
     * @return An {@link Iterable} of the {@link INode} present in the path.
     **/
    Iterable<INode> nodes();

    /**
     * Gets an iterable of all the {@link IRelationship} present in the path.
     *
     * @return An {@link Iterable} of the {@link IRelationship} present in the path.
     **/
    Iterable<IRelationship> relationships();
}
