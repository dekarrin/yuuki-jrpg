package yuuki;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import yuuki.action.ActionFactory;
import yuuki.entity.EntityFactory;
import yuuki.file.ActionLoader;
import yuuki.file.CsvResourceLoader;
import yuuki.file.EntityLoader;
import yuuki.file.ImageLoader;
import yuuki.file.PortalLoader;
import yuuki.file.ResourceFormatException;
import yuuki.file.ResourceNotFoundException;
import yuuki.file.SoundLoader;
import yuuki.file.TileLoader;
import yuuki.file.WorldLoader;
import yuuki.graphic.ImageFactory;
import yuuki.util.Progressable;
import yuuki.util.Progression;
import yuuki.world.PopulationFactory;
import yuuki.world.PortalFactory;
import yuuki.world.TileFactory;
import yuuki.world.World;

/**
 * Handles resource loading of resources that are on disk.
 */
public class ResourceManager {
	
	/**
	 * The name of the manifest file.
	 */
	public static final String MANIFEST_FILE = "content.def";
	
	/**
	 * The number of load operations completed.
	 */
	private int completedLoadOps;
	
	/**
	 * Whether this manager is currently in a load operation.
	 */
	private boolean inLoad = false;
	
	/**
	 * The master monitor for all loading tasks.
	 */
	private Progressable monitor;
	
	/**
	 * The paths to the resources contained within the archive.
	 */
	private Map<String, String> paths = new HashMap<String, String>();
	
	/**
	 * The number of load operations planned.
	 */
	private int plannedLoadOps;
	
	/**
	 * The root of the resources.
	 */
	private final File root;
	
	/**
	 * Creates a new ResourceManager for the specified content directory. This
	 * can be any directory on the file system.
	 * 
	 * @param root The path, relative to the resource root, to the content
	 * directory.
	 * @throws ResourceNotFoundException If the manifest file is not found.
	 * @throws IOException If an I/O exception occurs.
	 */
	public ResourceManager(File root) throws ResourceNotFoundException,
	IOException {
		this.root = root;
		readContentManifestFile();
	}
	
	/**
	 * Initializes a load.
	 * 
	 * @param count The number of loading operations planned.
	 * @return The monitor for the loading progress. This should not be used
	 * after the given number of loading operations are completed.
	 */
	public Progressable initLoad(int count) {
		inLoad = true;
		monitor = new Progression();
		plannedLoadOps = count;
		completedLoadOps = 0;
		return monitor;
	}
	
	/**
	 * Loads the entities from disk.
	 * 
	 * @param text The loading text to use on the monitor.
	 * @return The loaded EntityFactory.
	 */
	public EntityFactory loadEntities(String text) {
		Progressable sub = startLoadingOperation(text);
		Progressable m = sub.getSubProgressable(0.5);
		ActionFactory factory = loadActionDefinitions(m);
		m = sub.getSubProgressable(0.5);
		EntityFactory entityMaker = loadEntityDefinitions(factory, m);
		finishLoadingOperation(sub);
		return entityMaker;
	}
	
	/**
	 * Loads the images from disk.
	 * 
	 * @param text The loading text to use on the monitor.
	 * @return The ImageFactory with the loaded images.
	 */
	public ImageFactory loadImages(String text) {
		Progressable sub = startLoadingOperation(text);
		ImageLoader loader = createImageLoader();
		ImageFactory factory = null;
		loader.setProgressMonitor(sub);
		try {
			try {
				factory = loader.load(getPath("IMAGE_DEFS_FILE"));
			} catch (ResourceNotFoundException e) {
				System.err.println("Could not find: " + e.getMessage());
				throw e;
			}
		} catch (IOException e) {
			System.err.println("Could not load image file!");
		}
		finishLoadingOperation(sub);
		return factory;
	}
	
	/**
	 * Loads the background music from disk.
	 * 
	 * @param text The loading text to use on the monitor.
	 * @return A map that contains the background music data mapped to a sound
	 * index.
	 */
	public Map<String, byte[]> loadMusic(String text) {
		Progressable sub = startLoadingOperation(text);
		SoundLoader loader = createMusicLoader();
		Map<String, byte[]> soundData = null;
		loader.setProgressMonitor(sub);
		try {
			try {
				soundData = loader.load(getPath("MUSIC_DEFS_FILE"));
			} catch (ResourceNotFoundException e) {
				System.err.println("Could not find: " + e.getMessage());
				throw e;
			}
		} catch (IOException e) {
			System.err.println("Could not load music!");
		}
		finishLoadingOperation(sub);
		return soundData;
	}
	
	/**
	 * Loads the sound effects from disk.
	 * 
	 * @param text The loading text to use on the monitor.
	 * @return A map that contains the sound effect data mapped to a sound
	 * index.
	 */
	public Map<String, byte[]> loadSoundEffects(String text) {
		Progressable sub = startLoadingOperation(text);
		SoundLoader loader = createEffectLoader();
		Map<String, byte[]> soundData = null;
		loader.setProgressMonitor(sub);
		try {
			try {
				soundData = loader.load(getPath("SOUND_DEFS_FILE"));
			} catch (ResourceNotFoundException e) {
				System.err.println("Could not find: " + e.getMessage());
				throw e;
			}
		} catch (IOException e) {
			System.err.println("Could not load sound effects!");
		}
		finishLoadingOperation(sub);
		return soundData;
	}
	
