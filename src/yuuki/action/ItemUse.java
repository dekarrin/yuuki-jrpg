package yuuki.action;

import java.util.ArrayList;

import yuuki.buff.Buff;
import yuuki.entity.Character;
import yuuki.entity.Stat;
import yuuki.item.UsableItem;

/**
 * Uses an item.
 */
public class ItemUse extends Action {
	
	/**
	 * The Item to be used.
	 */
	private UsableItem item;
	
	/**
	 * Creates a new ItemAction.
	 */
	public ItemUse() {
		super("use item", 0.0, 0.0, null, null);
		setSkipCost(true);
	}
	
	@Override
	public void addTarget(Character t) {
		if (item != null) {
			item.getAction().addTarget(t);
		}
		super.addTarget(t);
	}
	
	@Override
	public boolean apply() {
		boolean success = item.getAction().apply();
		item.getAction().clearTargets();
		return success;
	}
	
	@Override
	public void clearTargets() {
		if (item != null) {
			item.getAction().clearTargets();
		}
		super.clearTargets();
	}
	
	@Override
	public ItemUse clone() {
		ItemUse iu = (ItemUse) super.clone();
		iu.item = null; // never copy the item
		return iu;
	}
	
	/**
	 * Takes no arguments.
	 */
	@Override
	public Action createInstance(String[] args) {
		return new ItemUse();
	}
	
	@Override
	public int[] getActualEffects() {
		return item.getAction().getActualEffects();
	}
	
	@Override
	public int[] getAffectedTeams() {
		return item.getAction().getAffectedTeams();
	}
	
	@Override
	public double getCost() {
		return item.getAction().getCost();
	}
	
	@Override
	public Stat getCostStat() {
		return item.getAction().getCostStat();
	}
	
	@Override
	public double getEffect() {
		return item.getAction().getEffect();
	}
	
	@Override
	public Stat getEffectStat() {
		return item.getAction().getEffectStat();
	}
	
	/**
	 * Gets the Item used by this ItemAction.
	 * 
	 * @return The item.
	 */
	public UsableItem getItem() {
		return item;
	}
	
	@Override
	public String getName() {
		if (item != null) {
			return "item (" + item.getName() + ")";
		} else {
			return super.getName();
		}
	}
	
	@Override
	public Character getOrigin() {
		if (item != null) {
			return item.getAction().getOrigin();
		} else {
			return super.getOrigin();
		}
	}
	
	@Override
	public Buff getOriginBuff() {
		return item.getAction().getOriginBuff();
	}
	
	@Override
	public boolean getSkipCost() {
		if (item != null) {
			return item.getAction().getSkipCost();
		} else {
			return super.getSkipCost();
		}
	}
	
	@Override
	public Buff getTargetBuff() {
		return item.getAction().getTargetBuff();
	}
	
	@Override
	public ArrayList<Character> getTargets() {
		if (item != null) {
			return item.getAction().getTargets();
		} else {
			return super.getTargets();
		}
	}
	
	/**
	 * Sets the Item to be used.
	 * 
	 * @param item The Item to use.
	 */
	public void setItem(UsableItem item) {
		if (item != null) {
			Action a = item.getAction();
			a.setSkipCost(getSkipCost());
			a.setOrigin(getOrigin());
			for (Character t : getTargets()) {
				a.addTarget(t);
			}
		}
		this.item = item;
	}
	
	@Override
	public void setOrigin(Character performer) {
		if (item != null) {
			item.getAction().setOrigin(performer);
		}
		super.setOrigin(performer);
	}
	
	@Override
	public void setSkipCost(boolean skip) {
		super.setSkipCost(skip);
		if (item != null) {
			item.getAction().setSkipCost(skip);
		}
	}
	
	@Override
	public boolean wasSuccessful() {
		return item.getAction().wasSuccessful();
	}
	
	@Override
	protected void applyEffect() {}
	
	@Override
	protected void applyBuffs() {}
	
	@Override
	protected boolean applyCost() {
		return true;
	}
	
	@Override
	protected void setCostStat(Character c) {}
	
	@Override
	protected void setEffectStat(Character c) {}
	
}
