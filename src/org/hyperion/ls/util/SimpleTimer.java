package org.hyperion.ls.util;

/**
 * A simple timing utility used miscellaneously throughout the Hyperion Login
 * Server.
 * 
 * @author blakeman8192
 */
public class SimpleTimer {

	private long cachedTime;

	/**
	 * Resets the timer.
	 */
	public void reset() {
		cachedTime = System.currentTimeMillis();
	}

	/**
	 * Gets the elapsed time.
	 * 
	 * @return the amount of time (in milliseconds) since the last call to the
	 *         <code>reset()</code> method.
	 */
	public long elapsed() {
		return System.currentTimeMillis() - cachedTime;
	}

}
