package com.grahamedgecombe.rs2.net;

import org.apache.mina.core.buffer.IoBuffer;

public class Packet {
	
	public enum Type {
		FIXED,
		VARIABLE,
		VARIABLE_SHORT,
	}
	
	private int opcode;
	private Type type;
	private IoBuffer payload;
	
	public Packet(int opcode, Type type, IoBuffer payload) {
		this.opcode = opcode;
		this.type = type;
		this.payload = payload;
	}
	
	public boolean isRaw() {
		return opcode == -1;
	}
	
	public int getOpcode() {
		return opcode;
	}
	
	public Type getType() {
		return type;
	}
	
	public IoBuffer getPayload() {
		return payload;
	}
	
	public int getLength() {
		return payload.limit();
	}
	
	public byte get() {
		return payload.get();
	}
	
	public byte getByte() {
		return get();
	}
	
	public int getUnsignedByte() {
		return payload.getUnsigned();
	}
	
	public short getShort() {
		return payload.getShort();
	}
	
	public int getUnsignedShort() {
		return payload.getUnsignedShort();
	}
	
	public int getInt() {
		return payload.getInt();
	}
	
	public long getLong() {
		return payload.getLong();
	}

}
