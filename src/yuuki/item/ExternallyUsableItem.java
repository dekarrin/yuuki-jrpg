package yuuki.item;

import yuuki.action.Action;
import yuuki.entity.Character;

/**
 * An Item that can be used outside of battle.
 */
public class ExternallyUsableItem extends UsableItem {
	
	/**
	 * Creates a new ExternallyUsableItem.
	 * 
	 * @param name The name of this item.
	 * @param value The value of this item in in-game currency.
	 * @param action The action that this item performs.
	 * @param uses The number of times that this item can be used.
	 */
	public ExternallyUsableItem(String name, int value, Action action,
			int uses) {
		super(name, value, action, uses);
	}
	
	/**
	 * Uses this item.
	 * 
	 * @param user The character using the item.
	 * @param target The character that the item is being used on.
	 */
	public void use(Character user, Character target) {
		Action a = getActionForUse();
		a.clearTargets();
		a.setSkipCost(true);
		a.setOrigin(user);
		a.addTarget(target);
		a.apply();
	}
	
}
