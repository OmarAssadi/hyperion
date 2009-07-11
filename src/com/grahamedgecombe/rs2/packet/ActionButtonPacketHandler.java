package com.grahamedgecombe.rs2.packet;

import java.util.logging.Logger;

import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.net.Packet;

public class ActionButtonPacketHandler implements PacketHandler {

	private static final Logger logger = Logger.getLogger(ActionButtonPacketHandler.class.getName());
	
	@Override
	public void handle(Player player, Packet packet) {
		final int button = packet.getShort();
		switch(button) {
		case 2458:
			player.getActionSender().sendLogout();
			break;
		default:
			logger.info("Unhandled action button : " + button);
			break;
		}
	}

}
