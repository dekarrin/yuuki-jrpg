package yuuki.world;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Points to a specific region of a TileGrid.
 */
public class TileSubGrid implements Grid<Tile> {
	
	/**
	 * The area in the source grid that this sub grid covers.
	 */
	private Rectangle boundingBox;
	
	/**
	 * The TileGrid that this sub grid points to.
	 */
	private Grid<Tile> sourceGrid;
	
	/**
	 * Creates a new TileSubGrid that points to an existing one.
	 * 
	 * @param sourceGrid The grid that this TileSubGrid points to.
	 * @param box The area within the grid that this TileSubGrid points to. Any
	 * invalid points that this box covers (i.e. points that lie outside of the
	 * source grid) are removed by resizing the given box.
	 */
	public TileSubGrid(Grid<Tile> sourceGrid, Rectangle box) {
		super();
		this.sourceGrid = sourceGrid;
		this.boundingBox = resizeToFit(box);
	}
	
	@Override
	public Dimension getSize() {
		return boundingBox.getSize();
	}
	
	@Override
	public Grid<Tile> getSubGrid(Rectangle boundingBox) {
		return new TileSubGrid(this, boundingBox);
	}
	
	@Override
	public Tile itemAt(Point p) {
		if (boundingBox.contains(p)) {
			transformRelativeToAbsolute(p);
			return sourceGrid.itemAt(p);
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}
	
	/**
	 * Resizes a Rectangle so that it does not contain any points that the
	 * original source grid does not contain.
	 * 
	 * @param box The Rectangle to resize.
	 * 
	 * @return The resized Rectangle.
	 */
	private Rectangle resizeToFit(Rectangle box) {
		Rectangle sourceBox = new Rectangle(sourceGrid.getSize());
		box = sourceBox.intersection(box);
		return box;
	}
	
	/**
	 * Transforms a relative point somewhere in this TileSubGrid to its
	 * absolute version in the source grid.
	 * 
	 * @param p The relative point to transform.
	 * 
	 * @return The absolute version of the given point.
	 */
	private Point transformRelativeToAbsolute(Point p) {
		p.x += boundingBox.x;
		p.y += boundingBox.y;
		return p;
	}
	
}
