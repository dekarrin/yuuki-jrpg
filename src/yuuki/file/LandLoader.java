package yuuki.file;

import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import yuuki.entity.NonPlayerCharacter;
import yuuki.world.Land;
import yuuki.world.Movable;
import yuuki.world.PopulationFactory;
import yuuki.world.Portal;
import yuuki.world.Tile;

/**
 * Loads a Land resource file.
 */
public class LandLoader extends ResourceLoader {
	
	/**
	 * Holds meta data for the currently loading land file.
	 */
	private static class MetaData {
		
		/**
		 * The number of entities in the map.
		 */
		public int entities;
		
		/**
		 * The name of the map.
		 */
		public String name;
		
		/**
		 * The number of portals in the map.
		 */
		public int portals;
		
		/**
		 * The size of the map.
		 */
		public Dimension size;
		
		/**
		 * Where the player starts.
		 */
		public Point start;
		
	};
	
	/**
	 * The sections of the file that need to be parsed in different ways.
	 */
	private enum ParserMode {
		ENTITIES,
		MAP,
		METADATA,
		PORTALS
	}
	
	/**
	 * The loaded entities.
	 */
	private ArrayList<Movable> entities;
	
	/**
	 * The meta data from the land file currently being read.
	 */
	private MetaData meta;
	
	/**
	 * The section of the land file that is being parsed.
	 */
	private ParserMode mode;
	
	/**
	 * Generates Land population members.
	 */
	private final PopulationFactory populator;
	
	/**
	 * The loaded portals.
	 */
	private ArrayList<Portal> portals;
	
	/**
	 * Reads from the resource file.
	 */
	private BufferedReader reader;
	
	/**
	 * Creates a new LandLoader for land files at the specified location.
	 * 
	 * @param location The path to the directory containing the land files to
	 * be loaded, relative to the package structure.
	 * @param populator The factory to use for populating lands.
	 */
	public LandLoader(String location, PopulationFactory populator) {
		super(location);
		this.populator = populator;
	}
	
	/**
	 * Loads the data from a land resource file into a Land object.
	 * 
	 * @param resource The path to the land file to load, relative to the
	 * resource root.
	 * 
	 * @return The Land object.
	 * 
	 * @throws ResourceNotFoundException If the resource does not exist.
	 * @throws IOException If an IOException occurs.
	 */
	public Land load(String resource) throws ResourceNotFoundException,
	IOException {
		meta = null;
		mode = ParserMode.METADATA;
		portals = new ArrayList<Portal>();
		entities = new ArrayList<Movable>();
		Land land = null;
		InputStream stream = getStream(resource);
		reader = new BufferedReader(new InputStreamReader(stream));
		land = loadLand();
		for (Portal p : portals) {
			land.addPortal(p);
		}
		for (Movable m : entities) {
			land.addResident(m);
		}
		return land;
	}
	
	/**
	 * Fills the given lists of tiles with rows of void tiles until the land
	 * height is reached.
	 * 
	 * @param list The list of tiles to fill.
	 * @param start The number of rows of tiles already added to the list.
	 */
	private void fillRemainingHeight(ArrayList<Tile> list, int start) {
		for (int i = start; i < meta.size.height; i++) {
			fillRemainingWidth(list, 0);
		}
	}
	
	/**
	 * Fills the given list of tiles with void tiles until the land width is
	 * reached.
	 * 
	 * @param list The list of tiles to fill.
	 * @param start The number of tiles already added to the list.
	 */
	private void fillRemainingWidth(ArrayList<Tile> list, int start) {
		for (int i = start; i < meta.size.width; i++) {
			list.add(populator.createVoidTile());
		}
	}
	
	/**
	 * Gets the number of lines that this land loader will need to read.
	 * 
	 * @return The number of lines.
	 */
	private int getLineCount() {
		int count = 1 + meta.portals + meta.entities + meta.size.height;
		return count;
	}
	
