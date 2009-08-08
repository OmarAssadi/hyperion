package org.hyperion.rs2.model;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.mina.core.session.IoSession;
import org.hyperion.rs2.action.ActionQueue;
import org.hyperion.rs2.model.UpdateFlags.UpdateFlag;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.container.Inventory;
import org.hyperion.rs2.net.ActionSender;
import org.hyperion.rs2.net.ISAACCipher;
import org.hyperion.rs2.net.Packet;

/**
 * Represents a player-controller character.
 * @author Graham
 *
 */
public class Player extends Entity {
	
	/**
	 * Represents the rights of a player.
	 * @author Graham
	 *
	 */
	public enum Rights {
		
		/**
		 * A standard account.
		 */
		PLAYER(0),
		
		/**
		 * A player-moderator account.
		 */
		MODERATOR(1),
		
		/**
		 * An administrator account.
		 */
		ADMINISTRATOR(2);
		
		/**
		 * The integer representing this rights level.
		 */
		private int value;
		
		/**
		 * Creates a rights level.
		 * @param value The integer representing this rights level.
		 */
		private Rights(int value) {
			this.value = value;
		}
		
		/**
		 * Gets an integer representing this rights level.
		 * @return An integer representing this rights level.
		 */
		public int toInteger() {
			return value;
		}

		/**
		 * Gets rights by a specific integer.
		 * @param value The integer returned by {@link #toInteger()}.
		 * @return The rights level.
		 */
		public static Rights getRights(int value) {
			if(value == 1) {
				return MODERATOR;
			} else if(value == 2) {
				return ADMINISTRATOR;
			} else {
				return PLAYER;
			}
		}
	}
	
	/*
	 * Attributes specific to our session.
	 */
	
	/**
	 * The <code>IoSession</code>.
	 */
	private final IoSession session;
	
	/**
	 * The ISAAC cipher for incoming data.
	 */
	private final ISAACCipher inCipher;
	
	/**
	 * The ISAAC cipher for outgoing data.
	 */
	private final ISAACCipher outCipher;
	
	/**
	 * The action sender.
	 */
	private final ActionSender actionSender = new ActionSender(this);
	
	/**
	 * A queue of pending chat messages.
	 */
	private final Queue<ChatMessage> chatMessages = new LinkedList<ChatMessage>();
	
	/**
	 * A queue of actions.
	 */
	private final ActionQueue actionQueue = new ActionQueue();
	
	/**
	 * The current chat message.
	 */
	private ChatMessage currentChatMessage;
	
	/**
	 * Active flag: if the player is not active certain changes (e.g. items)
	 * should not send packets as that indicates the player is still loading. 
	 */
	private boolean active = false;
	
	/*
	 * Core login details.
	 */
	
	/**
	 * The name.
	 */
	private final String name;
	
	/**
	 * The UID, i.e. number in <code>random.dat</code>.
	 */
	private final int uid;
	
	/**
	 * The password.
	 */
	private String pass;
	
	/**
	 * The rights level.
	 */
	private Rights rights = Rights.PLAYER;
	
	/**
	 * The members flag.
	 */
	private boolean members = true;
	
	/*
	 * Attributes.
	 */
	
	/**
	 * The player's appearance information.
	 */
	private final Appearance appearance = new Appearance();
	
	/**
	 * The player's equipment.
	 */
	private final Container<Item> equipment = new Container<Item>(Equipment.SIZE);
	
	/**
	 * The player's skill levels.
	 */
	private final Skills skills = new Skills(this);
	
	/**
	 * The player's inventory.
	 */
	private final Container<Item> inventory = new Container<Item>(Inventory.SIZE);
	
	/*
	 * Cached details.
	 */
	/**
	 * The cached update block.
	 */
	private Packet cachedUpdateBlock;
	
	/**
	 * Creates a player based on the details object.
	 * @param details The details object.
	 */
	public Player(PlayerDetails details) {
		this.session = details.getSession();
		this.inCipher = details.getInCipher();
		this.outCipher = details.getOutCipher();
		this.name = details.getName();
		this.pass = details.getPassword();
		this.uid = details.getUID();
		this.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
		this.setTeleporting(true);
	}
	
