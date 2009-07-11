package com.grahamedgecombe.rs2.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.grahamedgecombe.rs2.GameEngine;
import com.grahamedgecombe.util.BlockingExecutorService;

public class ParallelTask implements Task {
	
	private Collection<Task> tasks;
	
	public ParallelTask(Task... tasks) {
		List<Task> taskList = new ArrayList<Task>();
		for(Task task : tasks) {
			taskList.add(task);
		}
		this.tasks = Collections.unmodifiableCollection(taskList);
	}
	
	@Override
	public void execute(final GameEngine context) {
		BlockingExecutorService svc = context.getTaskService();
		for(final Task task : tasks) {
			context.getTaskService().submit(new Runnable() {
				@Override
				public void run() {
					task.execute(context);
				}
			});
		}
		try {
			svc.waitForPendingTasks();
		} catch(ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

}
