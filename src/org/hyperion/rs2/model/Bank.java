package org.hyperion.rs2.model;

import org.hyperion.rs2.model.container.impl.InterfaceContainerListener;

/**
 * Banking utility class.
 * @author Graham Edgecombe
 *
 */
public class Bank {
	
	/**
	 * The bank size.
	 */
	public static final int SIZE = 800;
	
	/**
	 * Bank interface.
	 */
	public static final int INTERFACE = 5292;
	
	/**
	 * The actual bank inventory interface.
	 */
	public static final int BANK_INVENTORY_INTERFACE = 5063;
	
	/**
	 * The inventory interface with banking options.
	 */
	public static final int PLAYER_INVENTORY_INTERFACE = 5064;
	
	/**
	 * Opens the bank for the specified player.
	 * @param player The player to open the bank for.
	 */
	public static void open(Player player) {
		player.getActionSender().sendInventoryInterface(INTERFACE, BANK_INVENTORY_INTERFACE);
		player.getInterfaceState().addListener(player.getInventory(), new InterfaceContainerListener(player, PLAYER_INVENTORY_INTERFACE));
	}

}
