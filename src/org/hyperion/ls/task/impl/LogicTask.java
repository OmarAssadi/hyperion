package org.hyperion.ls.task.impl;

import org.hyperion.ls.Server;
import org.hyperion.ls.io.Client;
import org.hyperion.ls.task.Task;

/**
 * A <code>Task</code> object used to handle packet logic.
 * 
 * @author blakeman8192
 */
public class LogicTask implements Task {

	@Override
	public void exec() {
		for (int i = 0; i < Server.getClients().length; i++)
			if (Server.getClients()[i] != null)
				handleLogic(Server.getClients()[i]);
	}

	private final void handleLogic(Client client) {
		// TODO: implement a protocol
	}

}
