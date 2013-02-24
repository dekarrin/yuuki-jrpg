package yuuki.content;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.zip.ZipFile;

import yuuki.file.ResourceNotFoundException;

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
	private final ContentLoader fileManager;
	
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
			fileManager = new ZippedContentLoader(location, BUILT_IN_ROOT);
		} else {
			location = new File(getPackageRootFile(), BUILT_IN_ROOT);
			fileManager = new ContentLoader(location);
		}
		manifest = fileManager.readManifest();
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
		fileManager = new ContentLoader(location);
		inArchive = false;
		name = location.getName();
		manifest = fileManager.readManifest();
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
		fileManager = new ContentLoader(location);
		inArchive = true;
		name = location.getName();
		manifest = fileManager.readManifest();
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
	 * Checks whether this ContentPack contains sound effect files.
	 * 
	 * @return Whether it does.
	 */
	public boolean hasEffectFiles() {
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
	public boolean hasImageFiles() {
		return manifest.has(ContentManifest.DIR_IMAGES);
	}
	
	/**
	 * Checks whether this ContentPack contains land files.
	 * 
	 * @return Whether it does.
	 */
	public boolean hasLandFiles() {
		return manifest.has(ContentManifest.DIR_LANDS);
	}
	
	/**
	 * Checks whether this ContentPack contains music files.
	 * 
	 * @return Whether it does.
	 */
	public boolean hasMusicFiles() {
		return manifest.has(ContentManifest.DIR_MUSIC);
	}
	
	/**
	 * Whether this ContentPack contains sound definitions.
	 */
	public boolean hasNewEffects() {
		return manifest.has(ContentManifest.FILE_EFFECTS);
	}
	
	/**
	 * Whether this ContentPack contains image definitions.
	 */
	public boolean hasNewImages() {
		return manifest.has(ContentManifest.FILE_IMAGES);
	}
	
	/**
	 * Whether this ContentPack contains music definitions.
	 */
	public boolean hasNewMusic() {
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
