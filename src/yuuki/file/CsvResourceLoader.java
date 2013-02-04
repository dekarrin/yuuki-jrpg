package yuuki.file;

import java.io.IOException;
import java.io.InputStream;

/**
 * Loads CSV data files containing tables of definitions.
 */
public class CsvResourceLoader extends ResourceLoader {
	
	/**
	 * Parses data from the resource files.
	 */
	private CsvParser parser;
	
	/**
	 * Creates a new CsvResourceLoader for resources at the specified location.
	 * 
	 * @param location The path to the directory containing the resource files
	 * to be loaded, relative to the package structure.
	 */
	public CsvResourceLoader(String location) {
		super(location);
	}
	
	/**
	 * Loads a CSV resource file's complete contents into memory.
	 * 
	 * @param resource The path to the resource to load, relative to the
	 * resource root.
	 * 
	 * @return An array of records retrieved from the CSV file, or null if the
	 * CSV file does not exist.
	 * 
	 * @throws IOException If an IOException occurs.
	 */
	public String[][] loadRecords(String resource) throws IOException {
		String[][] records = null;
		InputStream stream = getStream(resource);
		if (stream != null) {
			parser = CsvParser.defaultParser(stream);
			records = parser.read();
		}
		return records;
	}
	
}
