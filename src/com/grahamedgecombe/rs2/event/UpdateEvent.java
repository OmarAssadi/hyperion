package com.grahamedgecombe.rs2.event;

import java.util.ArrayList;
import java.util.List;

import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.model.World;
import com.grahamedgecombe.rs2.task.ConsecutiveTask;
import com.grahamedgecombe.rs2.task.ParallelTask;
import com.grahamedgecombe.rs2.task.Task;
import com.grahamedgecombe.rs2.task.UpdateTask;
import com.grahamedgecombe.rs2.task.WalkingTask;

public class UpdateEvent extends Event {

	public static final int CYCLE_TIME = 600;
	
	public UpdateEvent() {
		super(CYCLE_TIME);
	}

	@Override
	public void execute() {
		List<Task> walkingTasks = new ArrayList<Task>();
		List<Task> updateTasks = new ArrayList<Task>();
		List<Task> resetTasks = new ArrayList<Task>();
		
		for(Player player : World.getWorld().getPlayers()) {
			walkingTasks.add(new WalkingTask(player));
			updateTasks.add(new UpdateTask(player));
			resetTasks.add(new UpdateTask(player));
		}
		
		Task walkingTask = new ParallelTask(walkingTasks.toArray(new Task[0]));
		Task updateTask = new ParallelTask(updateTasks.toArray(new Task[0]));
		Task resetTask = new ParallelTask(resetTasks.toArray(new Task[0]));
		
		World.getWorld().submit(new ConsecutiveTask(walkingTask, updateTask, resetTask));
	}

}
