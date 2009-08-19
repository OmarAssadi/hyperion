package org.hyperion.rs2.model.container;

import org.hyperion.rs2.model.Item;
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
	 * The player inventory interface.
	 */
	public static final int PLAYER_INVENTORY_INTERFACE = 5064;
	
	/**
	 * Opens the bank for the specified player.
	 * @param player The player to open the bank for.
	 */
	public static void open(Player player) {
		player.getActionSender().sendInventoryInterface(5292, 5063);
		player.getInterfaceState().addListener(player.getBank(), new InterfaceContainerListener(player, 5382));
		player.getInterfaceState().addListener(player.getInventory(), new InterfaceContainerListener(player, 5064));
	}
	
	/**
	 * Deposits an item.
	 * @param player The player.
	 * @param slot The slot in the player's inventory.
	 * @param id The item id.
	 * @param amount The amount of the item to deposit.
	 */
	public static void deposit(Player player, int slot, int id, int amount) {
		Item item = player.getInventory().get(slot);
		if(item.getId() != id) {
			return; // invalid packet, or client out of sync
		}
		int transferAmount = player.getInventory().getCount(id);
		if(transferAmount >= amount) {
			transferAmount = amount;
		} else if(transferAmount == 0) {
			return; // invalid packet, or client out of sync
		}
		if(item.getDefinition().isStackable()) {
			if(player.getBank().freeSlots() < 1 && player.getBank().getById(item.getId()) == null) {
				player.getActionSender().sendMessage("Not enough room in your bank."); // TODO real messsage?
			}
			// we only need to remove from one stack
			int newInventoryAmount = item.getCount() - transferAmount;
			Item newItem;
			if(newInventoryAmount <= 0) {
				newItem = null;
			} else {
				newItem = new Item(item.getId(), newInventoryAmount);
			}
			player.getInventory().set(slot, newItem);
			player.getBank().add(new Item(item.getId(), transferAmount));
		} else {
			if(player.getBank().freeSlots() < transferAmount) {
				player.getActionSender().sendMessage("Not enough room in your bank."); // TODO real messsage?
			}
			// we need to remove multiple items
			for(int i = 0; i < transferAmount; i++) {
				if(i == 0) {
					player.getInventory().set(slot, null);
				} else {
					player.getInventory().set(player.getInventory().getSlotById(item.getId()), null);
				}
			}
			player.getBank().add(new Item(item.getId(), transferAmount));
		}
	}

}
