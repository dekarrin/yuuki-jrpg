package yuuki.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import yuuki.world.Land;
import yuuki.world.Tile;
import yuuki.world.TileFactory;

/**
 * Loads a Land resource file.
 */
public class LandLoader extends ResourceLoader {
	
	/**
	 * Generates tiles from tile definitions.
	 */
	private final TileFactory factory;
	
	/**
	 * The height of the land file currently being parsed.
	 */
	private int landHeight;
	
	/**
	 * The name of the land currently being created.
	 */
	private String landName;
	
	/**
	 * The width of the land file currently being parsed.
	 */
	private int landWidth;
	
	/**
	 * Reads from the resource file.
	 */
	private BufferedReader reader;
	
	/**
	 * Creates a new LandLoader for land files at the specified location.
	 * 
	 * @param location The path to the directory containing the land files to
	 * be loaded, relative to the package structure.
	 * @param tiles Contains the tile definitions.
	 */
	public LandLoader(String location, TileFactory tiles) {
		super(location);
		this.factory = tiles;
		landHeight = 0;
		landWidth = 0;
		landName = null;
	}
	
	/**
	 * Loads the data from a land resource file into a Land object.
	 * 
	 * @param name The name of the Land being created.
	 * @param resource The path to the land file to load, relative to the
	 * resource root.
	 * 
	 * @return The Land object if the resource file exists; otherwise, null.
	 * 
	 * @throws IOException If an IOException occurs.
	 */
	public Land load(String name, String resource) throws IOException {
		landName = name;
		Land land = null;
		InputStream stream = getStream(resource);
		reader = new BufferedReader(new InputStreamReader(stream));
		land = loadLand();
		return land;
	}
	
	/**
	 * Loads land data from the reader.
	 * 
	 * @return The Land object.
	 * 
	 * @throws IOException If an IOException occurs.
	 */
	private Land loadLand() throws IOException {
		ArrayList<Tile> tileData = new ArrayList<Tile>();
		String line = null;
		while ((line = reader.readLine()) != null) {
			tileData.addAll(parseLine(line));
		}
		Tile[] tiles = new Tile[tileData.size()];
		tileData.toArray(tiles);
		Land land = new Land(landName, landWidth, landHeight, tiles);
		return land;
	}
	
	/**
	 * Parses a line of land data into an array of Tile objects.
	 * 
	 * @param line The line of land data.
	 * 
	 * @return The array of Tile objects parsed from the line.
	 */
	private ArrayList<Tile> parseLine(String line) {
		ArrayList<Tile> tileList = new ArrayList<Tile>();
		if (line.length() > 0) {
			landHeight++;
			if (landWidth == 0) {
				landWidth = line.length();
			}
			for (int i = 0; i < line.length(); i++) {
				char c = line.charAt(i);
				tileList.add(factory.createTile(c));
			}
		}
		return tileList;
	}
	
}
