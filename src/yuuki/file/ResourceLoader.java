package yuuki.file;

import java.awt.Dimension;
import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
	 * Whether to load from file. If false, streams are loaded directly from
	 * the file system rather than from the class path.
	 */
	private boolean loadFromFileSystem = false;
	
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
		InputStream stream;
		if (loadFromFileSystem) {
			stream = getFileStream(actualPath);
		} else {
			stream = getResourceStream(actualPath);
		}
		if (stream == null) {
			throw new ResourceNotFoundException(actualPath);
		}
		return stream;
	}
	
	/**
	 * Sets this resource loader to load from the file system rather than from
	 * the class path.
	 * 
	 * @param use Whether to use the file system.
	 */
	public void setFileSystemLoading(boolean use) {
		loadFromFileSystem = use;
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
	 * Gets the stream from a file.
	 * 
	 * @param resource The path to the resource to load.
	 * 
	 * @return An InputStream to the resource.
	 * 
	 * @throws ResourceNotFoundException If the specified resource could not be
	 * found.
	 */
	private InputStream getFileStream(String path) {
		InputStream stream = null;
		try {
			stream = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			stream = null;
		}
		return stream;
	}
	
	/**
	 * Gets the stream from a resource.
	 * 
	 * @param resource The path to the resource to load.
	 * 
	 * @return An InputStream to the resource.
	 * 
	 * @throws ResourceNotFoundException If the specified resource could not be
	 * found.
	 */
	private InputStream getResourceStream(String path) {
		return getClass().getResourceAsStream(path);
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
	 * Parses a dimension field.
	 * 
	 * @param field The name of the field containing the value.
	 * @param value The field containing the dimension.
	 * @return The dimension.
	 * @throws FieldFormatException If the field does not contain a valid
	 * dimension.
	 */
	protected Dimension parseDimensionField(String field, String value) throws
	FieldFormatException {
		String[] parts = value.split(",");
		Dimension d = new Dimension();
		if (parts.length < 2) {
			throw new FieldFormatException(field, value);
		}
		try {
			d.width = Integer.parseInt(parts[0]);
			d.height = Integer.parseInt(parts[1]);
		} catch (NumberFormatException e) {
			throw new FieldFormatException(field, value, e);
		}
		return d;
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
	 * Parses an integer field.
	 * 
	 * @param field The name of the field containing the value.
	 * @param value The field containing the integer.
	 * @return The integer.
	 * @throws FieldFormatException If the field does not contain a valid
	 * integer.
	 */
	protected int parseIntField(String field, String value) throws
	FieldFormatException {
		int i = 0;
		try {
			i = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new FieldFormatException(field, value);
		}
		return i;
	}
	
	/**
	 * Parses a point field.
	 * 
	 * @param field The name of the field containing the value.
	 * @param value The field containing the point.
	 * @return The point.
	 * @throws FieldFormatException If the field does not contain a valid
	 * point.
	 */
	protected Point parsePointField(String field, String value) throws
	FieldFormatException {
		String[] parts = value.split(",");
		Point p = new Point();
		if (parts.length < 2) {
			throw new FieldFormatException(field, value);
		}
		try {
			p.x = Integer.parseInt(parts[0]);
			p.y = Integer.parseInt(parts[1]);
		} catch (NumberFormatException e) {
			throw new FieldFormatException(field, value, e);
		}
		return p;
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
