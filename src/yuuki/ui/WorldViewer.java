package yuuki.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JComponent;

import yuuki.graphic.ImageFactory;
import yuuki.sprite.ImageSprite;
import yuuki.util.ElementGrid;
import yuuki.util.Grid;
import yuuki.world.Locatable;
import yuuki.world.Tile;
import yuuki.world.TileFactory;

/**
 * Displays the overworld graphically.
 */
@SuppressWarnings("serial")
public class WorldViewer extends JPanel {
	
	/**
	 * The exact images being displayed.
	 */
	private Grid<Image> buffer;
	
	/**
	 * The section of the buffer that contains the drawn map.
	 */
	private Grid<Image> bufferView;
	
	/**
	 * Generates tile graphics.
	 */
	private ImageFactory images;
	
	/**
	 * The Locatables on the screen. They are arranged in layers, which specify
	 * the z-ordering of the locatables. Higher layers are drawn above lower
	 * layers.
	 */
	private Map<Integer, Set<Locatable>> locatables;
	
	/**
	 * The view of the world that is being drawn.
	 */
	private Grid<Tile> subView;
	
	/**
	 * The current view of this world.
	 */
	private Grid<Tile> view;
	
	/**
	 * The size of a tile, in pixels.
	 */
	public static final int TILE_SIZE = 32;
	
	/**
	 * The width of this viewer, in tiles.
	 */
	private int tileWidth;
	
	/**
	 * The height of this viewer, in tiles.
	 */
	private int tileHeight;
	
	/**
	 * Creates a new WorldViewer that can display the specified number of
	 * tiles.
	 * 
	 * @param width The width of this WorldViewer in tiles.
	 * @param height The height of this WorldViewer in tiles.
	 */
	public WorldViewer(int width, int height) {
		tileWidth = width;
		tileHeight = height;
		Dimension d = new Dimension(width, height);
		buffer = new ElementGrid<Image>(d);
		locatables = new HashMap<Integer, Set<Locatable>>();
		setLayout(null);
		setBounds(0, 0, width * TILE_SIZE, height * TILE_SIZE);
	}
	
	/**
	 * Adds a Locatable to the current display.
	 * 
	 * @param l The Locatable to add.
	 * @param zIndex The Z-index of the layer to add the locatable to.
	 */
	public void addLocatable(Locatable l, int zIndex) {
		if (!layerExists(zIndex)) {
			createLayer(zIndex);
		}
		Set<Locatable> layer = getLayer(zIndex);
		layer.add(l);
	}
	
	/**
	 * Removes all Locatables from the current display.
	 */
	public void clearLocatables() {
		locatables.clear();
	}
	
	/**
	 * Gets the number of layers of Locatable objects. The layers with Z-index
	 * 0 up to but not including the return value of this method are guaranteed
	 * to exist.
	 * 
	 * @return The number of layers.
	 */
	public int getLayerCount() {
		return locatables.size();
	}
	
	/**
	 * Gets the Locatables that fall within a certain rectangle.
	 * 
	 * @param box The Rectangle from within the Locatables should be drawn.
	 * @param zIndex The Z-index of the layer of Locatables to search in.
	 * 
	 * @return The Locatables that currently fall within the bounding box.
	 */
	public ArrayList<Locatable> getLocatablesInBox(Rectangle box, int zIndex) {
		ArrayList<Locatable> desired = new ArrayList<Locatable>();
		Set<Locatable> layer = getLayer(zIndex);
		for (Locatable l : layer) {
			if (box.contains(l.getLocation())) {
				desired.add(l);
			}
		}
		return desired;
	}
	
	/**
	 * Sets the image factory for tile graphics.
	 * 
	 * @param imageFactory The ImageFactory to use.
	 */
	public void setImageFactory(ImageFactory imageFactory) {
		images = imageFactory;
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
		clearBuffer();
		Point requested = setWorldSubView(center);
		setBufferView(requested);
		drawWorldSubView();
		drawLocatables();
		repaint();
	}
	
	/**
	 * Sets all tiles in the tile buffer to be empty.
	 */
	private void clearBuffer() {
		Image i = images.createImage(TileFactory.VOID_PATH);
		Point p = new Point();
		for (p.y = 0; p.y < tileHeight; p.y++) {
			for (p.x = 0; p.x < tileWidth; p.x++) {
				buffer.set(p, i);
			}
		}
	}
	
