package yuuki;

import java.io.File;
import java.io.IOException;

import yuuki.file.ResourceNotFoundException;

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
	 * 
	 * @param archive The ZIP file containing the resources.
	 * @param root The path within the archive to the root of the resources.
	 */
	public ZippedResourceManager(File archive, String root) throws
	ResourceNotFoundException, IOException {
		super(archive);
		zipRoot = root;
	}
	
	/**
	 * Creates a new ZippedResourceManager for resources in the given ZIP file.
	 * The archive root is assumed to be '/'.
	 * 
	 * @param archive The ZIP file containing the resources.
	 */
	public ZippedResourceManager(File archive) throws
	ResourceNotFoundException, IOException {
		super(archive);
		zipRoot = "/";
	}
	
}
