package yuuki.content;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yuuki.action.ActionFactory;
import yuuki.file.ActionLoader;
import yuuki.file.ByteArrayLoader;
import yuuki.file.CsvResourceLoader;
import yuuki.file.EntityLoader;
import yuuki.file.ImageLoader;
import yuuki.file.PortalLoader;
import yuuki.file.ResourceFormatException;
import yuuki.file.ResourceNotFoundException;
import yuuki.file.SoundLoader;
import yuuki.file.TileLoader;
import yuuki.file.WorldLoader;
import yuuki.util.Progressable;
import yuuki.util.Progression;
import yuuki.world.PopulationFactory;
import yuuki.world.PortalFactory;
import yuuki.world.TileFactory;

/**
 * Handles resource loading of resources that are on disk.
 */
public class ContentLoader {
	
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
	 * The ContentManifest for this loader.
	 */
	protected ContentManifest manifest;
	
	/**
	 * The number of load operations planned.
	 */
	private int plannedLoadOps;
	
	/**
	 * The root of the resources.
	 */
	protected final File root;
	
	/**
	 * Creates a new ResourceManager for the specified content directory. This
	 * can be any directory on the file system.
	 * 
	 * @param root The path, relative to the resource root, to the content
	 * directory.
	 * @throws ResourceNotFoundException If the manifest file is not found.
	 * @throws IOException If an I/O exception occurs.
	 */
	public ContentLoader(File root) {
		this.root = root;
	}
	
