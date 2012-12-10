/**
 * The basic attack. This is a zero-cost skill action that does some damage to
 * a single target. This Action is only applied to the first target.
 */

package yuuki.action;

import yuuki.entity.Character;

public class BasicAttack extends Skill implements Cloneable {

	/**
	 * Creates a new BasicAttack.
	 *
	 * @param damage The base damage of the attack.
	 */
	public BasicAttack(double damage) {
		super("attack", damage, 0.0, null, null);
	}
	
	/**
	 * Creates a clone of this BasicAttack.
	 *
	 * @return The clone.
	 */
	@Override
	public BasicAttack clone() {
		return (BasicAttack) super.clone();
	}
	
	/**
	 * Applies damage to the first target.
	 */
	@Override
	protected void applyEffect() {
		Character target = targets.get(0);
		int oStr = origin.getStrength();
		int tDef = target.getDefense();
		double mod = (oStr / tDef);
		int totalDamage = (int) Math.round(effect + mod);
		target.loseHP(totalDamage);
		actualEffects[0] = totalDamage;
	}
	
	/**
	 * Has no effect.
	 */
	@Override
	protected void applyBuffs() {}
	
	/**
	 * Sets the effect stat from a character.
	 *
	 * @param c The character to set it from.
	 */
	protected void setEffectStat(Character c) {
		effectStat = c.getHPStat().clone();
	}

}