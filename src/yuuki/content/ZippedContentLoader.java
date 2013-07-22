package yuuki.content;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

import yuuki.file.ActionLoader;
import yuuki.file.ByteArrayLoader;
import yuuki.file.CsvResourceLoader;
import yuuki.file.EntityLoader;
import yuuki.file.ItemLoader;
import yuuki.file.LandLoader;
import yuuki.file.PortalLoader;
import yuuki.file.TileLoader;
import yuuki.world.PopulationFactory;

/**
 * Manages resources loaded from a ZIP file.
 */
public class ZippedContentLoader extends ContentLoader {
	
	/**
	 * The path within the archive where all resources are located.
	 */
	private String zipRoot;
	
	/**
	 * Creates a new ZippedContentLoader for resources in the given ZIP file.
	 * The archive root is assumed to be '/'.
	 * 
	 * @param archive The ZIP file containing the resources.
	 */
	public ZippedContentLoader(File archive) {
		super(archive);
		zipRoot = "";
	}
	
	/**
	 * Creates a new ZippedResourceManager for resources in the given ZIP file.
	 * 
	 * @param archive The ZIP file containing the resources.
	 * @param root The path within the archive to the root of the resources.
	 */
	public ZippedContentLoader(File archive, String root) {
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
	protected ItemLoader createItemLoader() {
		ItemLoader loader = null;
		try {
			ZipFile zip = null;
			zip = new ZipFile(root);
			loader = new ItemLoader(zip, zipRoot);
			zip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loader;
	}
	
	@Override
	protected CsvResourceLoader createDefLoader() {
		CsvResourceLoader loader = null;
		try {
			ZipFile zip = new ZipFile(root);
			loader = new CsvResourceLoader(zip, zipRoot);
			zip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loader;
	}
	
	@Override
	protected EntityLoader createEntityLoader() {
		EntityLoader loader = null;
		try {
			ZipFile zip = null;
			zip = new ZipFile(root);
			loader = new EntityLoader(zip, zipRoot);
			zip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loader;
	}
	
	@Override
	protected ByteArrayLoader createFileLoader(String pathIndex) {
		ByteArrayLoader loader = null;
		try {
			ZipFile zip = new ZipFile(root);
			String path = manifest.appendPath(zipRoot, pathIndex);
			loader = new ByteArrayLoader(zip, path);
			zip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loader;
	}
	
	@Override
	protected LandLoader createLandLoader(PopulationFactory pop) {
		LandLoader loader = null;
		String dir = manifest.appendPath(zipRoot, ContentManifest.DIR_LANDS);
		try {
			ZipFile zip = new ZipFile(root);
			loader = new LandLoader(zip, dir, pop);
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
	
}
