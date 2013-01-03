package yuuki.ui.screen;

import java.awt.Dimension;

import javax.swing.JPanel;

/**
 * Base class for the screens that appear in the GUI.
 */
@SuppressWarnings("serial")
public abstract class Screen extends JPanel {
	
	/**
	 * A default concrete derived class of Screen. Obtainable through the
	 * getInstance() method of Screen.
	 */
	private static class GenericScreen extends Screen {
		
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
	public static Screen getInstance(int w, int h) {
		return new GenericScreen(w, h);
	}
	
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
