package org.hyperion.util.net;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * Represents a packet between the login server and world server.
 * @author Graham
 *
 */
public class LoginPacket {
	
	/**
	 * Authentication request opcode.
	 */
	public static final int AUTH = 0;
	
	/**
	 * Authentication response opcode.
	 */
	public static final int AUTH_RESPONSE = 1;
	
	/**
	 * The opcode.
	 */
	private int opcode;
	
	/**
	 * The length.
	 */
	private int length;
	
	/**
	 * The payload.
	 */
	private IoBuffer payload;
	
	/**
	 * Creates the login packet.
	 * @param opcode The opcode.
	 * @param payload The payload.
	 */
	public LoginPacket(int opcode, IoBuffer payload) {
		this.opcode = opcode;
		this.length = payload.remaining();
		this.payload = payload;
	}
	
	/**
	 * Gets the opcode.
	 * @return The opcode.
	 */
	public int getOpcode() {
		return opcode;
	}
	
	/**
	 * Gets the length.
	 * @return The length.
	 */
	public int getLength() {
		return length;
	}
	
	/**
	 * Gets the payload.
	 * @return The payload.
	 */
	public IoBuffer getPayload() {
		return payload;
	}

}
