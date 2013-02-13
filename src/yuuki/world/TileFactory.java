package yuuki.world;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import yuuki.file.ByteArrayLoader;

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
		
		/**
		 * The image data for the tile.
		 */
		public Image image;
		
	}
	
	/**
	 * The character that represents a void tile.
	 */
	public static final char VOID_CHAR = ' ';
	
	/**
	 * The image that represents a void tile.
	 */
	public static final String VOID_PATH = "tiles/void.png";
	
	/**
	 * Loads image data.
	 */
	private ByteArrayLoader imageLoader;
	
	/**
	 * The definitions in this TileFactory.
	 */
	private Map<Integer, TileDefinition> definitions;
	
	/**
	 * Creates a new TileFactory.
	 * 
	 * @throws IOException If an IOException occurs while reading the void tile
	 * data.
	 */
	public TileFactory() throws IOException {
		definitions = new HashMap<Integer, TileDefinition>();
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
			path) throws IOException {
		TileDefinition def = new TileDefinition();
		def.name = name;
		def.walkable = walkable;
		def.image = (new ImageIcon(imageLoader.load(path))).getImage();
		definitions.put(id, def);
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
		Tile tile = new Tile(td.name, td.walkable, td.image);
		tile.setId(id);
		return tile;
	}
	
}
