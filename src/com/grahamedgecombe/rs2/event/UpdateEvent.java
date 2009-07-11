package com.grahamedgecombe.rs2.event;

import java.util.ArrayList;
import java.util.List;

import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.model.World;
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
		List<Task> tasks = new ArrayList<Task>();
		for(Player player : World.getWorld().getPlayers()) {
			tasks.add(new UpdateTask(player));
		}
		World.getWorld().getEngine().pushTask(new ParallelTask(tasks.toArray(new Task[0])));
	}

}
