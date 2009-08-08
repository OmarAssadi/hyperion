package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.trigger.TriggerManager;
import org.hyperion.rs2.trigger.impl.cond.CommandCondition;

/**
 * Handles player commands (the ::words).
 * @author Graham
 *
 */
public class CommandPacketHandler implements PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		String commandString = packet.getRS2String();
		String[] args = commandString.split(" ");
		String command = args[0].toLowerCase();
		try {
			if(command.equals("tele")) {
				if(args.length == 3 || args.length == 4) {
					int x = Integer.parseInt(args[1]);
					int y = Integer.parseInt(args[2]);
					int z = player.getLocation().getZ();
					if(args.length == 4) {
						z = Integer.parseInt(args[3]);
					}
					player.setTeleportTarget(Location.create(x, y, z));
				} else {
					player.getActionSender().sendMessage("Syntax is ::tele [x] [y] [z].");
				}
			} else if(command.equals("item")) {
				if(args.length == 2 || args.length == 3) {
					int id = Integer.parseInt(args[1]);
					int count = 1;
					if(args.length == 3) {
						count = Integer.parseInt(args[2]);
					}
					player.getInventory().add(new Item(id, count));
				} else {
					player.getActionSender().sendMessage("Syntax is ::item [id] [count].");
				}
			} else {
				TriggerManager.getTriggerManager().fire(player, new CommandCondition(command));
			}
		} catch(Exception ex) {
			player.getActionSender().sendMessage("Error while processing command.");
		}
	}

}