	/**
	 * Creates an empty Locatable layer with the given Z-index. If there is
	 * already a layer with the given Z-index, it is replaced with an empty
	 * layer.
	 * 
	 * @param zIndex The Z-index of the layer to create.
	 */
	private void createLayer(int zIndex) {
		locatables.put(zIndex, new HashSet<Locatable>());
	}
	
	/**
	 * Draws the locatables on the screen.
	 */
	private void drawLocatables() {
		Rectangle box;
		box = new Rectangle(subView.getLocation(), subView.getSize());
		for (int i : locatables.keySet()) {
			ArrayList<Locatable> ls = getLocatablesInBox(box, i);
			for (Locatable l : ls) {
				Point p = new Point(l.getLocation());
				p.x -= box.x;
				p.y -= box.y;
				drawOnBuffer(p, l.getDisplayable().getOverworldImage());
			}
		}
	}
	
	/**
	 * Draws a single item on the buffer.
	 * 
	 * @param position The point to draw the item at.
	 * @param i The path of the image to draw.
	 */
	private void drawOnBuffer(Point position, String i) {
		Image img = images.createImage(i);
		bufferView.set(position, img);
	}
	
	/**
	 * Draws the displayed world view onto the buffer.
	 */
	private void drawWorldSubView() {
		Point p = new Point(0, 0);
		Dimension size = bufferView.getSize();
		for (p.x = 0; p.x < size.width; p.x++) {
			for (p.y = 0; p.y < size.height; p.y++) {
				drawOnBuffer(p, subView.itemAt(p).getOverworldImage());
			}
		}
	}
	
	/**
	 * Gets one layer of the Locatables to be drawn on the screen.
	 * 
	 * @param zIndex The Z-index of the layer to get.
	 * 
	 * @return The layer.
	 */
	private Set<Locatable> getLayer(int zIndex) {
		return locatables.get(zIndex);
	}
	
	/**
	 * Checks whether a Locatable with a given Z-index exists.
	 * 
	 * @param zIndex The Z-index of the layer to check.
	 * 
	 * @return True if the layer exists; otherwise, false.
	 */
	private boolean layerExists(int zIndex) {
		return (locatables.containsKey(zIndex));
	}
	
	/**
	 * Sets the buffer sub view as the section that matches the draw position
	 * of the current world sub view.
	 * 
	 * @param request The requested upper-left corner.
	 */
	private void setBufferView(Point request) {
		Rectangle subBox, bufBox, bufView;
		subBox = new Rectangle(subView.getLocation(), subView.getSize());
		bufBox = new Rectangle(buffer.getLocation(), buffer.getSize());
		bufView = new Rectangle(buffer.getLocation(), buffer.getSize());
		if (subBox.height < bufBox.height) {
			int shiftAmount = subBox.y - request.y;
			bufView.y += shiftAmount;
			bufView.height -= (bufBox.height - subBox.height);
		}
		if (subBox.width < bufBox.width) {
			int shiftAmount = subBox.x - request.x;
			bufView.x += shiftAmount;
			bufView.width -= (bufBox.width - subBox.width);
		}
		bufferView = buffer.getSubGrid(bufView);
	}
	
	/**
	 * Gets the proper sub view centered about a point.
	 * 
	 * @param center The center of the view to set as the sub view.
	 * 
	 * @return The position of the requested upper-left corner.
	 */
	private Point setWorldSubView(Point center) {
		Dimension size = buffer.getSize();
		Point actualLocation = new Point(center);
		actualLocation.translate(-(size.width / 2), -(size.height / 2));
		Rectangle subBox = new Rectangle(actualLocation, size);
		subView = view.getSubGrid(subBox);
		return subBox.getLocation();
	}
	
	/**
	 * Paints the images onto this component.
	 */
	
	/**
	 * Updates the actual display area with the buffer.
	 */
	private void showBuffer() {
		StringBuilder sb = new StringBuilder();
		Point p = new Point(0, 0);
		for (p.y = 0; p.y < tileHeight; p.y++) {
			for (p.x = 0; p.x < tileWidth; p.x++) {
				sb.append(buffer.itemAt(p));
			}
			if (p.y < tileHeight - 1) {
				sb.append('\n');
			}
		}
	}
	
}
