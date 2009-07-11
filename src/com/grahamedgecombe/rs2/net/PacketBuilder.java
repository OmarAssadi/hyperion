package com.grahamedgecombe.rs2.net;

import org.apache.mina.core.buffer.IoBuffer;

import com.grahamedgecombe.rs2.net.Packet.Type;

public class PacketBuilder {
	
	public static final int[] BIT_MASK_OUT = new int[32];
	static {
		for(int i = 0; i < BIT_MASK_OUT.length; i++) {
			BIT_MASK_OUT[i] = (1 << i) - 1;
		}
	}
	
	private int opcode;
	private Type type;
	private IoBuffer payload = IoBuffer.allocate(16);
	private int bitPosition;
	
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
	
	public PacketBuilder putShort(int s) {
		payload.putShort((short) s);
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

	public PacketBuilder putShortA(int val) {
		payload.put((byte) (val >> 8));
		payload.put((byte) (val + 128));
		return this;
	}

	public PacketBuilder putByteA(int val) {
		payload.put((byte) (val + 128));
		return this;
	}

	public PacketBuilder putLEShortA(int val) {
		payload.put((byte) (val + 128));
		payload.put((byte) (val >> 8));
		return this;
	}

	public boolean isEmpty() {
		return payload.position() == 0;
	}

	public void startBitAccess() {
		bitPosition = payload.position() * 8;
	}
	
	public void finishBitAccess() {
		payload.position((bitPosition + 7) / 8);
	}

	public void putBits(int numBits, int value) {
		if(!payload.hasArray()) {
			throw new UnsupportedOperationException("The IoBuffer implementation must support array() for bit usage.");
		}
		
		int bytes = (int) Math.ceil((double) numBits / 8D);
		if(payload.remaining() < bytes) {
			payload.expand(payload.remaining() - bytes);
		}
		
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
			buffer[bytePos] &= ~(BIT_MASK_OUT[numBits]<<(bitOffset - numBits));
			buffer[bytePos] |= (value & BIT_MASK_OUT[numBits]) << (bitOffset - numBits);
		}
	}

	public void put(IoBuffer buf) {
		payload.put(buf);
	}

}
