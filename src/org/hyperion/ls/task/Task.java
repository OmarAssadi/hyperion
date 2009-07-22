package org.hyperion.ls.task;

/**
 * A task for the <code>Engine</code> to run.
 * 
 * @author blakeman8192
 */
public interface Task {

	/**
	 * Executes the task.
	 */
	public void exec() throws Exception;

}
