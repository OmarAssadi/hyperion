package org.hyperion.ls;

import org.apache.mina.core.session.IoSession;
import org.hyperion.util.net.LoginPacket;

/**
 * Manages a single node (world).
 * @author Graham
 *
 */
public class Node {
	
	/**
	 * The session.
	 */
	private IoSession session;
	
	/**
	 * The id.
	 */
	private int id;
	
	/**
	 * Creates a node.
	 * @param session The session.
	 * @param id The id.
	 */
	public Node(IoSession session, int id) {
		this.session = session;
		this.id = id;
	}
	
	/**
	 * Gets the session.
	 * @return The session.
	 */
	public IoSession getSession() {
		return session;
	}
	
	/**
	 * Gets the id.
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Handles an incoming packet.
	 * @param packet The incoming packet.
	 */
	public void handlePacket(LoginPacket packet) {
		switch(packet.getOpcode()) {
		
		}
	}

}
