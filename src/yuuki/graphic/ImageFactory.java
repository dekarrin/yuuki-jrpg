package yuuki.graphic;

import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import yuuki.file.CsvParser;

/**
 * Loads image files and gets Image resources.
 */
public class ImageFactory {
	
	/**
	 * Maps image string indexes to resource ID indexes.
	 */
	private Map<String, Integer> indexIds;
	
	/**
	 * Maps image file names to resource ID indexes.
	 */
	private Map<String, Integer> fileIds;
	
	/**
	 * The loaded Image resources.
	 */
	private ArrayList<Image> images;
	
	/**
	 * Parses the definitions file.
	 */
	private CsvParser parser;
	
	/**
	 * The location of the file containing the graphics definitions. The
	 * location is relative to the package structure.
	 */
	public static final String GRAPHICS_FILE = "graphics.csv";
	
	/**
	 * The location of data files in the package structure.
	 */
	public static final String RESOURCE_LOCATION = "/yuuki/resource/data/";
	
	/**
	 * The location of image files in the package structure.
	 */
	public static final String IMAGE_LOCATION = "/yuuki/resource/images/";
	
	/**
	 * Creates a new ImageFactory. The data file is read and the image files
	 * are cached.
	 */
	public ImageFactory() {
		indexIds = new HashMap<String, Integer>();
		fileIds = new HashMap<String, Integer>();
		images = new ArrayList<Image>();
		Map<String, String> defs = getDefinitions();
		precacheImages(defs);
	}
	
	/**
	 * Gets the image for an index.
	 * 
	 * @param index The index for the Image to get.
	 * 
	 * @return The Image for the associated index, or null if the given index
	 * does not exist.
	 */
	public Image getImage(String index) {
		Image img = null;
		Integer imageId = indexIds.get(index);
		if (imageId != null) {
			int id = imageId.intValue();
			img = images.get(id);
		}
		return img;
	}
	
	/**
	 * Loads the actual image files into memory.
	 * 
	 * @param defs A map of the indexes to image files.
	 */
	private void precacheImages(Map<String, String> defs) {
		for (Map.Entry<String, String> entry: defs.entrySet()) {
			String index = entry.getKey();
			String path = entry.getValue();
			try {
				cacheImage(index, path);
			} catch (IOException e) {
				System.out.println("Could not load image '" + path + "'");
			}
		}
	}
	
	/**
	 * Loads an image from disk into memory. The image is stored in the images
	 * map if it hasn't already been loaded for a previous index. The index is
	 * mapped to the ID of the image that it is loaded for.
	 * 
	 * @param index The index of the image.
	 * @param path The path of the image relative to the graphics resource
	 * location.
	 * 
	 * @throws IOException If an IOException occurs.
	 */
	private void cacheImage(String index, String path) throws IOException {
		int id = 0;
		if (isLoaded(path)) {
			id = fileIds.get(path);
		} else {
			id = loadImage(path);
		}
		indexIds.put(index, id);
	}
	
	/**
	 * Checks whether an image at a path has already been loaded.
	 * 
	 * @param path The path of the image to check for.
	 * 
	 * @return True if the image at the given path has been loaded; otherwise,
	 * false.
	 */
	private boolean isLoaded(String path) {
		return fileIds.containsKey(path);
	}
	
	/**
	 * Loads an image from disk into memory.
	 * 
	 * @param path The path of the image to load, relative to the image
	 * resource directory in the resource file system.
	 * 
	 * @return The ID number of the loaded image.
	 * 
	 * @throws IOException If an IOException occurs.
	 */
	private int loadImage(String path) throws IOException {
		String fileLocation = IMAGE_LOCATION + path;
		byte[] imageData = readFileData(fileLocation);
		ImageIcon icon = new ImageIcon(imageData);
		Image i = icon.getImage();
		images.add(i);
		int imageId = images.size() - 1;
		fileIds.put(path, imageId);
		return imageId;
	}
	
	/**
	 * Reads image data directly from a file to an array of bytes.
	 * 
	 * @param file The path of the file to read, relative to the package
	 * directory.
	 * 
	 * @throws IOException If an IOException occurs.
	 */
	private byte[] readFileData(String file) throws IOException {
		InputStream stream = getClass().getResourceAsStream(file);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int n = 0;
		while ((n = stream.read()) != -1) {
			buffer.write(n);
		}
		buffer.flush();
		byte[] fileData = buffer.toByteArray();
		return fileData;
	}
	
	/**
	 * Loads the definition files from disk.
	 * 
	 * @return A map of the indexes to the image paths.
	 */
	private Map<String, String> getDefinitions() {
		String graphicsFile = RESOURCE_LOCATION + GRAPHICS_FILE;
		InputStream file = getClass().getResourceAsStream(graphicsFile);
		parser = new CsvParser(file, '\n', ',', '"');
		Map<String, String> defs = null;
		try {
			defs = readParser();
		} catch (IOException e) {
			System.err.println("Graphics file format error");
		}
		return defs;
	}
	
	/**
	 * Reads all CSV data from the parser into a map.
	 * 
	 * @return A map of the indexes to the image paths.
	 * 
	 * @throws IOException If an IOException occurs.
	 */
	private Map<String, String> readParser() throws IOException {
		String[][] records = parser.read();
		Map<String, String> recordMap = new HashMap<String, String>();
		for (String[] r: records) {
			String index = r[0];
			String imageFile = r[1];
			recordMap.put(index, imageFile);
		}
		return recordMap;
	}
	
}
