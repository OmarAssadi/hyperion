package com.grahamedgecombe.rs2.packet;

import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.net.Packet;

public interface PacketHandler {
	
	public void handle(Player player, Packet packet);

}
