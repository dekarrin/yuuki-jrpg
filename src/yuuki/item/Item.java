package yuuki.item;

/**
 * Something that can be held in a character's inventory.
 */
public class Item {
	
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
	 * @param name The name of this item.
	 * @param value The value of this item.
	 */
	public Item(String name, int value) {
		this.name = name;
		this.value = value;
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
	 * Gets the name of this item.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
}