	/**
	 * Loads the world from disk.
	 *
	 * @param text The loading text to use on the monitor.
	 * @param entities The EntityFactory to use.
	 * @return The loaded World.
	 */
	public World loadWorld(String text, EntityFactory ef) {
		Progressable sub = startLoadingOperation(text);
		Progressable m = sub.getSubProgressable(0.333);
		PortalFactory pf = loadPortalDefinitions(m);
		m = sub.getSubProgressable(0.333);
		TileFactory tf = loadTileDefinitions(m);
		m = sub.getSubProgressable(0.333);
		PopulationFactory pop = new PopulationFactory(tf, ef, pf);
		World world = loadWorldDefinitions(pop, m);
		finishLoadingOperation(sub);
		return world;
	}
	
	/**
	 * Completes the progress of the master monitor and sets the current load
	 * such that it is no longer considered initialized.
	 */
	private void finishLoad() {
		inLoad = false;
		monitor.finishProgress();
	}
	
	/**
	 * Finishes the current loading operation. The sub-monitor is immediately
	 * completed and the number of completed loading operations is incremented.
	 * If the load operation being finished is the last planned load operation,
	 * the master monitor is immediately completed and the current load is set
	 * such that it is no longer considered initialized.
	 * 
	 * @param sub The monitor of the loading operation being completed.
	 */
	private void finishLoadingOperation(Progressable sub) {
		sub.finishProgress();
		completedLoadOps++;
		if (completedLoadOps == plannedLoadOps) {
			finishLoad();
		}
	}
	
	/**
	 * Loads the action definitions from disk.
	 * 
	 * @param monitor Monitors the progress of the load.
	 * @return An ActionFactory containing the loaded definitions.
	 */
	private ActionFactory loadActionDefinitions(Progressable monitor) {
		ActionFactory factory = null;
		ActionLoader loader = createActionLoader();
		loader.setProgressMonitor(monitor);
		try {
			try {
				factory = loader.load(getPath("ACTION_DEFS_FILE"));
			} catch (ResourceFormatException e) {
				System.err.println(e.getMessage());
				throw e;
			} catch (ResourceNotFoundException e) {
				System.err.println("Could not find: " + e.getMessage());
				throw e;
			}
		} catch (Exception e) {
			System.err.println("Could not load action definitions!");
		}
		monitor.finishProgress();
		return factory;
	}
	
	/**
	 * Loads the entity definitions from disk.
	 * 
	 * @param af The ActionFactory for generating Actions used by the loaded
	 * entities.
	 * @param monitor Monitors the progress of the load.
	 * @return An EntityFactory containing the loaded entity definitions.
	 */
	private EntityFactory loadEntityDefinitions(ActionFactory af,
			Progressable monitor) {
		EntityFactory factory = null;
		EntityLoader loader = createEntityLoader(af);
		loader.setProgressMonitor(monitor);
		try {
			try {
				factory = loader.load(getPath("ENTITY_DEFS_FILE"));
			} catch (ResourceFormatException e) {
				System.err.println(e.getMessage());
				throw e;
			} catch (ResourceNotFoundException e) {
				System.err.println("Could not find: " + e.getMessage());
				throw e;
			}
		} catch (Exception e) {
			System.err.println("Could not load entity definitions!");
		}
		monitor.finishProgress();
		return factory;
	}
	
	/**
	 * Loads the portal definitions file from disk.
	 * 
	 * @param monitor Monitors the loading progress.
	 * @return The PortalFactory containing the portal definitions.
	 */
	private PortalFactory loadPortalDefinitions(Progressable monitor) {
		PortalFactory factory = null;
		PortalLoader loader = createPortalLoader();
		loader.setProgressMonitor(monitor);
		try {
			try {
				factory = loader.load(getPath("PORTAL_DEFS_FILE"));
			} catch (ResourceNotFoundException e) {
				System.err.println("Could not find: " + e.getMessage());
				throw e;
			}
		} catch (IOException e) {
			System.err.println("Could not load portal file!");
		}
		monitor.finishProgress();
		return factory;
	}
	
	/**
	 * Loads the tile definitions file from disk.
	 * 
	 * @param monitor Monitors the loading progress.
	 * @return The TileFactory containing the tile definitions.
	 */
	private TileFactory loadTileDefinitions(Progressable monitor) {
		TileFactory factory = null;
		TileLoader loader = createTileLoader();
		loader.setProgressMonitor(monitor);
		try {
			try {
				factory = loader.load(getPath("TILE_DEFS_FILE"));
			} catch (ResourceNotFoundException e) {
				System.err.println("Could not find: " + e.getMessage());
				throw e;
			}
		} catch (IOException e) {
			System.err.println("Could not load tile file!");
		}
		monitor.finishProgress();
		return factory;
	}
	
