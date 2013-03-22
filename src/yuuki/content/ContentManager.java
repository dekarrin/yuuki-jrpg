package yuuki.content;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipFile;

import yuuki.action.ActionFactory;
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
	 * Holds all loaded actions.
	 */
	private ActionFactory actionFactory;
	
	/**
	 * Represents the current model of all loaded content.
	 */
	private Content contentModel;
	
	/**
	 * Handles sound effect content.
	 */
	private EffectEngine effectEngine;
	
	/**
	 * The content packs that have been enabled.
	 */
	private List<ContentPack> enabledPacks;
	
	/**
	 * Handles Entity creation.
	 */
	private EntityFactory entityFactory;
	
	/**
	 * Handles image content.
	 */
	private ImageFactory imageFactory;
	
	/**
	 * Handles music content.
	 */
	private MusicEngine musicEngine;
	
	/**
	 * The content packs.
	 */
	private Map<String, ContentPack> packs;
	
	/**
	 * Creates a new ContentManager.
	 */
	public ContentManager() {
		packs = new HashMap<String, ContentPack>();
		enabledPacks = new ArrayList<ContentPack>();
		contentModel = new Content();
		effectEngine = new EffectEngine();
		musicEngine = new MusicEngine();
		imageFactory = new ImageFactory();
		actionFactory = new ActionFactory();
		entityFactory = new EntityFactory(actionFactory);
	}
	
	/**
	 * Disables a ContentPack.
	 * 
	 * @param id The identifier of the content pack to enable.
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
		if (pack.hasActions()) {
			actionFactory.subtract(c.getActions());
		}
		if (pack.hasImages() && pack.hasImageDefinitions()) {
			imageFactory.subtract(c.getImages());
		}
		contentModel.subtract(c);
		enabledPacks.remove(pack);
		pack.setEnabled(false);
	}
	
	/**
	 * Enables a ContentPack. The content pack must already be loaded, or a
	 * null pointer will be encountered. This method has no effect if the
	 * content pack is already enabled.
	 * 
	 * @param id The identifier of the content pack to enable.
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
		if (pack.hasActions()) {
			actionFactory.merge(c.getActions());
		}
		if (pack.hasImages() && pack.hasImageDefinitions()) {
			imageFactory.merge(c.getImages());
		}
		contentModel.merge(c);
		enabledPacks.add(pack);
		pack.setEnabled(true);
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
	 * Gets the IDs of all mods.
	 * 
	 * @return The IDs.
	 */
	public String[] getModIds() {
		ArrayList<String> ids = new ArrayList<String>();
		for (String id : packs.keySet()) {
			if (!id.equals(ContentPack.BUILT_IN_NAME)) {
				ids.add(id);
			}
		}
		Collections.sort(ids);
		return ids.toArray(new String[0]);
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
	 * Gets the world engine built of of enabled content packs that this
	 * ContentManager controls.
	 * 
	 * @return The world engine.
	 */
	public World getWorldEngine() {
		World world = new World();
		for (ContentPack cp : enabledPacks) {
			if (cp.hasWorld() && cp.hasLands() && cp.mapsAreLoaded()) {
				world.merge(cp.getContent().getLands());
			}
		}
		return world;
	}
	
	/**
	 * Loads non-map data in all but the given content packs.
	 * 
	 * @param exclude A list of IDs to exclude.
	 * @throws ResourceNotFoundException
	 * @throws IOException
	 */
	public void loadAll(Set<String> exclude) throws ResourceNotFoundException,
	IOException {
		for (String k : packs.keySet()) {
			if (!exclude.contains(k)) {
				loadAssets(k);
			}
		}
	}
	
	/**
	 * Loads all non-map content in a content pack.
	 * 
	 * @param name The name of the content pack.
	 * @throws IOException
	 * @throws ResourceNotFoundException
	 */
	public void loadAssets(String name) throws ResourceNotFoundException,
	IOException {
		packs.get(name).loadAssets(contentModel);
	}
	
	/**
	 * Loads the map data in all content packs that are enabled.
	 * 
	 * @throws ResourceNotFoundException
	 * @throws IOException
	 */
	public void loadEnabledWorlds() throws ResourceNotFoundException,
	IOException {
		for (ContentPack c : enabledPacks) {
			if (c.hasWorld() && c.hasLands()) {
				loadWorld(c.getName());
			}
		}
	}
	
	/**
	 * Loads all map content in a content pack.
	 * 
	 * @param name The name of the content pack.
	 * @throws IOException
	 * @throws ResourceNotFoundException
	 */
	public void loadWorld(String name) throws ResourceNotFoundException,
	IOException {
		packs.get(name).loadMaps(contentModel);
	}
	
	/**
	 * Initializes a ContentPack and reads its manifest.
	 * 
	 * @param id What name to identify the content pack with.
	 * @param file The archive or directory that contains the content pack's
	 * resources.
	 * 
	 * @throws IOException
	 * @throws ResourceNotFoundException
	 */
	public void scan(String id, File file) throws ResourceNotFoundException,
	IOException {
		ContentPack pack = null;
		if (file.isDirectory()) {
			pack = new ContentPack(file);
		} else {
			ZipFile z = new ZipFile(file.getCanonicalFile());
			pack = new ContentPack(z);
			z.close();
		}
		if (pack != null) {
			packs.put(id, pack);
		}
	}
	
	/**
	 * Loads the manifest for the built-in content pack.
	 * 
	 * @throws ResourceNotFoundException
	 * @throws IOException
	 */
	public void scanBuiltIn() throws ResourceNotFoundException, IOException {
		ContentPack builtIn = new ContentPack();
		packs.put(ContentPack.BUILT_IN_NAME, builtIn);
	}
	
	/**
	 * Gets the monitor for the progress of the next call to loadAssets(). This
	 * must be called only directly before loadAssets() is called, and only
	 * loadAssets() should be called after this method.
	 * 
	 * Getting the monitor of any loading operation is optional, but once the
	 * monitor for a particular load operation is obtained, the next load
	 * operation must be the one that the monitor is intended for. Failure to
	 * follow this will result in undefined behavior.
	 * 
	 * @param name The name of the content pack.
	 * @return The monitor.
	 */
	public Progressable startAssetLoadMonitor(String name) {
		return packs.get(name).startAssetLoadMonitor();
	}
	
	/**
	 * Gets the monitor for the progress of the next call to loadWorld(). This
	 * must be called only directly before loadWorld() is called, and only
	 * loadWorld() should be called after this method.
	 * 
	 * Getting the monitor of any loading operation is optional, but once the
	 * monitor for a particular load operation is obtained, the next load
	 * operation must be the one that the monitor is intended for. Failure to
	 * follow this will result in undefined behavior.
	 * 
	 * @param name The name of the content pack.
	 * @return The monitor.
	 */
	public Progressable startWorldLoadMonitor(String name) {
		return packs.get(name).startMapLoadMonitor();
	}
	
}
