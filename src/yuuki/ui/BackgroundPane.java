package yuuki.ui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;

/**
 * A component with a background image.
 */
@SuppressWarnings("serial")
public class BackgroundPane extends JComponent {
	
	/**
	 * The background image to display.
	 */
	private Image backgroundImage;
	
	/**
	 * Creates a new Background Pane.
	 */
	public BackgroundPane() {
		setLayout(new BorderLayout());
		this.backgroundImage = null;
	}
	
	/**
	 * Calls super.paintComponent() and then paints the background.
	 * 
	 * @param g The graphical context on which to paint.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
		}
	}
	
	/**
	 * Sets the background image.
	 * 
	 * @param image What to set the background image to.
	 */
	public void setBackgroundImage(Image image) {
		this.backgroundImage = image;
	}
	
}
