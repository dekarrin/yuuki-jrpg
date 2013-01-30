package yuuki.world;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * A grid of some objects in the world.
 * 
 * @param <T> The type of objects in the grid.
 */
public interface Grid<T> {
	
	/**
	 * Checks whether this grid contains a specific point.
	 * 
	 * @param point The point to check.
	 * 
	 * @return True if this grid contains the given point; otherwise, false.
	 */
	public boolean contains(Point point);
	
	/**
	 * Gets the dimensions of this Grid.
	 * 
	 * @return The dimensions.
	 */
	public Dimension getSize();
	
	/**
	 * Gets a representation of a section of this Grid. The returned Grid will
	 * only contain the valid coordinates specified by the given dimensions.
	 * 
	 * @param boundingBox The area that the sub grid covers.
	 * 
	 * @return A Grid that has the same references to element instances as this
	 * one does, with the specified dimensions.
	 */
	public Grid<T> getSubGrid(Rectangle boundingBox);
	
	/**
	 * Gets the element at a point in this grid. The origin is in the
	 * upper-left corner and coordinates are positive below and to the right of
	 * the origin.
	 * 
	 * @param point The point to get the element at.
	 * 
	 * @return The element at the given point.
	 */
	public T itemAt(Point point);
	
}
