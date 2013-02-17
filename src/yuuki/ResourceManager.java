package yuuki;

import java.io.IOException;
import java.util.Map;

import yuuki.action.ActionFactory;
import yuuki.entity.EntityFactory;
import yuuki.file.ActionLoader;
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
 * Handles resource loading.
 */
public class ResourceManager {
	
	/**
	 * The location of the file containing the action definitions. The location
	 * is relative to the package structure.
	 */
	public static final String ACTIONS_FILE = "actions.csv";
	
	/**
	 * The path to definitions files.
	 */
	public static final String DEFINITIONS_PATH = "/yuuki/resource/data/";
	
	/**
	 * The location of the file containing the monster definitions. The
	 * location is relative to the package structure.
	 */
	public static final String ENTITIES_FILE = "monsters.csv";
	
	/**
	 * The path to the image definitions file.
	 */
	public static final String IMAGE_FILE = "graphics.csv";
	
	/**
	 * The path to image files.
	 */
	public static final String IMAGE_PATH = "/yuuki/resource/images/";
	
	/**
	 * The path to land files.
	 */
	public static final String LAND_PATH = "/yuuki/resource/land/";
	
	/**
	 * The location of the file containing the music definitions.
	 */
	public static final String MUSIC_FILE = "music.csv";
	
	/**
	 * The location of music files.
	 */
	public static final String MUSIC_PATH = "/yuuki/resource/audio/bgm/";
	
	/**
	 * The name of the portal definitions file.
	 */
	public static final String PORTAL_FILE = "portals.csv";
	
	/**
	 * The path to the sound effect definitions file.
	 */
	public static final String SOUND_EFFECT_FILE = "effects.csv";
	
	/**
	 * The path to sound effect files.
	 */
	public static final String SOUND_EFFECT_PATH =
			"/yuuki/resource/audio/sfx/";
	
	/**
	 * The name of the tile definitions file.
	 */
	public static final String TILE_FILE = "tiles.csv";
	
	/**
	 * The name of the world definitions file.
	 */
	public static final String WORLD_FILE = "world.csv";
	
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
	 * The number of load operations planned.
	 */
	private int plannedLoadOps;
	
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
		ImageLoader loader = new ImageLoader(DEFINITIONS_PATH, IMAGE_PATH);
		ImageFactory factory = null;
		loader.setProgressMonitor(sub);
		try {
			try {
				factory = loader.load(IMAGE_FILE);
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
		SoundLoader loader = new SoundLoader(DEFINITIONS_PATH, MUSIC_PATH);
		Map<String, byte[]> soundData = null;
		loader.setProgressMonitor(sub);
		try {
			try {
				soundData = loader.load(MUSIC_FILE);
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
		SoundLoader loader = new SoundLoader(DEFINITIONS_PATH,
				SOUND_EFFECT_PATH);
		Map<String, byte[]> soundData = null;
		loader.setProgressMonitor(sub);
		try {
			try {
				soundData = loader.load(SOUND_EFFECT_FILE);
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
		ActionLoader loader = new ActionLoader(DEFINITIONS_PATH);
		loader.setProgressMonitor(monitor);
		try {
			try {
				factory = loader.load(ACTIONS_FILE);
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
		EntityLoader loader = new EntityLoader(DEFINITIONS_PATH, af);
		loader.setProgressMonitor(monitor);
		try {
			try {
				factory = loader.load(ENTITIES_FILE);
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
		PortalLoader loader = new PortalLoader(DEFINITIONS_PATH);
		loader.setProgressMonitor(monitor);
		try {
			try {
				factory = loader.load(PORTAL_FILE);
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
		TileLoader loader = new TileLoader(DEFINITIONS_PATH);
		loader.setProgressMonitor(monitor);
		try {
			try {
				factory = loader.load(TILE_FILE);
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
		WorldLoader loader;
		loader = new WorldLoader(DEFINITIONS_PATH, LAND_PATH, pop);
		loader.setProgressMonitor(monitor);
		try {
			try {
				w = loader.load(WORLD_FILE);
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
	
}
