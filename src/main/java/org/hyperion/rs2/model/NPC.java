package org.hyperion.rs2.model;

import org.hyperion.rs2.model.Damage.HitType;
import org.hyperion.rs2.model.region.Region;

/**
 * <p>Represents a non-player character in the in-game world.</p>
 *
 * @author Graham Edgecombe
 */
public class NPC extends Entity {

    /**
     * The definition.
     */
    private final NPCDefinition definition;

    /**
     * Creates the NPC with the specified definition.
     *
     * @param definition The definition.
     */
    public NPC(final NPCDefinition definition) {
        super();
        this.definition = definition;
    }

    /**
     * Gets the NPC definition.
     *
     * @return The NPC definition.
     */
    public NPCDefinition getDefinition() {
        return definition;
    }

    @Override
    public void removeFromRegion(final Region region) {
        region.removeNpc(this);
    }

    @Override
    public void addToRegion(final Region region) {
        region.addNpc(this);
    }

    @Override
    public void inflictDamage(final int damage, final HitType type) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getClientIndex() {
        return this.getIndex();
    }

}
