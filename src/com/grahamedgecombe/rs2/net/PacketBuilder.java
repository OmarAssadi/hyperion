package com.grahamedgecombe.rs2.net;

import org.apache.mina.core.buffer.IoBuffer;

import com.grahamedgecombe.rs2.net.Packet.Type;

public class PacketBuilder {
	
	private int opcode;
	private Type type;
	private IoBuffer payload = IoBuffer.allocate(16);
	
	public PacketBuilder() {
		this(-1);
	}
	
	public PacketBuilder(int opcode) {
		this(opcode, Type.FIXED);
	}
	
	public PacketBuilder(int opcode, Type type) {
		this.opcode = opcode;
		this.type = type;
		payload.setAutoExpand(true);
		payload.setAutoShrink(true);
	}
	
	public PacketBuilder put(byte b) {
		payload.put(b);
		return this;
	}
	
	public PacketBuilder put(byte[] b) {
		payload.put(b);
		return this;
	}
	
	public PacketBuilder putShort(short s) {
		payload.putShort(s);
		return this;
	}
	
	public PacketBuilder putInt(int i) {
		payload.putInt(i);
		return this;
	}
	
	public PacketBuilder putLong(long l) {
		payload.putLong(l);
		return this;
	}
	
	public Packet toPacket() {
		return new Packet(opcode, type, payload.flip().asReadOnlyBuffer());
	}

	public PacketBuilder putRS2String(String string) {
		payload.put(string.getBytes());
		payload.put((byte) 10);
		return this;
	}

}
