package org.hyperion.ls.task.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hyperion.ls.Server;
import org.hyperion.ls.io.Client;
import org.hyperion.ls.task.Task;
import org.hyperion.util.CommonConstants;

/**
 * A <code>Task</code> used for the networking of the Hyperion Login Server.
 * 
 * @author blakeman8192
 */
public class NetworkTask implements Task {

	private Selector selector;
	private ServerSocketChannel listener;
	private Iterator<SelectionKey> eventIterator;
	private final Map<SocketChannel, Client> clientMap;
	private final String host;
	private final int port;

	{
		clientMap = new HashMap<SocketChannel, Client>();
		host = "127.0.0.1";
		port = CommonConstants.LOGIN_PORT;
		try {
			selector = Selector.open();
			listener = ServerSocketChannel.open();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void exec() throws IOException {
		selector.selectNow();
		eventIterator = selector.selectedKeys().iterator();
		while (eventIterator.hasNext()) {
			SelectionKey event = eventIterator.next();
			if (event.isValid()) {
				switch (event.readyOps()) {
				case SelectionKey.OP_ACCEPT:
					handleAcceptEvent(event);
					break;
				case SelectionKey.OP_READ:
					handleReadEvent(event);
					break;
				case SelectionKey.OP_WRITE:
					handleWriteEvent(event);
					break;
				}
			}
			eventIterator.remove();
		}
	}

	/**
	 * Sets up the listener.
	 * 
	 * @throws IOException
	 */
	public final void setupListener() throws IOException {
		System.out.println("Binding listener to address: " + host + ":" + port);
		listener.configureBlocking(false);
		listener.socket().bind(new InetSocketAddress(host, port));
		listener.register(selector, SelectionKey.OP_ACCEPT);
	}

	/**
	 * Handles an accept IO event.
	 * 
	 * @param event
	 *            the <code>SelectionKey</code> event to handle.
	 */
	private final void handleAcceptEvent(SelectionKey event) {
		try {
			SocketChannel socketChannel = listener.accept();
			if (socketChannel != null) {
				socketChannel.configureBlocking(false);
				socketChannel.register(selector, SelectionKey.OP_READ);
				Client client = new Client(socketChannel);
				clientMap.put(socketChannel, client);
				Server.addClient(client);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Handles a read IO event.
	 * 
	 * @param event
	 *            the <code>SelectionKey</code> event to handle.
	 */
	private final void handleReadEvent(SelectionKey event) {
		SocketChannel socketChannel = (SocketChannel) event.channel();
		Client client = clientMap.get(socketChannel);
		ByteBuffer inBuffer = client.getInBuffer();
		try {
			socketChannel.read(inBuffer);
		} catch (IOException ex) {
			clientMap.remove(socketChannel);
			client.terminate();
			event.cancel();
		}
	}

	/**
	 * Handles a write IO event.
	 * 
	 * @param event
	 *            the <code>SelectionKey</code> event to handle.
	 */
	private final void handleWriteEvent(SelectionKey event) {
		SocketChannel socketChannel = (SocketChannel) event.channel();
		Client client = clientMap.get(socketChannel);
		ByteBuffer outBuffer = client.getOutBuffer();
		try {
			socketChannel.write(outBuffer);
		} catch (IOException ex) {
			clientMap.remove(socketChannel);
			client.terminate();
			event.cancel();
		}
	}

}
