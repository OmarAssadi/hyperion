package org.hyperion;

import org.hyperion.fileserver.FileServer;
import org.hyperion.rs2.RS2Server;
import org.hyperion.rs2.model.World;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class to start both the file and game servers.
 *
 * @author Graham Edgecombe
 */
public final class Server {

    /**
     * Private constructor to prevent instantiation.
     */
    private Server() {

    }

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
     *
     * @param args The command line arguments.
     */
    public static void main(final String[] args) {
        logger.info("Starting Hyperion...");
        World.getWorld(); // this starts off background loading
        try {
            new FileServer().bind().start();
            new RS2Server().bind(RS2Server.PORT).start();
        } catch (final Exception ex) {
            logger.log(Level.SEVERE, "Error starting Hyperion.", ex);
            System.exit(1);
        }
    }

}
