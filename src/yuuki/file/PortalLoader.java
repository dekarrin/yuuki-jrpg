package yuuki.file;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipFile;

import yuuki.world.Portal;

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
	 * @return A map of portal names mapped to portal definitions.
	 * @throws ResourceNotFoundException If the resource does not exist.
	 * @throws IOException If an IOException occurs.
	 */
	public Map<String, Portal.Definition> load(String resource) throws
	ResourceNotFoundException, IOException {
		Map<String, Portal.Definition> defs;
		defs = new HashMap<String, Portal.Definition>();
		String[][] records = loadRecords(resource);
		for (String[] r : records) {
			Portal.Definition d = new Portal.Definition();
			d.name = r[0];
			d.imageIndex = r[1];
			defs.put(d.name.toLowerCase(), d);
			advanceProgress(1.0 / records.length);
		}
		return defs;
	}
	
}
