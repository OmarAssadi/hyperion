package com.grahamedgecombe.rs2.task;

import org.apache.mina.core.session.IoSession;

import com.grahamedgecombe.rs2.GameEngine;
import com.grahamedgecombe.rs2.net.Packet;
import com.grahamedgecombe.rs2.net.PacketManager;

public class SessionMessageTask implements Task {
	
	private IoSession session;
	private Packet message;

	public SessionMessageTask(IoSession session, Packet message) {
		this.session = session;
		this.message = message;
	}

	@Override
	public void execute(GameEngine context) {
		PacketManager.getPacketManager().handle(session, message);
	}

}
