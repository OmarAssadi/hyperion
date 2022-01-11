package org.hyperion.rs2.model;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.GameEngine;
import org.hyperion.rs2.GenericWorldLoader;
import org.hyperion.rs2.WorldLoader;
import org.hyperion.rs2.WorldLoader.LoginResult;
import org.hyperion.rs2.event.Event;
import org.hyperion.rs2.event.EventManager;
import org.hyperion.rs2.event.impl.CleanupEvent;
import org.hyperion.rs2.event.impl.UpdateEvent;
import org.hyperion.rs2.login.LoginServerConnector;
import org.hyperion.rs2.login.LoginServerWorldLoader;
import org.hyperion.rs2.model.region.RegionManager;
import org.hyperion.rs2.net.PacketBuilder;
import org.hyperion.rs2.net.PacketManager;
import org.hyperion.rs2.packet.PacketHandler;
import org.hyperion.rs2.task.Task;
import org.hyperion.rs2.task.impl.SessionLoginTask;
import org.hyperion.rs2.util.ConfigurationParser;
import org.hyperion.rs2.util.EntityList;
import org.hyperion.rs2.util.NameUtils;
import org.hyperion.util.BlockingExecutorService;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Holds data global to the game world.
 *
 * @author Graham Edgecombe
 */
public class World {

    /**
     * Logging class.
     */
    private static final Logger logger = Logger.getLogger(World.class.getName());

    /**
     * World instance.
     */
    private static final World world = new World();
    /**
     * An executor service which handles background loading tasks.
     */
    private final BlockingExecutorService backgroundLoader = new BlockingExecutorService(Executors.newSingleThreadExecutor());
    /**
     * A list of connected players.
     */
    private final EntityList<Player> players = new EntityList<>(Constants.MAX_PLAYERS);
    /**
     * A list of active NPCs.
     */
    private final EntityList<NPC> npcs = new EntityList<>(Constants.MAX_NPCS);
    /**
     * The region manager.
     */
    private final RegionManager regionManager = new RegionManager();
    /**
     * The game engine.
     */
    private GameEngine engine;
    /**
     * The event manager.
     */
    private EventManager eventManager;
    /**
     * The current loader implementation.
     */
    private WorldLoader loader;
    /**
     * The game object manager.
     */
    private ObjectManager objectManager;

    /**
     * The login server connector.
     */
    private LoginServerConnector connector;

    /**
     * Creates the world and begins background loading tasks.
     */
    public World() {
        backgroundLoader.submit(() -> {
            objectManager = new ObjectManager();
            objectManager.load();
            return null;
        });
        backgroundLoader.submit(() -> {
            ItemDefinition.init();
            return null;
        });
    }

    /**
     * Gets the background loader.
     *
     * @return The background loader.
     */
    public BlockingExecutorService getBackgroundLoader() {
        return backgroundLoader;
    }

    /**
     * Gets the region manager.
     *
     * @return The region manager.
     */
    public RegionManager getRegionManager() {
        return regionManager;
    }