	/**
	 * Loads the world definitions and all land files within it into a new
	 * World object.
	 * 
	 * @param pop The factory to use for populating the Lands within the world.
	 * @param monitor Monitors the progress of the load.
	 * @return The World as read from the data files.
	 */
	private World loadWorldDefinitions(PopulationFactory pop,
			Progressable monitor) {
		World w = null;
		WorldLoader loader = createWorldLoader(pop);
		loader.setProgressMonitor(monitor);
		try {
			try {
				w = loader.load(getPath("WORLD_DEFS_FILE"));
			} catch (ResourceNotFoundException e) {
				System.err.println("Could not find: " + e.getMessage());
				throw e;
			}
		} catch (IOException e) {
			System.err.println("Could not load world file!");
		}
		monitor.finishProgress();
		return w;
	}
	
	/**
	 * Reads the manifest file and puts its contents into the paths map.
	 * 
	 * @throws ResourceNotFoundException If the manifest file could not be
	 * found.
	 * @throws IOException If an I/O error occurs.
	 */
	private void readContentManifestFile() throws ResourceNotFoundException,
	IOException {
		CsvResourceLoader loader = createManifestLoader();
		String[][] records = null;
		records = loader.loadRecords(MANIFEST_FILE);
		if (records != null) {
			for (String[] r : records) {
				paths.put(r[0], r[1]);
			}
		}
	}
	
	/**
	 * Starts a single loading operation. The master monitor's text is changed
	 * to reflect the loading operation, and a sub monitor for a portion of the
	 * master monitor with length equal to one divided by the number of planned
	 * load operations is obtained and returned.
	 * <p>
	 * Attempting to call this method after it has already been called the
	 * number of times specified by the last call to initLoad() will result in
	 * an exception being thrown. Attempting to call this method before
	 * initLoad() has been called at all will result in an exception being
	 * thrown.
	 * 
	 * @param text The text to display through the loading monitor.
	 * @return The sub-monitor for the loading operation.
	 * @throws IllegalStateOperation If this method is called when a load is
	 * not initialized.
	 */
	private Progressable startLoadingOperation(String text) {
		if (!inLoad) {
			throw new IllegalStateException("not in a load");
		}
		monitor.setText(text);
		Progressable m = monitor.getSubProgressable(1.0 / plannedLoadOps);
		return m;
	}
	
	/**
	 * Creates a loader for reading action definition files.
	 * 
	 * @return The created ActionLoader.
	 */
	protected ActionLoader createActionLoader() {
		return new ActionLoader(root);
	}
	
	/**
	 * Creates a loader for reading sound effect files.
	 * 
	 * @return The created SoundLoader.
	 */
	protected SoundLoader createEffectLoader() {
		File musicDir = new File(root, getPath("SOUND_DIR"));
		return new SoundLoader(root, musicDir);
	}
	
	/**
	 * Creates a loader for reading entity definition files.
	 * 
	 * @param af The ActionFactory to use with entity creation.
	 * @return The created EntityLoader.
	 */
	protected EntityLoader createEntityLoader(ActionFactory af) {
		return new EntityLoader(root, af);
	}
	
	/**
	 * Creates a loader for reading images.
	 * 
	 * @return The created ImageLoader.
	 */
	protected ImageLoader createImageLoader() {
		File imageDir = new File(root, getPath("IMAGE_DIR"));
		return new ImageLoader(root, imageDir);
	}
	
	/**
	 * Creates a loader for reading the content manifest file.
	 * 
	 * @return The created CsvResourceLoader.
	 */
	protected CsvResourceLoader createManifestLoader() {
		return new CsvResourceLoader(root);
	}
	
	/**
	 * Creates a loader for reading music files.
	 * 
	 * @return The created SoundLoader.
	 */
	protected SoundLoader createMusicLoader() {
		File musicDir = new File(root, getPath("MUSIC_DIR"));
		return new SoundLoader(root, musicDir);
	}
	
	/**
	 * Creates a loader for reading portal definition files.
	 * 
	 * @return The created PortalLoader.
	 */
	protected PortalLoader createPortalLoader() {
		return new PortalLoader(root);
	}
	
	/**
	 * Creates a loader for reading tile definition files.
	 * 
	 * @return The created TileLoader.
	 */
	protected TileLoader createTileLoader() {
		return new TileLoader(root);
	}
	
	/**
	 * Creates a loader for reading world definition files.
	 * 
	 * @param pop The PopulationFactory to use for populating the lands.
	 * @return The created WorldLoader.
	 */
	protected WorldLoader createWorldLoader(PopulationFactory pop) {
		File landDir = new File(root, getPath("LAND_DIR"));
		return new WorldLoader(root, landDir, pop);
	}
	
	/**
	 * Gets the path from the loaded list of paths.
	 * 
	 * @param index The index of the path to get.
	 * @return The path associated with the given index.
	 */
	protected final String getPath(String index) {
		String p = paths.get(index);
		if (p == null) {
			System.err.println("no resources for index '" + index + "'");
		}
		return p;
	}
	
}
