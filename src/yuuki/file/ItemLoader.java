package yuuki.file;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;
import yuuki.item.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * Loads Item resource files.
 */
class ItemLoader extends CsvResourceLoader {

	/**
	 * Creates a new ItemLoader for item files in the given directory.
	 * 
	 * @param directory The directory to create the loader for.
	 */
	public ItemLoader(File directory) {
		super(directory);
	}
	
	/**
	 * Creates a new ItemLoader for resource files in the given ZIP file.
	 *
	 * @param archive The ZIP file containing the resource files to be loaded.
	 * @param zipRoot The root within the ZIP file of all files to be loaded.
	 */
	public ItemLoader(ZipFile archive, String zipRoot) {
		super(archive, zipRoot);
	}
	
	/**
	 * Loads item data for a file into a map.
	 * 
	 * @param resource The location of the file to load.
	 * @return The item data.
	 * @throws IOException 
	 * @throws ResourceNotFoundException 
	 * @throws ResourceFormatException 
	 */
	public Map<Long, Item.Definition> load(String resource) throws
	IOException, ResourceNotFoundException, ResourceFormatException {
		Map<Long, Item.Definition> items;
		items = new HashMap<Long, Item.Definition>();
		String[][] records = loadRecords(resource);
		for (int i = 0; i < records.length; i++) {
			try {
				Item.Definition def = parseRecord(records, i);
				items.put(def.id, def);
			} catch (RecordFormatException e) {
				throw new ResourceFormatException(resource, e);
			}
			advanceProgress(1.0 / records.length);
		}
		return items;
	}
	
	/**
	 * Parses a record into an item definition.
	 * 
	 * @param records The records to parse from.
	 * @param num The index of the record to parse.
	 * @return The parsed item definition.
	 * @throws RecordFormatException If the given record is invalid.
	 */
	private Item.Definition parseRecord(String[][] records, int num) throws
	RecordFormatException {
		String[] r = records[num];
		Item.Definition d = new Item.Definition();
		try {
			d.id = parseLongField("ID", r[0]);
			d.name = r[1];
			d.value = parseIntField("Value", r[2]);
			d.image = r[3];
			d.usable = parseBooleanField(r[4]);
			d.external = parseBooleanField(r[5]);
			d.action = parseIntField("Action", r[6]);
		} catch (FieldFormatException e) {
			throw new RecordFormatException(num, e);
		}
		return d;
	}
	
	
}