	/**
	 * Loads the manifest file for this ResourceManager. This must be called
	 * before loading any other asset.
	 * 
	 * @return The loaded records.
	 */
	public ContentManifest readManifest() throws ResourceNotFoundException,
	IOException {
		CsvResourceLoader loader = createManifestLoader();
		String[][] records = null;
		records = loader.loadRecords(MANIFEST_FILE);
		manifest = new ContentManifest();
		if (records != null) {
			for (String[] r : records) {
				manifest.add(r[0], r[1]);
			}
		}
		return manifest;
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
	
	// start loading operation
	// load it
	// finish loading operation
	
	
	// load entities, given an action factory
	
	// load an action factory, given nothing
	
	// load 
	
	/**
	 * Loads sound effect definitions.
	 * 
	 * @param text What to set the text of the monitor to.
	 * @return The map loaded from the sound effect definitions file.
	 * @throws ResourceNotFoundException If the given path does not exist.
	 * @throws IOException If an I/O error occurs.
	 */
	public Map<String, String> loadEffectDefinitions(String text) throws
	ResourceNotFoundException, IOException {
		return loadIndexes(text, ContentManifest.FILE_EFFECTS);
	}
	
	/**
	 * Loads music data.
	 * 
	 * @param text What to set the text of the monitor to.
	 * @param indexes Maps the indexes to the paths of the files to load.
	 * @return A map containing the loaded music files identified by index.
	 */
	public Map<String, byte[]> loadMusic(String text,
			Map<String, String> indexes) {
		return loadIndexedFiles(text, indexes, ContentManifest.DIR_MUSIC);
	}
	
	/**
	 * Loads image data.
	 * 
	 * @param text What to set the text of the monitor to.
	 * @param indexes Maps the indexes to the paths of the files to load.
	 * @return A map containing the loaded image files identified by index.
	 */
	public Map<String, byte[]> loadImages(String text,
			Map<String, String> indexes) {
		return loadIndexedFiles(text, indexes, ContentManifest.DIR_IMAGES);
	}
	
	/**
	 * Loads sound effect data.
	 * 
	 * @param text What to set the text of the monitor to.
	 * @param indexes Maps the indexes to the paths of the files to load.
	 * @return A map containing the loaded sound files identified by index.
	 */
	public Map<String, byte[]> loadEffects(String text,
			Map<String, String> indexes) {
		return loadIndexedFiles(text, indexes, ContentManifest.DIR_EFFECTS);
	}
	
	/**
	 * Loads indexed files from a directory.
	 * 
	 * @param text What to set the text of the monitor.
	 * @param indexes Maps the indexes to the paths of the files to load.
	 * @param pathIndex The path to load files from.
	 * @return A map containing the loaded files identified by their indexes.
	 */
	private Map<String, byte[]> loadIndexedFiles(String text,
			Map<String, String> indexes, String pathIndex) {
		Progressable sub = startLoadingOperation(text);
		ByteArrayLoader loader = createFileLoader(pathIndex);
		loader.setProgressMonitor(sub);
		Map<String, byte[]> data = new HashMap<String, byte[]>();
		for (String i : indexes.keySet()) {
			byte[] fileData = null;
			try {
				fileData = loader.load(indexes.get(i));
				data.put(i, fileData);
			} catch (IOException e) {
				System.out.println(e);
			}
		}
		finishLoadingOperation(sub);
		return data;
	}
	
	/**
	 * Loads tile definitions.
	 * 
	 * @param text What to set the text of the monitor to.
	 * @return A TileFactory containing the loaded tile definitions.
	 * @throws ResourceNotFoundException If the given path does not exist.
	 * @throws IOException If an I/O error occurs.
	 */
	public TileFactory loadTiles(String text) throws
	ResourceNotFoundException, IOException {
		Progressable sub = startLoadingOperation(text);
		TileFactory factory = null;
		TileLoader loader = createTileLoader();
		loader.setProgressMonitor(sub);
		String path = manifest.get(ContentManifest.FILE_TILES);
		factory = loader.load(path);
		return factory;
	}
	
	/**
	 * Loads action definitions.
	 * 
	 * @param text What to set the text of the monitor to.
	 * @return An ActionFactory containing the loaded action definitions.
	 * @throws ResourceNotFoundException If the given path does not exist.
	 * @throws IOException If an I/O error occurs.
	 */
	public ActionFactory loadActions(String text) throws
	ResourceNotFoundException, IOException {
		Progressable sub = startLoadingOperation(text);
		ActionFactory factory = null;
		ActionLoader loader = createActionLoader();
		loader.setProgressMonitor(sub);
		String path = manifest.get(ContentManifest.FILE_ACTIONS);
		try {
			factory = loader.load(path);
		} catch (ResourceFormatException e) {
			System.err.println(e);
		}
		return factory;
	}
	
	/**
	 * Loads portal definitions.
	 * 
	 * @param text What to set the text of the monitor to.
	 * @return A PortalFactory containing the loaded portal definitions.
	 * @throws ResourceNotFoundException If the given path does not exist.
	 * @throws IOException If an I/O error occurs.
	 */
	public PortalFactory loadPortals(String text) throws
	ResourceNotFoundException, IOException {
		Progressable sub = startLoadingOperation(text);
		PortalFactory factory = null;
		PortalLoader loader = createPortalLoader();
		loader.setProgressMonitor(sub);
		String path = manifest.get(ContentManifest.FILE_PORTALS);
		factory = loader.load(path);
		return factory;
	}
	
	/**
	 * Loads music definitions.
	 * 
	 * @param text What to set the text of the monitor to.
	 * @return The map loaded from the music definitions file.
	 * @throws ResourceNotFoundException If the given path does not exist.
	 * @throws IOException If an I/O error occurs.
	 */
	public Map<String, String> loadMusicDefinitions(String text) throws
	ResourceNotFoundException, IOException {
		return loadIndexes(text, ContentManifest.FILE_MUSIC);
	}
	
	/**
	 * Loads image definitions.
	 * 
	 * @param text What to set the text of the monitor to.
	 * @return The map loaded from the image definitions file.
	 * @throws ResourceNotFoundException If the given path does not exist.
	 * @throws IOException If an I/O error occurs.
	 */
	public Map<String, String> loadImageDefinitions(String text) throws
	ResourceNotFoundException, IOException {
		return loadIndexes(text, ContentManifest.FILE_IMAGES);
	}
	
	/**
	 * Loads definitions as an index-value map.
	 * 
	 * @param text What to set the text of the monitor to.
	 * @param pathIndex The index of the path to load.
	 * @return The map loaded from the definition file.
	 * @throws ResourceNotFoundException If the given path does not exist.
	 * @throws IOException If an I/O error occurs.
	 */
	private Map<String, String> loadIndexes(String text, String pathIndex)
			throws ResourceNotFoundException, IOException {
		Map<String, String> indexes = new HashMap<String, String>();
		Map<String, List<String>> defs = loadDefinitions(text, pathIndex);
		for (String index : defs.keySet()) {
			String value = defs.get(index).get(0);
			indexes.put(index, value);
		}
		return indexes;
	}
	
	/**
	 * Loads definition files as a map of strings to lists of values. The first
	 * field is treated as the identifier and the remaining fields are placed
	 * into a list in the order that they appear.
	 * 
	 * @param text What to set the text of the monitor to.
	 * @param pathIndex the index of the path to load.
	 * @return The map loaded from the definition file.
	 * @throws ResourceNotFoundException If the given path does not exist.
	 * @throws IOException If an I/O error occurs.
	 */
	private Map<String, List<String>> loadDefinitions(String text,
			String pathIndex) throws ResourceNotFoundException, IOException {
		Progressable sub = startLoadingOperation(text);
		CsvResourceLoader loader = createDefinitionsLoader();
		loader.setProgressMonitor(sub);
		Map<String, List<String>> defs = new HashMap<String, List<String>>();
		String[][] records = loader.loadRecords(manifest.get(pathIndex));
		for (String[] r : records) {
			String index = r[0];
			List<String> fields = new ArrayList<String>();
			for (int i = 1; i < r.length; i++) {
				fields.add(r[i]);
			}
			defs.put(index, fields);
		}
		finishLoadingOperation(sub);
		return defs;
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
	 * Creates a loader for reading bytes from files in a folder.
	 * 
	 * @param pathIndex The index of the path of the directory.
	 * @return The created ByteArrayLoader.
	 */
	protected ByteArrayLoader createFileLoader(String pathIndex) {
		String path = manifest.get(pathIndex);
		File fileDir = new File(root, path);
		return new ByteArrayLoader(fileDir);
	}
	
	/**
	 * Creates a loader for reading sound effect files.
	 * 
	 * @return The created SoundLoader.
	 */
	protected SoundLoader createEffectLoader() {
		String path = manifest.get(ContentManifest.DIR_EFFECTS);
		File musicDir = new File(root, path);
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
		String path = manifest.get(ContentManifest.DIR_IMAGES);
		File imageDir = new File(root, path);
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
		String path = manifest.get(ContentManifest.DIR_MUSIC);
		File musicDir = new File(root, path);
		return new SoundLoader(root, musicDir);
	}
	
	/**
	 * Creates a loader for reading definitions file.
	 * 
	 * @return The created definitions loader.
	 */
	protected CsvResourceLoader createDefinitionsLoader() {
		return new CsvResourceLoader(root);
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
		String path = manifest.get(ContentManifest.DIR_LANDS);
		File landDir = new File(root, path);
		return new WorldLoader(root, landDir, pop);
	}
	
}
