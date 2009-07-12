package com.grahamedgecombe.rs2.packet;

import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.net.Packet;

/**
 * A packet handler which takes no action i.e. it ignores the packet.
 * @author Graham
 *
 */
public class QuietPacketHandler implements PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		
	}

}
