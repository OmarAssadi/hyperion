package com.grahamedgecombe.rs2.task;

import java.util.logging.Logger;

import org.apache.mina.core.session.IoSession;

import com.grahamedgecombe.rs2.GameEngine;

public class SessionOpenedTask implements Task {
	
	private static final Logger logger = Logger.getLogger(SessionOpenedTask.class.getName());
	private IoSession session;

	public SessionOpenedTask(IoSession session) {
		this.session = session;
	}

	@Override
	public void execute(GameEngine context) {
		logger.info("Session opened : " + session.getRemoteAddress());
	}

}
