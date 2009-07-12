package com.grahamedgecombe.rs2.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.grahamedgecombe.rs2.GameEngine;
import com.grahamedgecombe.util.BlockingExecutorService;

/**
 * A task which can execute multiple child tasks simultaneously.
 * @author Graham
 *
 */
public class ParallelTask implements Task {
	
	/**
	 * The child tasks.
	 */
	private Collection<Task> tasks;
	
	/**
	 * Creates the parallel task.
	 * @param tasks The child tasks.
	 */
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
