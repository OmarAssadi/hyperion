package com.grahamedgecombe.rs2.packet;

import java.util.logging.Logger;

import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.net.Packet;

public class DefaultPacketHandler implements PacketHandler {

	private static final Logger logger = Logger.getLogger(DefaultPacketHandler.class.getName());
	
	@Override
	public void handle(Player player, Packet packet) {
		logger.info("Packet : [opcode=" + packet.getOpcode() + " length=" + packet.getLength() + " payload=" + packet.getPayload() + "]");
	}

}
