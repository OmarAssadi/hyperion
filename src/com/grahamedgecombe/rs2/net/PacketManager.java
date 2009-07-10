package com.grahamedgecombe.rs2.net;

import org.apache.mina.core.session.IoSession;

public class PacketManager {
	
	private static PacketManager manager = new PacketManager();
	
	public static PacketManager getPacketManager() {
		return manager;
	}

	public void handle(IoSession session, Packet message) {
		
	}

}
