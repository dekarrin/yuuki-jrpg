package yuuki.world;

import java.awt.Point;

/**
 * Contains the walkable points adjacent to some other point.
 */
public class WalkGraph {
	
	/**
	 * Whether this WalkGraph returns a valid point to walk on for all 8
	 * directions.
	 */
	private boolean fullyValid;
	
	/**
	 * Whether the north point is valid.
	 */
	private boolean hasNorth;
	
	/**
	 * Whether the north-east point is valid.
	 */
	private boolean hasNorthEast;
	
	/**
	 * Whether the north-west point is valid.
	 */
	private boolean hasNorthWest;
	
	/**
	 * Whether the west point is valid.
	 */
	private boolean hasWest;
	
	/**
	 * Whether the east point is valid.
	 */
	private boolean hasEast;
	
	/**
	 * Whether the south point is valid.
	 */
	private boolean hasSouth;
	
	/**
	 * Whether the south-east point is valid.
	 */
	private boolean hasSouthEast;
	
	/**
	 * Whether the south-west point is valid.
	 */
	private boolean hasSouthWest;
	
	/**
	 * The coordinates of the center of this WalkGraph.
	 */
	private Point p;
	
	/**
	 * Creates a new WalkGraph.
	 * 
	 * @param position The position of the center tile of this WalkGraph,
	 * relative to the Land that it came from.
	 * @param tiles An array containing the center tile and the surrounding
	 * eight tiles.
	 */
	public WalkGraph(Point position, TileGrid tiles) {
		this.p = position;
		setValidity(tiles);
		setFullyValid();
	}
	
	/**
	 * Gets the position of the center tile of this graph.
	 * 
	 * @return The position of the center tile.
	 */
	public Point getPosition() {
		return p;
	}
	
	/**
	 * Checks whether this WalkGraph returns a valid point for each of the 8
	 * directions.
	 * 
	 * @return True if this WalkGraph returns a valid point for each of the 8
	 * directions; otherwise, false.
	 */
	public boolean isFullyValid() {
		return fullyValid;
	}
	
	/**
	 * Gets the coordinates of the northern tile.
	 * 
	 * @return The point containing the coordinates of the tile if is a valid
	 * tile to walk on; otherwise, null.
	 */
	public Point getNorth() {
		return (hasNorth) ? new Point(p.x, p.y - 1) : null;
	}
	
	/**
	 * Gets the coordinates of the southern tile.
	 * 
	 * @return The point containing the coordinates of the tile if is a valid
	 * tile to walk on; otherwise, null.
	 */
	public Point getSouth() {
		return (hasSouth) ? new Point(p.x, p.y + 1) : null;
	}
	
	/**
	 * Gets the coordinates of the eastern tile.
	 * 
	 * @return The point containing the coordinates of the tile if is a valid
	 * tile to walk on; otherwise, null.
	 */
	public Point getEast() {
		return (hasEast) ? new Point(p.x + 1, p.y) : null;
	}
	
	/**
	 * Gets the coordinates of the western tile.
	 * 
	 * @return The point containing the coordinates of the tile if is a valid
	 * tile to walk on; otherwise, null.
	 */
	public Point getWest() {
		return (hasWest) ? new Point(p.x - 1, p.y) : null;
	}
	
	/**
	 * Gets the coordinates of the north-western tile.
	 * 
	 * @return The point containing the coordinates of the tile if is a valid
	 * tile to walk on; otherwise, null.
	 */
	public Point getNorthWest() {
		return (hasNorthWest) ? new Point(p.x - 1, p.y - 1) : null;
	}
	
	/**
	 * Gets the coordinates of the north-eastern tile.
	 * 
	 * @return The point containing the coordinates of the tile if is a valid
	 * tile to walk on; otherwise, null.
	 */
	public Point getNorthEast() {
		return (hasNorthEast) ? new Point(p.x + 1, p.y - 1) : null;
	}
	
	/**
	 * Gets the coordinates of the south-eastern tile.
	 * 
	 * @return The point containing the coordinates of the tile if is a valid
	 * tile to walk on; otherwise, null.
	 */
	public Point getSouthEast() {
		return (hasSouthEast) ? new Point(p.x - 1, p.y + 1) : null;
	}
	
	/**
	 * Gets the coordinates of the south-western tile.
	 * 
	 * @return The point containing the coordinates of the tile if is a valid
	 * tile to walk on; otherwise, null.
	 */
	public Point getSouthWest() {
		return (hasSouthWest) ? new Point(p.x + 1, p.y + 1) : null;
	}
	
	/**
	 * Sets whether each direction is valid based on whether the tile in each
	 * direction is walkable.
	 * 
	 * @param tiles An array containing the center tile and the surrounding
	 * eight tiles.
	 */
	private void setValidity(TileGrid tiles) {
		hasNorthWest = tiles.tileAt(0, 0).isWalkable();
		hasNorth = tiles.tileAt(1, 0).isWalkable();
		hasNorthEast = tiles.tileAt(2, 0).isWalkable();
		hasWest = tiles.tileAt(0, 1).isWalkable();
		hasEast = tiles.tileAt(2, 1).isWalkable();
		hasSouthWest = tiles.tileAt(0, 2).isWalkable();
		hasSouth = tiles.tileAt(1, 2).isWalkable();
		hasSouthEast = tiles.tileAt(2, 2).isWalkable();
	}
	
	/**
	 * Sets whether this graph has every direction as valid.
	 */
	private void setFullyValid() {
		fullyValid = hasNorthWest && hasNorth && hasNorthEast && hasWest &&
				hasEast && hasSouthWest && hasSouth && hasSouthEast;
	}
	
}
