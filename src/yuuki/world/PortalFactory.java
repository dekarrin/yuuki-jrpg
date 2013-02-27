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
public class PortalFactory implements
Mergeable<Map<String, Portal.Definition>> {
	
	/**
	 * Contains Portal definitions.
	 */
	private Map<String, Deque<Portal.Definition>> definitions;
	
	/**
	 * Creates a new PortalFactory.
	 */
	public PortalFactory() {
		definitions = new HashMap<String, Deque<Portal.Definition>>();
	}
	
	/**
	 * Adds a portal definition to this PortalFactory.
	 * 
	 * @param name The name of the portal.
	 * @param imageIndex The index of the image for this portal.
	 */
	public void addDefinition(String name, String imageIndex) {
		Portal.Definition pd = new Portal.Definition();
		pd.name = name;
		pd.imageIndex = imageIndex;
		Deque<Portal.Definition> d = definitions.get(name);
		if (d == null) {
			d = new ArrayDeque<Portal.Definition>();
			definitions.put(name, d);
		}
		d.push(pd);
	}
	
	@Override
	public void merge(Map<String, Portal.Definition> content) {
		for (Portal.Definition pd : content.values()) {
			addDefinition(pd.name, pd.imageIndex);
		}
	}
	
	@Override
	public void subtract(Map<String, Portal.Definition> content) {
		for (Portal.Definition pd : content.values()) {
			Deque<Portal.Definition> d = definitions.get(pd.name);
			if (d != null) {
				d.remove(pd);
				if (d.isEmpty()) {
					definitions.remove(pd.name);
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
		Deque<Portal.Definition> pdDeque = definitions.get(name);
		if (pdDeque == null) {
			throw new InvalidIndexException(name);
		}
		Portal.Definition def = pdDeque.peek();
		Portal p = new Portal(def.name, land, link, def.imageIndex);
		return p;
	}
	
}
