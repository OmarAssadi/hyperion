package org.hyperion.ls.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.hyperion.ls.Server;

/**
 * A client object.
 * 
 * @author blakeman8192
 */
public class Client {

	private final SocketChannel socketChannel;
	private final ByteBuffer inBuffer, outBuffer;

	{
		inBuffer = ByteBuffer.allocateDirect(64000);
		outBuffer = ByteBuffer.allocateDirect(64000);
	}

	/**
	 * Creates a new Client.
	 * 
	 * @param socketChannel
	 *            the <code>SocketChannel</code> of the client.
	 */
	public Client(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}
	
	/**
	 * Terminates the client.
	 */
	public void terminate() {
		Server.removeClient(this);
		try {
			socketChannel.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Sets the incoming data buffer.
	 * 
	 * @param inBuffer
	 *            the new buffer.
	 */
	public void setInBuffer(ByteBuffer inBuffer) {
		this.inBuffer.clear();
		this.inBuffer.put(inBuffer);
	}

	/**
	 * Sets the outgoing data buffer.
	 * 
	 * @param outBuffer
	 *            the new buffer.
	 */
	public void setOutBuffer(ByteBuffer outBuffer) {
		this.outBuffer.clear();
		this.outBuffer.put(outBuffer);
	}

	/**
	 * Gets the socket channel.
	 * 
	 * @return the socket channel.
	 */
	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	/**
	 * Gets the incoming data buffer.
	 * 
	 * @return the in buffer.
	 */
	public ByteBuffer getInBuffer() {
		return inBuffer;
	}

	/**
	 * Gets the outgoing data buffer.
	 * 
	 * @return the out buffer.
	 */
	public ByteBuffer getOutBuffer() {
		return outBuffer;
	}

}
