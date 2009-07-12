package com.grahamedgecombe.rs2.net;

import org.apache.mina.core.buffer.IoBuffer;

import com.grahamedgecombe.rs2.util.IoBufferUtils;

/**
 * Represents a single packet.
 * @author Graham
 *
 */
public class Packet {
	
	/**
	 * The type of packet.
	 * @author Graham
	 *
	 */
	public enum Type {
		
		/**
		 * A fixed size packet where the size never changes.
		 */
		FIXED,
		
		/**
		 * A variable packet where the size is described by a byte.
		 */
		VARIABLE,
		
		/**
		 * A variable packet where the size is described by a word.
		 */
		VARIABLE_SHORT;
		
	}
	
	/**
	 * The opcode.
	 */
	private int opcode;
	
	/**
	 * The type.
	 */
	private Type type;
	
	/**
	 * The payload.
	 */
	private IoBuffer payload;
	
	/**
	 * Creates a packet.
	 * @param opcode The opcode.
	 * @param type The type.
	 * @param payload The payload.
	 */
	public Packet(int opcode, Type type, IoBuffer payload) {
		this.opcode = opcode;
		this.type = type;
		this.payload = payload;
	}
	
	/**
	 * Checks if this packet is raw. A raw packet does not have the usual
	 * headers such as opcode or size.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isRaw() {
		return opcode == -1;
	}
	
	/**
	 * Gets the opcode.
	 * @return The opcode.
	 */
	public int getOpcode() {
		return opcode;
	}
	
	/**
	 * Gets the type.
	 * @return The type.
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * Gets the payload.
	 * @return The payload.
	 */
	public IoBuffer getPayload() {
		return payload;
	}
	
	/**
	 * Gets the length.
	 * @return The length.
	 */
	public int getLength() {
		return payload.limit();
	}
	
	/**
	 * Reads a single byte.
	 * @return A single byte.
	 */
	public byte get() {
		return payload.get();
	}
	
	/**
	 * Reads several bytes.
	 * @param The target array.
	 */
	public void get(byte[] b) {
		payload.get(b);
	}
	
	/**
	 * Reads a byte.
	 * @return A single byte.
	 */
	public byte getByte() {
		return get();
	}
	
	/**
	 * Reads an unsigned byte.
	 * @return An unsigned byte.
	 */
	public int getUnsignedByte() {
		return payload.getUnsigned();
	}
	
	/**
	 * Reads a short.
	 * @return A short.
	 */
	public short getShort() {
		return payload.getShort();
	}
	
	/**
	 * Reads an unsigned short.
	 * @return An unsigned short.
	 */
	public int getUnsignedShort() {
		return payload.getUnsignedShort();
	}
	
	/**
	 * Reads an integer.
	 * @return An integer.
	 */
	public int getInt() {
		return payload.getInt();
	}
	
	/**
	 * Reads a long.
	 * @return A long.
	 */
	public long getLong() {
		return payload.getLong();
	}

	/**
	 * Reads a type C byte.
	 * @return A type C byte.
	 */
	public byte getByteC() {
		return (byte) (-get());
	}
	
	/**
	 * Reads a little-endian type A short.
	 * @return A little-endian type A short.
	 */
	public short getLEShortA() {
		int i = (payload.get() - 128 & 0xFF) | ((payload.get() & 0xFF) << 8);
		if(i > 32767)
			i -= 0x10000;
		return (short) i;
	}

	/**
	 * Reads a little-endian short.
	 * @return A little-endian short.
	 */
	public short getLEShort() {
		int i = (payload.get() & 0xFF) | ((payload.get() & 0xFF) << 8);
		if(i > 32767)
			i -= 0x10000;
		return (short) i;
	}

	/**
	 * Reads a type A byte.
	 * @return A type A byte.
	 */
	public byte getByteA() {
		return (byte) (128 - get());
	}
	
	/**
	 * Reads a RuneScape string.
	 * @return The string.
	 */
	public String getRS2String() {
		return IoBufferUtils.getRS2String(payload);
	}

}
