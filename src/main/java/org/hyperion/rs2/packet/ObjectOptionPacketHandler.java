package org.hyperion.rs2.packet;

import org.hyperion.rs2.action.impl.MiningAction;
import org.hyperion.rs2.action.impl.MiningAction.Node;
import org.hyperion.rs2.action.impl.ProspectingAction;
import org.hyperion.rs2.action.impl.WoodcuttingAction;
import org.hyperion.rs2.action.impl.WoodcuttingAction.Tree;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.Packet;

/**
 * Object option packet handler.
 *
 * @author Graham Edgecombe
 */
public class ObjectOptionPacketHandler implements PacketHandler {

    /**
     * Option 1 opcode.
     */
    private static final int OPTION_1 = 132, OPTION_2 = 252;

    @Override
    public void handle(final Player player, final Packet packet) {
        switch (packet.getOpcode()) {
            case OPTION_1 -> handleOption1(player, packet);
            case OPTION_2 -> handleOption2(player, packet);
        }
    }

    /**
     * Handles the option 1 packet.
     *
     * @param player The player.
     * @param packet The packet.
     */
    private void handleOption1(final Player player, final Packet packet) {
        final int x = packet.getLEShortA() & 0xFFFF;
        final int id = packet.getShort() & 0xFFFF;
        final int y = packet.getShortA() & 0xFFFF;
        final Location loc = Location.create(x, y, player.getLocation().getZ());
        // woodcutting
        final Tree tree = Tree.forId(id);
        if (tree != null && player.getLocation().isWithinInteractionDistance(loc)) {
            player.getActionQueue().addAction(new WoodcuttingAction(player, loc, tree));
        }
        // mining
        final Node node = Node.forId(id);
        if (node != null && player.getLocation().isWithinInteractionDistance(loc)) {
            player.getActionQueue().addAction(new MiningAction(player, loc, node));
        }
    }

    /**
     * Handles the option 2 packet.
     *
     * @param player The player.
     * @param packet The packet.
     */
    private void handleOption2(final Player player, final Packet packet) {
        final int id = packet.getLEShortA() & 0xFFFF;
        final int y = packet.getLEShort() & 0xFFFF;
        final int x = packet.getShortA() & 0xFFFF;
        final Location loc = Location.create(x, y, player.getLocation().getZ());
        final Node node = Node.forId(id);
        if (node != null && player.getLocation().isWithinInteractionDistance(loc)) {
            player.getActionQueue().addAction(new ProspectingAction(player, loc, node));
        }
    }


}
