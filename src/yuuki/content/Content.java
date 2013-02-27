package yuuki.content;

import java.util.List;
import java.util.Map;

import yuuki.action.Action;
import yuuki.entity.Character;
import yuuki.world.Land;
import yuuki.world.Portal;
import yuuki.world.Tile;

/**
 * Container class to hold all items that are a part of game engine content.
 */
class Content {
	
	/**
	 * Contains action definitions.
	 */
	public Map<Integer, Action.Definition> actions;
	
	/**
	 * Contains paths to sound effect files.
	 */
	public Map<String, String> effectDefinitions;
	
	/**
	 * Contains sound effect data.
	 */
	public Map<String, byte[]> effects;
	
	/**
	 * Contains entity definitions.
	 */
	public Map<String, Character.Definition> entities;
	
	/**
	 * Contains paths to image files.
	 */
	public Map<String, String> imageDefinitions;
	
	/**
	 * Contains image data.
	 */
	public Map<String, byte[]> images;
	
	/**
	 * Contains land data.
	 */
	public List<Land> lands;
	
	/**
	 * Contains music data.
	 */
	public Map<String, byte[]> music;
	
	/**
	 * Contains paths to music files.
	 */
	public Map<String, String> musicDefinitions;
	
	/**
	 * Contains portal definitions.
	 */
	public List<Portal.Definition> portals;
	
	/**
	 * Contains tile definitions.
	 */
	public Map<Integer, Tile.Definition> tiles;
	
	/**
	 * Contains paths to land files.
	 */
	public List<String> world;
	
	/**
	 * Creates a new Content instance.
	 */
	public Content() {
		reset();
	}
	
	/**
	 * Resets all internal content to null.
	 */
	public void reset() {
		resetAssets();
		resetWorld();
	}
	
	/**
	 * Resets all non-world properties to null.
	 */
	public void resetAssets() {
		musicDefinitions = null;
		effectDefinitions = null;
		imageDefinitions = null;
		music = null;
		effects = null;
		images = null;
		actions = null;
		entities = null;
	}
	
	/**
	 * Resets all world properties to null.
	 */
	public void resetWorld() {
		lands = null;
		portals = null;
		tiles = null;
		world = null;
	}
	
}
