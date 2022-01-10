package org.hyperion.cache.index.impl;

import org.hyperion.cache.index.Index;

/**
 * A standard index where each index maps to a single file.
 *
 * @author Graham Edgecombe
 */
public class StandardIndex extends Index {

    /**
     * The file.
     */
    private final int file;

    /**
     * Creates the index.
     *
     * @param identifier The identifier.
     * @param file       The file.
     */
    public StandardIndex(final int identifier, final int file) {
        super(identifier);
        this.file = file;
    }

    /**
     * Gets the file.
     *
     * @return The file.
     */
    public int getFile() {
        return file;
    }

}
