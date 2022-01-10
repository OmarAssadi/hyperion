package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.container.Equipment.EquipmentType;
import org.hyperion.rs2.model.container.Inventory;
import org.hyperion.rs2.net.Packet;

/**
 * Handles the 'wield' option on items.
 *
 * @author Graham Edgecombe
 */
public class WieldPacketHandler implements PacketHandler {

    @Override
    public void handle(final Player player, final Packet packet) {
        final int id = packet.getShort() & 0xFFFF;
        final int slot = packet.getShortA() & 0xFFFF;
        final int interfaceId = packet.getShortA() & 0xFFFF;

        switch (interfaceId) {
            case Inventory.INTERFACE:
                if (slot < Inventory.SIZE) {
                    final Item item = player.getInventory().get(slot);
                    if (item != null && item.getId() == id) {
                        final EquipmentType type = Equipment.getType(item);
                        Item oldEquip = null;
                        final boolean stackable = false;
                        if (player.getEquipment().isSlotUsed(type.getSlot()) && !stackable) {
                            oldEquip = player.getEquipment().get(type.getSlot());
                            player.getEquipment().set(type.getSlot(), null);
                        }
                        player.getInventory().set(slot, null);
                        if (oldEquip != null) {
                            player.getInventory().add(oldEquip);
                        }
                        if (stackable) {
                            player.getEquipment().add(item);
                        } else {
                            player.getEquipment().set(type.getSlot(), item);
                        }
                    }
                }
                break;
        }
    }

}
