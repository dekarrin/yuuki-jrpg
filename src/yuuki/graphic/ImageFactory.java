package yuuki.graphic;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import yuuki.util.InvalidIndexException;

/**
 * Loads image files and gets Image resources.
 */
public class ImageFactory {
	
	/**
	 * Maps image data to String indexes.
	 */
	private Map<String, byte[]> images;
	
	/**
	 * Creates a new ImageFactory. The data file is read and the image files
	 * are cached.
	 */
	public ImageFactory() {
		images = new HashMap<String, byte[]>();
	}
	
	/**
	 * Adds an image definition.
	 * 
	 * @param index The index to map the image to.
	 * @param imageData The bytes that make up the image.
	 */
	public void addDefinition(String index, byte[] imageData) {
		images.put(index, imageData);
	}
	
	/**
	 * Gets the image for an index.
	 * 
	 * @param index The index for the Image to get.
	 * 
	 * @return The Image for the associated index.
	 * 
	 * @throws InvalidIndexException If the given index does not exist.
	 */
	public Image createImage(String index) throws InvalidIndexException {
		Image image = null;
		byte[] imageData = images.get(index);
		if (imageData != null) {
			throw new InvalidIndexException(index);
		}
		ImageIcon icon = new ImageIcon(imageData);
		image = icon.getImage();
		return image;
	}
	
}
