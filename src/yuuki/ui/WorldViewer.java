package yuuki.ui;

import java.awt.Point;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import yuuki.world.TileGrid;

/**
 * Displays the overworld graphically.
 */
@SuppressWarnings("serial")
public class WorldViewer extends JPanel {
	
	/**
	 * The main text area for this world viewer.
	 */
	private JTextArea textArea;
	
	/**
	 * The current view of this world.
	 */
	private TileGrid view;
	
	/**
	 * Creates a new WorldViewer that can display the specified number of
	 * tiles.
	 * 
	 * @param width The width of this WorldViewer in tiles.
	 * @param height The height of this WorldViewer in tiles.
	 */
	public WorldViewer(int width, int height) {
		textArea = new JTextArea(height, width);
		textArea.setEditable(false);
		textArea.setFocusable(false);
		add(textArea);
	}
	
	/**
	 * Sets the view of the world being displayed.
	 * 
	 * @param view The view to show.
	 */
	public void setView(TileGrid view) {
		this.view = view;
	}
	
	/**
	 * Updates this WorldViewer to show a new area.
	 * 
	 * @param center The center of the area to show.
	 */
	public void updateDisplay(Point center) {
		
	}
	
}
