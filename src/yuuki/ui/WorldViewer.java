package yuuki.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import yuuki.util.ElementGrid;
import yuuki.util.Grid;
import yuuki.world.Locatable;
import yuuki.world.Tile;
/**
 * Displays the overworld graphically.
 */
@SuppressWarnings("serial")
public class WorldViewer extends JPanel {
	
	/**
	 * The exact text being displayed.
	 */
	private Grid<java.lang.Character> buffer;
	
	/**
	 * The Locatables on the screen.
	 */
	private Set<Locatable> locatables;
	
	/**
	 * The main text area for this world viewer.
	 */
	private JTextArea textArea;
	
	/**
	 * The current view of this world.
	 */
	private Grid<Tile> view;
	
	/**
	 * Creates a new WorldViewer that can display the specified number of
	 * tiles.
	 * 
	 * @param width The width of this WorldViewer in tiles.
	 * @param height The height of this WorldViewer in tiles.
	 */
	public WorldViewer(int width, int height) {
		Dimension d = new Dimension(width, height);
		buffer = new ElementGrid<java.lang.Character>(d);
		locatables = new HashSet<Locatable>();
		textArea = new JTextArea(height, width);
		textArea.setEditable(false);
		textArea.setFocusable(false);
		textArea.setFont(new Font("Courier New", Font.PLAIN, 12));
		add(textArea);
	}
	
	/**
	 * Adds a Locatable to the current display.
	 * 
	 * @param l The Locatable to add.
	 */
	public void addLocatable(Locatable l) {
		locatables.add(l);
	}
	
	/**
	 * Removes all Locatables from the current display.
	 */
	public void clearLocatables() {
		locatables.clear();
	}
	
	/**
	 * Gets the Locatables that fall within a certain rectangle.
	 * 
	 * @param box The Rectangle from within the Locatables should be drawn.
	 * 
	 * @return The Locatables that currently fall within the bounding box.
	 */
	public ArrayList<Locatable> getLocatablesInBox(Rectangle box) {
		ArrayList<Locatable> desired = new ArrayList<Locatable>();
		for (Locatable l : locatables) {
			if (box.contains(l.getLocation())) {
				desired.add(l);
			}
		}
		return desired;
	}
	
	/**
	 * Sets the view of the world being displayed.
	 * 
	 * @param view The view to show.
	 */
	public void setView(Grid<Tile> view) {
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
			Rectangle box = new Rectangle(subX, subY, w, h);
			Grid<Tile> sub = view.getSubGrid(box);
			int xOffset = (subX < 0) ? Math.abs(subX) : 0;
			int yOffset = (subY < 0) ? Math.abs(subY) : 0;
			Point subPos = new Point(subX, subY);
			setBufferTiles(sub, xOffset, yOffset);
			setBufferLocatables(subPos, sub.getSize());
			showBuffer();
		}
	}
	
	/**
	 * Adds a Locatable to the buffer.
	 * 
	 * @param l The locatable to add.
	 * @param position The position of the displayed view.
	 * @param size The size of the displayed view.
	 */
	private void addToBuffer(Locatable ls, Point position, Dimension size) {
		Rectangle box = new Rectangle(position, size);
		Point c = getRelativePosition(ls.getLocation(), box);
		buffer.set(c, ls.getDisplayable().getDisplayChar());
	}
	
	/**
	 * Sets all tiles in the tile buffer to be empty.
	 */
	private void clearBuffer() {
		Point p = new Point(0, 0);
		for (p.y = 0; p.y < textArea.getRows(); p.y++) {
			for (p.x = 0; p.x < textArea.getColumns(); p.x++) {
				buffer.set(p, ' ');
			}
		}
	}
	
	/**
	 * Transforms an absolute ordered pair to a relative ordered pair by using
	 * the size and location of the displayed point.
	 * 
	 * @param p The absolute point.
	 * @param viewBox The size and position of the displayed view.
	 */
	private Point getRelativePosition(Point p, Rectangle viewBox) {
		Point rel = new Point();
		int offsetX = 0, offsetY = 0;
		if (viewBox.y == 0 && viewBox.height < textArea.getHeight()) {
			offsetY = textArea.getHeight() - viewBox.height;
		}
		if (viewBox.x == 0 && viewBox.width < textArea.getWidth()) {
			offsetX = textArea.getWidth() - viewBox.width;
		}
		rel.x = p.x - viewBox.x + offsetX;
		rel.y = p.y - viewBox.y + offsetY;
		return rel;
	}
	
	/**
	 * Sets the buffer to contain the Locatables.
	 * 
	 * @param position The position of the displayed view.
	 * @param size The size of the displayed view.
	 */
	private void setBufferLocatables(Point position, Dimension size) {
		Rectangle bound = new Rectangle(position, size);
		ArrayList<Locatable> ls = getLocatablesInBox(bound);
		for (Locatable l : ls) {
			addToBuffer(l, position, size);
		}
	}
	
	/**
	 * Sets the buffer to the initial tiles.
	 * 
	 * @param grid The grid of tiles to use to populate the display.
	 * @param xOffset The number of tiles to shift the display right.
	 * @param yOffset The number of tiles to shift the display down.
	 */
	private void setBufferTiles(Grid<Tile> grid, int xOffset, int yOffset) {
		clearBuffer();
		char c = '\0';
		Dimension d = grid.getSize();
		Point p = new Point(0, 0);
		for (p.y = 0; p.y < d.height; p.y++) {
			for (p.x = 0; p.x < d.width; p.x++) {
				c = grid.itemAt(p).getDisplayChar();
				buffer.set(new Point(p.x + xOffset, p.y + yOffset), c);
			}
		}
	}
	
	/**
	 * Updates the actual display area with the buffer.
	 */
	private void showBuffer() {
		StringBuilder sb = new StringBuilder();
		Point p = new Point(0, 0);
		for (p.y = 0; p.y < textArea.getRows(); p.y++) {
			for (p.x = 0; p.x < textArea.getColumns(); p.x++) {
				sb.append(buffer.itemAt(p));
			}
			if (p.y < textArea.getRows() - 1) {
				sb.append('\n');
			}
		}
		textArea.setText(sb.toString());
	}
	
}
