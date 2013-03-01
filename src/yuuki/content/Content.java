package yuuki.content;

import java.util.ArrayDeque;
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
	 * Gets action definitions.
	 * 
	 * @return The definitions.
	 */
	public Map<Integer, Action.Definition> getActions() {
		return createMapView(actions);
	}
	
	/**
	 * Gets paths to sound effect files.
	 * 
	 * @return The paths.
	 */
	public Map<String, String> getEffectDefinitions() {
		return createMapView(effectDefinitions);
	}
	
	/**
	 * Gets sound effect data.
	 * 
	 * @return Sound effect data.
	 */
	public Map<String, byte[]> getEffects() {
		return createMapView(effects);
	}
	
	/**
	 * Gets entity definitions.
	 * 
	 * @return The definitions.
	 */
	public Map<String, Character.Definition> getEntities() {
		return createMapView(entities);
	}
	
	/**
	 * Gets paths to image files.
	 * 
	 * @return The paths.
	 */
	public Map<String, String> getImageDefinitions() {
		return createMapView(imageDefinitions);
	}
	
	/**
	 * Gets image data.
	 * 
	 * @return The image data.
	 */
	public Map<String, byte[]> getImages() {
		return createMapView(images);
	}
	
	/**
	 * Gets land data.
	 * 
	 * @return The land data.
	 */
	public List<Land> getLands() {
		return lands;
	}
	
	/**
	 * Gets music data.
	 * 
	 * @return The music data.
	 */
	public Map<String, byte[]> getMusic() {
		return createMapView(music);
	}
	
	/**
	 * Gets paths to music files.
	 * 
	 * @return The paths.
	 */
	public Map<String, String> getMusicDefinitions() {
		return createMapView(musicDefinitions);
	}
	
	/**
	 * Gets portal definitions.
	 * 
	 * @return The definitions.
	 */
	public Map<String, Portal.Definition> getPortals() {
		return createMapView(portals);
	}
	
	/**
	 * Gets tile definitions.
	 * 
	 * @return The definitions.
	 */
	public Map<Integer, Tile.Definition> getTiles() {
		return createMapView(tiles);
	}
	
	/**
	 * Gets the paths to the land files.
	 * 
	 * @return The paths.
	 */
	public List<String> getWorld() {
		return world;
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
	
	/**
	 * Sets action definitions.
	 * 
	 * @param defs The definitions.
	 */
	public void setActions(Map<Integer, Action.Definition> defs) {
		actions = createMap(defs);
	}
	
	/**
	 * Sets paths to sound effect files.
	 * 
	 * @param paths The paths.
	 */
	public void setEffectDefinitions(Map<String, String> paths) {
		effectDefinitions = createMap(paths);
	}
	
	/**
	 * Sets sound effect data.
	 * 
	 * @param data Sound effect data.
	 */
	public void setEffects(Map<String, byte[]> data) {
		effects = createMap(data);
	}
	
	/**
	 * Sets entity definitions.
	 * 
	 * @param defs The definitions.
	 */
	public void setEntities(Map<String, Character.Definition> defs) {
		entities = createMap(defs);
	}
	
	/**
	 * Sets paths to image files.
	 * 
	 * @param paths The paths.
	 */
	public void setImageDefinitions(Map<String, String> paths) {
		imageDefinitions = createMap(paths);
	}
	
	/**
	 * Sets image data.
	 * 
	 * @param data The image data.
	 */
	public void setImages(Map<String, byte[]> data) {
		images = createMap(data);
	}
	
	/**
	 * Sets land data.
	 * 
	 * @param data The land data.
	 */
	public void setLands(List<Land> data) {
		lands = data;
	}
	
	/**
	 * Sets music data.
	 * 
	 * @param data The music data.
	 */
	public void setMusic(Map<String, byte[]> data) {
		music = createMap(data);
	}
	
	/**
	 * Sets paths to music files.
	 * 
	 * @param paths The paths.
	 */
	public void setMusicDefinitions(Map<String, String> paths) {
		musicDefinitions = createMap(paths);
	}
	
	/**
	 * Sets portal definitions.
	 * 
	 * @param defs The definitions.
	 */
	public void setPortals(Map<String, Portal.Definition> defs) {
		portals = createMap(defs);
	}
	
	/**
	 * Sets tile definitions.
	 * 
	 * @param defs The definitions.
	 */
	public void setTiles(Map<Integer, Tile.Definition> defs) {
		tiles = createMap(defs);
	}
	
	/**
	 * Sets the paths to the land files.
	 * 
	 * @param paths The paths.
	 */
	public void setWorld(List<String> paths) {
		world = paths;
	}
	
	/**
	 * Creates a priority map from a normal map.
	 * @param <K> The type of the key.
	 * @param <V> The type of the value.
	 * @param view The view to create the map from.
	 * @return The created map.
	 */
	private <K, V> Map<K, Deque<V>> createMap(Map<K, V> view) {
		Map<K, Deque<V>> map = new HashMap<K, Deque<V>>();
		for (Map.Entry<K, V> entry : view.entrySet()) {
			Deque<V> vDeque = new ArrayDeque<V>();
			vDeque.push(entry.getValue());
			map.put(entry.getKey(), vDeque);
		}
		return map;
	}
	
	/**
	 * Creates a view for a map.
	 * 
	 * @param <K> The type of the key.
	 * @param <V> The type of the value.
	 * @return The view.
	 */
	private <K, V> Map<K, V> createMapView(Map<K, Deque<V>> map) {
		Map<K, V> defs = null;
		if (map != null) {
			defs = new HashMap<K, V>();
			for (K key : map.keySet()) {
				defs.put(key, map.get(key).peek());
			}
		}
		return defs;
	}
	
}
