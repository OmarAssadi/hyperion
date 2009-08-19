package org.hyperion.rs2.packet;

import java.util.logging.Logger;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.Packet;

/**
 * Handles clicking on most buttons in the interface.
 * @author Graham Edgecombe
 *
 */
public class ActionButtonPacketHandler implements PacketHandler {

	/**
	 * The logger instance.
	 */
	private static final Logger logger = Logger.getLogger(ActionButtonPacketHandler.class.getName());
	
	@Override
	public void handle(Player player, Packet packet) {
		final int button = packet.getShort();
		switch(button) {
		case 2458:
			player.getActionSender().sendLogout();
			break;
		case 5387:
			player.getSettings().setWithdrawAsNotes(false);
			break;
		case 5386:
			player.getSettings().setWithdrawAsNotes(true);
			break;
		case 8130:
			player.getSettings().setSwapping(true);
			break;
		case 8131:
			player.getSettings().setSwapping(false);
			break;
		default:
			logger.info("Unhandled action button : " + button);
			break;
		}
	}

}
