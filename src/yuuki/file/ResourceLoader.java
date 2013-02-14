package yuuki.file;

import java.io.InputStream;

import yuuki.util.Progressable;

/**
 * Loads resource files into memory. Resource files are the files that are
 * external to the source code. These include images, sounds, and data files.
 */
public class ResourceLoader {
	
	/**
	 * Separates multiple values in a single field.
	 */
	private static final String MULTIVALUE_DELIMITER = ":";
	
	/**
	 * Records the progress of loading.
	 */
	private Progressable monitor;
	
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
	 * @return An InputStream to the resource.
	 * 
	 * @throws ResourceNotFoundException If the specified resource could not be
	 * found.
	 */
	public InputStream getStream(String resource) throws
	ResourceNotFoundException {
		String actualPath = resourceRoot + resource;
		InputStream stream = getClass().getResourceAsStream(actualPath);
		if (stream == null) {
			throw new ResourceNotFoundException(actualPath);
		}
		return stream;
	}
	
	/**
	 * Sets the progress monitor for the next load.
	 * 
	 * @param monitor The progress monitor.
	 */
	public void setProgressMonitor(Progressable monitor) {
		this.monitor = monitor;
	}
	
	/**
	 * Advances the progress monitor by a certain percentage.
	 * 
	 * @param percent The amount to advance by.
	 */
	protected void advanceProgress(double percent) {
		if (monitor != null) {
			monitor.advanceProgress(percent);
		}
	}
	
	/**
	 * Sets the progress monitor for this loader to its finished value.
	 */
	protected void finishProgress() {
		if (monitor != null) {
			monitor.finishProgress();
		}
	}
	
	/**
	 * Gets the subsection for the monitor.
	 * 
	 * @param length The length in percent of the monitor of the sub-monitor to
	 * get.
	 */
	protected Progressable getProgressSubSection(double length) {
		Progressable p = null;
		if (monitor != null) {
			p = monitor.getSubProgressable(length);
		}
		return p;
	}
	
	/**
	 * Parses an array of strings into an array of integers.
	 * 
	 * @param str The array of strings to convert.
	 * @param start What index to start the conversion at.
	 * 
	 * @return An array containing the parsed integers.
	 */
	protected int[] parseIntArray(String[] str, int start) {
		int[] iValues = new int[str.length - start];
		for (int i = start; i < str.length; i++) {
			iValues[i - start] = Integer.parseInt(str[i]);
		}
		return iValues;
	}
	
	/**
	 * Splits a multiple-value field into its individual values. Each value is
	 * delimited by the MULTIVALUE_DELIMITER.
	 * 
	 * @param value The entire field as a String.
	 * 
	 * @return An array containing each of the field's values.
	 */
	protected String[] splitMultiValue(String value) {
		return value.split(MULTIVALUE_DELIMITER);
	}
	
}
