package org.hyperion.ls.util;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple logging utility.
 * 
 * @author blakeman8192
 */
public class SimpleLogger extends PrintStream {

	private Date cachedDate;
	private final DateFormat dateFormat;
	private final SimpleTimer refreshTimer;
	private final int refreshRate;

	{
		cachedDate = new Date();
		dateFormat = new SimpleDateFormat();
		refreshTimer = new SimpleTimer();
		refreshRate = 60000;
	}

	/**
	 * Creates a new SimpleLogger object.
	 * 
	 * @param out
	 *            the <code>OutputStream</code> that the logger will print to.
	 */
	public SimpleLogger(OutputStream out) {
		super(out);
	}

	@Override
	public void print(String s) {
		super.print(getPrefix() + s);
	}

	/**
	 * Gets the logger prefix.
	 * 
	 * @return the prefix.
	 */
	private final String getPrefix() {
		if (refreshTimer.elapsed() > refreshRate) {
			refreshTimer.reset();
			cachedDate = new Date();
		}
		return "[" + dateFormat.format(cachedDate) + "]["
				+ Thread.currentThread().getName() + "]: ";
	}

}
