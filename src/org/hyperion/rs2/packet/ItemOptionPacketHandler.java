package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Bank;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.container.Inventory;
import org.hyperion.rs2.net.Packet;

/**
 * Remove item options.
 * @author Graham Edgecombe
 *
 */
public class ItemOptionPacketHandler implements PacketHandler {
	
	/**
	 * Option 1 opcode.
	 */
	private static final int OPTION_1 = 145;

	@Override
	public void handle(Player player, Packet packet) {
		switch(packet.getOpcode()) {
		case OPTION_1:
			handleItemOption1(player, packet);
			break;
		}
	}

	private void handleItemOption1(Player player, Packet packet) {
		int interfaceId = packet.getShortA() & 0xFFFF;
		int slot = packet.getShortA() & 0xFFFF;
		int id = packet.getShortA() & 0xFFFF;
		
		switch(interfaceId) {
		case Equipment.INTERFACE:
			if(slot >= 0 && slot < Equipment.SIZE) { 
				if(!Container.transfer(player.getEquipment(), player.getInventory(), slot, id)) {
					// indicate it failed
				}
			}
			break;
		case Bank.PLAYER_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Inventory.SIZE) {
				Item item = player.getInventory().get(slot);
				if(item != null) {
					int removed = player.getInventory().remove(new Item(item.getId(), 1));
					if(removed > 0) {
						player.getBank().add(new Item(item.getId(), removed));
					}
				}
			}
			break;
		}
	}

}
