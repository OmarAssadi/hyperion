package org.hyperion.rs2.event.impl;

import java.util.ArrayList;
import java.util.List;

import org.hyperion.rs2.event.Event;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.task.ConsecutiveTask;
import org.hyperion.rs2.task.ParallelTask;
import org.hyperion.rs2.task.Task;
import org.hyperion.rs2.task.impl.ResetTask;
import org.hyperion.rs2.task.impl.TickTask;
import org.hyperion.rs2.task.impl.UpdateTask;


/**
 * An event which starts player update tasks.
 * @author Graham
 *
 */
public class UpdateEvent extends Event {

	/**
	 * The cycle time, in milliseconds.
	 */
	public static final int CYCLE_TIME = 600;
	
	/**
	 * Creates the update event to cycle every 600 milliseconds.
	 */
	public UpdateEvent() {
		super(CYCLE_TIME);
	}

	@Override
	public void execute() {
		List<Task> tickTasks = new ArrayList<Task>();
		List<Task> updateTasks = new ArrayList<Task>();
		List<Task> resetTasks = new ArrayList<Task>();
		
		for(Player player : World.getWorld().getPlayers()) {
			tickTasks.add(new TickTask(player));
			updateTasks.add(new UpdateTask(player));
			resetTasks.add(new ResetTask(player));
		}
		
		Task tickTask = new ParallelTask(tickTasks.toArray(new Task[0]));
		Task updateTask = new ParallelTask(updateTasks.toArray(new Task[0]));
		Task resetTask = new ParallelTask(resetTasks.toArray(new Task[0]));
		
		World.getWorld().submit(new ConsecutiveTask(tickTask, updateTask, resetTask));
	}

}