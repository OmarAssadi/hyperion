package com.grahamedgecombe.rs2.task;

import org.apache.mina.core.session.IoSession;

import com.grahamedgecombe.rs2.GameEngine;
import com.grahamedgecombe.rs2.net.Packet;
import com.grahamedgecombe.rs2.net.PacketManager;

/**
 * A task that is executed when a session receives a message.
 * @author Graham
 *
 */
public class SessionMessageTask implements Task {
	
	/**
	 * The session.
	 */
	private IoSession session;
	
	/**
	 * The packet.
	 */
	private Packet message;

	/**
	 * Creates the session message task.
	 * @param session The session.
	 * @param message The packet.
	 */
	public SessionMessageTask(IoSession session, Packet message) {
		this.session = session;
		this.message = message;
	}

	@Override
	public void execute(GameEngine context) {
		PacketManager.getPacketManager().handle(session, message);
	}

}