    /**
     * Initialises the world: loading configuration and registering global
     * events.
     *
     * @param engine The engine processing this world's tasks.
     * @throws IOException            if an I/O error occurs loading configuration.
     * @throws ClassNotFoundException if a class loaded through reflection was not found.
     * @throws IllegalAccessException if a class could not be accessed.
     * @throws InstantiationException if a class could not be created.
     * @throws IllegalStateException  if the world is already initialised.
     */
    public void init(final GameEngine engine) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (this.engine == null) {
            this.engine = engine;
            this.eventManager = new EventManager(engine);
            this.registerGlobalEvents();
            this.loadConfiguration();
        } else {
            throw new IllegalStateException("The world has already been initialised.");
        }
    }

    /**
     * Registers global events such as updating.
     */
    private void registerGlobalEvents() {
        submit(new UpdateEvent());
        submit(new CleanupEvent());
    }

    /**
     * Loads server configuration.
     *
     * @throws IOException            if an I/O error occurs.
     * @throws ClassNotFoundException if a class loaded through reflection was not found.
     * @throws IllegalAccessException if a class could not be accessed.
     * @throws InstantiationException if a class could not be created.
     */
    private void loadConfiguration() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        try (final FileInputStream fis = new FileInputStream("data/configuration.cfg")) {
            final ConfigurationParser p = new ConfigurationParser(fis);
            final Map<String, String> mappings = p.getMappings();
            if (mappings.containsKey("worldLoader")) {
                final String worldLoaderClass = mappings.get("worldLoader");
                final Class<?> loader = Class.forName(worldLoaderClass);
                this.loader = (WorldLoader) loader.getDeclaredConstructor().newInstance();
                logger.fine("WorldLoader set to : " + worldLoaderClass);
            } else {
                this.loader = new GenericWorldLoader();
                logger.fine("WorldLoader set to default");
            }
            final Map<String, Map<String, String>> complexMappings = p.getComplexMappings();
            if (complexMappings.containsKey("packetHandlers")) {
                final Map<Class<?>, Object> loadedHandlers = new HashMap<>();
                for (final Map.Entry<String, String> handler : complexMappings.get("packetHandlers").entrySet()) {
                    final int id = Integer.parseInt(handler.getKey());
                    final Class<?> handlerClass = Class.forName(handler.getValue());
                    final Object handlerInstance;
                    if (loadedHandlers.containsKey(handlerClass)) {
                        handlerInstance = loadedHandlers.get(loadedHandlers.get(handlerClass));
                    } else {
                        handlerInstance = handlerClass.getDeclaredConstructor().newInstance();
                    }
                    PacketManager.getPacketManager().bind(id, (PacketHandler) handlerInstance);
                    logger.fine("Bound " + handler.getValue() + " to opcode : " + id);
                }
            }
            if (loader instanceof LoginServerWorldLoader) {
                connector = new LoginServerConnector(mappings.get("loginServer"));
                connector.connect(mappings.get("nodePassword"), Integer.parseInt(mappings.get("nodeId")));
            }
        }
    }

    /**
     * Submits a new event.
     *
     * @param event The event to submit.
     */
    public void submit(final Event event) {
        this.eventManager.submit(event);
    }

    /**
     * Submits a new task.
     *
     * @param task The task to submit.
     */
    public void submit(final Task task) {
        this.engine.pushTask(task);
    }

    /**
     * Gets the object map.
     *
     * @return The object map.
     */
    public ObjectManager getObjectMap() {
        return objectManager;
    }

    /**
     * Gets the world loader.
     *
     * @return The world loader.
     */
    public WorldLoader getWorldLoader() {
        return loader;
    }

    /**
     * Gets the game engine.
     *
     * @return The game engine.
     */
    public GameEngine getEngine() {
        return engine;
    }

    /**
     * Loads a player's game in the work service.
     *
     * @param pd The player's details.
     */
    public void load(final PlayerDetails pd) {
        engine.submitWork(() -> {
            final LoginResult lr = loader.checkLogin(pd);
            int code = lr.getReturnCode();
            if (!NameUtils.isValidName(pd.getName())) {
                code = 11;
            }
            if (code == 2) {
                lr.getPlayer().getSession().setAttribute("player", lr.getPlayer());
                loader.loadPlayer(lr.getPlayer());
                engine.pushTask(new SessionLoginTask(lr.getPlayer()));
            } else {
                final PacketBuilder bldr = new PacketBuilder();
                bldr.put((byte) code);
                pd.getSession().write(bldr.toPacket()).addListener(future -> future.getSession().close(false));
            }
        });
    }

    /**
     * Registers a new npc.
     *
     * @param npc The npc to register.
     */
    public void register(final NPC npc) {
        npcs.add(npc);
    }

    /**
     * Unregisters an old npc.
     *
     * @param npc The npc to unregister.
     */
    public void unregister(final NPC npc) {
        npcs.remove(npc);
        npc.destroy();
    }

    /**
     * Registers a new player.
     *
     * @param player The player to register.
     */
    public void register(final Player player) {
        // do final checks e.g. is player online? is world full?
        int returnCode = 2;
        if (isPlayerOnline(player.getName())) {
            returnCode = 5;
        } else if (!players.add(player)) {
            returnCode = 7;
            logger.info("Could not register player : " + player + " [world full]");
        }
        final int fReturnCode = returnCode;
        final PacketBuilder bldr = new PacketBuilder();
        bldr.put((byte) returnCode);
        bldr.put((byte) player.getRights().toInteger());
        bldr.put((byte) 0);
        player.getSession().write(bldr.toPacket()).addListener(future -> {
            if (fReturnCode == 2) {
                player.getActionSender().sendLogin();
            } else {
                player.getSession().close(false);
            }
        });
        if (returnCode == 2) {
            logger.info("Registered player : " + player + " [online=" + players.size() + "]");
        }
    }

    /**
     * Checks if a player is online.
     *
     * @param name The player's name.
     * @return <code>true</code> if they are online, <code>false</code> if not.
     */
    public boolean isPlayerOnline(String name) {
        name = NameUtils.formatName(name);
        for (final Player player : players) {
            if (player.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the player list.
     *
     * @return The player list.
     */
    public EntityList<Player> getPlayers() {
        return players;
    }

    /**
     * Gets the npc list.
     *
     * @return The npc list.
     */
    public EntityList<NPC> getNPCs() {
        return npcs;
    }

    /**
     * Unregisters a player, and saves their game.
     *
     * @param player The player to unregister.
     */
    public void unregister(final Player player) {
        player.getActionQueue().cancelQueuedActions();
        player.destroy();
        player.getSession().close(false);
        players.remove(player);
        logger.info("Unregistered player : " + player + " [online=" + players.size() + "]");
        engine.submitWork(() -> {
            loader.savePlayer(player);
            if (World.getWorld().getLoginServerConnector() != null) {
                World.getWorld().getLoginServerConnector().disconnected(player.getName());
            }
        });
    }

    /**
     * Gets the login server connector.
     *
     * @return The login server connector.
     */
    public LoginServerConnector getLoginServerConnector() {
        return connector;
    }

    /**
     * Gets the world instance.
     *
     * @return The world instance.
     */
    public static World getWorld() {
        return world;
    }

    /**
     * Handles an exception in any of the pools.
     *
     * @param t The exception.
     */
    public void handleError(final Throwable t) {
        logger.severe("An error occurred in an executor service! The server will be halted immediately.");
        t.printStackTrace();
        System.exit(1);
    }

}
