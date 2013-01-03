package yuuki.action;

import yuuki.entity.Character;

/**
 * The basic attack. This is a zero-cost skill action that does some damage to
 * a single target. This Action is only applied to the first target.
 */
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
	 * Creates a BasicAttack from an existing one.
	 * 
	 * @param args Must contain a single double that is damage.
	 */
	@Override
	public BasicAttack createInstance(String[] args) {
		double d = Double.parseDouble(args[0]);
		return new BasicAttack(d);
	}
	
	/**
	 * Has no effect.
	 */
	@Override
	protected void applyBuffs() {}
	
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
	 * Sets the effect stat from a character.
	 *
	 * @param c The character to set it from.
	 */
	@Override
	protected void setEffectStat(Character c) {
		effectStat = c.getHPStat().clone();
	}
	
}
