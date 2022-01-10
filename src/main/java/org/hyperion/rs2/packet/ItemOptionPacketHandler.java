package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Bank;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.container.Inventory;
import org.hyperion.rs2.net.Packet;

/**
 * Remove item options.
 *
 * @author Graham Edgecombe
 */
public class ItemOptionPacketHandler implements PacketHandler {

    /**
     * Option 1 opcode.
     */
    private static final int OPTION_1 = 145;

    /**
     * Option 2 opcode.
     */
    private static final int OPTION_2 = 117;

    /**
     * Option 3 opcode.
     */
    private static final int OPTION_3 = 43;

    /**
     * Option 4 opcode.
     */
    private static final int OPTION_4 = 129;

    /**
     * Option 5 opcode.
     */
    private static final int OPTION_5 = 135;

    @Override
    public void handle(final Player player, final Packet packet) {
        switch (packet.getOpcode()) {
            case OPTION_1 -> handleItemOption1(player, packet);
            case OPTION_2 -> handleItemOption2(player, packet);
            case OPTION_3 -> handleItemOption3(player, packet);
            case OPTION_4 -> handleItemOption4(player, packet);
            case OPTION_5 -> handleItemOption5(player, packet);
        }
    }

    /**
     * Handles item option 1.
     *
     * @param player The player.
     * @param packet The packet.
     */
    private void handleItemOption1(final Player player, final Packet packet) {
        final int interfaceId = packet.getShortA() & 0xFFFF;
        final int slot = packet.getShortA() & 0xFFFF;
        final int id = packet.getShortA() & 0xFFFF;

        switch (interfaceId) {
            case Equipment.INTERFACE:
                if (slot >= 0 && slot < Equipment.SIZE) {
                    if (!Container.transfer(player.getEquipment(), player.getInventory(), slot, id)) {
                        // indicate it failed
                    }
                }
                break;
            case Bank.PLAYER_INVENTORY_INTERFACE:
                if (slot >= 0 && slot < Inventory.SIZE) {
                    Bank.deposit(player, slot, id, 1);
                }
                break;
            case Bank.BANK_INVENTORY_INTERFACE:
                if (slot >= 0 && slot < Bank.SIZE) {
                    Bank.withdraw(player, slot, id, 1);
                }
                break;
        }
    }

    /**
     * Handles item option 2.
     *
     * @param player The player.
     * @param packet The packet.
     */
    private void handleItemOption2(final Player player, final Packet packet) {
        final int interfaceId = packet.getLEShortA() & 0xFFFF;
        final int id = packet.getLEShortA() & 0xFFFF;
        final int slot = packet.getLEShort() & 0xFFFF;

        switch (interfaceId) {
            case Bank.PLAYER_INVENTORY_INTERFACE:
                if (slot >= 0 && slot < Inventory.SIZE) {
                    Bank.deposit(player, slot, id, 5);
                }
                break;
            case Bank.BANK_INVENTORY_INTERFACE:
                if (slot >= 0 && slot < Bank.SIZE) {
                    Bank.withdraw(player, slot, id, 5);
                }
                break;
        }
    }

    /**
     * Handles item option 3.
     *
     * @param player The player.
     * @param packet The packet.
     */
    private void handleItemOption3(final Player player, final Packet packet) {
        final int interfaceId = packet.getLEShort() & 0xFFFF;
        final int id = packet.getShortA() & 0xFFFF;
        final int slot = packet.getShortA() & 0xFFFF;

        switch (interfaceId) {
            case Bank.PLAYER_INVENTORY_INTERFACE:
                if (slot >= 0 && slot < Inventory.SIZE) {
                    Bank.deposit(player, slot, id, 10);
                }
                break;
            case Bank.BANK_INVENTORY_INTERFACE:
                if (slot >= 0 && slot < Bank.SIZE) {
                    Bank.withdraw(player, slot, id, 10);
                }
                break;
        }
    }

    /**
     * Handles item option 4.
     *
     * @param player The player.
     * @param packet The packet.
     */
    private void handleItemOption4(final Player player, final Packet packet) {
        final int slot = packet.getShortA() & 0xFFFF;
        final int interfaceId = packet.getShort() & 0xFFFF;
        final int id = packet.getShortA() & 0xFFFF;

        switch (interfaceId) {
            case Bank.PLAYER_INVENTORY_INTERFACE:
                if (slot >= 0 && slot < Inventory.SIZE) {
                    Bank.deposit(player, slot, id, player.getInventory().getCount(id));
                }
                break;
            case Bank.BANK_INVENTORY_INTERFACE:
                if (slot >= 0 && slot < Bank.SIZE) {
                    Bank.withdraw(player, slot, id, player.getBank().getCount(id));
                }
                break;
        }
    }

    /**
     * Handles item option 5.
     *
     * @param player The player.
     * @param packet The packet.
     */
    private void handleItemOption5(final Player player, final Packet packet) {
        final int slot = packet.getLEShort() & 0xFFFF;
        final int interfaceId = packet.getShortA() & 0xFFFF;
        final int id = packet.getLEShort() & 0xFFFF;

        switch (interfaceId) {
            case Bank.PLAYER_INVENTORY_INTERFACE:
                if (slot >= 0 && slot < Inventory.SIZE) {
                    player.getInterfaceState().openEnterAmountInterface(interfaceId, slot, id);
                }
                break;
            case Bank.BANK_INVENTORY_INTERFACE:
                if (slot >= 0 && slot < Bank.SIZE) {
                    player.getInterfaceState().openEnterAmountInterface(interfaceId, slot, id);
                }
                break;
        }
    }

}
