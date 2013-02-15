package yuuki.util;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Points to a specific region of a Grid.
 * 
 * @param <E> The type of element that this sub grid's source holds.
 */
public class SubGrid<E> implements Grid<E> {
	
	/**
	 * The area in the source grid that this sub grid covers.
	 */
	private Rectangle boundingBox;
	
	/**
	 * The TileGrid that this sub grid points to.
	 */
	private Grid<E> sourceGrid;
	
	/**
	 * Creates a new TileSubGrid that points to an existing one.
	 * 
	 * @param sourceGrid The grid that this TileSubGrid points to.
	 * @param box The area within the grid that this TileSubGrid points to. Any
	 * invalid points that this box covers (i.e. points that lie outside of the
	 * source grid) are removed by resizing the given box.
	 */
	public SubGrid(Grid<E> sourceGrid, Rectangle box) {
		super();
		this.sourceGrid = sourceGrid;
		this.boundingBox = resizeToFit(box);
	}
	
	@Override
	public void clear() {
		Dimension size = getSize();
		Point p = new Point(0, 0);
		Point p2;
		for (p.y = 0; p.y < size.height; p.y++) {
			for (p.x = 0; p.x < size.width; p.x++) {
				p2 = new Point(p);
				transformRelativeToAbsolute(p2);
				sourceGrid.set(p2, null);
			}
		}
	}
	
	@Override
	public boolean contains(Point p) {
		Rectangle box = new Rectangle(getSize());
		return box.contains(p);
	}
	
	@Override
	public Point getLocation() {
		return boundingBox.getLocation();
	}
	
	@Override
	public Dimension getSize() {
		return boundingBox.getSize();
	}
	
	@Override
	public Grid<E> getSubGrid(Rectangle boundingBox) {
		return new SubGrid<E>(this, boundingBox);
	}
	
	@Override
	public E itemAt(Point p) {
		p = new Point(p);
		if (contains(p)) {
			transformRelativeToAbsolute(p);
			return sourceGrid.itemAt(p);
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}
	
	@Override
	public void set(Point p, E e) {
		p = new Point(p);
		if (contains(p)) {
			transformRelativeToAbsolute(p);
			sourceGrid.set(p, e);
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Point p = new Point();
		for (p.y = 0; p.y < boundingBox.height; p.y++) {
			for (p.x = 0; p.x < boundingBox.width; p.x++) {
				sb.append(itemAt(p));
			}
			if (p.y < boundingBox.height - 1) {
				sb.append('\n');
			}
		}
		return sb.toString();
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
	 * @param p The relative point to transform. This reference will be
	 * mutated.
	 * 
	 * @return The absolute version of the given point.
	 */
	private Point transformRelativeToAbsolute(Point p) {
		p.x += boundingBox.x;
		p.y += boundingBox.y;
		return p;
	}
	
}
