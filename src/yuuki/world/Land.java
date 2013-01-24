package yuuki.world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Holds all data for a particular land in the world. In addition to their tile
 * data, Lands hold entities that have a specific location in the Land. All of
 * these entities implement the interface yuuki.world.Locatable, and there are
 * several types of entities.
 * 
 * A resident is a Locatable that moves from tile to tile around the Land on
 * every turn. Residents do not have a specific type, but all residents
 * implement the yuuki.world.Movable interface.
 * 
 * A portal is a Locatable that occupies a tile and serves to link the Land it
 * is in with another. All portals are of the type Portal.
 */
public class Land {
	
	/**
	 * The name of this Land.
	 */
	private String name;
	
	/**
	 * The Movable objects in this Land.
	 */
	private Set<Movable> residents;
	
	/**
	 * The Portals that link this Land to different areas.
	 */
	private Set<Portal> portals;
	
	/**
	 * The tiles that make up this Land.
	 */
	private TileGrid tiles;
	
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
		residents = new HashSet<Movable>();
		portals = new HashSet<Portal>();
	}
	
	/**
	 * Adds a resident to this Land.
	 * 
	 * @param r The resident to add.
	 */
	public void addResident(Movable r) {
		residents.add(r);
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
	 * Adds a Portal to this Land.
	 * 
	 * @param p The Portal to add.
	 */
	public void addPortal(Portal p) {
		portals.add(p);
	}
	
	/**
	 * Removes a Portal from this Land.
	 * 
	 * @param p The Portal to remove.
	 */
	public void removePortal(Portal p) {
		portals.remove(p);
	}
	
	/**
	 * Gets all the Locatables that occupy this Land.
	 * 
	 * @return The Locatables.
	 */
	public ArrayList<Locatable> getLocatables() {
		ArrayList<Locatable> ls = new ArrayList<Locatable>();
		ls.addAll(portals);
		ls.addAll(residents);
		return ls;
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
	 * Gets the height of this Land in tiles.
	 * 
	 * @return The height.
	 */
	public int getWidth() {
		return tiles.getWidth();
	}
	
	/**
	 * Removes a resident from this Land.
	 * 
	 * @param r The resident to remove.
	 */
	public void removeResident(Movable r) {
		residents.remove(r);
	}
	
}
