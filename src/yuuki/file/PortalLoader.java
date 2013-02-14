package yuuki.file;

import java.io.IOException;

import yuuki.world.PortalFactory;

/**
 * Loads portal definitions.
 */
public class PortalLoader extends CsvResourceLoader {
	
	/**
	 * Creates a new PortalLoader for portal files at the specified location.
	 * 
	 * @param location The path to the directory containing the portal files to
	 * be loaded, relative to the package structure.
	 */
	public PortalLoader(String location) {
		super(location);
	}
	
	/**
	 * Loads the data from a portal definitions file into a PortalFactory
	 * object.
	 * 
	 * @param resource The path to the resource file to load, relative to the
	 * resource root.
	 * 
	 * @return The PortalFactory object if the resource file exists; otherwise,
	 * null.
	 * 
	 * @throws IOException If an IOException occurs.
	 */
	public PortalFactory load(String resource) throws IOException {
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
