package yuuki.world;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains the definitions for creating tiles.
 */
public class TileFactory {
	
	/**
	 * The definition of a Tile.
	 */
	private static class TileDefinition {
		
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
	 * The definitions in this TileFactory.
	 */
	private Map<Integer, TileDefinition> definitions;
	
	/**
	 * Creates a new TileFactory.
	 */
	public TileFactory() {
		definitions = new HashMap<Integer, TileDefinition>();
	}
	
	/**
	 * Adds a Tile definition.
	 * 
	 * @param id The ID of the tile to add.
	 * @param name The name of the tile definition.
	 * @param walkable Whether the tile is walkable.
	 */
	public void addDefinition(int id, String name, boolean walkable) {
		TileDefinition td = new TileDefinition();
		td.name = name;
		td.walkable = walkable;
		definitions.put(id, td);
	}
	
	/**
	 * Creates a Tile from an ID.
	 * 
	 * @param id The ID of the Tile to create. This is the integer version of
	 * the character representing it.
	 * 
	 * @return The created Tile.
	 */
	public Tile createTile(int id) {
		TileDefinition td = definitions.get(id);
		Tile tile = new Tile(td.name, td.walkable);
		tile.setId(id);
		return tile;
	}
	
}
