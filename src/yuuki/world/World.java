package yuuki.world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yuuki.util.Grid;

/**
 * Handles overworld navigation and data. The World class is responsible for
 * loading land data, keeping track of entities on the current land, and
 * modifying the current land.
 */
public class World {
	
	/**
	 * The land currently being controlled by this World.
	 */
	private Land activeLand;
	
	/**
	 * All lands loaded, indexed by land name.
	 */
	private Map<String, Land> lands;
	
	/**
	 * Creates a new, empty World.
	 */
	public World() {
		lands = new HashMap<String, Land>();
	}
	
	/**
	 * Adds a new Land to this world.
	 * 
	 * @param land The Land to add.
	 */
	public void addLand(Land land) {
		lands.put(land.getName(), land);
	}
	
	/**
	 * Adds a resident to the active land.
	 * 
	 * @param resident The resident to add.
	 */
	public void addResident(Movable resident) {
		activeLand.addResident(resident);
	}
	
	/**
	 * Advances the world by one tick. The current Land is instructed to
	 * advance.
	 * 
	 * @throws InterruptedException If the current thread is interrupted while
	 * waiting for a move from the player.
	 */
	public void advance() throws InterruptedException {
		activeLand.advance();
		moveTransfers();
	}
	
	/**
	 * Changes the active land.
	 * 
	 * @param landName The name of the land to switch to.
	 */
	public void changeLand(String landName) {
		activeLand = lands.get(landName);
	}
	
	/**
	 * Gets the names of all lands in this World.
	 * 
	 * @return An array containing the names of all loaded Lands.
	 */
	public String[] getAllLandNames() {
		ArrayList<String> names = new ArrayList<String>();
		for (Land l : lands.values()) {
			names.add(l.getName());
		}
		return names.toArray(new String[0]);
	}
	
	/**
	 * Gets the name of the current Land.
	 * 
	 * @return The name of the current land.
	 */
	public String getLandName() {
		return activeLand.getName();
	}
	
	/**
	 * Gets the resident that another resident bumped into during the last
	 * world advancement.
	 * 
	 * @param resident The resident who bumped into another.
	 * 
	 * @return The resident that the given resident bumped into, or null if the
	 * given resident did not bump into another.
	 */
	public Movable getLastBump(Movable resident) {
		return activeLand.getLastBump(resident);
	}
	
	/**
	 * Gets the player start for the current land.
	 * 
	 * @return The player start.
	 */
	public Point getPlayerStart() {
		return activeLand.getPlayerStart();
	}
	
	/**
	 * Gets the portals in the current land.
	 * 
	 * @return The portals.
	 */
	public ArrayList<Portal> getPortals() {
		return activeLand.getPortals();
	}
	
	/**
	 * Gets the residents in the current land.
	 * 
	 * @return The residents.
	 */
	public ArrayList<Movable> getResidents() {
		return activeLand.getResidents();
	}
	
	/**
	 * Gets the tiles in the current Land.
	 * 
	 * @return The grid of tiles that makes up the current Land.
	 */
	public Grid<Tile> getTiles() {
		return activeLand.getTiles();
	}
	
	/**
	 * Removes a resident from the active land.
	 * 
	 * @param resident The resident to remove.
	 */
	public void removeResident(Movable resident) {
		activeLand.removeResident(resident);
	}
	
	/**
	 * Finalizes the transfers out of the active land and moves them to the
	 * land that they are transferring to.
	 */
	private void moveTransfers() {
		List<Movable> moves = activeLand.getTransfers();
		for (Movable m : moves) {
			Portal p = activeLand.portalAt(m.getLocation());
			Land destination = lands.get(p.getLinkedLand());
			if (destination == null) {
				throw new InvalidLinkNameException(p.getLinkedLand());
			}
			destination.transferInResident(m, p.getLink());
		}
	}
	
}
