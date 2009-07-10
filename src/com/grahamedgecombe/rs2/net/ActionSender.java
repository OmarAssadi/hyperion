package com.grahamedgecombe.rs2.net;

import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.net.Packet.Type;

public class ActionSender {
	
	private Player player;
	
	public ActionSender(Player player) {
		this.player = player;
	}
	
	public void sendLogin() {
		sendMessage("Welcome to RuneScape.");
	}
	
	public void sendMessage(String message) {
		player.getSession().write(new PacketBuilder(253, Type.VARIABLE).putRS2String(message).toPacket());
	}

}
