package org.hyperion.rs2.model.container;

import org.hyperion.rs2.model.Player;
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
	public static final int SIZE = 352;
	
	/**
	 * Opens the bank for the specified player.
	 * @param player The player to open the bank for.
	 */
	public static void open(Player player) {
		player.getActionSender().sendInventoryInterface(5292, 5063);
		player.getInterfaceState().addListener(player.getBank(), new InterfaceContainerListener(player, 5382));
		player.getInterfaceState().addListener(player.getInventory(), new InterfaceContainerListener(player, 5064));
	}

}