	/**
	 * Loads land data from the reader.
	 * 
	 * @return The Land object.
	 * 
	 * @throws IOException If an IOException occurs.
	 */
	@SuppressWarnings("fallthrough")
	private Land loadLand() throws IOException {
		ArrayList<Tile> tileData = new ArrayList<Tile>();
		String line = null;
		int heightCount = 0;
		while ((line = reader.readLine()) != null) {
			switch (mode) {
				case METADATA:
					readMetaData(line);
					mode = ParserMode.PORTALS;
					break;
					
				case PORTALS:
					if (portals.size() < meta.portals) {
						readPortalData(line);
						break;
					} else {
						mode = ParserMode.ENTITIES;
					}
					
				case ENTITIES:
					if (entities.size() < meta.entities) {
						readEntityData(line);
						break;
					} else {
						mode = ParserMode.MAP;
					}
					
				case MAP:
					tileData.addAll(parseLine(line));
					heightCount++;
					break;
			}
			advanceProgress(1.0 / getLineCount());
			if (heightCount == meta.size.height) {
				break;
			}
		}
		fillRemainingHeight(tileData, heightCount);
		Tile[] tiles = new Tile[tileData.size()];
		tileData.toArray(tiles);
		Land land = new Land(meta.name, meta.size, meta.start, tiles);
		return land;
	}
	
	/**
	 * Parses a Dimension value from a String value of the form "w,h", where w
	 * is the width and h is the height.
	 * 
	 * @param value The String to parse.
	 * 
	 * @return The Dimension represented by the given String.
	 */
	private Dimension parseDimension(String value) {
		String[] parts = value.split(",");
		Dimension d = new Dimension();
		d.width = parseInt(parts[0]);
		d.height = parseInt(parts[1]);
		return d;
	}
	
	/**
	 * Parses an integer value from a String value.
	 * 
	 * @param value The String to parse.
	 * 
	 * @return The integer represented by the given String.
	 */
	private int parseInt(String value) {
		return Integer.parseInt(value);
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
		int limit = Math.min(line.length(), meta.size.width);
		for (int i = 0; i < limit; i++) {
			char c = line.charAt(i);
			tileList.add(populator.createTile(c));
		}
		// for the remaining width, add void tiles.
		fillRemainingWidth(tileList, limit);
		return tileList;
	}
	
	/**
	 * Parses a Point value from a String value of the form "x,y", where x is
	 * the x-coordinate and y is the y-coordinate.
	 * 
	 * @param value The String to parse.
	 * 
	 * @return The Point represented by the given String.
	 */
	private Point parsePoint(String value) {
		String[] parts = value.split(",");
		Point p = new Point();
		p.x = parseInt(parts[0]);
		p.y = parseInt(parts[1]);
		return p;
	}
	
	/**
	 * Reads a line containing entity data.
	 * 
	 * @param line The line with the entity data.
	 */
	private void readEntityData(String line) {
		String[] parts = line.split(";");
		Point location = parsePoint(parts[0]);
		String name = parts[1];
		int level = parseInt(parts[2]);
		NonPlayerCharacter npc;
		npc = populator.createNpc(name, level);
		npc.setLocation(location);
		entities.add(npc);
	}
	
	/**
	 * Parses a line of meta data into the actual meta data.
	 * 
	 * @param line The line to parse.
	 */
	private void readMetaData(String line) {
		meta = new MetaData();
		String[] parts = line.split(";");
		for (String attribute : parts) {
			String key = attribute.split("=")[0];
			String value = attribute.split("=")[1];
			if (key.equalsIgnoreCase("start")) {
				meta.start = parsePoint(value);
			} else if (key.equalsIgnoreCase("size")) {
				meta.size = parseDimension(value);
			} else if (key.equalsIgnoreCase("portals")) {
				meta.portals = parseInt(value);
			} else if (key.equalsIgnoreCase("name")) {
				meta.name = value;
			} else if (key.equalsIgnoreCase("entities")) {
				meta.entities = parseInt(value);
			}
		}
	}
	
	/**
	 * Reads a line containing portal data.
	 * 
	 * @param line The line with the portal data.
	 */
	private void readPortalData(String line) {
		String[] parts = line.split(";");
		Point location = parsePoint(parts[0]);
		String name = parts[1];
		Point link = parsePoint(parts[2]);
		String land = parts[3];
		Portal p = populator.createPortal(name, land, link);
		p.setLocation(location);
		portals.add(p);
	}
	
}
