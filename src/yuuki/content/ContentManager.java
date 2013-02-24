package yuuki.content;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import yuuki.file.ResourceNotFoundException;

/**
 * Handles content and loaders for Yuuki.
 */
public class ContentManager {
	
	/**
	 * Holds all loaded content.
	 */
	private static class Content {
		
	}
	
	/**
	 * The content packs.
	 */
	private Map<String, ContentPack> content;
	
	/**
	 * The loaded content.
	 */
	private Content loadedContent;
	
	/**
	 * Creates a new ContentManager and loads the built-in content pack.
	 * 
	 * @throws IOException 
	 * @throws ResourceNotFoundException 
	 */
	public ContentManager() throws ResourceNotFoundException, IOException {
		content = new HashMap<String, ContentPack>();
		ContentPack builtIn = new ContentPack();
		content.put(ContentPack.BUILT_IN_NAME, builtIn);
		enable(ContentPack.BUILT_IN_NAME);
	}
	
	/**
	 * Enables a ContentPack.
	 * 
	 * @param The name of the content pack to enable.
	 */
	public void enable(String name) {
		
	}
	
}
