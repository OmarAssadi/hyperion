package org.hyperion.ls;

import org.hyperion.ls.task.Task;

/**
 * The engine of the Hyperion Login Server.
 * 
 * @author blakeman8192
 */
public class Engine implements Runnable {

	private Task[] tasks;
	private boolean running;

	{
		tasks = new Task[100];
		running = true;
	}

	@Override
	public void run() {
		while (running) {
			Thread.currentThread().setName("Engine");
			for (int i = 0; i < tasks.length; i++)
				if (tasks[i] != null)
					try {
						tasks[i].exec();
					} catch (Exception ex) {
						ex.printStackTrace();
						shutdown();
					}
		}
	}

	/**
	 * Assigns a <code>Task</code> to the engine.
	 * 
	 * @param t
	 *            the <code>Task</code> to assign.
	 */
	public void addTask(Task t) {
		for (int i = 0; i < tasks.length; i++)
			if (tasks[i] == null) {
				tasks[i] = t;
				return;
			}
	}

	/**
	 * Removes a <code>Task</code> from the engine.
	 * 
	 * @param t
	 *            the <code>Task</code> to assign.
	 */
	public void removeTask(Task t) {
		for (int i = 0; i < tasks.length; i++)
			if (tasks[i] == t) {
				tasks[i] = null;
				return;
			}
	}

	/**
	 * Shuts down the engine.
	 */
	public void shutdown() {
		Thread.currentThread().setName("Engine");
		System.out.println("Shutting down...");
		running = false;
	}

}
