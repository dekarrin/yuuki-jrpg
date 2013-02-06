package yuuki.file;

import java.io.IOException;

import yuuki.graphic.ImageFactory;
import yuuki.util.Progressable;

/**
 * Loads image files from a location on disk.
 */
public class ImageLoader extends CsvResourceLoader {
	
	/**
	 * Loads image file data.
	 */
	private ByteArrayLoader imageLoader;
	
	/**
	 * Creates a new ImageLoader for resource files at the specified
	 * location.
	 * 
	 * @param location The path to the directory containing the resource files
	 * to be loaded, relative to the package structure.
	 * @param imageRoot The path to the directory containing the image files
	 * referred to by the resource files.
	 */
	public ImageLoader(String location, String imageRoot) {
		super(location);
		imageLoader = new ByteArrayLoader(imageRoot);
	}
	
	/**
	 * Loads an image factory an all its associated images from disk.
	 * 
	 * @param resource The resource containing the image definitions.
	 * 
	 * @return The image factory with all loaded files referred to in the
	 * definitions file.
	 * 
	 * @throws IOException If an IOException occurs.
	 */
	public ImageFactory load(String resource) throws IOException {
		ImageFactory factory = new ImageFactory();
		String[][] records = loadRecords(resource);
		for (String[] r : records) {
			String index = r[0];
			String file = r[1];
			Progressable m = getProgressSubSection(1.0 / records.length);
			imageLoader.setProgressMonitor(m);
			byte[] imageData = imageLoader.load(file);
			if (m != null) {
				m.finishProgress();
			}
			factory.addDefinition(index, imageData);
			advanceProgress(1.0 / records.length);
		}
		return factory;
	}
	
}
