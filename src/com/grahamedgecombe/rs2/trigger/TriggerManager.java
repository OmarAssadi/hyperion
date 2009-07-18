package com.grahamedgecombe.rs2.trigger;

import java.util.HashMap;
import java.util.Map;

import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.trigger.impl.cond.CommandCondition;
import com.grahamedgecombe.rs2.trigger.impl.trigger.TestCommandTrigger;

/**
 * The <code>TriggerManager</code> manages the relationship between a
 * <code>TriggerCondition</code> and a <code>Trigger</code> and processes a
 * <code>TriggerCondition</code> when one is met.
 * @author Graham
 *
 */
public class TriggerManager {
	
	/**
	 * The singleton instance of the <code>TriggerManager</code>.
	 */
	private static final TriggerManager INSTANCE = new TriggerManager();
	
	/**
	 * Gets the singleton instance of the <code>TriggerManager</code>.
	 * @return The <code>TriggerManager</code>.
	 */
	public static TriggerManager getTriggerManager() {
		return INSTANCE;
	}
	
	/**
	 * The map between <code>TriggerCondition</code>s and
	 * <code>Trigger</code>s.
	 */
	private Map<TriggerCondition, Trigger> triggers = new HashMap<TriggerCondition, Trigger>();
	
	/**
	 * Creates the <code>TriggerManager</code> and adds a few test triggers.
	 */
	public TriggerManager() {
		triggers.put(new CommandCondition("test"), new TestCommandTrigger());
	}
	
	/**
	 * Fires a trigger for a player.
	 * @param player The player.
	 * @param trigger The trigger condition.
	 */
	public void fire(Player player, TriggerCondition trigger) {
		if(triggers.containsKey(trigger)) {
			triggers.get(trigger).fire(player);
		}
	}

}
