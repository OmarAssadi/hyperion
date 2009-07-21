package org.hyperion;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.hyperion.js5.JS5Server;
import org.hyperion.rs2.RS2Server;

/**
 * A class to start both the update and game servers.
 * @author Graham
 *
 */
public class Server {
	
	/**
	 * The protocol version.
	 */
	public static final int VERSION = 317;
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(Server.class.getName());
	
	/**
	 * The entry point of the application.
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		logger.info("Starting Hyperion...");
		try {
			new JS5Server().bind().start();
			new RS2Server().bind(RS2Server.PORT).start();
		} catch(Exception ex) {
			logger.log(Level.SEVERE, "Error starting Hyperion.", ex);
		}
	}

}
