package yuuki.file;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

import yuuki.world.PortalFactory;

/**
 * Loads portal definitions.
 */
public class PortalLoader extends CsvResourceLoader {
	
	/**
	 * Creates a new PortalLoader for portal files at the specified location.
	 * 
	 * @param directory The directory containing the portal files to be loaded.
	 */
	public PortalLoader(File directory) {
		super(directory);
	}
	
	/**
	 * Creates a new PortalLoader for resource files in the given ZIP file.
	 *
	 * @param archive The ZIP file containing the resource files to be loaded.
	 * @param zipRoot The root within the ZIP file of resource files to be
	 * loaded.
	 */
	public PortalLoader(ZipFile archive, String zipRoot) {
		super(archive, zipRoot);
	}
	
	/**
	 * Loads the data from a portal definitions file into a PortalFactory
	 * object.
	 * 
	 * @param resource The path to the resource file to load, relative to the
	 * resource root.
	 * 
	 * @return The PortalFactory object.
	 * 
	 * @throws ResourceNotFoundException If the resource does not exist.
	 * @throws IOException If an IOException occurs.
	 */
	public PortalFactory load(String resource) throws
	ResourceNotFoundException, IOException {
		PortalFactory factory = new PortalFactory();
		String[][] records = loadRecords(resource);
		for (String[] r : records) {
			String name = r[0];
			String imageIndex = r[1];
			factory.addDefinition(name, imageIndex);
			advanceProgress(1.0 / records.length);
		}
		return factory;
	}
	
}
