package yuuki.world;

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
	 * Advances the world by one tick. All Locatables are queried for where
	 * they wish to move, and if they make a valid request, they are moved
	 * to where they requested.
	 */
	public void advance() {
		// TODO: advancement logic
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
	 * Gets the current land.
	 * 
	 * @return The current land.
	 */
	public Land getLand() {
		return activeLand;
	}
	
}
