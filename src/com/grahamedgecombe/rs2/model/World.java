package com.grahamedgecombe.rs2.model;

import java.util.logging.Logger;

import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;

import com.grahamedgecombe.rs2.GameEngine;
import com.grahamedgecombe.rs2.GenericWorldLoader;
import com.grahamedgecombe.rs2.WorldLoader;
import com.grahamedgecombe.rs2.WorldLoader.LoginResult;
import com.grahamedgecombe.rs2.net.PacketBuilder;
import com.grahamedgecombe.rs2.task.SessionLoginTask;

public class World {
	
	private static final Logger logger = Logger.getLogger(World.class.getName());
	private static final World world = new World();
	
	public static World getWorld() {
		return world;
	}

	private GameEngine engine;
	private WorldLoader loader = new GenericWorldLoader();
	
	public void init(GameEngine engine) {
		if(this.engine != null) {
			throw new IllegalStateException("The world has already been initialized.");
		} else {
			this.engine = engine;
		}
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
		PacketBuilder bldr = new PacketBuilder();
		bldr.put((byte) returnCode);
		bldr.put((byte) player.getRights().toInteger());
		bldr.put((byte) 0);
		player.getSession().write(bldr.toPacket()).addListener(new IoFutureListener<IoFuture>() {
			@Override
			public void operationComplete(IoFuture future) {
				player.getActionSender().sendLogin();
			}
		});
		if(returnCode == 2) {
			logger.info("Registered player : " + player);
		}
	}
	
	public void unregister(Player player) {
		player.getSession().close(false);
		logger.info("Unregistered player : " + player);
	}
	
}
