package yuuki.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipFile;

/**
 * Loads CSV data files containing tables of definitions.
 */
public class CsvResourceLoader extends ResourceLoader {
	
	/**
	 * The names of the columns. This must be set by using setColumnNames() if
	 * columns are to be later accessed by name.
	 */
	private String[] columns = null;
	
	/**
	 * Parses data from the resource files.
	 */
	private CsvParser parser;
	
	/**
	 * The record that fields are currently being accessed for.
	 */
	private int recordIndex = -1;
	
	/**
	 * The data read from the last resource.
	 */
	private List<Map<String, String>> records = null;
	
	/**
	 * Creates a new CsvResourceLoader for resources at the specified location.
	 * 
	 * @param directory The directory containing the resource files to be
	 * loaded.
	 */
	public CsvResourceLoader(File directory) {
		super(directory);
	}
	
	/**
	 * Creates a new CsvResourceLoader for resources in the given ZIP file.
	 * 
	 * @param archive The ZIP file containing the resource files to be loaded.
	 * @param zipRoot The root within the ZIP file of all files to be loaded.
	 */
	public CsvResourceLoader(ZipFile archive, String zipRoot) {
		super(archive, zipRoot);
	}
	
	/**
	 * Gets a field from the current record as a String.
	 * 
	 * @param field The name of the field.
	 * @return The exact contents of the field, or null if the current record
	 * does not have a value for that field.
	 */
	public String getField(String field) {
		Map<String, String> record = getCurrentRecord();
		return record.get(field);
	}
	
	/**
	 * Gets the number of records in the loaded resource file.
	 * 
	 * @return The number of records in the loaded resource file, or 0 if no
	 * resource file has been read by a call to readRecords().
	 */
	public int getRecordCount() {
		return (records != null) ? records.size() : 0;
	}
	
	/**
	 * Loads a CSV resource file's complete contents into memory.
	 * 
	 * @param resource The path to the resource to load, relative to the
	 * resource root.
	 * 
	 * @return An array of records retrieved from the CSV file.
	 * 
	 * @throws ResourceNotFoundException If the given resource doesn't exist.
	 * @throws IOException If an IOException occurs.
	 */
	public String[][] loadRecords(String resource) throws
	ResourceNotFoundException, IOException {
		String[][] records = null;
		InputStream stream = getStream(resource);
		if (stream != null) {
			parser = CsvParser.defaultParser(stream);
			records = parser.read();
		}
		return records;
	}
	
	/**
	 * Changes the target record of getField() methods.
	 * 
	 * @param record The index of the record to set as the current record.
	 */
	public void setRecord(int record) {
		recordIndex = record;
	}
	
	/**
	 * Gets the record pointed to by the current record index.
	 * 
	 * @return The current record.
	 * @throws IllegalStateException If the current record index does not point
	 * to a valid record.
	 */
	private Map<String, String> getCurrentRecord() {
		if (recordIndex == -1) {
			throw new IllegalStateException();
		}
		Map<String, String> r = records.get(recordIndex);
		if (r == null) {
			throw new IllegalStateException();
		}
		return r;
	}
	
	/**
	 * Converts raw record fields into named fields for later retrieval through
	 * the getField() methods. No more than the number of fields named by
	 * columns will be process from each record, but each record may have
	 * fewer columns; if this is the case, the missing columns are set to null.
	 * 
	 * @param rawRecords The raw record fields.
	 */
	private void processRecords(String[][] rawRecords) {
		setRecord(0);
		records = new ArrayList<Map<String, String>>(rawRecords.length);
		Map<String, String> fieldMap;
		for (String[] r : rawRecords) {
			fieldMap = new HashMap<String, String>();
			for (int i = 0; i < columns.length; i++) {
				String fieldValue = (i < r.length) ? r[i] : null;
				fieldMap.put(columns[i], fieldValue);
			}
			records.add(fieldMap);
		}
	}
	
	/**
	 * Reads records into memory. This method may only be called if the column
	 * names have been set by using setColumnNames().
	 * 
	 * @param resource The path to the resource to load, relative to the
	 * resource root.
	 * @throws ResourceNotFoundException If the given resource doesn't exist.
	 * @throws IOException If an IOException occurs.
	 */
	protected void readRecords(String resource) throws
	ResourceNotFoundException, IOException {
		String[][] records = loadRecords(resource);
		processRecords(records);
	}
	
	/**
	 * Sets the names of columns for later access.
	 * 
	 * @param names The names of the columns.
	 */
	protected void setColumnNames(String[] names) {
		this.columns = names;
	}
	
}
