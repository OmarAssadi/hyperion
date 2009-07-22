package org.hyperion.ls;

import org.hyperion.ls.io.Client;
import org.hyperion.ls.task.impl.IdleTask;
import org.hyperion.ls.task.impl.LogicTask;
import org.hyperion.ls.task.impl.NetworkTask;
import org.hyperion.ls.util.SimpleLogger;

/**
 * Main class of the Hyperion Login Server.
 * 
 * @author blakeman8192
 */
public class Server {

	private static final Engine engine;
	private static final Thread engineThread;
	private static final SimpleLogger logger;
	private static final Client[] clients;

	static {
		engine = new Engine();
		engineThread = new Thread(engine);
		logger = new SimpleLogger(System.out);
		clients = new Client[100];
	}

	public static void main(String[] args) {
		System.setOut(logger);
		System.out.println("Starting Hyperion Login Server...");
		engine.addTask(new NetworkTask());
		engine.addTask(new LogicTask());
		engine.addTask(new IdleTask());
		engineThread.start();
		System.out.println("Hyperion Login Server ready.");
	}

	/**
	 * Adds a <code>Client</code> to the server.
	 * 
	 * @param client
	 *            the client to add.
	 */
	public static void addClient(Client client) {
		for (int i = 0; i < clients.length; i++)
			if (clients[i] == null) {
				clients[i] = client;
				return;
			}
	}

	/**
	 * Removes a <code>client</code> from the server.
	 * 
	 * @param client
	 *            the client to remove.
	 */
	public static void removeClient(Client client) {
		for (int i = 0; i < clients.length; i++)
			if (clients[i] == client) {
				clients[i] = null;
				return;
			}
	}

	/**
	 * Gets all registered clients.
	 * 
	 * @return all registered clients.
	 */
	public static Client[] getClients() {
		return clients;
	}

}
