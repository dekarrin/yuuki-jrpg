package yuuki.action;

import yuuki.entity.Character;
import yuuki.item.UsableItem;

/**
 * Uses an item.
 */
public class ItemAction extends Action {
	
	/**
	 * The Item to be used.
	 */
	private UsableItem item;
	
	/**
	 * Creates a new ItemAction.
	 */
	public ItemAction() {
		super("use item", 0.0, 0.0, null, null);
	}
	
	/**
	 * Takes no arguments.
	 */
	@Override
	public Action createInstance(String[] args) {
		return new ItemAction();
	}
	
	public void setItem(UsableItem item) {
		this.item = item;
	}
	
	@Override
	protected void applyBuffs() {}
	
	@Override
	protected boolean applyCost() {
		return true;
	}
	
	@Override
	protected void applyEffect() {
		Action a = item.getActionForUse();
		a.clearTargets();
		a.setSkipCost(true);
		a.setOrigin(origin);
		a.addTarget(targets.get(0));
		a.apply();
	}
	
	@Override
	protected void setCostStat(Character c) {}
	
	@Override
	protected void setEffectStat(Character c) {}
	
}
