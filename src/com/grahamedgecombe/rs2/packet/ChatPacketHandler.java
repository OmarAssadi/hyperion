package com.grahamedgecombe.rs2.packet;

import com.grahamedgecombe.rs2.model.ChatMessage;
import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.net.Packet;

public class ChatPacketHandler implements PacketHandler {

	public void handle(Player player, Packet packet) {
		int effects = packet.getByteA() & 0xFF;
		int colour = packet.getByteA() & 0xFF;
		int size = packet.getLength() - 2;
		byte[] rawChatData = new byte[size];
		packet.get(rawChatData);
		byte[] chatData = new byte[size];
		for(int i = 0; i < size; i++) {
			chatData[i] = (byte) (rawChatData[size - i - 1] - 128);
		}
		player.getChatMessageQueue().add(new ChatMessage(effects, colour, chatData));
	}

}
