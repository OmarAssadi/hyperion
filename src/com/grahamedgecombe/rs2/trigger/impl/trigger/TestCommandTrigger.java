package com.grahamedgecombe.rs2.trigger.impl.trigger;

import com.grahamedgecombe.rs2.action.Action;
import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.trigger.Trigger;

public class TestCommandTrigger implements Trigger {

	public void fire(final Player player) {
		player.getActionQueue().addAction(new Action(player, 2000) {
			@Override
			public void execute() {
				player.getActionSender().sendMessage("Action system test!");
				stop();
			}

			@Override
			public QueuePolicy getQueuePolicy() {
				return QueuePolicy.NEVER;
			}
		});
	}

}