	/**
	 * Checks if there is a cached update block for this cycle.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean hasCachedUpdateBlock() {
		return cachedUpdateBlock != null;
	}
	
	/**
	 * Sets the cached update block for this cycle.
	 * @param cachedUpdateBlock The cached update block.
	 */
	public void setCachedUpdateBlock(Packet cachedUpdateBlock) {
		this.cachedUpdateBlock = cachedUpdateBlock;
	}
	
	/**
	 * Gets the cached update block.
	 * @return The cached update block.
	 */
	public Packet getCachedUpdateBlock() {
		return cachedUpdateBlock;
	}
	
	/**
	 * Resets the cached update block.
	 */
	public void resetCachedUpdateBlock() {
		cachedUpdateBlock = null;
	}
	
	/**
	 * Gets the current chat message.
	 * @return The current chat message.
	 */
	public ChatMessage getCurrentChatMessage() {
		return currentChatMessage;
	}
	
	/**
	 * Sets the current chat message.
	 * @param currentChatMessage The current chat message to set.
	 */
	public void setCurrentChatMessage(ChatMessage currentChatMessage) {
		this.currentChatMessage = currentChatMessage;
	}
	
	/**
	 * Gets the queue of pending chat messages.
	 * @return The queue of pending chat messages.
	 */
	public Queue<ChatMessage> getChatMessageQueue() {
		return chatMessages;
	}
	
	/**
	 * Gets the player's appearance.
	 * @return The player's appearance.
	 */
	public Appearance getAppearance() {
		return appearance;
	}
	
	/**
	 * Gets the player's equipment.
	 * @return The player's equipment.
	 */
	public Container<Item> getEquipment() {
		return equipment;
	}
	
	/**
	 * Gets the player's skills.
	 * @return The player's skills.
	 */
	public Skills getSkills() {
		return skills;
	}
	
	/**
	 * Gets the action sender.
	 * @return The action sender.
	 */
	public ActionSender getActionSender() {
		return actionSender;
	}
	
	/**
	 * Gets the incoming ISAAC cipher.
	 * @return The incoming ISAAC cipher.
	 */
	public ISAACCipher getInCipher() {
		return inCipher;
	}
	
	/**
	 * Gets the outgoing ISAAC cipher.
	 * @return The outgoing ISAAC cipher.
	 */
	public ISAACCipher getOutCipher() {
		return outCipher;
	}
	
	/**
	 * Gets the player's name.
	 * @return The player's name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the player's password.
	 * @return The player's password.
	 */
	public String getPassword() {
		return pass;
	}
	
	/**
	 * Sets the player's password.
	 * @param pass The password.
	 */
	public void setPassword(String pass) {
		this.pass = pass;
	}
	
	/**
	 * Gets the player's UID.
	 * @return The player's UID.
	 */
	public int getUID() {
		return uid;
	}
	
	/**
	 * Gets the <code>IoSession</code>.
	 * @return The player's <code>IoSession</code>.
	 */
	public IoSession getSession() {
		return session;
	}
	
	/**
	 * Sets the rights.
	 * @param rights The rights level to set.
	 */
	public void setRights(Rights rights) {
		this.rights = rights;
	}

	/**
	 * Gets the rights.
	 * @return The player's rights.
	 */
	public Rights getRights() {
		return rights;
	}

	/**
	 * Checks if this player has a member's account.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isMembers() {
		return members;
	}
	
	/**
	 * Sets the members flag.
	 * @param members The members flag.
	 */
	public void setMembers(boolean members) {
		this.members = members;
	}
	
	@Override
	public String toString() {
		return Player.class.getName() + " [name=" + name + " rights=" + rights + " members=" + members + " index=" + this.getIndex() + "]";
	}
	
	/**
	 * Sets the active flag.
	 * @param active The active flag.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Gets the active flag.
	 * @return The active flag.
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * Gets the action queue.
	 * @return The action queue.
	 */
	public ActionQueue getActionQueue() {
		return actionQueue;
	}

	/**
	 * Gets the inventory.
	 * @return The inventory.
	 */
	public Container<Item> getInventory() {
		return inventory;
	}
	
}
