package com.grahamedgecombe.rs2.model;

import java.util.logging.Logger;

import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;

import com.grahamedgecombe.rs2.Constants;
import com.grahamedgecombe.rs2.GameEngine;
import com.grahamedgecombe.rs2.GenericWorldLoader;
import com.grahamedgecombe.rs2.WorldLoader;
import com.grahamedgecombe.rs2.WorldLoader.LoginResult;
import com.grahamedgecombe.rs2.event.Event;
import com.grahamedgecombe.rs2.event.EventManager;
import com.grahamedgecombe.rs2.event.UpdateEvent;
import com.grahamedgecombe.rs2.net.PacketBuilder;
import com.grahamedgecombe.rs2.task.SessionLoginTask;
import com.grahamedgecombe.rs2.task.Task;
import com.grahamedgecombe.rs2.util.EntityList;

public class World {
	
	private static final Logger logger = Logger.getLogger(World.class.getName());
	private static final World world = new World();
	
	public static World getWorld() {
		return world;
	}

	private GameEngine engine;
	private EventManager eventManager;
	private WorldLoader loader = new GenericWorldLoader();
	private EntityList<Player> players = new EntityList<Player>(Constants.MAX_PLAYERS);
	
	public void init(GameEngine engine) {
		if(this.engine != null) {
			throw new IllegalStateException("The world has already been initialized.");
		} else {
			this.engine = engine;
			this.eventManager = new EventManager(engine);
			this.registerEvents();
		}
	}
	
	public void registerEvents() {
		submit(new UpdateEvent());
	}
	
	public void submit(Event event) {
		this.eventManager.submit(event);
	}
	
	public void submit(Task task) {
		this.engine.pushTask(task);
	}
	
	public WorldLoader getWorldLoader() {
		return loader;
	}
	
	public GameEngine getEngine() {
		return engine;
	}
	
	public void load(final PlayerDetails pd) {
		engine.getWorkService().submit(new Runnable() {
			public void run() {
				LoginResult lr = loader.checkLogin(pd);
				if(lr.getReturnCode() != 2) {
					PacketBuilder bldr = new PacketBuilder();
					bldr.put((byte) lr.getReturnCode());
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
	
	public EntityList<Player> getPlayers() {
		return players;
	}
	
	public boolean isPlayerOnline(String name) {
		for(Player player : players) {
			if(player.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public void unregister(final Player player) {
		player.getSession().close(false);
		players.remove(player);
		logger.info("Unregistered player : " + player + " [online=" + players.size() + "]");
		engine.getWorkService().submit(new Runnable() {
			public void run() {
				loader.savePlayer(player);
			}
		});
	}
	
}
