package com.grahamedgecombe.rs2.task.impl;

import java.util.logging.Logger;

import org.apache.mina.core.session.IoSession;

import com.grahamedgecombe.rs2.GameEngine;
import com.grahamedgecombe.rs2.task.Task;

/**
 * A task that is executed when a session is opened.
 * @author Graham
 *
 */
public class SessionOpenedTask implements Task {
	
	/**
	 * The logger class.
	 */
	private static final Logger logger = Logger.getLogger(SessionOpenedTask.class.getName());
	
	/**
	 * The session.
	 */
	private IoSession session;

	/**
	 * Creates the session opened task.
	 * @param session The session.
	 */
	public SessionOpenedTask(IoSession session) {
		this.session = session;
	}

	@Override
	public void execute(GameEngine context) {
		logger.fine("Session opened : " + session.getRemoteAddress());
	}

}
