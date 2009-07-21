package org.hyperion.js5;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.hyperion.js5.UpdateSession.Type;

/**
 * <p>An implementation of the JS5 server.</p>
 * 
 * <p>A lot of the work in the JAGGRAB server is based on some code
 * Miss Silabsoft re-released, originally authored by winterLove.</p>
 * @author Graham
 *
 */
public class JS5Server {
	
	/**
	 * The HTTP port to listen on.
	 */
	public static final int HTTP_PORT = 80;
	
	/**
	 * The JAGGRAB port to listen on.
	 */
	public static final int JAGGRAB_PORT = 43595;
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(JS5Server.class.getName());
	
	/**
	 * The <code>IoAcceptor</code> instance.
	 */
	private final IoAcceptor jaggrabAcceptor = new NioSocketAcceptor();
	
	/**
	 * The <code>IoAcceptor</code> instance.
	 */
	private final IoAcceptor httpAcceptor = new NioSocketAcceptor();

	/**
	 * Creates the jaggrab server.
	 */
	public JS5Server() {
		jaggrabAcceptor.setHandler(new ConnectionHandler(Type.JAGGRAB));
		httpAcceptor.setHandler(new ConnectionHandler(Type.HTTP));
	}
	
	/**
	 * Binds the server to the ports.
	 * @return The server instance, for chaining.
	 * @throws IOException 
	 */
	public JS5Server bind() throws IOException {
		logger.info("Binding to port : " + JAGGRAB_PORT + "...");
		jaggrabAcceptor.bind(new InetSocketAddress(JAGGRAB_PORT));
		logger.info("Binding to port : " + HTTP_PORT + "...");
		httpAcceptor.bind(new InetSocketAddress(HTTP_PORT));
		return this;
	}

	/**
	 * Starts the jaggrab server.
	 */
	public void start() {
		logger.info("Ready");
	}

}
