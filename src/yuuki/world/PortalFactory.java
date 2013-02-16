package yuuki.world;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import yuuki.util.InvalidIndexException;

/**
 * Contains definitions for creating Portals.
 */
public class PortalFactory {
	
	/**
	 * The definition for a portal.
	 */
	private static class PortalDefinition {
		
		/**
		 * The index of the image file for this portal.
		 */
		String imageIndex;
		
		/**
		 * The name of the portal.
		 */
		String name;
		
	}
	
	/**
	 * Contains Portal definitions.
	 */
	private Map<String, PortalDefinition> definitions;
	
	/**
	 * Creates a new PortalFactory.
	 */
	public PortalFactory() {
		definitions = new HashMap<String, PortalDefinition>();
	}
	
	/**
	 * Adds a portal definition to this PortalFactory.
	 * 
	 * @param name The name of the portal.
	 * @param imageIndex The index of the image for this portal.
	 */
	public void addDefinition(String name, String imageIndex) {
		PortalDefinition pd = new PortalDefinition();
		pd.name = name;
		pd.imageIndex = imageIndex;
		definitions.put(name, pd);
	}
	
	/**
	 * Creates an instance of a Portal.
	 * 
	 * @param name The name of the Portal to create an instance of.
	 * @param land The name of the Land that the new Portal links to.
	 * @param link The position in the linked land that this Portal is linked
	 * to.
	 * 
	 * @return An instance of a Portal of the given name.
	 * 
	 * @throws InvalidIndexException If the given name does not refer to an
	 * existing portal.
	 */
	public Portal createPortal(String name, String land, Point link) throws
	InvalidIndexException {
		PortalDefinition pd = definitions.get(name);
		if (pd == null) {
			throw new InvalidIndexException(name);
		}
		Portal p = new Portal(pd.name, land, link, pd.imageIndex);
		return p;
	}
	
}
