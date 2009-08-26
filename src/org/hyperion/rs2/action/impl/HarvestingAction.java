package org.hyperion.rs2.action.impl;

import org.hyperion.rs2.action.Action;
import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.ItemDefinition;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;

/**
 * <p>A harvesting action is a resource-gathering action, which includes, but
 * is not limited to, woodcutting and mining.</p>
 * 
 * <p>This class implements code related to all harvesting-type skills, such as
 * dealing with the action itself, looping, expiring the object (i.e. changing
 * rocks to the gray rock and trees to the stump), checking requirements and
 * giving out the harvested resources.</p>
 * 
 * <p>The individual woodcutting and mining classes implement things specific
 * to these individual skills such as random events.</p>
 * @author Graham Edgecombe
 *
 */
public abstract class HarvestingAction extends Action {
	
	/**
	 * The location.
	 */
	private Location location;
	
	/**
	 * Creates the harvesting action for the specified player.
	 * @param player The player to create the action for.
	 */
	public HarvestingAction(Player player, Location location) {
		super(player, 0);
		this.location = location;
	}
	
	@Override
	public QueuePolicy getQueuePolicy() {
		return QueuePolicy.NEVER;
	}
	
	/**
	 * Called when the action is initialized.
	 */
	public abstract void init();
	
	/**
	 * Gets the harvest delay.
	 * @return The delay between consecutive harvests.
	 */
	public abstract long getHarvestDelay();
	
	/**
	 * Gets the number of cycles.
	 * @return The number of cycles.
	 */
	public abstract int getCycles();
	
	/**
	 * Gets the success factor.
	 * @return The success factor.
	 */
	public abstract double getFactor();
	
	/**
	 * Gets the harvested item.
	 * @return The harvested item.
	 */
	public abstract Item getHarvestedItem();
	
	/**
	 * Gets the experience.
	 * @return The experience.
	 */
	public abstract double getExperience();
	
	/**
	 * Gets the animation.
	 * @return The animation.
	 */
	public abstract Animation getAnimation();
	
	/**
	 * The total number of cycles.
	 */
	private int totalCycles;
	
	/**
	 * The number of remaining cycles.
	 */
	private int cycles;

	@Override
	public void execute() {
		final Player player = getPlayer();
		if(this.getDelay() == 0) {
			this.setDelay(getHarvestDelay());
			init();
			if(this.isRunning()) {
				player.playAnimation(getAnimation());
				player.face(location);
			}
			this.cycles = getCycles();
			this.totalCycles = cycles;
		} else {
			cycles--;
			Item item = getHarvestedItem();
			if(player.getInventory().hasRoomFor(item)) {
				if(totalCycles == 1 || Math.random() > getFactor()) {
					player.getInventory().add(item);
					ItemDefinition def = item.getDefinition();
					player.getActionSender().sendMessage("You get some " + def.getName() + ".");
					player.getSkills().addExperience(Skills.WOODCUTTING, getExperience());
				}
			} else {
				stop();
				player.getActionSender().sendMessage("There is not enough space in your inventory.");
				return;
			}
			if(cycles == 0) {
				// TODO replace with expired object!
				stop();
			} else {
				player.playAnimation(getAnimation());
				player.face(location);
			}
		}
	}

}
