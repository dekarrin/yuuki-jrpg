package yuuki.item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A collection of items that keeps track of which ones may be used outside of
 * battle.
 */
public class InventoryPouch {
	
	/**
	 * All items that are in the inventory.
	 */
	private List<Item> items;
	
	/**
	 * The number of each item in the inventory.
	 */
	private List<Integer> itemCounts;
	
	/**
	 * The indexes of the items in the inventory that can be used.
	 */
	private List<Integer> usableItems;
	
	/**
	 * The indexes of the items in the inventory that can be used outside of
	 * battle.
	 */
	private List<Integer> externalItems;
	
	/**
	 * The maximum number of items in this inventory.
	 */
	private int capacity;
	
	/**
	 * The name of this InventoryPouch.
	 */
	private String name;
	
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
		itemCounts = new ArrayList<Integer>();
		usableItems = new ArrayList<Integer>();
		externalItems = new ArrayList<Integer>();
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
		int size = 0;
		for (int count : itemCounts) {
			size += count;
		}
		return size;
	}
	
}
