package yuuki.content;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import yuuki.file.ResourceNotFoundException;
import yuuki.sound.DualSoundEngine;

/**
 * Handles content and loaders for Yuuki.
 */
public class ContentManager {
	
	/**
	 * The content packs.
	 */
	private Map<String, ContentPack> packs;
	
	/**
	 * Creates sound effects.
	 */
	private Mergeable<Map<String, byte[]>> effectsEngine;
	
	
	
	/**
	 * Creates a new ContentManager and loads the built-in content pack.
	 * 
	 * @throws IOException 
	 * @throws ResourceNotFoundException 
	 */
	public ContentManager() throws ResourceNotFoundException, IOException {
		packs = new HashMap<String, ContentPack>();
		ContentPack builtIn = new ContentPack();
		packs.put(ContentPack.BUILT_IN_NAME, builtIn);
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
