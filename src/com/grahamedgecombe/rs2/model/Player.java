package com.grahamedgecombe.rs2.model;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.mina.core.session.IoSession;

import com.grahamedgecombe.rs2.model.UpdateFlags.UpdateFlag;
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
	private final WalkingQueue walkingQueue = new WalkingQueue(this);
	private final Queue<ChatMessage> chatMessages = new LinkedList<ChatMessage>();
	private ChatMessage currentChatMessage;
	private Location lastKnownRegion = this.getLocation();
	private boolean mapRegionChanging = false;
	
	/*
	 * Core login details.
	 */
	private final String name;
	private final int uid;
	private String pass;
	private Rights rights = Rights.PLAYER;
	private boolean members = true;
	
	/*
	 * Attributes.
	 */
	private Appearance appearance = new Appearance();
	private Equipment equipment = new Equipment();
	
	public Player(PlayerDetails details) {
		this.session = details.getSession();
		this.inCipher = details.getInCipher();
		this.outCipher = details.getOutCipher();
		this.name = details.getName();
		this.pass = details.getPassword();
		this.uid = details.getUID();
		
		this.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
	}
	
	public ChatMessage getCurrentChatMessage() {
		return currentChatMessage;
	}
	
	public void setCurrentChatMessage(ChatMessage currentChatMessage) {
		this.currentChatMessage = currentChatMessage;
	}
	
	public Queue<ChatMessage> getChatMessageQueue() {
		return chatMessages;
	}
	
	public boolean isMapRegionChanging() {
		return mapRegionChanging;
	}
	
	public void setMapRegionChanging(boolean mapRegionChanging) {
		this.mapRegionChanging = mapRegionChanging;
	}
	
	public WalkingQueue getWalkingQueue() {
		return walkingQueue;
	}
	
	public Appearance getAppearance() {
		return appearance;
	}
	
	public Equipment getEquipment() {
		return equipment;
	}
	
	public void setLastKnownRegion(Location lastKnownRegion) {
		this.lastKnownRegion = lastKnownRegion;
	}
	
	public Location getLastKnownRegion() {
		return lastKnownRegion;
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
	
	@Override
	public String toString() {
		return Player.class.getName() + " [name=" + name + " rights=" + rights + " members=" + members + " index=" + this.getIndex() + "]";
	}
	
}
