package com.grahamedgecombe.rs2.net;

import java.util.logging.Logger;

import org.apache.mina.core.session.IoSession;

import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.packet.*;

public class PacketManager {
	
	private static final Logger logger = Logger.getLogger(PacketManager.class.getName());
	private static final PacketManager INSTANCE = new PacketManager();
	
	public static PacketManager getPacketManager() {
		return INSTANCE;
	}
	
	private PacketHandler[] packetHandlers = new PacketHandler[256];
	
	public PacketManager() {
		/*
		 * Set handlers.
		 */
		// keep alive
		packetHandlers[0] = new QuietPacketHandler();
		// region load
		packetHandlers[121] = new QuietPacketHandler();
		// camera move
		packetHandlers[86] = new QuietPacketHandler();
		// click
		packetHandlers[241] = new QuietPacketHandler();
		// action button
		packetHandlers[185] = new ActionButtonPacketHandler();
		// walking
		packetHandlers[248] = new WalkingPacketHandler();
		packetHandlers[164] = new WalkingPacketHandler();
		packetHandlers[98] = new WalkingPacketHandler();
		// public chat
		packetHandlers[4] = new ChatPacketHandler();
		
		/*
		 * Set default handlers.
		 */
		final PacketHandler defaultHandler = new DefaultPacketHandler();
		for(int i = 0; i < packetHandlers.length; i++) {
			if(packetHandlers[i] == null) {
				packetHandlers[i] = defaultHandler;
			}
		}
	}

	public void handle(IoSession session, Packet packet) {
		try {
			packetHandlers[packet.getOpcode()].handle((Player) session.getAttribute("player"), packet);
		} catch(Exception ex) {
			logger.severe("Exception handling packet : " + ex.getMessage());
			session.close(false);
		}
	}

}
