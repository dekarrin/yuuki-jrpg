package yuuki.ui;

import java.awt.Font;
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
	 * The exact text being displayed.
	 */
	private char[][] buffer;
	
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
		buffer = new char[width][height];
		textArea = new JTextArea(height, width);
		textArea.setEditable(false);
		textArea.setFocusable(false);
		textArea.setFont(new Font("Courier New", Font.PLAIN, 12));
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
			int xOffset = (subX < 0) ? Math.abs(subX) : 0;
			int yOffset = (subY < 0) ? Math.abs(subY) : 0;
			displayGrid(subView, xOffset, yOffset);
		}
	}
	
	/**
	 * Sets all tiles in the tile buffer to be empty.
	 */
	private void clearBuffer() {
		for (int y = 0; y < textArea.getRows(); y++) {
			for (int x = 0; x < textArea.getColumns(); x++) {
				buffer[x][y] = ' ';
			}
		}
	}
	
	/**
	 * Displays the given tiles on the display.
	 * 
	 * @param grid The grid of tiles to display.
	 * @param xOffset The number of tiles to shift the display right.
	 * @param yOffset The number of tiles to shift the display down.
	 */
	private void displayGrid(TileGrid grid, int xOffset, int yOffset) {
		setBufferTiles(grid, xOffset, yOffset);
		showBuffer();
	}
	
	/**
	 * Sets the buffer to the initial tiles.
	 * 
	 * @param grid The grid of tiles to use to populate the display.
	 * @param xOffset The number of tiles to shift the display right.
	 * @param yOffset The number of tiles to shift the display down.
	 */
	private void setBufferTiles(TileGrid grid, int xOffset, int yOffset) {
		clearBuffer();
		char c = '\0';
		for (int y = 0; y < grid.getHeight(); y++) {
			for (int x = 0; x < grid.getWidth(); x++) {
				c = grid.tileAt(x, y).getDisplayChar();
				buffer[x + xOffset][y + yOffset] = c;
			}
		}
	}
	
	/**
	 * Updates the actual display area with the buffer.
	 */
	private void showBuffer() {
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < textArea.getRows(); y++) {
			for (int x = 0; x < textArea.getColumns(); x++) {
				sb.append(buffer[x][y]);
			}
			if (y < textArea.getRows() - 1) {
				sb.append('\n');
			}
		}
		textArea.setText(sb.toString());
	}
	
}
