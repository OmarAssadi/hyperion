package org.hyperion.rs2.trigger.impl.trigger;

import org.hyperion.rs2.action.Action;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.trigger.Trigger;

/**
 * A test trigger.
 * @author Graham
 *
 */
public class TestCommandTrigger implements Trigger {

	@Override
	public void fire(final Player player, Object... arguments) {
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
