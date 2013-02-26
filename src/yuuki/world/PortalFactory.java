package yuuki.world;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import yuuki.content.Mergeable;
import yuuki.util.InvalidIndexException;

/**
 * Contains definitions for creating Portals.
 */
public class PortalFactory implements Mergeable<PortalFactory> {
	
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
	private Map<String, Deque<PortalDefinition>> definitions;
	
	/**
	 * Creates a new PortalFactory.
	 */
	public PortalFactory() {
		definitions = new HashMap<String, Deque<PortalDefinition>>();
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
		Deque<PortalDefinition> d = definitions.get(name);
		if (d == null) {
			d = new ArrayDeque<PortalDefinition>();
			definitions.put(name, d);
		}
		d.addFirst(pd);
	}
	
	@Override
	public void merge(PortalFactory content) {
		for (String name : content.definitions.keySet()) {
			PortalDefinition pd = content.definitions.get(name).peekFirst();
			addDefinition(name, pd.imageIndex);
		}
	}
	
	@Override
	public void subtract(PortalFactory content) {
		for (String name : content.definitions.keySet()) {
			PortalDefinition pd = content.definitions.get(name).peekFirst();
			Deque<PortalDefinition> d = this.definitions.get(name);
			if (d != null) {
				d.removeFirstOccurrence(pd);
				if (d.isEmpty()) {
					this.definitions.remove(name);
				}
			}
		}
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
		Deque<PortalDefinition> pdDeque = definitions.get(name);
		if (pdDeque == null) {
			throw new InvalidIndexException(name);
		}
		PortalDefinition def = pdDeque.peekFirst();
		Portal p = new Portal(def.name, land, link, def.imageIndex);
		return p;
	}
	
}
