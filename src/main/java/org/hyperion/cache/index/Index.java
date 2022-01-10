package org.hyperion.cache.index;

/**
 * An index points to a file in the cache.
 *
 * @author Graham Edgecombe
 */
public abstract class Index {

    /**
     * The identifier.
     */
    private final int identifier;

    /**
     * Creates the index.
     *
     * @param identifier The identifier.
     */
    public Index(final int identifier) {
        this.identifier = identifier;
    }

    /**
     * Gets the identifier.
     *
     * @return The identifier.
     */
    public int getIdentifier() {
        return identifier;
    }

}
