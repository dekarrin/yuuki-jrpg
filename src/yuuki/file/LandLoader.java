package yuuki.file;

import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import yuuki.entity.NonPlayerCharacter;
import yuuki.util.InvalidIndexException;
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
	 * The number of fields in a line of entity data.
	 */
	private static final int ENTITY_FIELD_COUNT = 3;
	
	/**
	 * The number of fields in the header line.
	 */
	private static final int META_FIELD_COUNT = 5;
	
	/**
	 * The number of fields in a portal line.
	 */
	private static final int PORTAL_FIELD_COUNT = 4;
	
	/**
	 * The number of the line currently being read.
	 */
	private int currentLine;
	
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
	 * The name of the resource currently being loaded.
	 */
	private String resourceName;
	
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
	 * @throws ResourceFormatException If there is a problem with the format of
	 * the given file.
	 * @throws IOException If an IOException occurs.
	 */
	public Land load(String resource) throws ResourceNotFoundException,
	ResourceFormatException, IOException {
		resourceName = resource;
		meta = null;
		mode = ParserMode.METADATA;
		portals = new ArrayList<Portal>();
		entities = new ArrayList<Movable>();
		Land land = null;
		InputStream stream = getStream(resource);
		reader = new BufferedReader(new InputStreamReader(stream));
		land = loadLand();
		for (Portal p : portals) {
			if (p != null) {
				land.addPortal(p);
			}
		}
		for (Movable m : entities) {
			if (m != null) {
				land.addResident(m);
			}
		}
		resourceName = null;
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
	 * @throws ResourceFormatException If the land file is invalid.
	 * @throws IOException If an IOException occurs.
	 */
	@SuppressWarnings("fallthrough")
	private Land loadLand() throws IOException, ResourceFormatException {
		ArrayList<Tile> tileData = new ArrayList<Tile>();
		String line = null;
		int heightCount = 0;
		currentLine = 0;
		while ((line = reader.readLine()) != null) {
			currentLine++;
			switch (mode) {
				case METADATA:
					try {
						readMetaData(line);
					} catch (RecordFormatException e) {
						throw new ResourceFormatException(resourceName, e);
					}
					mode = ParserMode.PORTALS;
					break;
					
				case PORTALS:
					if (portals.size() < meta.portals) {
						try {
							readPortalData(line);
						} catch (RecordFormatException e) {
							System.err.println(e.getMessage() + " - skipping");
							portals.add(null);
						}
						break;
					} else {
						mode = ParserMode.ENTITIES;
					}
					
				case ENTITIES:
					if (entities.size() < meta.entities) {
						try {
							readEntityData(line);
						} catch (RecordFormatException e) {
							System.err.println(e.getMessage() + " - skipping");
							entities.add(null);
						}
						break;
					} else {
						mode = ParserMode.MAP;
					}
					
				case MAP:
					tileData.addAll(readMapData(line));
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
	 * Reads a line containing entity data.
	 * 
	 * @param line The line with the entity data.
	 * 
	 * @throws RecordFormatException If one of the fields of the entity data is
	 * invalid or if some of the parts are missing.
	 */
	private void readEntityData(String line) throws RecordFormatException {
		String[] parts = line.split(";");
		if (parts.length < ENTITY_FIELD_COUNT) {
			throw new RecordFormatException(currentLine, "missing fields");
		}
		try {
			Point location = parsePointField("location", parts[0]);
			String name = parts[1];
			int level = parseIntField("level", parts[2]);
			NonPlayerCharacter npc = populator.createNpc(name, level);
			npc.setLocation(location);
			entities.add(npc);
		} catch (Exception e) {
			throw new RecordFormatException(currentLine, e);
		}
	}
	
	/**
	 * Parses a line of land data into an array of Tile objects.
	 * 
	 * @param line The line of land data.
	 * 
	 * @return The array of Tile objects parsed from the line.
	 */
	private ArrayList<Tile> readMapData(String line) {
		ArrayList<Tile> tileList = new ArrayList<Tile>();
		int limit = Math.min(line.length(), meta.size.width);
		for (int i = 0; i < limit; i++) {
			char c = line.charAt(i);
			Tile t;
			try {
				t = populator.createTile(c);
			} catch (InvalidIndexException e) {
				String raw = "bad tile '%s' on line %i of '%s' - using void";
				String msg = String.format(raw, e.getIndex(), currentLine,
						resourceName);
				System.err.println(msg);
				t = populator.createVoidTile();
			}
			tileList.add(t);
		}
		// for the remaining width, add void tiles.
		fillRemainingWidth(tileList, limit);
		return tileList;
	}
	
	/**
	 * Parses a line of meta data into the actual meta data.
	 * 
	 * @param line The line to parse.
	 * 
	 * @throws RecordFormatException If any of the fields are invalid, or if
	 * there are too few fields.
	 */
	private void readMetaData(String line) throws RecordFormatException {
		meta = new MetaData();
		String[] parts = line.split(";");
		if (parts.length < META_FIELD_COUNT) {
			throw new RecordFormatException(currentLine, "Missing parameters");
		}
		try {
			for (String attribute : parts) {
				String key = attribute.split("=")[0];
				String value = attribute.split("=")[1];
				if (key.equalsIgnoreCase("start")) {
					meta.start = parsePointField("start", value);
				} else if (key.equalsIgnoreCase("size")) {
					meta.size = parseDimensionField("size", value);
				} else if (key.equalsIgnoreCase("portals")) {
					meta.portals = parseIntField("portals", value);
				} else if (key.equalsIgnoreCase("name")) {
					meta.name = value;
				} else if (key.equalsIgnoreCase("entities")) {
					meta.entities = parseIntField("entities", value);
				}
			}
		} catch (FieldFormatException e) {
			throw new RecordFormatException(currentLine, e);
		}
	}
	
	/**
	 * Reads a line containing portal data.
	 * 
	 * @param line The line with the portal data.
	 * @throws RecordFormatException If any field contains invalid data, or if
	 * there are too few parameters.
	 */
	private void readPortalData(String line) throws RecordFormatException {
		String[] parts = line.split(";");
		if (parts.length < PORTAL_FIELD_COUNT) {
			throw new RecordFormatException(currentLine, "missing parameters");
		}
		try {
			Point location = parsePointField("location", parts[0]);
			String name = parts[1];
			Point link = parsePointField("link", parts[2]);
			String land = parts[3];
			Portal p = populator.createPortal(name, land, link);
			p.setLocation(location);
			portals.add(p);
		} catch (Exception e) {
			throw new RecordFormatException(currentLine, e);
		}
	}
	
}
