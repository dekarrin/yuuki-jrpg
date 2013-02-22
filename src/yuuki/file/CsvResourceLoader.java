package yuuki.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

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
	
}
