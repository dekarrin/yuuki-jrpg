package yuuki.action;

import java.util.Random;

import yuuki.entity.Character;

/**
 * Attempts to escape from the current battle.
 */
public class Flee extends Action implements Cloneable {
	
	/**
	 * Allocates a new Flee instance.
	 */
	public Flee() {
		super("flee", 0.0, 0.0, null, null);
	}
	
	/**
	 * Creates a clone of this Flee.
	 * 
	 * @return The clone.
	 */
	@Override
	public Flee clone() {
		return (Flee) super.clone();
	}
	
	/**
	 * Creates an instance from an existing Flee.
	 * 
	 * @param args Not used; may be empty.
	 */
	@Override
	public Flee createInstance(String[] args) {
		return new Flee();
	}
	
	/**
	 * Sets the origin of this Flee. In addition, its target is immediately set
	 * to be the same Character.
	 * 
	 * @param performer The fighter to set as the origin of this attack.
	 */
	@Override
	public void setOrigin(Character performer) {
		super.setOrigin(performer);
		super.addTarget(performer);
	}
	
	/**
	 * Determines whether the performer may flee based on the agility of the
	 * entities on the field.
	 * 
	 * @return True if the character can escape; false otherwise.
	 */
	private boolean calculateFlee()	{
		boolean flee = false;
		Random rand;
		int playerLevel = origin.getLevel();
		int monsterLevel = 0;
		for (Character c: targets) {
			monsterLevel += c.getLevel();
		}
		double advantageLevel = (double) playerLevel / monsterLevel;
		int randomNumber;
		rand = new Random();
		//Get Character agility level.
		int agility = origin.getAgility();
		advantageLevel += (agility * 0.2);
		if (advantageLevel == 1) {
			randomNumber = rand.nextInt(2) + 1;
			if (randomNumber == 1) {
				flee = true;
			} else {
				flee = false;
			}
		} else if (advantageLevel > 1) {
			flee = true;
		} else if (advantageLevel < 0.1) {
			flee = false;
		} else {
			double escapeChance = rand.nextDouble();
			flee = (escapeChance <= advantageLevel);
		}	
		return flee;
	}
	
	/**
	 * Has no effect.
	 */
	@Override
	protected void applyBuffs() {}
	
	/**
	 * Does not actually apply any cost, but determines whether the flee was
	 * successful based on the agility of the performer and the opposing teams.
	 * 
	 * @return True if the character can escape; false otherwise.
	 */
	@Override
	protected boolean applyCost() {
		return calculateFlee();
	}
	
	/**
	 * Has no effect.
	 */
	@Override
	protected void applyEffect() {}
	
	/**
	 * Has no effect.
	 */
	@Override
	protected void setCostStat(Character c) {}
	
	/**
	 * Has no effect.
	 */
	@Override
	protected void setEffectStat(Character c) {}
	
}