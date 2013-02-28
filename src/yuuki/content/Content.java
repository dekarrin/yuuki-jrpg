package yuuki.content;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
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
	private Map<Integer, Deque<Action.Definition>> actions;
	
	/**
	 * Contains paths to sound effect files.
	 */
	private Map<String, Deque<String>> effectDefinitions;
	
	/**
	 * Contains sound effect data.
	 */
	private Map<String, Deque<byte[]>> effects;
	
	/**
	 * Contains entity definitions.
	 */
	private Map<String, Deque<Character.Definition>> entities;
	
	/**
	 * Contains paths to image files.
	 */
	private Map<String, Deque<String>> imageDefinitions;
	
	/**
	 * Contains image data.
	 */
	private Map<String, Deque<byte[]>> images;
	
	/**
	 * Contains land data.
	 */
	private List<Land> lands;
	
	/**
	 * Contains music data.
	 */
	private Map<String, Deque<byte[]>> music;
	
	/**
	 * Contains paths to music files.
	 */
	private Map<String, Deque<String>> musicDefinitions;
	
	/**
	 * Contains portal definitions.
	 */
	private Map<String, Deque<Portal.Definition>> portals;
	
	/**
	 * Contains tile definitions.
	 */
	private Map<Integer, Deque<Tile.Definition>> tiles;
	
	
	
	/**
	 * Contains paths to land files.
	 */
	private List<String> world;
	
	/**
	 * Creates a new Content instance. All properties are set to null.
	 */
	public Content() {
		reset();
	}
	
	/**
	 * Initializes all internal content to empty collections.
	 */
	public void init() {
		initAssets();
		initWorld();
	}
	
	/**
	 * Initializes all non-world properties to empty collections.
	 */
	public void initAssets() {
		musicDefinitions = new HashMap<String, Deque<String>>();
		effectDefinitions = new HashMap<String, Deque<String>>();
		imageDefinitions = new HashMap<String, Deque<String>>();
		music = new HashMap<String, Deque<byte[]>>();
		effects = new HashMap<String, Deque<byte[]>>();
		images = new HashMap<String, Deque<byte[]>>();
		actions = new HashMap<Integer, Deque<Action.Definition>>();
		entities = new HashMap<String, Deque<Character.Definition>>();
	}
	
	/**
	 * Initializes all world properties to empty collections.
	 */
	public void initWorld() {
		lands = new ArrayList<Land>();
		portals = new HashMap<String, Deque<Portal.Definition>>();
		tiles = new HashMap<Integer, Deque<Tile.Definition>>();
		world = new ArrayList<String>();
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
