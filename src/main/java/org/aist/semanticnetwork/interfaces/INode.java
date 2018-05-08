package org.aist.semanticnetwork.interfaces;

/**
 * Interface for a Node in a graph database.
 * */
public interface INode {

    /**
     * Gets an Iterable of {@link String} of labels for the node.
     *
     * @return Iterable of {@link String} of labels.
     **/
    Iterable<String> labels();

    /**
     * Gets an Iterable of {@link String} of keys for the node.
     *
     * @return Iterable of {@link String} of keys.
     **/
    Iterable<String> keys();

    /**
     * Gets a {@link String} value associated to a key in this node.
     *
     * @param key The String value of a key to retrieve the value of.
     * @return Iterable of {@link String} of labels.
     **/
    String get(String key);

    /**
     * Gets the id for this node.
     *
     * @return The {@link Long} id value of the node.
     **/
    long id();
}
