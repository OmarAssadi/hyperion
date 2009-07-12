package com.grahamedgecombe.rs2.net;

import java.util.logging.Logger;

import org.apache.mina.core.session.IoSession;

import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.packet.*;

/**
 * Managers <code>PacketHandler</code>s.
 * @author Graham
 *
 */
public class PacketManager {
	
	/**
	 * The logger class.
	 */
	private static final Logger logger = Logger.getLogger(PacketManager.class.getName());
	
	/**
	 * The instance.
	 */
	private static final PacketManager INSTANCE = new PacketManager();
	
	/**
	 * Gets the packet manager instance.
	 * @return The packet manager instance.
	 */
	public static PacketManager getPacketManager() {
		return INSTANCE;
	}
	
	/**
	 * The packet handler array.
	 */
	private PacketHandler[] packetHandlers = new PacketHandler[256];
	
	/**
	 * Creates the packet manager.
	 */
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
		// commands
		packetHandlers[103] = new CommandPacketHandler();
		
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

	/**
	 * Handles a packet.
	 * @param session The session.
	 * @param packet The packet.
	 */
	public void handle(IoSession session, Packet packet) {
		try {
			packetHandlers[packet.getOpcode()].handle((Player) session.getAttribute("player"), packet);
		} catch(Exception ex) {
			logger.severe("Exception handling packet : " + ex.getMessage());
			session.close(false);
		}
	}

}
