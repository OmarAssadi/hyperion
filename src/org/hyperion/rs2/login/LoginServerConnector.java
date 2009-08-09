package org.hyperion.rs2.login;

import java.net.InetSocketAddress;
import java.util.logging.Logger;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.util.IoBufferUtils;
import org.hyperion.util.CommonConstants;
import org.hyperion.util.net.LoginCodecFactory;
import org.hyperion.util.net.LoginPacket;

/**
 * <p>The <code>LoginServerConnector</code> manages the communication between
 * the game server and the login server.</p>
 * @author Graham
 *
 */
public class LoginServerConnector extends IoHandlerAdapter {
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(LoginServerConnector.class.getName());
	
	/**
	 * The connector.
	 */
	private IoConnector connector = new NioSocketConnector();
	
	/**
	 * The login server address.
	 */
	private String address;
	
	/**
	 * The login server password.
	 */
	private String password;
	
	/**
	 * The world server node id.
	 */
	private int node;
	
	/**
	 * The client session.
	 */
	private IoSession session;
	
	/**
	 * Authenticated flag.
	 */
	private boolean authenticated = false;
	
	/**
	 * Creates the login server connector.
	 * @param address The address of the login server.
	 */
	public LoginServerConnector(String address) {
		this.address = address;
		connector.setHandler(this);
	}
	
	/**
	 * Checks if the client is connected.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isConnected() {
		return session != null && session.isConnected();
	}
	
	/**
	 * Checks if the client is authenticated.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isAuthenticated() {
		return isConnected() && authenticated;
	}

	/**
	 * Connects to the server.
	 * @param password The password.
	 * @param node The node id.
	 */
	public void connect(final String password, final int node) {
		this.password = password;
		this.node = node;
		logger.info("Connecting to login server : " + address + ":" + CommonConstants.LOGIN_PORT + "...");
		ConnectFuture cf = connector.connect(new InetSocketAddress(address, CommonConstants.LOGIN_PORT));
		cf.awaitUninterruptibly();
		if(!cf.isConnected() && (session == null || !session.isConnected())) {
			logger.severe("Connection to login server failed. Retrying...");
			// this stops stack overflow errors
			World.getWorld().getEngine().submitLogic(new Runnable() {
				public void run() {
					World.getWorld().getLoginServerConnector().connect(password, node);
				}
			});
		} else {
			this.session = cf.getSession();
			logger.info("Connected.");
			session.getFilterChain().addFirst("protocolCodecFilter", new ProtocolCodecFilter(new LoginCodecFactory()));
			// create and send auth packet
			IoBuffer buf = IoBuffer.allocate(16);
			buf.setAutoExpand(true);
			buf.putShort((short) node);
			IoBufferUtils.putRS2String(buf, password);
			buf.flip();
			this.session.write(new LoginPacket(LoginPacket.AUTH, buf));
		}
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable throwable) throws Exception {
		session.close(false);
	}

	@Override
	public void messageReceived(IoSession session, Object in) throws Exception {
		read((LoginPacket) in);
	}
	
	/**
	 * Write a packet.
	 * @param packet The packet to write.
	 */
	public void write(LoginPacket packet) {
		if(!this.isConnected()) {
			session.write(packet);
		} else {
			throw new IllegalStateException("Not connected.");
		}
	}

	/**
	 * Read and process a packet.
	 * @param packet The packet to read.
	 */
	private void read(LoginPacket packet) {
		final IoBuffer payload = packet.getPayload();
		switch(packet.getOpcode()) {
		case LoginPacket.AUTH_RESPONSE:
			int code = payload.getUnsigned();
			if(code == 0) {
				authenticated = true;
				logger.info("Authenticated as node : World-" + node + ".");
			} else {
				session.close(false);
				logger.severe("Login server authentication error : " + code + ". Check your password and node id.");
			}
			break;
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		if(this.session == session) {
			logger.info("Disconnected. Retrying...");
			connect(password, node);
			this.session = null;
		}
	}

}
