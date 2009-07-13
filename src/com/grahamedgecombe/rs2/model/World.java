package com.grahamedgecombe.rs2.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;

import com.grahamedgecombe.rs2.Constants;
import com.grahamedgecombe.rs2.GameEngine;
import com.grahamedgecombe.rs2.GenericWorldLoader;
import com.grahamedgecombe.rs2.WorldLoader;
import com.grahamedgecombe.rs2.WorldLoader.LoginResult;
import com.grahamedgecombe.rs2.event.CleanupEvent;
import com.grahamedgecombe.rs2.event.Event;
import com.grahamedgecombe.rs2.event.EventManager;
import com.grahamedgecombe.rs2.event.UpdateEvent;
import com.grahamedgecombe.rs2.net.PacketBuilder;
import com.grahamedgecombe.rs2.net.PacketManager;
import com.grahamedgecombe.rs2.packet.PacketHandler;
import com.grahamedgecombe.rs2.task.SessionLoginTask;
import com.grahamedgecombe.rs2.task.Task;
import com.grahamedgecombe.rs2.util.ConfigurationParser;
import com.grahamedgecombe.rs2.util.EntityList;
import com.grahamedgecombe.rs2.util.NameUtils;

/**
 * Holds data global to the game world.
 * @author Graham
 *
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
	 * Gets the world instance.
	 * @return The world instance.
	 */
	public static World getWorld() {
		return world;
	}

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
	 * The region manager.
	 */
	private RegionManager regionManager = new RegionManager();
	
	/**
	 * A list of connected players.
	 */
	private EntityList<Player> players = new EntityList<Player>(Constants.MAX_PLAYERS);
	
	/**
	 * Initialises the world: loading configuration and registering global
	 * events.
	 * @param engine The engine processing this world's tasks.
	 * @throws IOException if an I/O error occurs loading configuration.
	 * @throws ClassNotFoundException if a class loaded through reflection was not found.
	 * @throws IllegalAccessException if a class could not be accessed.
	 * @throws InstantiationException if a class could not be created.
	 * @throws IllegalStateException if the world is already initialised.
	 */
	public void init(GameEngine engine) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		if(this.engine != null) {
			throw new IllegalStateException("The world has already been initialised.");
		} else {
			this.engine = engine;
			this.eventManager = new EventManager(engine);
			this.registerEvents();
			this.loadConfiguration();
		}
	}
	
	/**
	 * Loads server configuration.
	 * @throws IOException if an I/O error occurs.
	 * @throws ClassNotFoundException if a class loaded through reflection was not found.
	 * @throws IllegalAccessException if a class could not be accessed.
	 * @throws InstantiationException if a class could not be created.
	 */
	private void loadConfiguration() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		FileInputStream fis = new FileInputStream("data/configuration.cfg");
		try {
			ConfigurationParser p = new ConfigurationParser(fis);
			Map<String, String> mappings = p.getMappings();
			if(mappings.containsKey("worldLoader")) {
				String worldLoaderClass = mappings.get("worldLoader");
				Class<?> loader = Class.forName(worldLoaderClass);
				this.loader = (WorldLoader) loader.newInstance();
				logger.fine("WorldLoader set to : " + worldLoaderClass);
			} else {
				this.loader = new GenericWorldLoader();
				logger.fine("WorldLoader set to default");
			}
			Map<String, Map<String, String>> complexMappings = p.getComplexMappings();
			if(complexMappings.containsKey("packetHandlers")) {
				Map<Class<?>, Object> loadedHandlers = new HashMap<Class<?>, Object>();
				for(Map.Entry<String, String> handler : complexMappings.get("packetHandlers").entrySet()) {
					int id = Integer.parseInt(handler.getKey());
					Class<?> handlerClass = Class.forName(handler.getValue());
					Object handlerInstance;
					if(loadedHandlers.containsKey(handlerClass)) {
						handlerInstance = loadedHandlers.get(loadedHandlers.get(handlerClass));
					} else {
						handlerInstance = handlerClass.newInstance();
					}
					PacketManager.getPacketManager().bind(id, (PacketHandler) handlerInstance);
					logger.fine("Bound " + handler.getValue() + " to opcode : " + id);
				}
			}
		} finally {
			fis.close();
		}
	}
	
	/**
	 * Registers global events such as updating.
	 */
	private void registerEvents() {
		submit(new UpdateEvent());
		submit(new CleanupEvent());
	}
	
	/**
	 * Submits a new event.
	 * @param event The event to submit.
	 */
	public void submit(Event event) {
		this.eventManager.submit(event);
	}
	
	/**
	 * Submits a new task.
	 * @param task The task to submit.
	 */
	public void submit(Task task) {
		this.engine.pushTask(task);
	}
	
	/**
	 * Gets the world loader.
	 * @return The world loader.
	 */
	public WorldLoader getWorldLoader() {
		return loader;
	}
	
	/**
	 * Gets the game engine.
	 * @return The game engine.
	 */
	public GameEngine getEngine() {
		return engine;
	}
	
	/**
	 * Gets the region manager.
	 * @return The region manager.
	 */
	public RegionManager getRegionManager() {
		return regionManager;
	}
	
	/**
	 * Loads a player's game in the work service.
	 * @param pd The player's details.
	 */
	public void load(final PlayerDetails pd) {
		engine.submitWork(new Runnable() {
			public void run() {
				LoginResult lr = loader.checkLogin(pd);
				int code = lr.getReturnCode();
				if(!NameUtils.isValidName(pd.getName())) {
					code = 11;
				}
				if(code != 2) {
					PacketBuilder bldr = new PacketBuilder();
					bldr.put((byte) code);
					bldr.put((byte) 0);
					bldr.put((byte) 0);
					pd.getSession().write(bldr.toPacket()).addListener(new IoFutureListener<IoFuture>() {
						@Override
						public void operationComplete(IoFuture future) {
							future.getSession().close(false);
						}
					});
				} else {
					lr.getPlayer().getSession().setAttribute("player", lr.getPlayer());
					loader.loadPlayer(lr.getPlayer());
					engine.pushTask(new SessionLoginTask(lr.getPlayer()));
				}
			}
		});
	}

	/**
	 * Registers a new player.
	 * @param player The player to register.
	 */
	public void register(final Player player) {
		// do final checks e.g. is player online? is world full?
		int returnCode = 2;
		if(isPlayerOnline(player.getName())) {
			returnCode = 5;
		}
		if(!players.add(player)) {
			returnCode = 7;
			logger.info("Could not register player : " + player + " [world full]");
		}
		final int fReturnCode = returnCode;
		PacketBuilder bldr = new PacketBuilder();
		bldr.put((byte) returnCode);
		bldr.put((byte) player.getRights().toInteger());
		bldr.put((byte) 0);
		player.getSession().write(bldr.toPacket()).addListener(new IoFutureListener<IoFuture>() {
			@Override
			public void operationComplete(IoFuture future) {
				if(fReturnCode != 2) {
					player.getSession().close(false);
				} else {
					player.getActionSender().sendLogin();
				}
			}
		});
		if(returnCode == 2) {
			logger.info("Registered player : " + player + " [online=" + players.size() + "]");
		}
	}
	
	/**
	 * Gets the player list.
	 * @return The player list.
	 */
	public EntityList<Player> getPlayers() {
		return players;
	}
	
	/**
	 * Checks if a player is online.
	 * @param name The player's name.
	 * @return <code>true</code> if they are online, <code>false</code> if not.
	 */
	public boolean isPlayerOnline(String name) {
		name = NameUtils.formatName(name);
		for(Player player : players) {
			if(player.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Unregisters a player, and saves their game.
	 * @param player The player to unregister.
	 */
	public void unregister(final Player player) {
		player.getSession().close(false);
		player.unsetRegion();
		players.remove(player);
		logger.info("Unregistered player : " + player + " [online=" + players.size() + "]");
		engine.submitWork(new Runnable() {
			public void run() {
				loader.savePlayer(player);
			}
		});
	}

	/**
	 * Handles an exception in any of the pools.
	 * @param t The exception.
	 */
	public void handleError(Throwable t) {
		logger.severe("An error occurred in an executor service! The server will be halted immediately.");
		t.printStackTrace();
		System.exit(1);
	}
	
}
