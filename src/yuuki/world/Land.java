package yuuki.world;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yuuki.util.ElementGrid;
import yuuki.util.Grid;

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
	 * The transfers that are waiting to come in.
	 */
	private List<Movable> incomingTransfers;
	
	/**
	 * The name of this Land.
	 */
	private String name;
	
	/**
	 * The position that the player starts at.
	 */
	private Point playerStart;
	
	/**
	 * The Portals that link this Land to different areas.
	 */
	private Map<Point, Portal> portals;
	
	/**
	 * The Movable objects in this Land.
	 */
	private Map<Point, Movable> residents;
	
	/**
	 * The tiles that make up this Land.
	 */
	private Grid<Tile> tiles;
	
	/**
	 * The residents that were transfered out in the last advancement.
	 */
	private List<Movable> transfers;
	
	/**
	 * Creates a new Land.
	 * 
	 * @param name The name of this Land.
	 * @param size The size of this Land, in tiles.
	 * @param start The player start for this land.
	 * @param tileData The tiles that makes up this Land.
	 */
	public Land(String name, Dimension size, Point start, Tile[] tileData) {
		this.name = name;
		playerStart = start;
		tiles = new ElementGrid<Tile>(size, tileData);
		residents = new HashMap<Point, Movable>();
		portals = new HashMap<Point, Portal>();
		this.incomingTransfers = new ArrayList<Movable>();
	}
	
	/**
	 * Adds a Portal to this Land if it has not already been added.
	 * 
	 * @param p The Portal to add.
	 */
	public void addPortal(Portal p) {
		Point pos = p.getLocation();
		if (portals.get(pos) == null) {
			portals.put(pos, p);
		}
	}
	
	/**
	 * Adds a resident to this Land if it has not already been added.
	 * 
	 * @param r The resident to add.
	 */
	public void addResident(Movable r) {
		Point pos = r.getLocation();
		if (residents.get(pos) == null) {
			residents.put(pos, r);
		}
	}
	
	/**
	 * Advances this Land by one tick. All residents are queried for where they
	 * wish to move, and if they make a valid request, they are moved to where
	 * they requested.
	 */
	public void advance() {
		processIncomingTransfers();
		moveResidents();
		transferOutResidents();
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
	 * Gets the player start.
	 */
	public Point getPlayerStart() {
		return playerStart;
	}
	
	/**
	 * Gets the portals in this Land.
	 * 
	 * @return The portals.
	 */
	public ArrayList<Portal> getPortals() {
		return new ArrayList<Portal>(portals.values());
	}
	
	/**
	 * Gets the residents in this Land.
	 * 
	 * @return The residents.
	 */
	public ArrayList<Movable> getResidents() {
		return new ArrayList<Movable>(residents.values());
	}
	
	/**
	 * Gets the size of this Land in tiles.
	 * 
	 * @return The size.
	 */
	public Dimension getSize() {
		return tiles.getSize();
	}
	
	/**
	 * Gets the tiles that make up this Land.
	 * 
	 * @return The tiles.
	 */
	public Grid<Tile> getTiles() {
		return tiles;
	}
	
	/**
	 * Gets the list of residents who transfered out during the last
	 * advancement.
	 * 
	 * @return The list of residents.
	 */
	public List<Movable> getTransfers() {
		return transfers;
	}
	
	/**
	 * Gets the WalkGraph for a given point.
	 * 
	 * @param center The coordinates of the center of the WalkGraph.
	 * 
	 * @return The WalkGraph for the given point.
	 */
	public WalkGraph getWalkGraph(Point center) {
		Point graphCenter = new Point(center.x - 1, center.y - 1);
		Dimension size = new Dimension(3, 3);
		Rectangle box = new Rectangle(graphCenter, size);
		Grid<Tile> grid = tiles.getSubGrid(box);
		WalkGraph graph = new WalkGraph(center, grid);
		return graph;
	}
	
	/**
	 * Checks whether a tile is occupied by a resident.
	 * 
	 * @param location The point to check.
	 * 
	 * @return True if the tile at the given point has a resident standing on
	 * it; otherwise, false.
	 */
	public boolean isOccupied(Point location) {
		Tile toCheck = tiles.itemAt(location);
		return toCheck.isOccupied();
	}
	
	/**
	 * Gets the portal at a specific location.
	 * 
	 * @param p The point that the portal is at.
	 * 
	 * @return The portal at the given location, or null if there is no portal.
	 */
	public Portal portalAt(Point p) {
		return portals.get(p);
	}
	
	/**
	 * Removes a Portal from this Land.
	 * 
	 * @param p The Portal to remove.
	 */
	public void removePortal(Portal p) {
		portals.remove(p.getLocation());
	}
	
	/**
	 * Removes a resident from this Land.
	 * 
	 * @param r The resident to remove.
	 */
	public void removeResident(Movable r) {
		residents.remove(r.getLocation());
	}
	
	/**
	 * Gets a String representation of this Land. This will be a .lnd format
	 * String containing all meta data and tiles.
	 * 
	 * @return The String representation of this Land.
	 */
	@Override
	public String toString() {
		StringBuilder strVersion = new StringBuilder();
		Point p = new Point();
		Dimension d = tiles.getSize();
		for (p.y = 0; p.y < d.height; p.y++) {
			for (p.x = 0; p.x < d.width; p.x++) {
				strVersion.append(tiles.itemAt(p).getDisplayChar());
			}
			if (p.x < d.height - 1) {
				strVersion.append('\n');
			}
		}
		return strVersion.toString();
	}
	
	/**
	 * Transfers in a resident to a specific point.
	 * 
	 * @param r The resident to transfer in.
	 * @param p The point to transfer to.
	 */
	public void transferInResident(Movable r, Point p) {
		r.setLocation(p);
		if (!residents.containsKey(p)) {
			addResident(r);
		} else {
			incomingTransfers.add(r);
		}
	}
	
	/**
	 * Clears the resident map and moves all residents to their new positions.
	 * 
	 * @param moveList An array containing the updated residents.
	 */
	private void applyMove(Movable[] moveList) {
		residents.clear();
		for (Movable r: moveList) {
			residents.put(r.getLocation(), r);
		}
	}
	
	/**
	 * Applies all of the transfers in the given list.
	 * 
	 * @param transfers The residents that need to be transfered.
	 */
	private void applyTransfers(List<Movable> transfers) {
		for (Movable r : transfers) {
			removeResident(r);
		}
	}
	
	/**
	 * Moves all residents. Each resident is asked where it wishes to move, and
	 * is then moved there.
	 */
	private void moveResidents() {
		Movable[] moveList = residents.values().toArray(new Movable[0]);
		for (Movable r: moveList) {
			Point destination = r.getNextMove(this);
			if (destination != null) {
				r.setLocation(destination);
			}
		}
		applyMove(moveList);
	}
	
	/**
	 * Adds waiting incoming transfers if they can be added.
	 */
	private void processIncomingTransfers() {
		Movable[] incoming = incomingTransfers.toArray(new Movable[0]);
		incomingTransfers.clear();
		for (Movable m : incoming) {
			transferInResident(m, m.getLocation());
		}
	}
	
	/**
	 * Moves transferable residents that have stepped on a portal.
	 */
	private void transferOutResidents() {
		Movable[] moveList = residents.values().toArray(new Movable[0]);
		List<Movable> transfers = new ArrayList<Movable>();
		for (Movable r: moveList) {
			Point p = r.getLocation();
			if (portals.containsKey(p) && r.isTransferrable()) {
				transfers.add(r);
			}
		}
		applyTransfers(transfers);
		this.transfers = transfers;
	}
	
}
