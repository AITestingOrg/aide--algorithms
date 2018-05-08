package org.aist.semanticnetwork.interfaces;

import exceptions.UncoercibleTypeException;
import org.aist.semanticnetwork.exceptions.UncoercibleTypeException;

/**
 * Interface for a single record, the result of a query, in a graph database.
 * */
public interface IRecord {
    /**
     * Gets the {@link INode}  associated with a given key in the record.
     * If the key is invalid or not a {@link INode}  an exception is thrown.
     * @return The requested {@link INode}
     *
     * @throws UncoercibleTypeException If the retrieved value is not of the expected type.
     * */
    INode getNode(String key) throws UncoercibleTypeException, UncoercibleTypeException;

    /**
     * Gets the {@link IPath}  associated with a given key in the record.
     * If the key is invalid or not a {@link IPath}  an exception is thrown.
     * @return The requested {@link IPath}
     *
     * @throws UncoercibleTypeException If the retrieved value is not of the expected type.
     * */
    IPath getPath(String key) throws UncoercibleTypeException;

    /**
     * Gets the {@link IRelationship}  associated with a given key in the record.
     * If the key is invalid or not a {@link IRelationship}  an exception is thrown.
     * @return The requested {@link IRelationship}
     *
     * @throws UncoercibleTypeException If the retrieved value is not of the expected type.
     * */
    IRelationship getRelationship(String key) throws UncoercibleTypeException;
}
