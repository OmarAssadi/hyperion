package com.grahamedgecombe.rs2.model;

import org.apache.mina.core.session.IoSession;

public class Player {
	
	public enum Rights {
		PLAYER(0),
		MODERATOR(1),
		ADMINISTRATOR(2);
		
		private int value;
		
		private Rights(int value) {
			this.value = value;
		}
		
		public int toInteger() {
			return value;
		}
	}
	
	private IoSession session;
	private Rights rights = Rights.PLAYER;
	
	public Player(IoSession session) {
		this.session = session;
	}
	
	public IoSession getSession() {
		return session;
	}
	
	public void setRights(Rights rights) {
		this.rights = rights;
	}

	public Rights getRights() {
		return rights;
	}

}
