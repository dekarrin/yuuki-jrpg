package yuuki.world;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import yuuki.content.Mergeable;
import yuuki.util.InvalidIndexException;

/**
 * Contains the definitions for creating tiles.
 */
public class TileFactory implements Mergeable<TileFactory> {
	
	/**
	 * The definition of a Tile.
	 */
	private static class TileDefinition {
		
		/**
		 * The image data for the tile.
		 */
		public String image;
		
		/**
		 * The name of the Tile.
		 */
		public String name;
		
		/**
		 * Whether the Tile can be walked on.
		 */
		public boolean walkable;
		
	}
	
	/**
	 * The character that represents a void tile.
	 */
	public static final char VOID_CHAR = ' ';
	
	/**
	 * The image that represents a void tile.
	 */
	public static final String VOID_PATH = "TILE_VOID";
	
	/**
	 * The definitions in this TileFactory.
	 */
	private Map<Integer, Deque<TileDefinition>> definitions;
	
	/**
	 * Creates a new TileFactory.
	 */
	public TileFactory() {
		definitions = new HashMap<Integer, Deque<TileDefinition>>();
		addDefinition(VOID_CHAR, "void", false, VOID_PATH);
	}
	
	/**
	 * Adds a Tile definition.
	 * 
	 * @param id The ID of the tile to add.
	 * @param name The name of the tile definition.
	 * @param walkable Whether the tile is walkable.
	 * @param path The path to the tile graphics file.
	 * 
	 * @throws IOException If an IOException occurs.
	 */
	public void addDefinition(int id, String name, boolean walkable, String
			path) {
		TileDefinition def = new TileDefinition();
		def.name = name;
		def.walkable = walkable;
		def.image = path;
		Deque<TileDefinition> d = definitions.get(name);
		if (d == null) {
			d = new ArrayDeque<TileDefinition>();
			definitions.put(id, d);
		}
		d.push(def);
	}
	
	@Override
	public void merge(TileFactory content) {
		for (int id : content.definitions.keySet()) {
			TileDefinition td = content.definitions.get(id).peek();
			addDefinition(id, td.name, td.walkable, td.image);
		}
	}
	
	@Override
	public void subtract(TileFactory content) {
		for (int id : content.definitions.keySet()) {
			TileDefinition td = content.definitions.get(id).peek();
			Deque<TileDefinition> d = this.definitions.get(id);
			if (d != null) {
				d.remove(td);
				if (d.isEmpty()) {
					this.definitions.remove(id);
				}
			}
		}
	}
	
	/**
	 * Creates a Tile from an ID.
	 * 
	 * @param id The ID of the Tile to create. This is the integer version of
	 * the character representing it.
	 * 
	 * @return The created Tile.
	 * 
	 * @throws InvalidIndexException If the given ID does not refer to a valid
	 * Tile.
	 */
	public Tile createTile(int id) throws InvalidIndexException {
		Deque<TileDefinition> tdDeque = definitions.get(id);
		if (tdDeque == null) {
			throw new InvalidIndexException(id);
		}
		TileDefinition def = tdDeque.peekFirst();
		Tile tile = new Tile(def.name, def.walkable, def.image);
		tile.setId(id);
		return tile;
	}
	
}
