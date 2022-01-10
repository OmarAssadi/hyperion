package org.hyperion.rs2.action.impl;

import org.hyperion.rs2.action.impl.MiningAction.Node;
import org.hyperion.rs2.model.ItemDefinition;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.Player;

public class ProspectingAction extends InspectAction {

    /**
     * The delay.
     */
    private static final int DELAY = 3000;
    /**
     * The node type.
     */
    private final Node node;

    /**
     * Constructor.
     *
     * @param player
     * @param location
     * @param node
     */
    public ProspectingAction(final Player player, final Location location, final Node node) {
        super(player, location);
        this.node = node;
    }

    @Override
    public long getInspectDelay() {
        return DELAY;
    }

    @Override
    public void init() {
        final Player player = getPlayer();
        player.getActionSender().sendMessage("You examine the rock for ores...");
    }

    @Override
    public void giveRewards(final Player player) {
        player.getActionSender().sendMessage("This rock contains "
            + ItemDefinition.forId(node.getOreId()).getName().toLowerCase().replaceAll("ore", "").trim() + ".");
    }

}
