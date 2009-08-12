package org.hyperion.rs2.model;

import java.util.ArrayList;
import java.util.List;

import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.ContainerListener;

/**
 * Contains information about the state of interfaces open in the client.
 * @author Graham Edgecombe
 *
 */
public class InterfaceState {
	
	/**
	 * The current open interface.
	 */
	private int currentInterface = -1;
	
	/**
	 * The player.
	 */
	private Player player;
		
	/**
	 * A list of container listeners used on interfaces that have containers.
	 */
	private List<ContainerListener> containerListeners = new ArrayList<ContainerListener>();
	
	/**
	 * Creates the interface state.
	 */
	public InterfaceState(Player player) {
		this.player = player;
	}
	
	/**
	 * Checks if the specified interface is open.
	 * @param id The interface id.
	 * @return <code>true</code> if the interface is open, <code>false</code> if not.
	 */
	public boolean isInterfaceOpen(int id) {
		return currentInterface == id;
	}
	
	/**
	 * Gets the current open interface.
	 * @return The current open interface.
	 */
	public int getCurrentInterface() {
		return currentInterface;
	}
	
	/**
	 * Called when an interface is opened.
	 * @param id The interface.
	 */
	public void interfaceOpened(int id) {
		if(currentInterface != -1) {
			interfaceClosed();
		}
		currentInterface = id;
	}
	
	/**
	 * Called when an interface is closed.
	 */
	public void interfaceClosed() {
		currentInterface = -1;
		for(ContainerListener l : containerListeners) {
			player.getInventory().removeListener(l);
			player.getEquipment().removeListener(l);
			player.getBank().removeListener(l);
		}
	}

	/**
	 * Adds a listener to an interface that is closed when the inventory is closed.
	 * @param container The container.
	 * @param containerListener The listener.
	 */
	public void addListener(Container container, ContainerListener containerListener) {
		container.addListener(containerListener);
		containerListeners.add(containerListener);
	}

}
