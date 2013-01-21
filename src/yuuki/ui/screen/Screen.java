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
public abstract class Screen<L extends ScreenListener> extends
JPanel implements yuuki.ui.Listenable<L> {
	
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
	 * The background music associated with this Screen.
	 */
	private String bgmIndex;
	
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
	 * Associates a background music index with this Screen.
	 * 
	 * @param index The index of the background music to associate.
	 */
	public void setBackgroundMusic(String index) {
		bgmIndex = index;
	}
	
	/**
	 * Gets this Screen's associated background music index.
	 * 
	 * @return The background music index.
	 */
	public String getBackgroundMusic() {
		return bgmIndex;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addListener(L listener) {
		return listeners.add(listener);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<L> getElementListeners() {
		List<L> listenersList = new LinkedList<L>();
		for (L listener: listeners) {
			listenersList.add(listener);
		}
		return listenersList;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getPreferredSize() {
		return size;
	}
	
	/**
	 * Gets the maximum size of this Screen. This will be the same as the
	 * preferred size.
	 * 
	 * @return The maximum size of this Screen.
	 */
	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
	
	/**
	 * Gets the minimum size of this Screen. This will be the same as the
	 * preferred size.
	 * 
	 * @return The minimum size of this Screen.
	 */
	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}
	
	/**
	 * Gets the size of this Screen. This will be the same as the preferred
	 * size.
	 * 
	 * @return The size of this Screen.
	 */
	@Override
	public Dimension getSize() {
		return getPreferredSize();
	}
	
	/**
	 * Gets the set height of this Screen.
	 * 
	 * @return The height that this Screen was set at during construction.
	 */
	public int getSetHeight() {
		return size.height;
	}
	
	/**
	 * Gets the set width of this Screen.
	 * 
	 * @return The height that this Screen was set at during construction.
	 */
	public int getSetWidth() {
		return size.width;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeListener(Object listener) {
		return listeners.remove(listener);
	}
	
	/**
	 * Sets focus on the primary element in this screen.
	 */
	public abstract void setInitialFocus();
	
}
