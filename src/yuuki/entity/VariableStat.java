/**
 * A Stat that has a variable current value. The total effective value is used
 * as the maximum value for the current value.
 */

package yuuki.entity;

public class VariableStat extends Stat implements Cloneable {

	/**
	 * The current value of this Stat.
	 */
	private int currentValue;
	
	/**
	 * Allocates a new VariableStat. The current value is immediately set to 0.
	 *
	 * @param name The name of this VariableStat.
	 * @param base The base value of this VariableStat.
	 * @param gain The amount this VariableStat gains every level.
	 */
	public VariableStat(String name, int base, int gain) {
		super(name, base, gain);
		currentValue = 0;
	}
	
	/**
	 * Creates a clone of this VariableStat.
	 *
	 * @return The clone.
	 */
	public VariableStat clone() {
		return (VariableStat) super.clone();
	}
	
	/**
	 * Adds a modifier and changes the current value so that it is at the same
	 * percentage of effective as it was before the modifier was applied. It is
	 * rounded to the nearest integer, but it will always be at least 1.
	 *
	 * @param mod The amount of the modifier to add.
	 * @param level The level of the Character that the stat is on.
	 */
	public void addModifier(double mod, int level) {
		double percent = currentValue / getMax(level);
		addModifier(mod);
		currentValue = (int) Math.round(getMax(level) * percent);
		currentValue = (currentValue >= 1) ? currentValue : 1;
	}
	
	/**
	 * Removes a modifier and changes the current value so that it is at the
	 * same percentage of effective as it was before the modifier was applied.
	 * It is rounded to the nearest integer, but it will always be at least 1.
	 *
	 * @param mod The amount of the modifier to add.
	 * @param level The level of the Character that the stat is on.
	 */
	public void removeModifier(double mod, int level) {
		double percent = currentValue / getMax(level);
		removeModifier(mod);
		currentValue = (int) Math.round(getMax(level) * percent);
		currentValue = (currentValue >= 1) ? currentValue : 1;
	}
	
	/**
	 * Gets the current value of this Stat.
	 *
	 * @return The current value.
	 */
	public int getCurrent() {
		return currentValue;
	}
	
	/**
	 * Increases the current value of this Stat. The current value will not be
	 * increased past the effective value. If the amount given is greater than
	 * the difference between the effective value and the current value, the
	 * current value is increased only to the effective value.
	 *
	 * @param amount The amount to increase the current value by.
	 * @param level The level of the Character the stat is on.
	 *
	 * @return True if the current value increased by the full amount given;
	 * otherwise, false.
	 */
	public boolean gain(int amount, int level) {
		int maxAmount = getMax(level) - currentValue;
		boolean amountInBounds = (amount <= maxAmount);
		currentValue += (amountInBounds ? amount : maxAmount);
		return amountInBounds;
	}
	
	/**
	 * Decreases the current value of this Stat. The current value will not be
	 * decreased to a value lower than 0. If the amount given is greater than
	 * the current value, the current value is decreased only to 0.
	 *
	 * @param amount The amount to decrease the current value by.
	 *
	 * @return True if the current value decreased by the full amount given;
	 * otherwise, false.
	 */
	public boolean lose(int amount) {
		boolean amountInBounds = (amount <= currentValue);
		currentValue -= (amountInBounds ? amount : currentValue);
		return amountInBounds;
	}
	
	/**
	 * Sets the current value to the effective level.
	 *
	 * @param level The level of the Character the stat is on.
	 */
	public void restore(int level) {
		currentValue = getMax(level);
	}
	
	/**
	 * Sets the current value to 0.
	 */
	public void drain() {
		currentValue = 0;
	}
	
	/**
	 * Gets the maximum value for this VariableStat.
	 *
	 * @param level The level of the Character the stat is on.
	 *
	 * @return The effective value.
	 */
	public int getMax(int level) {
		return getEffective(level);
	}
}