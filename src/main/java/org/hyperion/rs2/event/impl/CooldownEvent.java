package org.hyperion.rs2.event.impl;

import org.hyperion.rs2.event.Event;
import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.EntityCooldowns.CooldownFlags;

/**
 * This event handles the expiry of a cooldown.
 *
 * @author Brett Russell
 */
@SuppressWarnings("unused")
public class CooldownEvent extends Event {

    private final Entity entity;

    private final CooldownFlags cooldown;

    /**
     * Creates a cooldown event for a single CooldownFlag.
     *
     * @param entity   The entity for whom we are expiring a cooldown.
     * @param duration The length of the cooldown.
     */
    public CooldownEvent(final Entity entity, final CooldownFlags cooldown, final int duration) {
        super(duration);
        this.entity = entity;
        this.cooldown = cooldown;
    }

    @Override
    public void execute() {
        entity.getEntityCooldowns().set(cooldown, false);
        this.stop();
    }

}
