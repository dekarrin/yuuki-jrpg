package yuuki.world;

import java.awt.Rectangle;

/**
 * Points to a specific region of a TileGrid.
 */
public class TileSubGrid extends TileGrid {
	
	/**
	 * The TileGrid that this sub grid points to.
	 */
	private TileGrid sourceGrid;
	
	/**
	 * The area in the source grid that this sub grid covers.
	 */
	private Rectangle boundingBox;
	
	/**
	 * Creates a new TileSubGrid that points to an existing one.
	 * 
	 * @param sourceGrid The grid that this TileSubGrid points to.
	 * @param boundingBox The area within the grid that this TileSubGrid point
	 * to.
	 */
	public TileSubGrid(TileGrid sourceGrid, Rectangle boundingBox) {
		super();
		this.sourceGrid = sourceGrid;
		this.boundingBox = boundingBox;
	}
	
}
