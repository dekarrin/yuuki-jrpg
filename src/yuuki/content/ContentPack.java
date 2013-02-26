package yuuki.content;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Map;
import java.util.zip.ZipFile;

import yuuki.action.ActionFactory;
import yuuki.file.ResourceNotFoundException;
import yuuki.util.Progressable;

/**
 * Loads content data and provides content pack meta data.
 */
public class ContentPack {
	
	/**
	 * The name for the built-in ContentPack.
	 */
	public static final String BUILT_IN_NAME = "__BUILT-IN";
	
	/**
	 * The root directory for all resource files.
	 */
	private static final String BUILT_IN_ROOT = "yuuki/resource/";
	
	/**
	 * Handles the actual loading of resources from the content pack.
	 */
	private final ContentLoader loader;
	
	/**
	 * Whether the resources included in this ContentPack are in a ZIP archive.
	 */
	private final boolean inArchive;
	
	/**
	 * The location of the content directory that holds all data for this pack.
	 * If this content pack is in a ZIP file, then this will be that ZIP file.
	 * If this content pack is simply in a directory, then this will be that
	 * directory.
	 */
	private final File location;
	
	/**
	 * The manifest for this ContentPack.
	 */
	private final ContentManifest manifest;
	
	/**
	 * The name of this ContentPack.
	 */
	private final String name;
	
	/**
	 * Creates a new ContentPack for loading built-in resources.
	 */
	public ContentPack() throws ResourceNotFoundException, IOException {
		name = BUILT_IN_NAME;
		File jar = getJarFile();
		inArchive = (jar != null);
		if (inArchive) {
			location = jar;
			loader = new ZippedContentLoader(location, BUILT_IN_ROOT);
		} else {
			location = new File(getPackageRootFile(), BUILT_IN_ROOT);
			loader = new ContentLoader(location);
		}
		manifest = loader.readManifest();
	}
	
	/**
	 * Creates a new ContentPack for files in the given directory.
	 * 
	 * @param directory The directory that contains the ContentPack.
	 * @throws ResourceNotFoundException If the given directory does not exist
	 * or if the given archive does not contain a manifest file.
	 * @throws IOException If an IOException occurs.
	 */
	public ContentPack(File directory) throws ResourceNotFoundException,
	IOException {
		location = directory;
		loader = new ContentLoader(location);
		inArchive = false;
		name = location.getName();
		manifest = loader.readManifest();
	}
	
	/**
	 * Creates a new ContentPack for files in the given ZIP archive.
	 * 
	 * @param archive The archive that contains the ContentPack.
	 * @throws ResourceNotFoundException If the given archive does not exist or
	 * if the given archive does not contain a manifest file.
	 * @throws IOException If an IOException occurs.
	 */
	public ContentPack(ZipFile archive) throws ResourceNotFoundException,
	IOException {
		location = new File(archive.getName());
		loader = new ContentLoader(location);
		inArchive = true;
		name = location.getName();
		manifest = loader.readManifest();
	}
	
	/**
	 * Gets the location of this ContentPack. If this ContentPack is located in
	 * an archive, the File representing that archive will be returned. If this
	 * ContentPack is located in a directory, the File representing that
	 * directory will be returned.
	 * 
	 * @return The location of this ContentPack.
	 */
	public File getLocation() {
		return location;
	}
	
