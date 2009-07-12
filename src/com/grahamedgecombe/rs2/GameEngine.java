package com.grahamedgecombe.rs2;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;

import com.grahamedgecombe.rs2.task.Task;
import com.grahamedgecombe.util.BlockingExecutorService;

/**
 * The 'core' class of the server which processes all the logic tasks in one
 * single logic <code>ExecutorService</code>. This service is scheduled which
 * means <code>Event</code>s are also submitted to it.
 * @author Graham
 *
 */
public class GameEngine implements Runnable {
	
	/**
	 * A queue of pending tasks.
	 */
	private final BlockingQueue<Task> tasks = new LinkedBlockingQueue<Task>();
	
	/**
	 * The logic service.
	 */
	private final ScheduledExecutorService logicService = Executors.newScheduledThreadPool(1);
	
	/**
	 * The task service, used by <code>ParallelTask</code>s.
	 */
	private final BlockingExecutorService taskService = new BlockingExecutorService(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
	
	/**
	 * The work service, generally for file I/O and other blocking operations.
	 */
	private final ExecutorService workService = Executors.newSingleThreadExecutor();
	
	/**
	 * Running flag.
	 */
	private boolean running = false;
	
	/**
	 * Thread instance.
	 */
	private Thread thread;
	
	/**
	 * Gets the logic <code>ScheduledExecutorService</code>.
	 * @return The logic service.
	 */
	public ScheduledExecutorService getLogicService() {
		return logicService;
	}
	
	/**
	 * Gets the work <code>ExecutorService</code>.
	 * @return The work service.
	 */
	public ExecutorService getWorkService() {
		return workService;
	}
	
	/**
	 * Gets the task <code>BlockingExecutorService</code>.
	 * @return The task service.
	 */
	public BlockingExecutorService getTaskService() {
		return taskService;
	}
	
	/**
	 * Submits a new task which is processed on the logic thread as soon as
	 * possible.
	 * @param task The task to submit.
	 */
	public void pushTask(Task task) {
		tasks.offer(task);
	}

	/**
	 * Checks if this <code>GameEngine</code> is running.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * Starts the <code>GameEngine</code>'s thread.
	 */
	public void start() {
		if(running) {
			throw new IllegalStateException("The engine is already running.");
		}
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	/**
	 * Stops the <code>GameEngine</code>'s thread.
	 */
	public void stop() {
		if(!running) {
			throw new IllegalStateException("The engine is already stopped.");
		}
		running = false;
		thread.interrupt();
	}
	
	@Override
	public void run() {
		while(running) {
			try {
				final Task task = tasks.take();
				try {
					logicService.submit(new Runnable() {
						@Override
						public void run() {
							task.execute(GameEngine.this);
						}
					}).get();
				} catch(ExecutionException e) {
					throw new RuntimeException(e);
				}
			} catch(InterruptedException e) {
				continue;
			}
		}
	}

}
