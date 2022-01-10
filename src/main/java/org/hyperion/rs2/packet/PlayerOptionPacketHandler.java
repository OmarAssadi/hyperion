package org.hyperion.rs2.packet;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.action.impl.AttackAction;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.net.Packet;

public class PlayerOptionPacketHandler implements PacketHandler {

    @Override
    public void handle(final Player player, final Packet packet) {
        switch (packet.getOpcode()) {
            case 128 ->
                /*
                 * Option 1.
                 */
                option1(player, packet);
            case 37 ->
                /*
                 * Option 2.
                 */
                option2(player, packet);
            case 227 ->
                /*
                 * Option 3.
                 */
                option3(player, packet);
        }
    }

    /**
     * Handles the first option on a player option menu.
     *
     * @param player
     * @param packet
     */
    private void option1(final Player player, final Packet packet) {
        final int id = packet.getShort() & 0xFFFF;
        if (id < 0 || id >= Constants.MAX_PLAYERS) {
            return;
        }
        final Player victim = (Player) World.getWorld().getPlayers().get(id);
        if (victim != null && player.getLocation().isWithinInteractionDistance(victim.getLocation())) {
            player.getActionQueue().addAction(new AttackAction(player, victim));
        }
    }

    /**
     * Handles the second option on a player option menu.
     *
     * @param player
     * @param packet
     */
    private void option2(final Player player, final Packet packet) {
        final int id = packet.getShort() & 0xFFFF;
        if (id < 0 || id >= Constants.MAX_PLAYERS) {
        }
    }

    /**
     * Handles the third option on a player option menu.
     *
     * @param player
     * @param packet
     */
    private void option3(final Player player, final Packet packet) {
        final int id = packet.getLEShortA() & 0xFFFF;
        if (id < 0 || id >= Constants.MAX_PLAYERS) {
        }
    }

}

