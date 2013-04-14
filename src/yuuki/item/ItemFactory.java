package yuuki.item;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import yuuki.action.Action;
import yuuki.action.ActionFactory;
import yuuki.content.Mergeable;
import yuuki.util.InvalidIndexException;

/**
 * Creates Item instances.
 */
public class ItemFactory implements Mergeable<Map<Long, Item.Definition>> {
	
	/**
	 * Interprets action IDs.
	 */
	private ActionFactory actionFactory;
	
	/**
	 * All item definitions needed to create instances.
	 */
	private Map<Long, Deque<Item.Definition>> definitions;
	
	/**
	 * Creates a new ItemFactory.
	 * 
	 * @param actions The ActionFactory that is to be used to interpret action
	 * indexes attached to items.
	 */
	public ItemFactory(ActionFactory actions) {
		definitions = new HashMap<Long, Deque<Item.Definition>>();
		actionFactory = actions;
	}
	
	/**
	 * Adds a definition to this ItemFactory.
	 * 
	 * @param def The definition to add.
	 */
	public void addDefinition(Item.Definition def) {
		Deque<Item.Definition> d = definitions.get(def.id);
		if (d == null) {
			d = new ArrayDeque<Item.Definition>();
			definitions.put(def.id, d);
		}
		d.push(def);
	}
	
	/**
	 * Creates an instance of an item.
	 * 
	 * @param id The ID of the item to create.
	 * @throws InvalidIndexException If the given ID is invalid.
	 */
	public Item createItem(long id) throws InvalidIndexException {
		Deque<Item.Definition> idDeque = definitions.get(id);
		if (idDeque == null) {
			throw new InvalidIndexException(id);
		}
		Item.Definition def = idDeque.peek();
		Item item = null;
		if (def.usable) {
			Action a = actionFactory.createAction(def.action);
			if (def.external) {
				item = new ExternallyUsableItem(def.id, def.name, def.value,
						def.image, def.description, a, def.uses);
			} else {
				item = new UsableItem(def.id, def.name, def.value, def.image,
						def.description, a, def.uses);
			}
		} else {
			item = new Item(def.id, def.name, def.value, def.image,
					def.description);
		}
		return item;
	}
	
	@Override
	public void merge(Map<Long, Item.Definition> content) {
		for (Item.Definition def : content.values()) {
			addDefinition(def);
		}
	}
	
	@Override
	public void subtract(Map<Long, Item.Definition> content) {
		for (Item.Definition def : content.values()) {
			Deque<Item.Definition> d = definitions.get(def.id);
			if (d != null) {
				d.remove(def);
				if (d.isEmpty()) {
					definitions.remove(def.id);
				}
			}
		}
	}
	
}
