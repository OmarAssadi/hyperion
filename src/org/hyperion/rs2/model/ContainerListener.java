package org.hyperion.rs2.model;

/**
 * Listens to events from a container.
 * @author Graham
 *
 */
public interface ContainerListener {
	
	/**
	 * Called when an item is changed.
	 * @param container The container.
	 * @param slots The slot that was changed.
	 */
	public void itemChanged(Container<?> container, int slot);
	
	/**
	 * Called when a group of items are changed.
	 * @param container The container.
	 */
	public void itemsChanged(Container<?> container);

}
