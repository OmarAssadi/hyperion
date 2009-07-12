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
	
	public void get(byte[] b) {
		payload.get(b);
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

	public byte getByteC() {
		return (byte) (-get());
	}
	
	public short getLEShortA() {
		int i = (payload.get() - 128 & 0xFF) | ((payload.get() & 0xFF) << 8);
		if(i > 32767)
			i -= 0x10000;
		return (short) i;
	}

	public short getLEShort() {
		int i = (payload.get() & 0xFF) | ((payload.get() & 0xFF) << 8);
		if(i > 32767)
			i -= 0x10000;
		return (short) i;
	}

}
