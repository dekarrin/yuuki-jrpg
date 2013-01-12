package yuuki.ui;

import java.util.List;

/**
 * A component that ElementListeners may register themselves for listening
 * on.
 */
public interface Listenable<L extends ElementListener> {
	
	/**
	 * Registers a listener for events fired from this screen. The listener is
	 * registered if and only if it has not already been registered.
	 * 
	 * @param listener The listener to add.
	 * 
	 * @return Whether the listener was successfully added. Adding the listener
	 * will only fail if it has already been added.
	 */
	public boolean addListener(L listener);
	
	/**
	 * Removes a listener from the list of registered listeners.
	 * 
	 * @param listener The listener to remove.
	 * 
	 * @return Whether the listener was successfully removed. Removing the
	 * listener will only fail it was not in the list of listeners.
	 */
	public boolean removeListener(Object listener);
	
	/**
	 * Gets the listeners as a list. The list returned must be modifiable.
	 * 
	 * @return A modifiable version of the list of listeners.
	 */
	public List<L> getElementListeners();
	
}
