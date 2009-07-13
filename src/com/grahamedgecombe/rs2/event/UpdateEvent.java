package com.grahamedgecombe.rs2.event;

import java.util.ArrayList;
import java.util.List;

import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.model.World;
import com.grahamedgecombe.rs2.task.ConsecutiveTask;
import com.grahamedgecombe.rs2.task.ParallelTask;
import com.grahamedgecombe.rs2.task.ResetTask;
import com.grahamedgecombe.rs2.task.Task;
import com.grahamedgecombe.rs2.task.UpdateTask;
import com.grahamedgecombe.rs2.task.TickTask;

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
		
		Task tickTask = new ConsecutiveTask(tickTasks.toArray(new Task[0]));
		Task updateTask = new ParallelTask(updateTasks.toArray(new Task[0]));
		Task resetTask = new ParallelTask(resetTasks.toArray(new Task[0]));
		
		World.getWorld().submit(new ConsecutiveTask(tickTask, updateTask, resetTask));
	}

}
