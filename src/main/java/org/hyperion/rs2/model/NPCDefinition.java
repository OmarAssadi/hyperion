package org.hyperion.rs2.model;

/**
 * <p>Represents a type of NPC.</p>
 *
 * @author Graham Edgecombe
 */
public class NPCDefinition {

    /**
     * The id.
     */
    private final int id;

    /**
     * Creates the definition.
     *
     * @param id The id.
     */
    private NPCDefinition(final int id) {
        this.id = id;
    }

    /**
     * Gets an npc definition by its id.
     *
     * @param id The id.
     * @return The definition.
     */
    public static NPCDefinition forId(final int id) {
        return new NPCDefinition(id);
    }

    /**
     * Gets the id.
     *
     * @return The id.
     */
    public int getId() {
        return this.id;
    }

}
