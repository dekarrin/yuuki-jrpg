package yuuki.ui.screen;

import java.awt.Dimension;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;

/**
 * Base class for the screens that appear in the GUI.
 * 
 * @param <L> The type of the listener that this screen uses.
 */
@SuppressWarnings("serial")
public abstract class Screen<L extends ScreenListener> extends JPanel {
	
	/**
	 * A default concrete derived class of Screen. Obtainable through the
	 * getInstance() method of Screen.
	 */
	private static class GenericScreen extends Screen<ScreenListener> {
		
		/**
		 * Creates a new GenericScreen.
		 * 
		 * @param w The width of the screen.
		 * @param h The height of the screen.
		 */
		public GenericScreen(int w, int h) {
			super(w, h);
		}
		
		/**
		 * Empty implementation.
		 */
		@Override
		public void setInitialFocus() {}
	}
	
	/**
	 * Gets an instance of Screen. The actual type will be some concrete
	 * subclass of Screen.
	 * 
	 * @param w The width of the screen.
	 * @param h The height of the screen.
	 * 
	 * @return A concrete subclass of Screen with generic implementations of
	 * abstract methods.
	 */
	public static Screen<? extends ScreenListener> getInstance(int w, int h) {
		return new GenericScreen(w, h);
	}
	
	/**
	 * Keeps track of all registered listeners.
	 */
	private Set<L> listeners;
	
	/**
	 * The size of this Screen.
	 */
	private final Dimension size;
	
	/**
	 * Creates a new Screen with the given dimensions.
	 * 
	 * @param width The width of the Screen.
	 * @param height The height of the Screen.
	 */
	public Screen(int width, int height) {
		size = new Dimension(width, height);
		listeners = new HashSet<L>();
	}
	
	/**
	 * Registers a listener for events fired from this screen. The listener is
	 * registered if and only if it has not already been registered.
	 * 
	 * @param listener The listener to add.
	 */
	public void addListener(L listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes a listener from the list of registered listeners.
	 * 
	 * @param listener The listener to remove.
	 */
	public void removeListener(Object listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Gets the listeners as a list. The actual list of listeners is copied,
	 * and so it may be modified while iterating over the array returned by
	 * this method.
	 */
	protected List<L> getScreenListeners() {
		List<L> listenersList = new LinkedList<L>();
		for (L listener: listeners) {
			listenersList.add(listener);
		}
		return listenersList;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public Dimension getPreferredSize() {
		return size;
	}
	
	/**
	 * Sets focus on the primary element in this screen.
	 */
	public abstract void setInitialFocus();
	
}
