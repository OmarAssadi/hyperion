package com.grahamedgecombe.rs2.model;

import org.apache.mina.core.session.IoSession;

import com.grahamedgecombe.rs2.net.ActionSender;
import com.grahamedgecombe.rs2.net.ISAACCipher;

public class Player extends Entity {
	
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
	
	/*
	 * Attributes specific to our session.
	 */
	private final IoSession session;
	private final ISAACCipher inCipher;
	private final ISAACCipher outCipher;
	private final ActionSender actionSender = new ActionSender(this);
	private Location lastKnownRegion;
	
	/*
	 * Core login details.
	 */
	private final String name;
	private final int uid;
	private String pass;
	private Rights rights = Rights.PLAYER;
	private boolean members = true;
	private boolean active = false;
	
	public Player(PlayerDetails details) {
		this.session = details.getSession();
		this.inCipher = details.getInCipher();
		this.outCipher = details.getOutCipher();
		this.name = details.getName();
		this.pass = details.getPassword();
		this.uid = details.getUID();
	}
	
	public void setLastKnownRegion(Location lastKnownRegion) {
		this.lastKnownRegion = lastKnownRegion;
	}
	
	public Location getLastKnownRegion() {
		return lastKnownRegion;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public ActionSender getActionSender() {
		return actionSender;
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

	public boolean isMembers() {
		return members;
	}
	
	public void setMembers(boolean members) {
		this.members = members;
	}

}
