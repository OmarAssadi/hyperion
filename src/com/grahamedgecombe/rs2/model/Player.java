package com.grahamedgecombe.rs2.model;

import org.apache.mina.core.session.IoSession;

import com.grahamedgecombe.rs2.net.ISAACCipher;

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
	
	private final IoSession session;
	private final ISAACCipher inCipher;
	private final ISAACCipher outCipher;
	private final String name;
	private String pass;
	private final int uid;
	private Rights rights = Rights.PLAYER;
	
	public Player(PlayerDetails details) {
		this.session = details.getSession();
		this.inCipher = details.getInCipher();
		this.outCipher = details.getOutCipher();
		this.name = details.getName();
		this.pass = details.getPassword();
		this.uid = details.getUID();
	}
	
	public ISAACCipher getInCipher() {
		return inCipher;
	}
	
	public ISAACCipher getOutCipher() {
		return outCipher;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPassword() {
		return pass;
	}
	
	public void setPassword(String pass) {
		this.pass = pass;
	}
	
	public int getUID() {
		return uid;
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
