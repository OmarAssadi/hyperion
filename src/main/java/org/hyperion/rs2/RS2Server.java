package org.hyperion.rs2;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.hyperion.rs2.model.World;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

/**
 * Starts everything else including MINA and the <code>GameEngine</code>.
 *
 * @author Graham Edgecombe
 */
public class RS2Server {

    /**
     * The port to listen on.
     */
    public static final int PORT = 43594;

    /**
     * Logger instance.
     */
    private static final Logger logger = Logger.getLogger(RS2Server.class.getName());
    /**
     * The <code>GameEngine</code> instance.
     */
    private static final GameEngine engine = new GameEngine();
    /**
     * The <code>IoAcceptor</code> instance.
     */
    private final IoAcceptor acceptor = new NioSocketAcceptor();

    /**
     * Creates the server and the <code>GameEngine</code> and initializes the
     * <code>World</code>.
     *
     * @throws IOException            if an I/O error occurs loading the world.
     * @throws ClassNotFoundException if a class the world loads was not found.
     * @throws IllegalAccessException if a class loaded by the world was not accessible.
     * @throws InstantiationException if a class loaded by the world was not created.
     */
    public RS2Server() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        World.getWorld().init(engine);
        acceptor.setHandler(new ConnectionHandler());
        //acceptor.getFilterChain().addFirst("throttleFilter", new ConnectionThrottleFilter());
    }

    /**
     * Gets the <code>GameEngine</code>.
     *
     * @return The game engine.
     */
    public static GameEngine getEngine() {
        return engine;
    }

    /**
     * Binds the server to the specified port.
     *
     * @param port The port to bind to.
     * @return The server instance, for chaining.
     * @throws IOException
     */
    public RS2Server bind(final int port) throws IOException {
        logger.info("Binding to port : " + port + "...");
        acceptor.bind(new InetSocketAddress(port));
        return this;
    }

    /**
     * Starts the <code>GameEngine</code>.
     *
     * @throws ExecutionException if an error occured during background loading.
     */
    public void start() throws ExecutionException, IOException {
        ScriptManager.getScriptManager().loadScripts(Constants.SCRIPTS_DIRECTORY);
        if (World.getWorld().getBackgroundLoader().getPendingTaskAmount() > 0) {
            logger.info("Waiting for pending background loading tasks...");
            World.getWorld().getBackgroundLoader().waitForPendingTasks();
        }
        World.getWorld().getBackgroundLoader().shutdown();
        engine.start();
        logger.info("Ready");
    }

}
