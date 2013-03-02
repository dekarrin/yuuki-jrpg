package yuuki.graphic;

import java.awt.Image;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import yuuki.content.Mergeable;
import yuuki.util.InvalidIndexException;

/**
 * Loads image files and gets Image resources.
 */
public class ImageFactory implements Mergeable<Map<String, byte[]>> {
	
	/**
	 * Maps image data to String indexes.
	 */
	private Map<String, Deque<byte[]>> images;
	
	/**
	 * Creates a new ImageFactory. The data file is read and the image files
	 * are cached.
	 */
	public ImageFactory() {
		images = new HashMap<String, Deque<byte[]>>();
	}
	
	/**
	 * Adds an image definition.
	 * 
	 * @param index The index to map the image to.
	 * @param imageData The bytes that make up the image.
	 */
	public void addDefinition(String index, byte[] imageData) {
		Deque<byte[]> d = images.get(index);
		if (d == null) {
			d = new ArrayDeque<byte[]>();
			images.put(index, d);
		}
		d.push(imageData);
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
		Deque<byte[]> imageDataDeque = images.get(index);
		if (imageDataDeque == null) {
			throw new InvalidIndexException(index);
		}
		ImageIcon icon = new ImageIcon(imageDataDeque.peek());
		image = icon.getImage();
		return image;
	}
	
	@Override
	public void merge(Map<String, byte[]> content) {
		for (String k : content.keySet()) {
			addDefinition(k, content.get(k));
		}
	}
	
	@Override
	public void subtract(Map<String, byte[]> content) {
		for (String k : content.keySet()) {
			Deque<byte[]> d = images.get(k);
			if (d != null) {
				d.remove(content.get(k));
				if (d.isEmpty()) {
					images.remove(k);
				}
			}
		}
	}
	
}
