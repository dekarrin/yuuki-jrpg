package yuuki.content;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
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
class Content implements Mergeable<Content> {
	
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
	private List<String> map;
	
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
	public Map<String, Land> getLands() {
		Map<String, Land> map = new HashMap<String, Land>(lands.size());
		for (Land l : lands) {
			map.put(l.getName(), l);
		}
		return map;
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
		return map;
	}
	
	@Override
	public void merge(Content content) {
		musicDefinitions =
				mergeMaps(musicDefinitions, content.musicDefinitions);
		effectDefinitions =
				mergeMaps(effectDefinitions, content.effectDefinitions);
		imageDefinitions =
				mergeMaps(imageDefinitions, content.imageDefinitions);
		music = mergeMaps(music, content.music);
		effects = mergeMaps(effects, content.effects);
		images = mergeMaps(images, content.images);
		actions = mergeMaps(actions, content.actions);
		entities = mergeMaps(entities, content.entities);
		lands = mergeLists(lands, content.lands);
		portals = mergeMaps(portals, content.portals);
		tiles = mergeMaps(tiles, content.tiles);
		map = mergeLists(map, content.map);
	}
	
	/**
	 * Resets all internal content to null.
	 */
	public void reset() {
		resetAssets();
		resetMaps();
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
		portals = null;
		tiles = null;
	}
	
	/**
	 * Resets all world properties to null.
	 */
	public void resetMaps() {
		lands = null;
		map = null;
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
		map = paths;
	}
	
	@Override
	public void subtract(Content content) {
		musicDefinitions =
				subtractMaps(musicDefinitions, content.musicDefinitions);
		effectDefinitions =
				subtractMaps(effectDefinitions, content.effectDefinitions);
		imageDefinitions =
				subtractMaps(imageDefinitions, content.imageDefinitions);
		music = subtractMaps(music, content.music);
		effects = subtractMaps(effects, content.effects);
		images = subtractMaps(images, content.images);
		actions = subtractMaps(actions, content.actions);
		entities = subtractMaps(entities, content.entities);
		lands = subtractLists(lands, content.lands);
		portals = subtractMaps(portals, content.portals);
		tiles = subtractMaps(tiles, content.tiles);
		map = subtractLists(map, content.map);
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
	
	/**
	 * Merges the items in one list with those of another.
	 * 
	 * @param original The original list.
	 * @param merging The list containing the items to be merged.
	 * @return The original list after the merge is applied to it. This may
	 * differ from the given original list if a null original list is given.
	 */
	private <E> List<E> mergeLists(List<E> original, List<E> merging) {
		if (merging != null) {
			if (original == null) {
				original = new ArrayList<E>();
			}
			for (E element : merging) {
				original.add(element);
			}
		}
		return original;
	}
	
	/**
	 * Merges the items in one map with those of another.
	 * 
	 * @param original The original map.
	 * @param merging The map containing the entries to be merged.
	 * @return The original map after the merge is applied to it. This may
	 * differ from the given original map if a null original map is given.
	 */
	private <V, K> Map<K, Deque<V>> mergeMaps(Map<K, Deque<V>> original,
			Map<K, Deque<V>> merging) {
		if (merging != null) {
			if (original == null) {
				original = new HashMap<K, Deque<V>>();
			}
			for (K key : merging.keySet()) {
				Deque<V> mergeDeque = merging.get(key);
				Deque<V> originalDeque = original.get(key);
				if (originalDeque == null) {
					originalDeque = new ArrayDeque<V>();
					original.put(key, originalDeque);
				}
				Iterator<V> it = mergeDeque.descendingIterator();
				while (it.hasNext()) {
					originalDeque.push(it.next());
				}
			}
		}
		return original;
	}
	
	/**
	 * Subtracts the items in one list from those of another.
	 * 
	 * @param original The original list.
	 * @param subtracting The list containing the items to be subtracted.
	 * @return The original list after the subtraction is applied to it. This
	 * will be null if the subtraction causes the original list to be empty.
	 */
	private <E> List<E> subtractLists(List<E> original, List<E> subtracting) {
		if (original != null && subtracting != null) {
			for (E element : subtracting) {
				original.remove(element);
			}
			if (original.isEmpty()) {
				original = null;
			}
		}
		return original;
	}
	
	/**
	 * Subtracts the entries in one map from those of another.
	 * 
	 * @param original The original map.
	 * @param merging The map containing the entries to be subtracted.
	 * @return The original map after the subtraction is applied to it. This
	 * will be null if the subtraction causes the original map to be empty.
	 */
	private <V, K> Map<K, Deque<V>> subtractMaps(Map<K, Deque<V>> original,
			Map<K, Deque<V>> subtracting) {
		if (original != null && subtracting != null) {
			for (K key : subtracting.keySet()) {
				Deque<V> subDeque = subtracting.get(key);
				Deque<V> originalDeque = original.get(key);
				if (originalDeque != null) {
					for (V element : subDeque) {
						originalDeque.remove(element);
					}
					if (originalDeque.isEmpty()) {
						original.remove(key);
					}
				}
			}
			if (original.isEmpty()) {
				original = null;
			}
		}
		return original;
	}
	
}
