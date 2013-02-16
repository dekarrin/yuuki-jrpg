package yuuki.file;

import java.io.IOException;
import java.util.ArrayList;

import yuuki.world.Land;
import yuuki.world.PopulationFactory;
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
	 * @param populator The factory to use for populating lands.
	 */
	public WorldLoader(String location, String landsPath, PopulationFactory
			populator) {
		super(location);
		landLoader = new LandLoader(landsPath, populator);
	}
	
	/**
	 * Loads the data from a world resource file into a World object.
	 * 
	 * @param resource The path to the world file to load, relative to the
	 * resource root.
	 * 
	 * @return The World object if the resource file exists; otherwise, null.
	 * 
	 * @throws ResourceNotFoundException If the resource file or a file
	 * referenced by the resource file does not exist.
	 * @throws IOException If an IOException occurs.
	 */
	public World load(String resource) throws ResourceNotFoundException,
	IOException {
		World world = new World();
		String[][] records = loadRecords(resource);
		ArrayList<String> paths = new ArrayList<String>();
		double percent = 1.0 / (records.length + 1);
		for (String[] r : records) {
			paths.add(r[0]);
			advanceProgress(1.0 / records.length * percent);
		}
		String[] pathsArr = paths.toArray(new String[0]);
		loadLands(world, pathsArr, percent);
		return world;
	}
	
	/**
	 * Loads the land data into a World.
	 * 
	 * @param world The world to load the lands into.
	 * @param paths The paths to the land data files.
	 * @param percent The percent that loading each part should take up.
	 * 
	 * @throws ResourceNotFoundException If one of the given paths does not
	 * exist.
	 * @throws IOException If an IOException occurs.
	 */
	private void loadLands(World world, String[] paths, double percent) throws
	ResourceNotFoundException, IOException {
		for (String path : paths) {
			Land land = null;
			try {
				land = landLoader.load(path);
			} catch (ResourceFormatException e) {
				System.err.println(e.getMessage() + " - skipping");
			}
			if (land != null) {
				world.addLand(land);
			}
			advanceProgress(percent);
		}
	}
	
}
