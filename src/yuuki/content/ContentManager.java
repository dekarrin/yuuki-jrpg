package yuuki.content;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import yuuki.action.ActionFactory;
import yuuki.entity.EntityFactory;
import yuuki.file.ResourceNotFoundException;
import yuuki.graphic.ImageFactory;
import yuuki.sound.DualSoundEngine;
import yuuki.sound.EffectEngine;
import yuuki.sound.MusicEngine;
import yuuki.util.Progressable;
import yuuki.world.PortalFactory;
import yuuki.world.TileFactory;
import yuuki.world.World;

/**
 * Handles content and loaders for Yuuki.
 */
public class ContentManager {
	
	/**
	 * The content packs.
	 */
	private Map<String, ContentPack> packs;
	
	/**
	 * Handles sound effect content.
	 */
	private EffectEngine effectEngine;
	
	/**
	 * Handles music content.
	 */
	private MusicEngine musicEngine;
	
	/**
	 * Handles image content.
	 */
	private ImageFactory imageFactory;
	
	/**
	 * Handles world content.
	 */
	private World world;
	
	/**
	 * Handles Entity creation.
	 */
	private EntityFactory entityFactory;
	
	/**
	 * Represents the current model of all loaded content.
	 */
	private Content loadedContent;
	
	/**
	 * Creates a new ContentManager and loads the manifest for the built-in
	 * content pack.
	 * 
	 * @throws IOException 
	 * @throws ResourceNotFoundException 
	 * 
	 */
	public ContentManager() throws ResourceNotFoundException, IOException {
		packs = new HashMap<String, ContentPack>();
		loadedContent = new Content();
		loadedContent.init();
		effectEngine = new EffectEngine();
		musicEngine = new MusicEngine();
		imageFactory = new ImageFactory();
		world = new World();
		entityFactory = new EntityFactory();
		ContentPack builtIn = new ContentPack();
		packs.put(ContentPack.BUILT_IN_NAME, builtIn);
	}
	
	/**
	 * Gets the monitor for the progress of the next call to load(). This must
	 * be called only directly before load() is called, and only load() should
	 * be called after this method.
	 * 
	 * Getting the monitor of any loading operation is optional, but once the
	 * monitor for a particular load operation is obtained, the next load
	 * operation must be the one that the monitor is intended for. Failure to
	 * follow this will result in undefined behavior.
	 * 
	 * @param name The name of the content pack.
	 * @return The monitor.
	 */
	public Progressable startLoadMonitor(String name) {
		return packs.get(name).startLoadMonitor();
	}
	
	/**
	 * Loads all content in a content pack.
	 * 
	 * @param name The name of the content pack.
	 * @throws IOException 
	 * @throws ResourceNotFoundException 
	 */
	public void load(String name) throws ResourceNotFoundException,
	IOException {
		packs.get(name).load(loadedContent);
	}
	
	/**
	 * Loads all non-world assets from the built-in content pack.
	 * 
	 * @throws IOException 
	 * @throws ResourceNotFoundException 
	 */
	public void loadBuiltIn() throws ResourceNotFoundException,
	IOException {
		load(ContentPack.BUILT_IN_NAME);
	}
	
	/**
	 * Gets the sound engine that this ContentManager controls.
	 * 
	 * @return The sound engine.
	 */
	public DualSoundEngine getSoundEngine() {
		return new DualSoundEngine(effectEngine, musicEngine);
	}
	
	/**
	 * Gets the image factory that this ContentManager controls.
	 * 
	 * @return The image factory.
	 */
	public ImageFactory getImageFactory() {
		return imageFactory;
	}
	
	/**
	 * Gets the world engine that this ContentManager controls.
	 * 
	 * @return The world engine.
	 */
	public World getWorldEngine() {
		return world;
	}
	
	/**
	 * Gets the entity factory that this ContentManager controls.
	 * 
	 * @return The entity factory.
	 */
	public EntityFactory getEntityFactory() {
		return entityFactory;
	}
	
	/**
	 * Enables a ContentPack. The content pack must already be loaded, or a
	 * null pointer will be encountered. This method has no effect if the
	 * content pack is already enabled.
	 * 
	 * @param The name of the content pack to enable.
	 */
	public void enable(String name) {
		ContentPack pack = packs.get(name);
		if (pack.isEnabled()) {
			return;
		}
		Content c = pack.getContent();
	}
	
}
