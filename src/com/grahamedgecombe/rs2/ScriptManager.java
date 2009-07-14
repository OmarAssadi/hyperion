package com.grahamedgecombe.rs2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Manages server scripts.
 * 
 * @author blakeman8192
 */
public class ScriptManager {

	/**
	 * The ScriptEngineManager.
	 */
	private ScriptEngineManager mgr;

	/**
	 * The JavaScript Engine.
	 */
	private ScriptEngine jsEngine;

	/**
	 * The Python Engine.
	 */
	private ScriptEngine pyEngine;

	/**
	 * The Ruby Engine.
	 */
	private ScriptEngine rubyEngine;

	/**
	 * The directory for the JavaScript Engine scripts.
	 */
	private final String jsScriptsDir = System.getProperty("$SCRIPTING_SRC")
			+ "/engines/JavaScript/src/";

	/**
	 * The directory for the Python Engine scripts.
	 */
	private final String pyScriptsDir = System.getProperty("$SCRIPTING_SRC")
			+ "/engines/Python/src/";

	/**
	 * The directory for the Ruby Engine scripts.
	 */
	private final String rubyScriptsDir = System.getProperty("$SCRIPTING_SRC")
			+ "/engines/Ruby/src/";

	/**
	 * The logger for this manager.
	 */
	private final Logger logger = Logger.getLogger(this.toString());

	{
		mgr = new ScriptEngineManager();
		jsEngine = mgr.getEngineByName("JavaScript");
		pyEngine = mgr.getEngineByName("jython");
		rubyEngine = mgr.getEngineByName("jruby");
	}

	public void loadAllScripts() {
		loadJS(jsScriptsDir);
		loadPy(pyScriptsDir);
		loadRuby(rubyScriptsDir);
	}

	/**
	 * Calls a JavaScript function.
	 * 
	 * @param identifier
	 *            The identifier of the function.
	 * @param args
	 *            The function arguments.
	 */
	public void callJSFunc(String identifier, Object... args) {
		Invocable invEngine = (Invocable) jsEngine;
		try {
			invEngine.invokeFunction(identifier, args);
		} catch (NoSuchMethodException ex) {
			logger.log(Level.WARNING, "No such method: " + identifier, ex);
		} catch (ScriptException ex) {
			logger.log(Level.WARNING, "ScriptException thrown!", ex);
		}
	}

	/**
	 * Calls a Python function.
	 * 
	 * @param identifier
	 *            The identifier of the function.
	 * @param args
	 *            The function arguments.
	 */
	public void callPyFunc(String identifier, Object... args) {
		Invocable invEngine = (Invocable) pyEngine;
		try {
			invEngine.invokeFunction(identifier, args);
		} catch (NoSuchMethodException ex) {
			logger.log(Level.WARNING, "No such method: " + identifier, ex);
		} catch (ScriptException ex) {
			logger.log(Level.WARNING, "ScriptException thrown!", ex);
		}
	}

	/**
	 * Calls a Ruby function.
	 * 
	 * @param identifier
	 *            The identifier of the function.
	 * @param args
	 *            The function arguments.
	 */
	public void callRubyFunc(String identifier, Object... args) {
		Invocable invEngine = (Invocable) rubyEngine;
		try {
			invEngine.invokeFunction(identifier, args);
		} catch (NoSuchMethodException ex) {
			logger.log(Level.WARNING, "No such method: " + identifier, ex);
		} catch (ScriptException ex) {
			logger.log(Level.WARNING, "ScriptException thrown!", ex);
		}
	}

	/**
	 * Loads JavaScript files into the JavaScript ScriptEngine from the argued
	 * path.
	 * 
	 * @param dirPath
	 *            The path of the directory to load the JavaScript source files
	 *            from.
	 */
	private void loadJS(String dirPath) {
		File dir = new File(dirPath);
		if (dir.exists() && dir.isDirectory()) {
			File[] children = dir.listFiles();
			for (File child : children) {
				if (child.isFile() && child.getName().endsWith(".js")) {
					try {
						jsEngine.eval(new InputStreamReader(
								new FileInputStream(child)));
					} catch (ScriptException ex) {
						logger.log(Level.SEVERE, "Unable to load script!", ex);
					} catch (FileNotFoundException ex) {
						logger.log(Level.SEVERE, "Unable to find script!", ex);
					}
				} else if (child.isDirectory())
					loadJS(child.getPath());
			}
		}
	}

	/**
	 * Loads Python files into the Python ScriptEngine from the argued path.
	 * 
	 * @param dirPath
	 *            The path of the directory to load the Python source files
	 *            from.
	 */
	private void loadPy(String dirPath) {
		File dir = new File(dirPath);
		if (dir.exists() && dir.isDirectory()) {
			File[] children = dir.listFiles();
			for (File child : children) {
				if (child.isFile() && child.getName().endsWith(".py")) {
					try {
						pyEngine.eval(new InputStreamReader(
								new FileInputStream(child)));
					} catch (ScriptException ex) {
						logger.log(Level.SEVERE, "Unable to load script!", ex);
					} catch (FileNotFoundException ex) {
						logger.log(Level.SEVERE, "Unable to find script!", ex);
					}
				} else if (child.isDirectory())
					loadJS(child.getPath());
			}
		}
	}

	/**
	 * Loads Ruby files into the Ruby ScriptEngine from the argued path.
	 * 
	 * @param dirPath
	 *            The path of the directory to load the Ruby source files from.
	 */
	private void loadRuby(String dirPath) {
		File dir = new File(dirPath);
		if (dir.exists() && dir.isDirectory()) {
			File[] children = dir.listFiles();
			for (File child : children) {
				if (child.isFile() && child.getName().endsWith(".rb")) {
					try {
						rubyEngine.eval(new InputStreamReader(
								new FileInputStream(child)));
					} catch (ScriptException ex) {
						logger.log(Level.SEVERE, "Unable to load script!", ex);
					} catch (FileNotFoundException ex) {
						logger.log(Level.SEVERE, "Unable to find script!", ex);
					}
				} else if (child.isDirectory())
					loadJS(child.getPath());
			}
		}
	}

}
