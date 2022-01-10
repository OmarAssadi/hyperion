package org.hyperion.rs2.model.container.impl;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.ContainerListener;

/**
 * A ContainerListener which updates a client-side interface to match the
 * server-side copy of the container.
 *
 * @author Graham Edgecombe
 */
public class InterfaceContainerListener implements ContainerListener {

    /**
     * The player.
     */
    private final Player player;

    /**
     * The interface id.
     */
    private final int interfaceId;

    /**
     * Creates the container listener.
     *
     * @param player      The player.
     * @param interfaceId The interface id.
     */
    public InterfaceContainerListener(final Player player, final int interfaceId) {
        this.player = player;
        this.interfaceId = interfaceId;
    }

    @Override
    public void itemChanged(final Container container, final int slot) {
        final Item item = container.get(slot);
        player.getActionSender().sendUpdateItem(interfaceId, slot, item);
    }

    @Override
    public void itemsChanged(final Container container, final int[] slots) {
        player.getActionSender().sendUpdateItems(interfaceId, slots, container.toArray());
    }

    @Override
    public void itemsChanged(final Container container) {
        player.getActionSender().sendUpdateItems(interfaceId, container.toArray());
    }

}
