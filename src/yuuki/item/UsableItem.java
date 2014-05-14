package yuuki.item;

import yuuki.action.Action;

/**
 * An item that executes some action at no cost with a 100% success rate.
 */
public class UsableItem extends Item {
	
	/**
	 * The action that this Item executes.
	 */
	private final Action action;
	
	/**
	 * The number of times that this Item may be used.
	 */
	private final int maxUses;
	
	/**
	 * The number of times that this Item has been used.
	 */
	private int uses;
	
	/**
	 * Creates a new UsableItem.
	 * 
	 * @param id The ID of this item's type.
	 * @param name The name of the item.
	 * @param value The value of the item.
	 * @param image The index of the image for this item.
	 * @param description The description of this item.
	 * @param action The action that using this item performs.
	 * @param uses The number of times that this item may be used.
	 */
	public UsableItem(long id, String name, int value, String image,
			String description, Action action, int uses) {
		super(id, name, value, image, description);
		this.action = action;
		this.maxUses = uses;
		this.uses = 0;
	}
	
	/**
	 * Increases the use count of this item.
	 * 
	 * @param amount The amount to increase the use count by.
	 */
	public void increaseUses(int amount) {
		uses += amount;
		if (uses > maxUses) {
			throw new IllegalStateException("Item used too many times");
		}
	}
	
	/**
	 * Gets the action that this item performs.
	 * @return
	 */
	public Action getAction() {
		return action;
	}
	
	/**
	 * Gets the maximum number of uses for this item.
	 * 
	 * @return The maximum number of uses.
	 */
	public int getMaxUses() {
		return maxUses;
	}
	
	/**
	 * Gets the use count for this item.
	 * 
	 * @return The current number of uses.
	 */
	public int getUses() {
		return uses;
	}
	
	@Override
	public boolean isUsable() {
		return true;
	}
	
	/**
	 * Sets the use count for this item. This is bounded by the maxUses.
	 * 
	 * @param uses What to set the use count to.
	 */
	public void setUses(int uses) {
		if (uses < 0 || uses > maxUses) {
			throw new IllegalArgumentException(uses + " is out of range");
		}
		this.uses = uses;
	}
	
}
