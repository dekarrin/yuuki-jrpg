package yuuki.item;

/**
 * Something that can be held in a character's inventory.
 */
public class Item {
	
	/**
	 * The unique identifier for this item's type.
	 */
	private final long id;
	
	/**
	 * The name of this Item.
	 */
	private final String name;
	
	/**
	 * The value of this Item.
	 */
	private final int value;
	
	/**
	 * Creates a new Item.
	 * 
	 * @param id The ID of this item's type.
	 * @param name The name of this item.
	 * @param value The value of this item.
	 */
	public Item(long id, String name, int value) {
		this.name = name;
		this.value = value;
		this.id = id;
	}
	
	/**
	 * Checks whether this Item is the same type as another.
	 * 
	 * @param i2 The item to check against.
	 */
	public boolean equals(Item i2) {
		return (this.id == i2.id);
	}
	
	/**
	 * Gets the type ID of this Item.
	 * 
	 * @return The ID.
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Gets the name of this item.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the value of this item.
	 * 
	 * @return The value.
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Checks whether this Item may be used outside of battle.
	 * 
	 * @return false in the general case; subclasses may override.
	 */
	public boolean isExternal() {
		return false;
	}
	
	/**
	 * Checks whether this Item may be used.
	 * 
	 * @return false in the general case; subclasses may override.
	 */
	public boolean isUsable() {
		return false;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
}
