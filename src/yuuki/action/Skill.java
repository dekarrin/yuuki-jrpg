/**
 * An Action that costs mana to apply some effect to the targets.
 */

package yuuki.action;

import yuuki.buff.Buff;
import yuuki.entity.Character;

public abstract class Skill extends Action implements Cloneable {

	/**
	 * Creates a new Skill that costs the specified amount of mana.
	 *
	 * @inheritDoc
	 */
	public Skill(String name, double effect, double manaCost, Buff tBuff,
					Buff oBuff) {
		super(name, effect, manaCost, tBuff, oBuff);
	}
	
	/**
	 * Creates a clone of this Skill.
	 *
	 * @return The clone.
	 */
	public Skill clone() {
		return (Skill) super.clone();
	}
	
	/**
	 * Attempts to apply the action by subtracting mana from the origin and
	 * then applying the result.
	 */
	protected boolean applyCost() {
		int manaCost = (int) cost;
		if (origin.getMP() < manaCost) {
			return false;
		} else {
			origin.loseMP(manaCost);
			return true;
		}
	}
	
	/**
	 * Sets the cost stat to a mana stat from a character.
	 *
	 * @param c The character to set the cost stat from.
	 */
	@Override
	protected void setCostStat(Character c) {
		costStat = c.getMPStat().clone();
	}
	
}