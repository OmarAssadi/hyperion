package org.hyperion.rs2.action.impl;

import org.hyperion.rs2.action.Action;
import org.hyperion.rs2.action.impl.ProspectingAction.Node;
import org.hyperion.rs2.model.ItemDefinition;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.Player;

public abstract class InspectAction extends Action {
    
    /**
     * The location.
     */
    private Location location;

    /**
     * Constructor.
     * @param player
     * @param location
     */
    public InspectAction(Player player, Location location) {
        super(player, 0);
        this.location = location;
    }

    @Override
    public QueuePolicy getQueuePolicy() {
        return QueuePolicy.NEVER;
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }
    
    /**
     * Initialization method.
     */
    public abstract void init();
    
    /**
     * Reward upon inspection completion.
     * @return
     */
    public abstract Node getInspectItem();
    
    /**
     * Inspection time consumption.
     * @return
     */
    public abstract long getInspectDelay();
    
    /**
     * Rewards to give the player.
     * @param player
     * @param node
     */
    private void giveRewards(Player player, Node node) {
        player.getActionSender().sendMessage("This rock contains "
                + ItemDefinition.forId(node.getOreId()).getName().toLowerCase().replaceAll("ore", "").trim() + ".");
    }

    @Override
    public void execute() {        
        final Player player = getPlayer();
        if(this.getDelay() == 0) {
            this.setDelay(getInspectDelay());
            init();
            if(this.isRunning()) {
                player.face(location);
            }
        } else {
            giveRewards(player, getInspectItem());
            stop();
        }
    }

}