package yuuki.entity;

/**
 * A stat used by a Character. Fully thread-safe to enable the UI to establish
 * change listeners on a Stat.
 */
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
	private final String name;
	
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
	 * Adds a modifier to this Stat. Modifiers change the final effective
	 * value.
	 *
	 * @param mod The amount of the modifier to add.
	 */
	public synchronized void addModifier(double mod) {
		modifier *= mod;
	}
	
	/**
	 * Creates a clone of this Stat.
	 *
	 * @return The clone.
	 */
	@Override
	public synchronized Stat clone() {
		Stat clone = null;
		try {
			clone = (Stat) super.clone();
		} catch(CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}
	
	/**
	 * Checks whether two Stats are equal.
	 * 
	 * @param i2 The instance to check this one against.
	 * 
	 * @return Whether the two instances are equal.
	 */
	public synchronized boolean equals(Stat i2) {
		boolean sameName = (this.name == i2.name);
		boolean sameBase = (this.base == i2.base);
		boolean sameGain = (this.gain == i2.gain);
		boolean sameMod = (this.modifier == i2.modifier);
		return (sameName && sameBase && sameGain && sameMod);
	}
	
	/**
	 * Gets this Stat's base value.
	 *
	 * @return The base value.
	 */
	public synchronized int getBaseValue() {
		return base;
	}
	
	/**
	 * Gets the total calculated value of this Stat given a level.
	 *
	 * @param level The level of the Character that the Stat is being
	 * calculated for.
	 *
	 * @return The effective value of this Stat for the given level.
	 */
	public synchronized int getEffective(int level) {
		int effective = (base + (gain * level));
		effective = (int) Math.round(effective * modifier);
		return effective;
	}
	
	/**
	 * Gets the amount gained per level.
	 *
	 * @return Amount gained per level.
	 */
	public synchronized int getLevelGain() {
		return gain;
	}
	
	
	/**
	 * Gets the total modifier for this Stat.
	 * 
	 * The total modifier.
	 */
	public synchronized double getModifier() {
		return modifier;
	}
	
	/**
	 * Gets the name of this stat.
	 *
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Checks whether this Stat has a modifier.
	 * 
	 * @return True if the modifier multiplier is anything but 1.
	 */
	public synchronized boolean hasModifier() {
		return (modifier != 1.0);
	}
	
	/**
	 * Increases this Stat's base value.
	 *
	 * @param amount The amount to increase the base value by.
	 */
	public synchronized void increaseBase(int amount) {
		base += amount;
	}
	
	/**
	 * Removes a modifier from this Stat. This method has the exact same effect
	 * as addModifier(-mod).
	 *
	 * @param mod The amount of the modifier to remove.
	 */
	public synchronized void removeModifier(double mod) {
		modifier /= mod;
		if (Math.abs(1.0 - modifier) <= 0.1) { // fixes precision issues
			modifier = 1.0;
		}
	}
	
}