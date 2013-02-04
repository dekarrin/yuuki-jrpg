package yuuki.file;

import java.io.IOException;
import java.util.ArrayList;

import yuuki.world.Land;
import yuuki.world.TileFactory;
import yuuki.world.World;

/**
 * Loads the World and all of its associated land files.
 */
public class WorldLoader extends CsvResourceLoader {
	
	/**
	 * The TileFactory for creating the Land data.
	 */
	private final LandLoader landLoader;
	
	/**
	 * Creates a new WorldLoader for world files at the specified location.
	 * 
	 * @param location The path to the directory containing the world files to
	 * be loaded, relative to the package structure.
	 * @param landsPath The path to the directory containing the land files to
	 * be loaded, relative to the package structure.
	 * @param fact The TileFactory to use for populating the Land objects.
	 */
	public WorldLoader(String location, String landsPath, TileFactory fact) {
		super(location);
		landLoader = new LandLoader(landsPath, fact);
	}
	
	/**
	 * Loads the data from a world resource file into a World object.
	 * 
	 * @param resource The path to the world file to load, relative to the
	 * resource root.
	 * 
	 * @return The World object if the resource file exists; otherwise, null.
	 * 
	 * @throws IOException If an IOException occurs.
	 */
	public World load(String resource) throws IOException {
		World world = new World();
		String[][] records = loadRecords(resource);
		ArrayList<String> paths = new ArrayList<String>();
		for (String[] r : records) {
			paths.add(r[0]);
		}
		String[] pathsArr = paths.toArray(new String[0]);
		loadLands(world, pathsArr);
		return world;
	}
	
	/**
	 * Loads the land data into a World.
	 * 
	 * @param world The world to load the lands into.
	 * @param paths The paths to the land data files.
	 * @throws IOException
	 */
	private void loadLands(World world, String[] paths)
			throws IOException {
		for (int i = 0; i < paths.length; i++) {
			Land land = landLoader.load(paths[i]);
			if (land != null) {
				world.addLand(land);
			}
		}
	}
	
}
