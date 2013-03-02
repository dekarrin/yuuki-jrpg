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
public class TileFactory implements Mergeable<Map<Integer, Tile.Definition>> {
	
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
	private Map<Integer, Deque<Tile.Definition>> definitions;
	
	/**
	 * Creates a new TileFactory.
	 */
	public TileFactory() {
		definitions = new HashMap<Integer, Deque<Tile.Definition>>();
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
		Tile.Definition def = new Tile.Definition();
		def.name = name;
		def.walkable = walkable;
		def.image = path;
		Deque<Tile.Definition> d = definitions.get(name);
		if (d == null) {
			d = new ArrayDeque<Tile.Definition>();
			definitions.put(id, d);
		}
		d.push(def);
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
		Deque<Tile.Definition> tdDeque = definitions.get(id);
		if (tdDeque == null) {
			throw new InvalidIndexException(id);
		}
		Tile.Definition def = tdDeque.peekFirst();
		Tile tile = new Tile(def.name, def.walkable, def.image);
		tile.setId(id);
		return tile;
	}
	
	@Override
	public void merge(Map<Integer, Tile.Definition> content) {
		for (int id : content.keySet()) {
			Tile.Definition td = content.get(id);
			addDefinition(id, td.name, td.walkable, td.image);
		}
	}
	
	@Override
	public void subtract(Map<Integer, Tile.Definition> content) {
		for (int id : content.keySet()) {
			Tile.Definition td = content.get(id);
			Deque<Tile.Definition> d = definitions.get(id);
			if (d != null) {
				d.remove(td);
				if (d.isEmpty()) {
					definitions.remove(id);
				}
			}
		}
	}
	
}
