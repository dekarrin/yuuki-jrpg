package yuuki.ui.screen;

import java.awt.Dimension;

import javax.swing.JPanel;

/**
 * Base class for the screens that appear in the GUI.
 */
@SuppressWarnings("serial")
public abstract class Screen extends JPanel {
	
	private static class GenericScreen extends Screen {
		public GenericScreen(int w, int h) {
			super(w, h);
		}
		@Override
		public void setInitialFocus() {}
	}
	
	public static Screen getInstance(int w, int h) {
		return new GenericScreen(w, h);
	}
	
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
	
	@Override
	public Dimension getPreferredSize() {
		return size;
	}
	
	/**
	 * Sets focus on the primary element in this screen.
	 */
	public abstract void setInitialFocus();
	
}
