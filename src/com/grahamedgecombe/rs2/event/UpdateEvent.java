package com.grahamedgecombe.rs2.event;

import java.util.ArrayList;
import java.util.List;

import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.model.World;
import com.grahamedgecombe.rs2.task.ConsecutiveTask;
import com.grahamedgecombe.rs2.task.LocalListTask;
import com.grahamedgecombe.rs2.task.ParallelTask;
import com.grahamedgecombe.rs2.task.Task;
import com.grahamedgecombe.rs2.task.UpdateTask;

public class UpdateEvent extends Event {

	public static final int CYCLE_TIME = 600;
	
	public UpdateEvent() {
		super(CYCLE_TIME);
	}

	@Override
	public void execute() {
		List<Task> localListTasks = new ArrayList<Task>();
		for(Player player : World.getWorld().getPlayers()) {
			localListTasks.add(new LocalListTask(player));
		}
		Task localListTask = new ParallelTask(localListTasks.toArray(new Task[0]));
		List<Task> updateTasks = new ArrayList<Task>();
		for(Player player : World.getWorld().getPlayers()) {
			updateTasks.add(new UpdateTask(player));
		}
		Task updateTask = new ParallelTask(updateTasks.toArray(new Task[0]));
		List<Task> resetTasks = new ArrayList<Task>();
		for(Player player : World.getWorld().getPlayers()) {
			resetTasks.add(new UpdateTask(player));
		}
		Task resetTask = new ParallelTask(resetTasks.toArray(new Task[0]));
		World.getWorld().submit(new ConsecutiveTask(localListTask, updateTask, resetTask));
	}

}