	/**
	 * Gets the name of this ContentPack.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Whether this ContentPack contains actions.
	 */
	public boolean hasActions() {
		return manifest.has(ContentManifest.FILE_ACTIONS);
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
	 * @return The monitor.
	 */
	public Progressable startLoadMonitor() {
		return loader.initLoad(getLoadCount());
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
	 * @return The monitor.
	 */
	public Progressable startAssetLoadMonitor() {
		return loader.initLoad(getAssetLoadCount());
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
	 * @return The monitor.
	 */
	public Progressable startWorldLoadMonitor() {
		return loader.initLoad(getWorldLoadCount());
	}
	
	/**
	 * Loads all content that this ContentPack has. If any content is included
	 * that requires some other content that exists outside of this
	 * ContentPack, then the required item is taken from the given Content
	 * object.
	 * <P>
	 * Before the load, any content already loaded is cleared from memory.
	 * After the load, the loaded resources are stored in this ContentPack and
	 * can be retrieved by using the getContent() method.
	 * 
	 * @param resolver Used to satisfy requirements that are not included in
	 * this ContentPack. Set to null if requirements should not be
	 * automatically fulfilled.
	 * @return A monitor for getting the progress of the load.
	 */
	public void load(Content resolver) {
		content.reset();
		if (!loader.isInLoad()) {
			startLoadMonitor();
		}
		loadAssets(resolver);
		loadWorld(resolver);
	}
	
	/**
	 * Loads all content that is not the world and its lands.
	 * <P>
	 * Before the load, any content already loaded is cleared from memory.
	 * After the load, the loaded resources are stored in this ContentPack and
	 * can be retrieved by using the getContent() method.
	 * 
	 * @param resolver Used to satisfy requirements that are not included in
	 * this ContentPack. Set to null if requirements should not be
	 * automatically fulfilled.
	 * @throws ResourceNotFoundException If any resource in the load is not
	 * found.
	 * @throws IOException If an I/O error occurs during the load.
	 */
	public void loadAssets(Content resolver) throws ResourceNotFoundException,
	IOException {
		content.resetAssets();
		if (!loader.isInLoad()) {
			startAssetLoadMonitor();
		}
		loadMusicDefinitions();
		loadEffectDefinitions();
		loadImageDefinitions();
		loadMusic(resolver);
		loadEffects(resolver);
		loadImages(resolver);
		loadActions();
		loadEntities(resolver);
	}
	
	/**
	 * Loads Entities. The content is loaded from the content container if this
	 * ContentPack contains it as indicated by the manifest. This method must
	 * be called after loadActions(), or it must be provided with an
	 * appropriate resolver.
	 * 
	 * @param resolver Used to satisfy requirements that are not included in
	 * this ContentPack. Set to null if requirements should not be
	 * automatically fulfilled.
	 * @throws ResourceNotFoundException If any resource in the load is not
	 * found.
	 * @throws IOException If an I/O error occurs during the load.
	 */
	private void loadEntities(Content resolver) throws
	ResourceNotFoundException, IOException {
		if (hasEntities()) {
			ActionFactory a = null;
			if (content.actions != null) {
				a = content.actions;
			} else if (resolver != null && resolver.actions != null) {
				a = resolver.actions;
			} else {
				String msg = "Cannot load entities with no actions";
				throw new IllegalStateException(msg);
			}
			String msg = "Loading entities...";
			content.entities = loader.loadEntities(msg, a);
		}
	}
	
	/**
	 * Loads actions. The content is loaded from the content container if this
	 * ContentPack contains it as indicated by the manifest.
	 * 
	 * @throws ResourceNotFoundException If any resource in the load is not
	 * found.
	 * @throws IOException If an I/O error occurs during the load.
	 */
	private void loadActions() throws ResourceNotFoundException, IOException {
		if (hasActions()) {
			String msg = "Loading actions...";
			content.actions = loader.loadActions(msg);
		}
	}
	
	/**
	 * Loads images. The content is loaded from the content container if this
	 * ContentPack contains it as indicated by the manifest. This method must
	 * be called after loadImageDefinitions(), or it must be provided with an
	 * appropriate resolver.
	 * 
	 * @param resolver Used to satisfy requirements that are not included in
	 * this ContentPack. Set to null if requirements should not be
	 * automatically fulfilled.
	 * @throws ResourceNotFoundException If any resource in the load is not
	 * found.
	 * @throws IOException If an I/O error occurs during the load.
	 */
	private void loadImages(Content resolver) throws ResourceNotFoundException,
	IOException {
		if (hasImages()) {
			Map<String, String> paths = null;
			if (content.imageDefinitions != null) {
				paths = content.imageDefinitions;
			} else if (resolver != null && resolver.imageDefinitions != null) {
				paths = resolver.imageDefinitions;
			} else {
				String msg = "Cannot load images with no definitions";
				throw new IllegalStateException(msg);
			}
			String msg = "Loading images...";
			content.images = loader.loadImages(msg, paths);
		}
	}
	
	/**
	 * Loads sound effects. The content is loaded from the content container if
	 * this ContentPack contains it as indicated by the manifest. This method
	 * must be called after loadEffectDefinitions(), or it must be provided
	 * with an appropriate resolver.
	 * 
	 * @param resolver Used to satisfy requirements that are not included in
	 * this ContentPack. Set to null if requirements should not be
	 * automatically fulfilled.
	 * @throws ResourceNotFoundException If any resource in the load is not
	 * found.
	 * @throws IOException If an I/O error occurs during the load.
	 */
	private void loadEffects(Content resolver) throws
	ResourceNotFoundException, IOException {
		if (hasEffects()) {
			Map<String, String> paths = null;
			if (content.effectDefinitions != null) {
				paths = content.effectDefinitions;
			} else if (resolver != null &&
					resolver.effectDefinitions != null) {
				paths = resolver.effectDefinitions;
			} else {
				String msg = "Cannot load effects with no definitions";
				throw new IllegalStateException(msg);
			}
			String msg = "Loading sound effects...";
			content.effects = loader.loadEffects(msg, paths);
		}
	}
	
	/**
	 * Loads music. The content is loaded from the content container if this
	 * ContentPack contains it as indicated by the manifest. This method must
	 * be called after loadMusicDefinitions(), or it must be provided with an
	 * appropriate resolver.
	 * 
	 * @param resolver Used to satisfy requirements that are not included in
	 * this ContentPack. Set to null if requirements should not be
	 * automatically fulfilled.
	 * @throws ResourceNotFoundException If any resource in the load is not
	 * found.
	 * @throws IOException If an I/O error occurs during the load.
	 */
	private void loadMusic(Content resolver) throws ResourceNotFoundException,
	IOException {
		if (hasMusic()) {
			Map<String, String> paths = null;
			if (content.musicDefinitions != null) {
				paths = content.musicDefinitions;
			} else if (resolver != null && resolver.musicDefinitions != null) {
				paths = resolver.musicDefinitions;
			} else {
				String msg = "Cannot load music with no definitions";
				throw new IllegalStateException(msg);
			}
			String msg = "Loading music...";
			content.music = loader.loadMusic(msg, paths);
		}
	}
	
	/**
	 * Loads image definitions. The content is loaded from the content
	 * container if this ContentPack contains it as indicated by the manifest.
	 * 
	 * @throws ResourceNotFoundException If any resource in the load is not
	 * found.
	 * @throws IOException If an I/O error occurs during the load.
	 */
	private void loadImageDefinitions() throws ResourceNotFoundException,
	IOException {
		if (hasImageDefinitions()) {
			String msg = "Loading image definitions...";
			content.imageDefinitions = loader.loadImageDefinitions(msg);
		}
	}
	
	/**
	 * Loads sound effect definitions. The content is loaded from the content
	 * container if this ContentPack contains it as indicated by the manifest.
	 * 
	 * @throws ResourceNotFoundException If any resource in the load is not
	 * found.
	 * @throws IOException If an I/O error occurs during the load.
	 */
	private void loadEffectDefinitions() throws ResourceNotFoundException,
	IOException {
		if (hasEffectDefinitions()) {
			String msg = "Loading effect definitions...";
			content.effectDefinitions = loader.loadEffectDefinitions(msg);
		}
	}
	
	/**
	 * Gets the number of loads necessary to load every type of non-world
	 * content that this ContentPack has.
	 * 
	 * @return The number of different non-world resource types that this
	 * ContentPack has.
	 */
	private int getAssetLoadCount() {
		int count = 0;
		count += (hasMusicDefinitions())	? 1 : 0;
		count += (hasEffectDefinitions())	? 1 : 0;
		count += (hasImageDefinitions())	? 1 : 0;
		count += (hasMusic())				? 1 : 0;
		count += (hasEffects())				? 1 : 0;
		count += (hasImages())				? 1 : 0;
		count += (hasActions())				? 1 : 0;
		count += (hasEntities())			? 1 : 0;
		return count;
	}
	
	/**
	 * Loads music definitions. The content is loaded from the content
	 * container if this ContentPack contains it as indicated by the manifest.
	 * 
	 * @throws ResourceNotFoundException If any resource in the load is not
	 * found.
	 * @throws IOException If an I/O error occurs during the load.
	 */
	private void loadMusicDefinitions() throws ResourceNotFoundException,
	IOException {
		if (hasMusicDefinitions()) {
			String msg = "Loading music definitions...";
			content.musicDefinitions = loader.loadMusicDefinitions(msg);
		}
	}
	
	/**
	 * Gets the number of loads necessary to load every type of content that
	 * this ContentPack has.
	 * 
	 * @return The number of different resource types that this ContentPack
	 * has.
	 */
	private int getLoadCount() {
		return getAssetLoadCount() + getWorldLoadCount();
	}
	
	/**
	 * Gets the number of loads necessary to load every type of world content
	 * that this ContentPack has.
	 * 
	 * @return The number of different world resource types that this
	 * ContentPack has.
	 */
	private int getWorldLoadCount() {
		int count = 0;
		count += (hasLands())	? 1 : 0;
		count += (hasPortals())	? 1 : 0;
		count += (hasTiles())	? 1 : 0;
		count += (hasWorld())	? 1 : 0;
		return count;
	}
	
	/**
	 * The content loaded from disk.
	 */
	private final Content content = new Content();
	
	/**
	 * Checks whether this ContentPack contains sound effect files.
	 * 
	 * @return Whether it does.
	 */
	public boolean hasEffects() {
		return manifest.has(ContentManifest.DIR_EFFECTS);
	}
	
	/**
	 * Whether this ContentPack contains entities.
	 */
	public boolean hasEntities() {
		return manifest.has(ContentManifest.FILE_ENTITIES);
	}
	
	/**
	 * Checks whether this ContentPack contains image files.
	 * 
	 * @return Whether it does.
	 */
	public boolean hasImages() {
		return manifest.has(ContentManifest.DIR_IMAGES);
	}
	
	/**
	 * Checks whether this ContentPack contains land files.
	 * 
	 * @return Whether it does.
	 */
	public boolean hasLands() {
		return manifest.has(ContentManifest.DIR_LANDS);
	}
	
	/**
	 * Checks whether this ContentPack contains music files.
	 * 
	 * @return Whether it does.
	 */
	public boolean hasMusic() {
		return manifest.has(ContentManifest.DIR_MUSIC);
	}
	
	/**
	 * Whether this ContentPack contains sound definitions.
	 */
	public boolean hasEffectDefinitions() {
		return manifest.has(ContentManifest.FILE_EFFECTS);
	}
	
	/**
	 * Whether this ContentPack contains image definitions.
	 */
	public boolean hasImageDefinitions() {
		return manifest.has(ContentManifest.FILE_IMAGES);
	}
	
	/**
	 * Whether this ContentPack contains music definitions.
	 */
	public boolean hasMusicDefinitions() {
		return manifest.has(ContentManifest.FILE_MUSIC);
	}
	
	/**
	 * Whether this ContentPack contains portal definitions.
	 */
	public boolean hasPortals() {
		return manifest.has(ContentManifest.FILE_PORTALS);
	}
	
	/**
	 * Whether this ContentPack contains a tile definition.
	 */
	public boolean hasTiles() {
		return manifest.has(ContentManifest.FILE_TILES);
	}
	
	/**
	 * Whether this ContentPack contains a world definition.
	 */
	public boolean hasWorld() {
		return manifest.has(ContentManifest.FILE_WORLD);
	}
	
	/**
	 * Checks whether this ContentPack is located inside of a ZIP archive.
	 * 
	 * @return Whether this ContentPack is an a ZIP archive.
	 */
	public boolean isInArchive() {
		return inArchive;
	}
	
	/**
	 * Creates a File object that points to the package root located by a URL.
	 * 
	 * @param jarUrl The URL that locates the current class.
	 * @param packages The package depth of the current class.
	 * @return The File object that points to the package root in the given
	 * URL.
	 */
	private File composePackageRootFile(URL resource, int packageCount) {
		String decoded = null;
		try {
			decoded = URLDecoder.decode(resource.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// should never happen.
			System.err.println("UTF-8 identified as unsupported!");
		}
		String path = decoded.replace("!", "");
		if (path.startsWith("jar:")) {
			path = path.substring(4);
		}
		if (path.startsWith("file:")) {
			path = path.substring(5);
		}
		File jarFile = new File(path);
		for (int i = 0; i < packageCount + 1; i++) {
			jarFile = jarFile.getParentFile();
		}
		return jarFile;
	}
	
	/**
	 * Gets the JAR file this class is being executed from if it exists.
	 * 
	 * @return The JAR file, or null if this class is not being executed from a
	 * JAR file.
	 */
	private File getJarFile() {
		String className = getClass().getName();
		int packageCount = getPackageCount(className);
		String classPath = className.replace('.', '/');
		URL resource = getClass().getResource("/" + classPath + ".class");
		if (resource.toString().startsWith("jar:")) {
			return composePackageRootFile(resource, packageCount);
		} else {
			return null;
		}
	}
	
	/**
	 * Counts the number of packages in a class name.
	 * 
	 * @param className The fully-qualified class name to count the number of
	 * packages in.
	 * @return The number of packages in the given name.
	 */
	private int getPackageCount(String className) {
		int count = 0;
		for (int i = 0; i < className.length(); i++) {
			if (className.charAt(i) == '.') {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Gets the File that represents the root of the package structure that
	 * this class is located in.
	 * 
	 * @return The File that represents the package root.
	 */
	private File getPackageRootFile() {
		String className = getClass().getName();
		int packageCount = getPackageCount(className);
		String classPath = className.replace('.', '/');
		URL resource = getClass().getResource("/" + classPath + ".class");
		return composePackageRootFile(resource, packageCount);
	}
	
}
