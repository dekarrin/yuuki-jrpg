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
 * @param <L> The type of the listener for primary screen events. Primary
 * screen events are events fired from this Screen that need the help of
 * external main program classes to execute, such as a button press that causes
 * the current screen to switch.
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
		public void setInitialProperties() {}
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
	 * The background image of this Screen.
	 */
	private String bgiIndex;
	
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
		setOpaque(false);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addListener(L listener) {
		return listeners.add(listener);
	}
	
	/**
	 * Gets the index of the background image for this screen.
	 * 
	 * @return The background image.
	 */
	public String getBackgroundImage() {
		return bgiIndex;
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
	public List<L> getElementListeners() {
		List<L> listenersList = new LinkedList<L>();
		for (L listener: listeners) {
			listenersList.add(listener);
		}
		return listenersList;
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
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getPreferredSize() {
		return size;
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
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeListener(Object listener) {
		return listeners.remove(listener);
	}
	
	/**
	 * Associates a background image index with this Screen.
	 * 
	 * @param index The index of the background image to associate.
	 */
	public void setBackgroundImage(String index) {
		bgiIndex = index;
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
	 * Called when this screen is switched to. May be used to set the focus on
	 * the initial element and/or set the properties of elements within this
	 * Screen.
	 */
	public abstract void setInitialProperties();
	
}
