package yuuki.content;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipFile;

import yuuki.entity.EntityFactory;
import yuuki.file.ResourceNotFoundException;
import yuuki.graphic.ImageFactory;
import yuuki.sound.DualSoundEngine;
import yuuki.sound.EffectEngine;
import yuuki.sound.MusicEngine;
import yuuki.util.Progressable;
import yuuki.world.World;

/**
 * Handles content and loaders for Yuuki.
 */
public class ContentManager {
	
	/**
	 * Handles sound effect content.
	 */
	private EffectEngine effectEngine;
	
	/**
	 * Handles Entity creation.
	 */
	private EntityFactory entityFactory;
	
	/**
	 * Handles image content.
	 */
	private ImageFactory imageFactory;
	
	/**
	 * Represents the current model of all loaded content.
	 */
	private Content contentModel;
	
	/**
	 * Handles music content.
	 */
	private MusicEngine musicEngine;
	
	/**
	 * The content packs.
	 */
	private Map<String, ContentPack> packs;
	
	/**
	 * Handles world content.
	 */
	private World world;
	
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
		contentModel = new Content();
		contentModel.init();
		effectEngine = new EffectEngine();
		musicEngine = new MusicEngine();
		imageFactory = new ImageFactory();
		world = new World();
		entityFactory = new EntityFactory();
		ContentPack builtIn = new ContentPack();
		packs.put(ContentPack.BUILT_IN_NAME, builtIn);
	}
	
	/**
	 * Initializes a ContentPack and reads its manifest.
	 * 
	 * @param id What name to identify the content pack with.
	 * @param file The archive or directory that contains the content pack's
	 * resources.
	 */
	public void scan(String id, File file) {
		ContentPack pack = null;
		if (file.isDirectory()) {
			try {
				pack = new ContentPack(file);
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				ZipFile z = new ZipFile(file);
				pack = new ContentPack(z);
				z.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (pack != null) {
			packs.put(id, pack);
		}
	}
	
	/**
	 * Disables a ContentPack.
	 * 
	 * @param name The name of the content pack to enable.
	 */
	public void disable(String name) {
		ContentPack pack = packs.get(name);
		if (!pack.isEnabled()) {
			return;
		}
		Content c = pack.getContent();
		if (pack.hasEffects() && pack.hasEffectDefinitions()) {
			effectEngine.subtract(c.getEffects());
		}
		if (pack.hasMusic() && pack.hasMusicDefinitions()) {
			musicEngine.subtract(c.getMusic());
		}
		if (pack.hasEntities()) {
			entityFactory.subtract(c.getEntities());
		}
		if (pack.hasImages() && pack.hasImageDefinitions()) {
			imageFactory.subtract(c.getImages());
		}
		if (pack.hasLands() && pack.hasWorld()) {
			world.subtract(c.getLands());
		}
		contentModel.subtract(c);
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
		if (pack.hasEffects() && pack.hasEffectDefinitions()) {
			effectEngine.merge(c.getEffects());
		}
		if (pack.hasMusic() && pack.hasMusicDefinitions()) {
			musicEngine.merge(c.getMusic());
		}
		if (pack.hasEntities()) {
			entityFactory.merge(c.getEntities());
		}
		if (pack.hasImages() && pack.hasImageDefinitions()) {
			imageFactory.merge(c.getImages());
		}
		if (pack.hasLands() && pack.hasWorld()) {
			world.merge(c.getLands());
		}
		contentModel.merge(c);
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
	 * Gets the image factory that this ContentManager controls.
	 * 
	 * @return The image factory.
	 */
	public ImageFactory getImageFactory() {
		return imageFactory;
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
	 * Gets the world engine that this ContentManager controls.
	 * 
	 * @return The world engine.
	 */
	public World getWorldEngine() {
		return world;
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
		packs.get(name).load(contentModel);
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
	
}
