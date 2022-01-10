package org.hyperion.rs2.model.container.impl;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.UpdateFlags.UpdateFlag;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.ContainerListener;

/**
 * A ContainerListener which flags for an appearance update when the player
 * equips or removes an item.
 *
 * @author Graham Edgecombe
 */
public class EquipmentContainerListener implements ContainerListener {

    /**
     * The player.
     */
    private final Player player;

    /**
     * Creates the container listener.
     *
     * @param player The player.
     */
    public EquipmentContainerListener(final Player player) {
        this.player = player;
    }

    @Override
    public void itemChanged(final Container container, final int slot) {
        player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
    }

    @Override
    public void itemsChanged(final Container container, final int[] slots) {
        player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
    }

    @Override
    public void itemsChanged(final Container container) {
        player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
    }

}
