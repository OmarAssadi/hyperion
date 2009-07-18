package org.hyperion.rs2.net;

import org.apache.mina.core.buffer.IoBuffer;
import org.hyperion.rs2.net.Packet.Type;


/**
 * A utility class for building packets.
 * @author Graham
 *
 */
public class PacketBuilder {
	
	/**
	 * Bit mask array.
	 */
	public static final int[] BIT_MASK_OUT = new int[32];
	
	/**
	 * Creates the bit mask array.
	 */
	static {
		for(int i = 0; i < BIT_MASK_OUT.length; i++) {
			BIT_MASK_OUT[i] = (1 << i) - 1;
		}
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
	private IoBuffer payload = IoBuffer.allocate(16);
	
	/**
	 * The current bit position.
	 */
	private int bitPosition;
	
	/**
	 * Creates a raw packet builder.
	 */
	public PacketBuilder() {
		this(-1);
	}
	
	/**
	 * Creates a fixed packet builder with the specified opcode.
	 * @param opcode The opcode.
	 */
	public PacketBuilder(int opcode) {
		this(opcode, Type.FIXED);
	}
	
	/**
	 * Creates a packet builder with the specified opcode and type.
	 * @param opcode The opcode.
	 * @param type The type.
	 */
	public PacketBuilder(int opcode, Type type) {
		this.opcode = opcode;
		this.type = type;
		payload.setAutoExpand(true);
		payload.setAutoShrink(true);
	}
	
	/**
	 * Writes a byte.
	 * @param b The byte to write.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder put(byte b) {
		payload.put(b);
		return this;
	}
	
	/**
	 * Writes an array of bytes.
	 * @param b The byte array.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder put(byte[] b) {
		payload.put(b);
		return this;
	}
	
	/**
	 * Writes a short.
	 * @param s The short.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putShort(int s) {
		payload.putShort((short) s);
		return this;
	}
	
	/**
	 * Writes an integer.
	 * @param i The integer.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putInt(int i) {
		payload.putInt(i);
		return this;
	}
	
	/**
	 * Writes a long.
	 * @param l The long.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putLong(long l) {
		payload.putLong(l);
		return this;
	}
	
	/**
	 * Converts this PacketBuilder to a packet.
	 * @return The Packet object.
	 */
	public Packet toPacket() {
		return new Packet(opcode, type, payload.flip().asReadOnlyBuffer());
	}

	/**
	 * Writes a RuneScape string.
	 * @param string The string to write.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putRS2String(String string) {
		payload.put(string.getBytes());
		payload.put((byte) 10);
		return this;
	}

	/**
	 * Writes a type-A short.
	 * @param val The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putShortA(int val) {
		payload.put((byte) (val >> 8));
		payload.put((byte) (val + 128));
		return this;
	}

	/**
	 * Writes a type-A byte.
	 * @param val The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putByteA(int val) {
		payload.put((byte) (val + 128));
		return this;
	}

	/**
	 * Writes a little endian type-A short.
	 * @param val The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putLEShortA(int val) {
		payload.put((byte) (val + 128));
		payload.put((byte) (val >> 8));
		return this;
	}

	/**
	 * Checks if this packet builder is empty.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isEmpty() {
		return payload.position() == 0;
	}

	/**
	 * Starts bit access.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder startBitAccess() {
		bitPosition = payload.position() * 8;
		return this;
	}
	
	/**
	 * Finishes bit access.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder finishBitAccess() {
		payload.position((bitPosition + 7) / 8);
		return this;
	}

	/**
	 * Writes some bits.
	 * @param numBits The number of bits to write.
	 * @param value The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putBits(int numBits, int value) {
		if(!payload.hasArray()) {
			throw new UnsupportedOperationException("The IoBuffer implementation must support array() for bit usage.");
		}
		
		int bytes = (int) Math.ceil((double) numBits / 8D) + 1;
		payload.expand((bitPosition + 7) / 8 + bytes);
		
		byte[] buffer = payload.array();
		
		int bytePos = bitPosition >> 3;
		int bitOffset = 8 - (bitPosition & 7);
		bitPosition += numBits;
		
		for(; numBits > bitOffset; bitOffset = 8) {
			buffer[bytePos] &= ~BIT_MASK_OUT[bitOffset];
			buffer[bytePos++] |= (value >> (numBits-bitOffset)) & BIT_MASK_OUT[bitOffset];
			numBits -= bitOffset;
		}
		if(numBits == bitOffset) {
			buffer[bytePos] &= ~BIT_MASK_OUT[bitOffset];
			buffer[bytePos] |= value & BIT_MASK_OUT[bitOffset];
		} else {
			buffer[bytePos] &= ~(BIT_MASK_OUT[numBits] << (bitOffset - numBits));
			buffer[bytePos] |= (value & BIT_MASK_OUT[numBits]) << (bitOffset - numBits);
		}
		return this;
	}

	/**
	 * Puts an <code>IoBuffer</code>.
	 * @param buf The buffer.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder put(IoBuffer buf) {
		payload.put(buf);
		return this;
	}

	/**
	 * Writes a type-C byte.
	 * @param val The value to write.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putByteC(int val) {
		put((byte) (-val));
		return this;
	}

	/**
	 * Writes a little-endian short.
	 * @param val The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putLEShort(int val) {
		payload.put((byte) (val));
		payload.put((byte) (val >> 8));
		return this;
	}

	/**
	 * Writes a type-1 integer.
	 * @param val The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putInt1(int val) {
		payload.put((byte) (val >> 8));
		payload.put((byte) val);
		payload.put((byte) (val >> 24));
		payload.put((byte) (val >> 16));
		return this;
	}

}