package yuuki.item;

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of items that keeps track of which ones may be used outside of
 * battle.
 */
public class InventoryPouch {
	
	/**
	 * The maximum number of items in this inventory.
	 */
	private int capacity;
	
	/**
	 * The indexes of the items in the inventory that can be used outside of
	 * battle.
	 */
	private List<Integer> externalItems;
	
	/**
	 * All items that are in the inventory.
	 */
	private List<Item> items;
	
	/**
	 * The name of this InventoryPouch.
	 */
	private String name;
	
	/**
	 * The indexes of the items in the inventory that can be used.
	 */
	private List<Integer> usableItems;
	
	/**
	 * Creates a new InventoryPouch.
	 * 
	 * @param capacity The number of items it can hold.
	 * @param name The name of the InventoryPouch.
	 */
	public InventoryPouch(int capacity, String name) {
		this.capacity = capacity;
		this.name = name;
		items = new ArrayList<Item>();
		usableItems = new ArrayList<Integer>();
		externalItems = new ArrayList<Integer>();
	}
	
	/**
	 * Adds an Item to this pouch.
	 * 
	 * @param item The item to add.
	 * @throws PouchFullException If this pouch is already full.
	 */
	public void addItem(Item item) throws PouchFullException {
		if (items.size() == capacity) {
			throw new PouchFullException();
		}
		items.add(item);
		int index = items.size() - 1;
		if (item.isUsable()) {
			usableItems.add(index);
		}
		if (item.isExternal()) {
			externalItems.add(index);
		}
	}
	
	/**
	 * Gets the maximum number of items that this pouch can hold.
	 * 
	 * @return The capacity.
	 */
	public int getCapacity() {
		return capacity;
	}
	
	/**
	 * Gets the number of a certain type of item that this pouch contains.
	 * 
	 * @param id The type ID of the item to check for.
	 * @return The number of the given type of item.
	 */
	public int getCount(long id) {
		int count = 0;
		for (Item i : items) {
			if (i.getId() == id) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Gets all externally usable items in this InventoryPouch.
	 * 
	 * @return The externally usable items.
	 */
	public ExternallyUsableItem[] getExternallyUsableItems() {
		ExternallyUsableItem[] exties =
				new ExternallyUsableItem[externalItems.size()];
		for (int i = 0; i < externalItems.size(); i++) {
			int index = externalItems.get(i);
			exties[i] = (ExternallyUsableItem) items.get(index);
		}
		return exties;
	}
	
	/**
	 * Gets all items in this InventoryPouch.
	 * 
	 * @return The items.
	 */
	public Item[] getItems() {
		Item[] arr = new Item[items.size()];
		items.toArray(arr);
		return arr;
	}
	
	/**
	 * Gets the display name of this pouch.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the number of items currently in this pouch.
	 * 
	 * @return The total number of items.
	 */
	public int getSize() {
		return items.size();
	}
	
	/**
	 * Gets all usable items in this InventoryPouch.
	 * 
	 * @return The usable items.
	 */
	public UsableItem[] getUsableItems() {
		UsableItem[] usies = new UsableItem[usableItems.size()];
		for (int i = 0; i < usableItems.size(); i++) {
			int index = usableItems.get(i);
			usies[i] = (UsableItem) items.get(index);
		}
		return usies;
	}
	
	/**
	 * Removes an Item from this pouch.
	 * 
	 * @param item The item to remove.
	 * @throws ItemNotInPouchException If this pouch does not contain the given
	 * item.
	 */
	public void removeItem(Item item) throws ItemNotInPouchException {
		int index = -1;
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i) == item) {
				index = i;
				break;
			}
		}
		if (index == -1) {
			throw new ItemNotInPouchException(item);
		} else {
			items.remove(index);
			removeIndex(index);
			shiftIndexes(index);
		}
	}
	
	/**
	 * Removes the first Item with a certain type ID from this pouch.
	 * 
	 * @param id The type ID to remove an instance of.
	 * @throws ItemNotInPouchException If this pouch does not contain an item
	 * with the given ID.
	 */
	public void removeItem(long id) throws ItemNotInPouchException {
		int index = -1;
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getId() == id) {
				index = i;
				break;
			}
		}
		if (index == -1) {
			throw new ItemNotInPouchException(id);
		} else {
			items.remove(index);
			removeIndex(index);
			shiftIndexes(index);
		}
	}
	
	/**
	 * Sets the maximum number of items that this pouch can hold.
	 * 
	 * @param capacity The new capacity.
	 * @throws PouchFullException If the new capacity is lower than the number
	 * of items currently in the bag.
	 */
	public void setCapacity(int capacity) throws PouchFullException {
		if (getSize() > capacity) {
			throw new PouchFullException();
		} else {
			this.capacity = capacity;
		}
	}
	
	/**
	 * Sets the display name of this pouch.
	 * 
	 * @param name The new name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Removes references to the given index from the usable items and external
	 * items lists.
	 * 
	 * @param index The index to remove.
	 */
	private void removeIndex(int index) {
		usableItems.remove(new Integer(index));
		externalItems.remove(new Integer(index));
	}
	
	/**
	 * Shifts indexes higher then the given index left by one.
	 * 
	 * @param min The index above which indexes should be shifted.
	 */
	private void shiftIndexes(int min) {
		shiftIndexes(min, usableItems);
		shiftIndexes(min, externalItems);
	}
	
	/**
	 * Shifts indexes higher then the given index left by one in the given
	 * list.
	 * 
	 * @param min The index above which indexes should be shifted.
	 * @param list The list to shift the indexes in.
	 */
	private void shiftIndexes(int min, List<Integer> list) {
		for (int i = 0; i < list.size(); i++) {
			int current = list.get(i).intValue();
			if (current > min) {
				list.set(i, current - 1);
			}
		}
	}
	
}
