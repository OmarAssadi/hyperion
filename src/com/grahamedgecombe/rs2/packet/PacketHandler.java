package com.grahamedgecombe.rs2.packet;

import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.net.Packet;

/**
 * An interface which describes a class that handles packets.
 * @author Graham
 *
 */
public interface PacketHandler {
	
	/**
	 * Handles a single packet.
	 * @param player The player.
	 * @param packet The packet.
	 */
	public void handle(Player player, Packet packet);

}
