package yuuki.world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
	 * Gets the player start for the current land.
	 * 
	 * @return The player start.
	 */
	public Point getPlayerStart() {
		return activeLand.getPlayerStart();
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
	 */
	public void advance() {
		activeLand.advance();
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
	 * Gets the current land as a .lnd format String.
	 * 
	 * @return The current Land's String representation.
	 */
	public String getLandAsString() {
		return activeLand.toString();
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
	 * Gets the Locatables in the current Land.
	 * 
	 * @return The Locatables in the current Land.
	 */
	public ArrayList<Locatable> getLocatables() {
		return activeLand.getLocatables();
	}
	
	/**
	 * Gets the tiles in the current Land.
	 * 
	 * @return The TileGrid that makes up the current Land.
	 */
	public TileGrid getTiles() {
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
	
}
