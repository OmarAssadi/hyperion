package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.net.Packet;

/**
 * Remove item (unequip) packet handler.
 * @author Graham
 *
 */
public class RemovePacketHandler implements PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
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
		}
	}

}
