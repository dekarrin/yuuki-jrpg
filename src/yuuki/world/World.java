package yuuki.world;

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
	 * Advances the world by one tick. All Locatables are queried for where
	 * they wish to move, and if they make a valid request, they are moved
	 * to where they requested.
	 */
	public void advance() {
		
	}
	
	/**
	 * Get the tiles in the current Land.
	 * 
	 * @return The TileGrid that makes up the current Land.
	 */
	public TileGrid getTiles() {
		return activeLand.getTiles();
	}
	
	/**
	 * Get the occupants in the current Land.
	 * 
	 * @return The occupants in the current Land.
	 */
	public ArrayList<Locatable> getOccupants() {
		return activeLand.getOccupants();
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
	 * Changes the active land.
	 * 
	 * @param landName The name of the land to switch to.
	 */
	public void changeLand(String landName) {
		activeLand = lands.get(landName);
	}
	
	/**
	 * Gets the name of the current Land.
	 * 
	 * @return The name of the current land.
	 */
	public String getLandName() {
		return activeLand.getName();
	}
	
}
