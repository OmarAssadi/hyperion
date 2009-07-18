package org.hyperion.rs2.trigger.impl.cond;

import org.hyperion.rs2.trigger.TriggerCondition;

/**
 * A condition which is triggered upon a command.
 * @author Graham
 *
 */
public class CommandCondition implements TriggerCondition {
	
	/**
	 * The command.
	 */
	private String command;
	
	/**
	 * Creates the condition.
	 * @param command The command.
	 */
	public CommandCondition(String command) {
		this.command = command;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((command == null) ? 0 : command.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		CommandCondition other = (CommandCondition) obj;
		if(command == null) {
			if(other.command != null)
				return false;
		} else if(!command.equals(other.command))
			return false;
		return true;
	}

}
