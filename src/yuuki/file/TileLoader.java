package yuuki.file;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipFile;

import yuuki.world.Tile;

/**
 * Loads Tile definitions from a tile definitions file.
 */
public class TileLoader extends CsvResourceLoader {
	
	/**
	 * Creates a new TileLoader for tile files at the specified location.
	 * 
	 * @param directory The directory containing the tile files to be loaded.
	 */
	public TileLoader(File directory) {
		super(directory);
	}
	
	/**
	 * Creates a new TileLoader for resource files in the given ZIP file.
	 *
	 * @param archive The ZIP file containing the resource files to be loaded.
	 * @param zipRoot The root within the ZIP file of resource files to be
	 * loaded.
	 */
	public TileLoader(ZipFile archive, String zipRoot) {
		super(archive, zipRoot);
	}
	
	/**
	 * Loads the tile definitions from a file.
	 * 
	 * @param resource The location of the file to load, relative to the
	 * resource root.
	 * @return A map containing tile IDs mapped to the definitions found in the
	 * file.
	 * @throws ResourceNotFoundException If the resource does not exist.
	 * @throws IOException If an IOException occurs.
	 */
	public Map<Integer, Tile.Definition> load(String resource) throws
	ResourceNotFoundException, IOException {
		Map<Integer, Tile.Definition> tiles;
		tiles = new HashMap<Integer, Tile.Definition>();
		String[][] records = loadRecords(resource);
		for (String[] r : records) {
			int id = r[0].charAt(0);
			Tile.Definition td = new Tile.Definition();
			td.name = r[1];
			td.walkable = r[2].equals("1");
			td.image = r[3];
			tiles.put(id, td);
			advanceProgress(1.0 / records.length);
		}
		return tiles;
	}
	
}
