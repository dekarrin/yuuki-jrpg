package yuuki.file;

import java.io.InputStream;

/**
 * Loads resource files into memory. Resource files are the files that are
 * external to the source code. These include images, sounds, and data files.
 */
public class ResourceLoader {
	
	/**
	 * The location of the resource files to be loaded. This is relative to the
	 * package structure.
	 */
	private final String resourceRoot;
	
	/**
	 * Creates a new ResourceLoader for resources at the specified location.
	 * 
	 * @param location The path to the directory containing the resource files
	 * to be loaded, relative to the package structure.
	 */
	public ResourceLoader(String location) {
		resourceRoot = location;
	}
	
	/**
	 * Obtains the InputStream for a single resource in the this ResourceLoader
	 * instance's resource location. This will return null if the specified
	 * resource does not exist.
	 * 
	 * @param resource The path to the resource to load, relative to the
	 * resource root.
	 * 
	 * @return An InputStream to the resource, or null if the given path does
	 * not refer to an existing resource file.
	 */
	public InputStream getStream(String resource) {
		String actualPath = resourceRoot + resource;
		InputStream stream = getClass().getResourceAsStream(actualPath);
		return stream;
	}
	
}
