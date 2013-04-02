package yuuki.file;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipFile;

import yuuki.item.Item;

/**
 * Loads Item resource files.
 */
public class ItemLoader extends CsvResourceLoader {
	
	/**
	 * The names of columns in an item resource file.
	 */
	private static final String[] COLUMNS =
		{"id", "name", "value", "image", "usable", "external", "action"};
	
	/**
	 * Creates a new ItemLoader for item files in the given directory.
	 * 
	 * @param directory The directory to create the loader for.
	 */
	public ItemLoader(File directory) {
		super(directory);
		setColumnNames(COLUMNS);
	}
	
	/**
	 * Creates a new ItemLoader for resource files in the given ZIP file.
	 *
	 * @param archive The ZIP file containing the resource files to be loaded.
	 * @param zipRoot The root within the ZIP file of all files to be loaded.
	 */
	public ItemLoader(ZipFile archive, String zipRoot) {
		super(archive, zipRoot);
		setColumnNames(COLUMNS);
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
		readRecords(resource);
		for (int i = 0; i < getRecordCount(); i++) {
			try {
				Item.Definition def = parseRecord(i);
				items.put(def.id, def);
			} catch (RecordFormatException e) {
				throw new ResourceFormatException(resource, e);
			}
			advanceProgress(1.0 / getRecordCount());
		}
		return items;
	}
	
	/**
	 * Parses a record into an item definition.
	 * 
	 * @param num The index of the record to parse.
	 * @return The parsed item definition.
	 * @throws RecordFormatException If the given record is invalid.
	 */
	private Item.Definition parseRecord(int num) throws	RecordFormatException {
		setRecord(num);
		Item.Definition d = new Item.Definition();
		try {
			d.id = getLongField("id");
			d.name = getField("name");
			d.value = getIntField("value");
			d.image = getField("image");
			d.usable = getBooleanField("usable");
			d.external = getBooleanField("external");
			d.action = getIntField("action");
		} catch (FieldFormatException e) {
			throw new RecordFormatException(num, e);
		}
		return d;
	}
	
	
}