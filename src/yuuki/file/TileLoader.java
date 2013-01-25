package yuuki.file;

import java.io.IOException;

import yuuki.world.TileFactory;

/**
 * Loads Tile definitions from a tile definitions file.
 */
public class TileLoader extends CsvResourceLoader {
	
	/**
	 * Creates a new TileLoader for land files at the specified location.
	 * 
	 * @param location The path to the directory containing the land files to
	 * be loaded, relative to the package structure.
	 */
	public TileLoader(String location) {
		super(location);
	}
	
	/**
	 * Loads the Tile from a file.
	 * 
	 * @param resource The location of the file to load, relative to the
	 * resource root.
	 * 
	 * @return A TileFactory with the definitions from the file, or null if the
	 * definitions file does not exist.
	 * 
	 * @throws IOException If an IOException occurs.
	 */
	public TileFactory loadTiles(String resource) throws IOException {
		TileFactory factory = null;
		String[][] records = loadRecords(resource);
		factory = new TileFactory();
		for (String[] r : records) {
			int id = r[0].charAt(0);
			String name = r[1];
			boolean walkable = r[2].equals("1");
			factory.addDefinition(id, name, walkable);
		}
		return factory;
	}
	
}
