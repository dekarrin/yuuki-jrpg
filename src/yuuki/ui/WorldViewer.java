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
		if (center != null) {
			int w = textArea.getColumns();
			int h = textArea.getRows();
			int subX = center.x - (w / 2);
			int subY = center.y - (h / 2);
			TileGrid subView = view.getSubGrid(subX, subY, w, h);
			displayGrid(subView);
		}
	}
	
	/**
	 * Displays the given tiles on the display.
	 * 
	 * @param grid The grid of tiles to display.
	 */
	private void displayGrid(TileGrid grid) {
		StringBuilder strVer = new StringBuilder();
		for (int i = 0; i < grid.getHeight(); i++) {
			for (int j = 0; j < grid.getWidth(); j++) {
				strVer.append(grid.tileAt(j, i).getDisplayChar());
			}
			if (i < grid.getHeight() - 1) {
				strVer.append('\n');
			}
		}
		textArea.setText(strVer.toString());
	}
	
}
