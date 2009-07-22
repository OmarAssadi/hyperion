package org.hyperion.ls.task.impl;

import org.hyperion.ls.task.Task;
import org.hyperion.ls.util.SimpleTimer;

/**
 * A simple task used to idle the engine for a period of time before proceeding
 * with the next cycle.
 * 
 * @author blakeman8192
 */
public class IdleTask implements Task {

	private final int idleRate;
	private final SimpleTimer idleTimer;

	{
		idleRate = 100;
		idleTimer = new SimpleTimer();
	}

	@Override
	public void exec() throws InterruptedException {
		long idleAmount = idleRate - idleTimer.elapsed();
		if (idleAmount >= 0)
			Thread.sleep(idleAmount);
		idleTimer.reset();
	}

}
