package com.grahamedgecombe.rs2.task;

import java.net.SocketAddress;
import java.util.logging.Logger;

import org.apache.mina.core.session.IoSession;

import com.grahamedgecombe.rs2.GameEngine;
import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.model.World;

/**
 * A task that is executed when a session is closed.
 * @author Graham
 *
 */
public class SessionClosedTask implements Task {

	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(SessionClosedTask.class.getName());
	
	/**
	 * The session that closed.
	 */
	private IoSession session;
	
	/**
	 * Creates the session closed task.
	 * @param session The session.
	 */
	public SessionClosedTask(IoSession session) {
		this.session = session;
	}

	@Override
	public void execute(GameEngine context) {
		SocketAddress address = (SocketAddress) session.getAttribute("remote");
		logger.info("Session closed : " + address);
		if(session.containsAttribute("player")) {
			Player p = (Player) session.getAttribute("player");
			World.getWorld().unregister(p);
		}
	}

}
