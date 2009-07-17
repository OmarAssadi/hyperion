package com.grahamedgecombe.rs2.action;

import java.util.LinkedList;
import java.util.Queue;

import com.grahamedgecombe.rs2.model.World;

/**
 * Stores a queue of pending actions.
 * @author blakeman8192
 * @author Graham
 *
 */
public class ActionQueue {
	
	/**
	 * The maximum number of actions allowed to be queued at once, deliberately
	 * set to the size of the player's inventory.
	 */
	public static final int MAXIMUM_SIZE = 28;
	
	/**
	 * A queue of <code>Action</code> objects.
	 */
	private final Queue<Action> queuedActionEvents = new LinkedList<Action>();
	
	/**
	 * The current action.
	 */
	private Action currentAction = null;
	
	/**
	 * Cancels all queued action events.
	 */
	public void cancelQueuedActionEvents() {
		for(Action actionEvent : queuedActionEvents) {
			actionEvent.stop();
		}
		queuedActionEvents.clear();
		currentAction.stop();
		currentAction = null;
	}
	
	/**
	 * Adds an <code>Action</code> to the queue.
	 * @param actionEvent
	 */
	public void addActionEvent(Action actionEvent) {
		if(queuedActionEvents.size() >= MAXIMUM_SIZE) {
			return;
		}
		queuedActionEvents.add(actionEvent);
		processNextAction();
	}

	/**
	 * Processes this next action.
	 */
	public void processNextAction() {
		if(currentAction != null) {
			if(currentAction.isRunning()) {
				return;
			}
		}
		if(queuedActionEvents.size() > 0) {
			currentAction = queuedActionEvents.poll();
			World.getWorld().submit(currentAction);
		}
	}

}
