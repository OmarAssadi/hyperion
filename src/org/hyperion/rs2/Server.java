package org.hyperion.rs2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.firewall.ConnectionThrottleFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.hyperion.rs2.model.World;


/**
 * Starts everything else including MINA and the <code>GameEngine</code>.
 * @author Graham
 * 
 */
public class Server {

	/**
	 * The port to listen on.
	 */
	private static final int PORT = 43594;

	/**
	 * The entry point of the application.
	 * @param args The command-line arguments.
	 */
	public static void main(String[] args) {
		logger.info("Starting Hyperion...");
		try {
			new Server().bind(PORT).start();
		} catch (Exception e) {
			logger.severe("Error while starting server : " + e.getMessage());
		}
	}

	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(Server.class.getName());

	/**
	 * The <code>IoAcceptor</code> instance.
	 */
	private final IoAcceptor acceptor = new NioSocketAcceptor();

	/**
	 * The <code>GameEngine</code> instance.
	 */
	private static final GameEngine engine = new GameEngine();

	/**
	 * Cretaes the server and the <code>GameEngine</code> and initializes the
	 * <code>World</code>.
	 * @throws IOException if an I/O error occurs loading the world.
	 * @throws ClassNotFoundException if a class the world loads was not found.
	 * @throws IllegalAccessException if a class loaded by the world was not accessible.
	 * @throws InstantiationException if a class loaded by the world was not created.
	 */
	public Server() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		World.getWorld().init(engine);
		acceptor.setHandler(new ConnectionHandler());
		acceptor.getFilterChain().addFirst("throttleFilter", new ConnectionThrottleFilter());
	}

	/**
	 * Binds the server to the specified port.
	 * @param port The port to bind to.
	 * @return The server instance, for chaining.
	 * @throws IOException
	 */
	public Server bind(int port) throws IOException {
		logger.info("Binding to port : " + port + "...");
		acceptor.bind(new InetSocketAddress(port));
		return this;
	}

	/**
	 * Starts the <code>GameEngine</code>.
	 */
	public void start() {
		ScriptManager.getScriptManager().loadScripts(Constants.scriptsDirectory);
		engine.start();
		logger.info("Ready");
	}

	/**
	 * Gets the <code>GameEngine</code.
	 * @return
	 */
	public static GameEngine getEngine() {
		return engine;
	}
	
}
