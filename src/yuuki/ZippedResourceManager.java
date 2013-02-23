package yuuki;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

import yuuki.action.ActionFactory;
import yuuki.file.ActionLoader;
import yuuki.file.CsvResourceLoader;
import yuuki.file.EntityLoader;
import yuuki.file.ImageLoader;
import yuuki.file.PortalLoader;
import yuuki.file.SoundLoader;
import yuuki.file.TileLoader;
import yuuki.file.WorldLoader;
import yuuki.world.PopulationFactory;

/**
 * Manages resources loaded from a ZIP file.
 */
public class ZippedResourceManager extends ResourceManager {
	
	/**
	 * The path within the archive where all resources are located.
	 */
	private String zipRoot;
	
	/**
	 * Creates a new ZippedResourceManager for resources in the given ZIP file.
	 * The archive root is assumed to be '/'.
	 * 
	 * @param archive The ZIP file containing the resources.
	 */
	public ZippedResourceManager(File archive) {
		super(archive);
		zipRoot = "";
	}
	
	/**
	 * Creates a new ZippedResourceManager for resources in the given ZIP file.
	 * 
	 * @param archive The ZIP file containing the resources.
	 * @param root The path within the archive to the root of the resources.
	 */
	public ZippedResourceManager(File archive, String root) {
		super(archive);
		if (root.startsWith("/")) {
			root = root.substring(1);
		}
		zipRoot = root;
	}
	
	@Override
	protected ActionLoader createActionLoader() {
		ActionLoader loader = null;
		try {
			ZipFile zip = null;
			zip = new ZipFile(root);
			loader = new ActionLoader(zip, zipRoot);
			zip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loader;
	}
	
	@Override
	protected SoundLoader createEffectLoader() {
		SoundLoader loader = null;
		try {
			String soundRoot = zipRoot + getPath("SOUND_DIR");
			ZipFile zip = null;
			zip = new ZipFile(root);
			loader = new SoundLoader(zip, zipRoot, soundRoot);
			zip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loader;
	}
	
	@Override
	protected EntityLoader createEntityLoader(ActionFactory af) {
		EntityLoader loader = null;
		try {
			ZipFile zip = null;
			zip = new ZipFile(root);
			loader = new EntityLoader(zip, zipRoot, af);
			zip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loader;
	}
	
	@Override
	protected ImageLoader createImageLoader() {
		ImageLoader loader = null;
		try {
			String imageRoot = zipRoot + getPath("IMAGE_DIR");
			ZipFile zip = null;
			zip = new ZipFile(root);
			loader = new ImageLoader(zip, zipRoot, imageRoot);
			zip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loader;
	}
	
	@Override
	protected CsvResourceLoader createManifestLoader() {
		CsvResourceLoader loader = null;
		try {
			ZipFile zip = null;
			zip = new ZipFile(root);
			loader = new CsvResourceLoader(zip, zipRoot);
			zip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loader;
	}
	
	@Override
	protected SoundLoader createMusicLoader() {
		SoundLoader loader = null;
		try {
			String soundRoot = zipRoot + getPath("MUSIC_DIR");
			ZipFile zip = null;
			zip = new ZipFile(root);
			loader = new SoundLoader(zip, zipRoot, soundRoot);
			zip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loader;
	}
	
	@Override
	protected PortalLoader createPortalLoader() {
		PortalLoader loader = null;
		try {
			ZipFile zip = null;
			zip = new ZipFile(root);
			loader = new PortalLoader(zip, zipRoot);
			zip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loader;
	}
	
	@Override
	protected TileLoader createTileLoader() {
		TileLoader loader = null;
		try {
			ZipFile zip = null;
			zip = new ZipFile(root);
			loader = new TileLoader(zip, zipRoot);
			zip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loader;
	}
	
	@Override
	protected WorldLoader createWorldLoader(PopulationFactory pop) {
		WorldLoader loader = null;
		try {
			String landRoot = zipRoot + getPath("LAND_DIR");
			ZipFile zip = null;
			zip = new ZipFile(root);
			loader = new WorldLoader(zip, zipRoot, landRoot, pop);
			zip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loader;
	}
	
}
