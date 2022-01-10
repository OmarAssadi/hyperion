package org.hyperion.ls;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.hyperion.rs2.GenericWorldLoader;
import org.hyperion.rs2.WorldLoader;
import org.hyperion.util.NetworkConstants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

/**
 * The login server.
 *
 * @author Graham Edgecombe
 */
public class LoginServer {

    /**
     * Logger instance.
     */
    private static final Logger logger = Logger.getLogger(LoginServer.class.getName());

    /**
     * The acceptor.
     */
    private final IoAcceptor acceptor = new NioSocketAcceptor();

    /**
     * The task queue.
     */
    private final BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();

    /**
     * Login server loader.
     */
    private final WorldLoader loader = new GenericWorldLoader();

    /**
     * Creates the login server.
     */
    public LoginServer() {
        logger.info("Starting Hyperion Login Server...");
        acceptor.setHandler(new LoginConnectionHandler(this));
    }

    /**
     * The entry point of the program.
     *
     * @param args The command line arguments.
     * @throws IOException if an I/O error occurs.
     */
    public static void main(final String[] args) throws IOException {
        new LoginServer().bind().start();
    }

    /**
     * Starts the login server.
     */
    public void start() {
        logger.info("Ready.");
        while (true) {
            try {
                tasks.take().run();
            } catch (final InterruptedException ignored) {
            }
        }
    }

    /**
     * Binds the login server to the default port.
     *
     * @return The login server instance, for chaining.
     * @throws IOException if an I/O error occurs.
     */
    public LoginServer bind() throws IOException {
        logger.info("Binding to port : " + NetworkConstants.LOGIN_PORT + "...");
        acceptor.bind(new InetSocketAddress(NetworkConstants.LOGIN_PORT));
        return this;
    }

    /**
     * Pushses a task onto the queue.
     *
     * @param runnable The runnable.
     */
    public void pushTask(final Runnable runnable) {
        tasks.add(runnable);
    }

    /**
     * Gets the login server loader.
     *
     * @return The loader.
     */
    public WorldLoader getLoader() {
        return loader;
    }

}
