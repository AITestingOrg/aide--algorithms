package interfaces;

import java.util.List;

public interface IStatementResult {
    /**
     * Gets a {@link IRecord}, there must only be one present.
     *
     * @return The {@link IRecord} retrieved.
     **/
    IRecord single();

    /**
     * Checks if there are any more records present.
     *
     * @return {@link Boolean} True if there are records.
     **/
    boolean hasNext();

    /**
     * Retrieves the next record available and pops it.
     *
     * @return {@link IRecord} The next record.
     **/
    IRecord next();

    /**
     * Retrieves the next record available but does not remove it.
     *
     * @return {@link IRecord} The next record.
     **/
    IRecord peek();

    /**
     * Retrieves the next record available but does not remove it.
     *
     * @return A List {@link String} The keys present.
     **/
    List<String> keys();
}
