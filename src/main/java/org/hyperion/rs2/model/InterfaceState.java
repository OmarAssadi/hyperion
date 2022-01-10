package org.hyperion.rs2.model;

import org.hyperion.rs2.model.container.Bank;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.ContainerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains information about the state of interfaces open in the client.
 *
 * @author Graham Edgecombe
 */
public class InterfaceState {

    /**
     * The player.
     */
    private final Player player;
    /**
     * A list of container listeners used on interfaces that have containers.
     */
    private final List<ContainerListener> containerListeners = new ArrayList<>();
    /**
     * The current open interface.
     */
    private int currentInterface = -1;
    /**
     * The active enter amount interface.
     */
    private int enterAmountInterfaceId = -1;
    /**
     * The active enter amount id.
     */
    private int enterAmountId;
    /**
     * The active enter amount slot.
     */
    private int enterAmountSlot;

    /**
     * Creates the interface state.
     */
    public InterfaceState(final Player player) {
        this.player = player;
    }

    /**
     * Checks if the specified interface is open.
     *
     * @param id The interface id.
     * @return <code>true</code> if the interface is open, <code>false</code> if not.
     */
    public boolean isInterfaceOpen(final int id) {
        return currentInterface == id;
    }

    /**
     * Gets the current open interface.
     *
     * @return The current open interface.
     */
    public int getCurrentInterface() {
        return currentInterface;
    }

    /**
     * Called when an interface is opened.
     *
     * @param id The interface.
     */
    public void interfaceOpened(final int id) {
        if (currentInterface != -1) {
            interfaceClosed();
        }
        currentInterface = id;
    }

    /**
     * Called when an interface is closed.
     */
    public void interfaceClosed() {
        currentInterface = -1;
        enterAmountInterfaceId = -1;
        for (final ContainerListener l : containerListeners) {
            player.getInventory().removeListener(l);
            player.getEquipment().removeListener(l);
            player.getBank().removeListener(l);
        }
    }

    /**
     * Adds a listener to an interface that is closed when the inventory is closed.
     *
     * @param container         The container.
     * @param containerListener The listener.
     */
    public void addListener(final Container container, final ContainerListener containerListener) {
        container.addListener(containerListener);
        containerListeners.add(containerListener);
    }

    /**
     * Called to open the enter amount interface.
     *
     * @param interfaceId The interface id.
     * @param slot        The slot.
     * @param id          The id.
     */
    public void openEnterAmountInterface(final int interfaceId, final int slot, final int id) {
        enterAmountInterfaceId = interfaceId;
        enterAmountSlot = slot;
        enterAmountId = id;
        player.getActionSender().sendEnterAmountInterface();
    }

    /**
     * Checks if the enter amount interface is open.
     *
     * @return <code>true</code> if so, <code>false</code> if not.
     */
    public boolean isEnterAmountInterfaceOpen() {
        return enterAmountInterfaceId != -1;
    }

    /**
     * Called when the enter amount interface is closed.
     *
     * @param amount The amount that was entered.
     */
    public void closeEnterAmountInterface(final int amount) {
        try {
            switch (enterAmountInterfaceId) {
                case Bank.PLAYER_INVENTORY_INTERFACE -> Bank.deposit(player, enterAmountSlot, enterAmountId, amount);
                case Bank.BANK_INVENTORY_INTERFACE -> Bank.withdraw(player, enterAmountSlot, enterAmountId, amount);
            }
        } finally {
            enterAmountInterfaceId = -1;
        }
    }

}
