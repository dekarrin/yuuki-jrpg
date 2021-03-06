package yuuki.content;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yuuki.action.Action;
import yuuki.entity.Character;
import yuuki.file.ActionLoader;
import yuuki.file.ByteArrayLoader;
import yuuki.file.CsvResourceLoader;
import yuuki.file.EntityLoader;
import yuuki.file.ItemLoader;
import yuuki.file.LandLoader;
import yuuki.file.PortalLoader;
import yuuki.file.ResourceFormatException;
import yuuki.file.ResourceNotFoundException;
import yuuki.file.TileLoader;
import yuuki.item.Item;
import yuuki.ui.DialogHandler;
import yuuki.util.Progressable;
import yuuki.util.Progression;
import yuuki.world.Land;
import yuuki.world.PopulationFactory;
import yuuki.world.Portal;
import yuuki.world.Tile;

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
	 * The number of load operations planned.
	 */
	private int plannedLoadOps;
	
	/**
	 * The ContentManifest for this loader.
	 */
	protected ContentManifest manifest;
	
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
	 * Checks whether this ContentLoader is currently in a loading operation.
	 * 
	 * @return Whether it is.
	 */
	public boolean isInLoad() {
		return inLoad;
	}
	
	/**
	 * Loads action definitions.
	 * 
	 * @param text What to set the text of the monitor to.
	 * @return A map containing action IDs mapped to the loaded definitions.
	 * @throws ResourceNotFoundException If the given path does not exist.
	 * @throws IOException If an I/O error occurs.
	 */
	public Map<Integer, Action.Definition> loadActions(String text) throws
	ResourceNotFoundException, IOException {
		Progressable sub = startLoadingOperation(text);
		Map<Integer, Action.Definition> actions = null;
		ActionLoader loader = createActionLoader();
		loader.setProgressMonitor(sub);
		String path = manifest.get(ContentManifest.FILE_ACTIONS);
		try {
			actions = loader.load(path);
		} catch (ResourceFormatException e) {
			DialogHandler.showError(e);
		}
		finishLoadingOperation(sub);
		return actions;
	}
	
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
	 * Loads entity definitions.
	 * 
	 * @param text What to set as the text of the monitor.
	 * @param actions The ActionFactory to use for creating entity actions.
	 * @return A map containing names mapped to the loaded entities.
	 * @throws ResourceNotFoundException If the given path does not exist.
	 * @throws IOException If an I/O error occurs.
	 */
	public Map<String, Character.Definition> loadEntities(String text)
			throws ResourceNotFoundException, IOException {
		Progressable sub = startLoadingOperation(text);
		Map<String, Character.Definition> entities = null;
		EntityLoader loader = createEntityLoader();
		loader.setProgressMonitor(sub);
		String path = manifest.get(ContentManifest.FILE_ENTITIES);
		try {
			entities = loader.load(path);
		} catch (ResourceFormatException e) {
			DialogHandler.showError(e);
		}
		finishLoadingOperation(sub);
		return entities;
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
	 * Loads item definitions.
	 * 
	 * @param text What to set the text of the monitor to.
	 * @return A map containing item IDs mapped to the loaded definitions.
	 * @throws ResourceNotFoundException If the given path does not exist.
	 * @throws IOException If an I/O error occurs.
	 */
	public Map<Long, Item.Definition> loadItems(String text) throws
	ResourceNotFoundException, IOException {
		Progressable sub = startLoadingOperation(text);
		Map<Long, Item.Definition> items = null;
		ItemLoader loader = createItemLoader();
		loader.setProgressMonitor(sub);
		String path = manifest.get(ContentManifest.FILE_ITEMS);
		try {
			items = loader.load(path);
		} catch (ResourceFormatException e) {
			DialogHandler.showError(e);
		}
		finishLoadingOperation(sub);
		return items;
	}
	
	/**
	 * Loads land file data.
	 * 
	 * @param text What to set the text of the monitor to.
	 * @param paths The paths to the land files.
	 * @param pop The population factory to use for populating the lands.
	 * @return A list containing the land file data.
	 */
	public List<Land> loadLands(String text, List<String> paths,
			PopulationFactory pop) {
		Progressable sub = startLoadingOperation(text);
		LandLoader loader = createLandLoader(pop);
		List<Land> lands = new ArrayList<Land>();
		for (String p : paths) {
			try {
				Progressable m = sub.getSubProgressable(1.0 / paths.size());
				loader.setProgressMonitor(m);
				Land land = loader.load(p);
				lands.add(land);
				m.finishProgress();
			} catch (Exception e) {
				DialogHandler.showError(e);
			}
		}
		finishLoadingOperation(sub);
		return lands;
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
	 * Loads portal definitions.
	 * 
	 * @param text What to set the text of the monitor to.
	 * @return A PortalFactory containing the loaded portal definitions.
	 * @throws ResourceNotFoundException If the given path does not exist.
	 * @throws IOException If an I/O error occurs.
	 */
	public Map<String, Portal.Definition> loadPortals(String text) throws
	ResourceNotFoundException, IOException {
		Progressable sub = startLoadingOperation(text);
		Map<String, Portal.Definition> portals = null;
		PortalLoader loader = createPortalLoader();
		loader.setProgressMonitor(sub);
		String path = manifest.get(ContentManifest.FILE_PORTALS);
		portals = loader.load(path);
		finishLoadingOperation(sub);
		return portals;
	}
	
	/**
	 * Loads tile definitions.
	 * 
	 * @param text What to set the text of the monitor to.
	 * @return A map containing tile IDs mapped to the loaded tiles.
	 * @throws ResourceNotFoundException If the given path does not exist.
	 * @throws IOException If an I/O error occurs.
	 */
	public Map<Integer, Tile.Definition> loadTiles(String text) throws
	ResourceNotFoundException, IOException {
		Progressable sub = startLoadingOperation(text);
		Map<Integer, Tile.Definition> tiles = null;
		TileLoader loader = createTileLoader();
		loader.setProgressMonitor(sub);
		String path = manifest.get(ContentManifest.FILE_TILES);
		tiles = loader.load(path);
		finishLoadingOperation(sub);
		return tiles;
	}
	
	/**
	 * Loads world definitions.
	 * 
	 * @param text What to set as the text of the monitor.
	 * @return A list of paths to land files.
	 * @throws ResourceNotFoundException If the given path does not exist.
	 * @throws IOException If an I/O error occurs.
	 */
	public List<String> loadWorld(String text) throws
	ResourceNotFoundException, IOException {
		return loadList(text, ContentManifest.FILE_WORLD);
	}
	
	/**
	 * Loads the manifest file for this ResourceManager. This must be called
	 * before loading any other asset.
	 * 
	 * @return The loaded records.
	 */
	public ContentManifest readManifest() throws ResourceNotFoundException,
	IOException {
		CsvResourceLoader loader = createDefLoader();
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
	 * Loads definition files as a map of strings to lists of values. The first
	 * field is treated as the identifier and the remaining fields are placed
	 * into a list in the order that they appear.
	 * 
	 * @param text What to set the text of the monitor to.
	 * @param pathIndex The index of the path to load.
	 * @return The map loaded from the definition file.
	 * @throws ResourceNotFoundException If the given path does not exist.
	 * @throws IOException If an I/O error occurs.
	 */
	private Map<String, List<String>> loadDefinitions(String text,
			String pathIndex) throws ResourceNotFoundException, IOException {
		Progressable sub = startLoadingOperation(text);
		CsvResourceLoader loader = createDefLoader();
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
		Map<String, byte[]> data = new HashMap<String, byte[]>();
		for (String i : indexes.keySet()) {
			loader.setProgressMonitor(sub);
			byte[] fileData = null;
			try {
				Progressable m = sub.getSubProgressable(1.0 / indexes.size());
				loader.setProgressMonitor(m);
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
	 * Loads a definition file as a list of items. Only the first field from
	 * each record is preserved.
	 * 
	 * @param text What to set the text of the monitor to.
	 * @param pathIndex The index of the path to load.
	 * @return The list loaded from the definition file.
	 * @throws ResourceNotFoundException If the given path does not exist.
	 * @throws IOException If an I/O error occurs.
	 */
	private List<String> loadList(String text, String pathIndex) throws
	ResourceNotFoundException, IOException {
		Progressable sub = startLoadingOperation(text);
		CsvResourceLoader loader = createDefLoader();
		loader.setProgressMonitor(sub);
		String[][] records = loader.loadRecords(manifest.get(pathIndex));
		List<String> list = new ArrayList<String>(records.length);
		for (String[] r : records) {
			String item = r[0];
			list.add(item);
		}
		finishLoadingOperation(sub);
		return list;
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
	 * Creates a loader for reading definitions files.
	 * 
	 * @return The created definitions loader.
	 */
	protected CsvResourceLoader createDefLoader() {
		return new CsvResourceLoader(root);
	}
	
	/**
	 * Creates a loader for reading entity definition files.
	 * 
	 * @return The created EntityLoader.
	 */
	protected EntityLoader createEntityLoader() {
		return new EntityLoader(root);
	}
	
	/**
	 * Creates a loader for reading bytes from files in a folder.
	 * 
	 * @param pathIndex The index of the path of the directory.
	 * @return The created ByteArrayLoader.
	 */
	protected ByteArrayLoader createFileLoader(String pathIndex) {
		File fileDir = manifest.appendFile(root, pathIndex);
		return new ByteArrayLoader(fileDir);
	}
	
	/**
	 * Creates a loader for reading item definition files.
	 * 
	 * @return The created ItemLoader.
	 */
	protected ItemLoader createItemLoader() {
		return new ItemLoader(root);
	}
	
	/**
	 * Creates a loader for reading land files.
	 * 
	 * @param pop The PopulationFactory for populating the Lands.
	 * @return The created LandLoader.
	 */
	protected LandLoader createLandLoader(PopulationFactory pop) {
		File landDir = manifest.appendFile(root, ContentManifest.DIR_LANDS);
		return new LandLoader(landDir, pop);
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
	
}
