package yuuki.file;

import java.io.FileNotFoundException;

/**
 * Signals that a given stream was unable to be located.
 */
public class ResourceNotFoundException extends FileNotFoundException {
	
	private static final long serialVersionUID = -2445716789505483275L;
	
	/**
	 * Constructs a new ResourceNotFoundException for the given path.
	 * 
	 * @param path The path to the resource file.
	 */
	public ResourceNotFoundException(String path) {
		super("'" + path + "' does not exist");
	}
	
}
