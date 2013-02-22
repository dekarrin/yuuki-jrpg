package yuuki.file;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

import yuuki.world.TileFactory;

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
	 * 
	 * @return A TileFactory with the definitions from the file.
	 * 
	 * @throws ResourceNotFoundException If the resource does not exist.
	 * @throws IOException If an IOException occurs.
	 */
	public TileFactory load(String resource) throws ResourceNotFoundException,
	IOException {
		TileFactory factory = null;
		String[][] records = loadRecords(resource);
		factory = new TileFactory();
		for (String[] r : records) {
			int id = r[0].charAt(0);
			String name = r[1];
			boolean walkable = r[2].equals("1");
			String path = r[3];
			factory.addDefinition(id, name, walkable, path);
			advanceProgress(1.0 / records.length);
		}
		return factory;
	}
	
}
