package yuuki.world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Holds all data for a particular land in the world.
 */
public class Land {
	
	/**
	 * The name of this Land.
	 */
	private String name;
	
	/**
	 * The tiles that make up this Land.
	 */
	private TileGrid tiles;
	
	/**
	 * The Locatable objects in this Land.
	 */
	private Set<Locatable> occupants;
	
	/**
	 * Creates a new Land.
	 * 
	 * @param name The name of this Land.
	 * @param width The width, in tiles, of this Land.
	 * @param height The height, in tiles, of this Land.
	 * @param tileData The tiles that makes up this Land.
	 */
	public Land(String name, int width, int height, Tile[] tileData) {
		this.name = name;
		tiles = new TileGrid(width, height, tileData);
		occupants = new HashSet<Locatable>();
	}
	
	/**
	 * Gets the WalkGraph for a given point.
	 * 
	 * @param center The coordinates of the center of the WalkGraph.
	 * 
	 * @return The WalkGraph for the given point.
	 */
	public WalkGraph getWalkGraph(Point center) {
		Point p = center;
		TileGrid grid = tiles.getSubGrid(p.x - 1, p.y - 1, 3, 3);
		WalkGraph graph = new WalkGraph(p, grid);
		return graph;
	}
	
	/**
	 * Gets the next Point in a path to a destination.
	 * 
	 * @param current The start of the path.
	 * @param dest The destination of the path.
	 * 
	 * @return The next point along the path described.
	 */
	public Point getNextStep(Point current, Point dest) {
		// TODO: Path-finding
		return current;
	}
	
	/**
	 * Gets the tiles that make up this Land.
	 * 
	 * @return The tiles.
	 */
	public TileGrid getTiles() {
		return tiles;
	}
	
	/**
	 * Gets all the Locatables that occupy this Land.
	 * 
	 * @return The Locatables.
	 */
	public ArrayList<Locatable> getOccupants() {
		return new ArrayList<Locatable>(occupants);
	}
	
	/**
	 * Adds a Locatable to this Land.
	 * 
	 * @param o The Locatable to add.
	 */
	public void addOccupant(Locatable o) {
		occupants.add(o);
	}
	
	/**
	 * Removes a Locatable from this Land.
	 * 
	 * @param o The Locatable to remove.
	 */
	public void removeOccupant(Locatable o) {
		occupants.remove(o);
	}
	
	/**
	 * Gets the height of this Land in tiles.
	 * 
	 * @return The height.
	 */
	public int getWidth() {
		return tiles.getWidth();
	}
	
	/**
	 * Gets the height of this Land in tiles.
	 * 
	 * @return The height.
	 */
	public int getHeight() {
		return tiles.getHeight();
	}
	
	/**
	 * Gets the name of this Land.
	 * 
	 * @return The name of this Land.
	 */
	public String getName() {
		return name;
	}
	
}
