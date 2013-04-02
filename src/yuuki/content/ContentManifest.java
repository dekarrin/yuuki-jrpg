package yuuki.content;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains information on the resources located inside a ContentPack.
 */
public class ContentManifest {
	
	/**
	 * The key that contains the path for the sound effect directory.
	 */
	public static final String DIR_EFFECTS = "SOUND_DIR";
	
	/**
	 * The key that contains the path for the images directory.
	 */
	public static final String DIR_IMAGES = "IMAGE_DIR";
	
	/**
	 * The key that contains the path for the lands directory.
	 */
	public static final String DIR_LANDS = "LAND_DIR";
	
	/**
	 * The key that contains the path for the music directory.
	 */
	public static final String DIR_MUSIC = "MUSIC_DIR";
	
	/**
	 * The key that contains the path for the action definition file.
	 */
	public static final String FILE_ACTIONS = "ACTION_DEFS_FILE";
	
	/**
	 * The key that contains the path for the sound effect definition file.
	 */
	public static final String FILE_EFFECTS = "SOUND_DEFS_FILE";
	
	/**
	 * The key that contains the path for the entity definition file.
	 */
	public static final String FILE_ENTITIES = "ENTITY_DEFS_FILE";
	
	/**
	 * The key that contains the path for the image definition file.
	 */
	public static final String FILE_IMAGES = "IMAGE_DEFS_FILE";
	
	/**
	 * The key that contains the path for the item definition file.
	 */
	public static final String FILE_ITEMS = "ITEM_DEFS_FILE";
	
	/**
	 * The key that contains the path for the music definition file.
	 */
	public static final String FILE_MUSIC = "MUSIC_DEFS_FILE";
	
	/**
	 * The key that contains the path for the portal definition file.
	 */
	public static final String FILE_PORTALS = "PORTAL_DEFS_FILE";
	
	/**
	 * The key that contains the path for the tile definition file.
	 */
	public static final String FILE_TILES = "TILE_DEFS_FILE";
	
	/**
	 * The key that contains the path for the world definition file.
	 */
	public static final String FILE_WORLD = "WORLD_DEFS_FILE";
	
	/**
	 * The paths to the resources.
	 */
	private Map<String, String> paths;
	
	/**
	 * Creates a new ContentManifest.
	 */
	public ContentManifest() {
		paths = new HashMap<String, String>();
	}
	
	/**
	 * Adds a path to this ContentManifest.
	 * 
	 * @param key The key of the path to add.
	 * @param value The path to add.
	 */
	public void add(String key, String value) {
		paths.put(key, value);
	}
	
	/**
	 * Gets a path from this ContentManifest.
	 * 
	 * @param index The index of the path to get.
	 * @return The path associated with the given index.
	 */
	public String get(String index) {
		String p = paths.get(index);
		if (p == null) {
			throw new IllegalArgumentException(index);
		}
		return p;
	}
	
	/**
	 * Checks whether this ContentManifest has a path for an index.
	 * 
	 * @param index The index to check.
	 * @return Whether this ContentManifest has a path for the index.
	 */
	public boolean has(String index) {
		return paths.containsKey(index);
	}
	
}
