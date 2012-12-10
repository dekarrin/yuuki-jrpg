/**
 * A stat used by a Character.
 */

package yuuki.entity;

public class Stat implements Cloneable {

	/**
	 * The base value for calculating this Stat's effective value.
	 */
	private int base;
	
	/**
	 * The amount of this stat that is gained with every level.
	 */
	private int gain;
	
	/**
	 * How much to modify the effective value by.
	 */
	private double modifier;
	
	/**
	 * The name of this stat.
	 */
	private String name;
	
	/**
	 * Allocates a new Stat.
	 *
	 * @param name The name of this Stat.
	 * @param base The base value of the Stat.
	 * @param gain The amount this Stat gains every level.
	 */
	public Stat(String name, int base, int gain) {
		this.name = name;
		this.base = base;
		this.gain = gain;
		this.modifier = 1.0;
	}
	
	/**
	 * Creates a clone of this Stat.
	 *
	 * @return The clone.
	 */
	public Stat clone() {
		Stat clone = null;
		try {
			clone = (Stat) super.clone();
		} catch(CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}
	
	/**
	 * Checks whether or not this Stat has a current value that varies. Derived
	 * classes may override.
	 *
	 * @return false.
	 */
	public boolean hasCurrentValue() {
		return false;
	}
	
	/**
	 * Adds a modifier to this Stat. Modifiers change the final effective
	 * value.
	 *
	 * @param mod The amount of the modifier to add.
	 */
	public void addModifier(double mod) {
		modifier *= mod;
	}
	
	/**
	 * Removes a modifier from this Stat. This method has the exact same effect
	 * as addModifier(-mod).
	 *
	 * @param mod The amount of the modifier to remove.
	 */
	public void removeModifier(double mod) {
		modifier /= mod;
	}
	
	
	/**
	 * Gets this Stat's base value.
	 *
	 * @return The base value.
	 */
	public int getBaseValue() {
		return base;
	}
	
	/**
	 * Increases this Stat's base value.
	 *
	 * @param amount The amount to increase the base value by.
	 */
	public void increaseBase(int amount) {
		base += amount;
	}
	
	/**
	 * Gets the total calculated value of this Stat given a level.
	 *
	 * @param level The level of the Character that the Stat is being
	 * calculated for.
	 *
	 * @return The effective value of this Stat for the given level.
	 */
	public int getEffective(int level) {
		int effective = (base + (gain * level));
		effective = (int) Math.round(effective * modifier);
		return effective;
	}
	
	/**
	 * Gets the amount gained per level.
	 *
	 * @return Amount gained per level.
	 */
	public int getLevelGain() {
		return gain;
	}
	
	/**
	 * Gets the name of this stat.
	 *
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
}