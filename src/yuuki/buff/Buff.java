/**
 * An effect that either boosts or reduces stats. Effects can either be passive
 * or active. Passive effects are applied during stat calculation, while active
 * effects are applied every time a player's turn comes up.
 */

package yuuki.buff;

import yuuki.entity.Character;

public abstract class Buff implements Cloneable {

	/**
	 * The amount of effect that this buff applies. This could be a multiplier
	 * or a discrete number.
	 */
	protected double effect;
	
	/**
	 * The Character that this Buff applies its effect to.
	 */
	protected Character target;
	
	/**
	 * The number of turns remaining that this Buff can be applied for.
	 */
	protected int turnsLeft;
	
	/**
	 * Whether or not this Buff has been activated.
	 */
	private boolean active;
	
	/**
	 * The name of this Buff. Used for display purposes.
	 */
	private String name;
	
	/**
	 * Creates a new Buff for a Character.
	 *
	 * @param name The display name of this Buff.
	 * @param effect The amount of effect that this Buff has.
	 * @param turns The number of turns that this Buff lasts for.
	 */
	public Buff(String name, double effect, int turns) {
		this.name = name;
		this.effect = effect;
		this.turnsLeft = turns;
		this.active = false;
	}
	
	/**
	 * Creates a clone of this Buff.
	 *
	 * @return The clone.
	 */
	public Buff clone() {
		Buff b = null;
		try {
			b = (Buff) super.clone();
		} catch(CloneNotSupportedException e) {
			e.printStackTrace();
		}
		// no deep-clone required
		return b;
	}
	
	/**
	 * Sets the target.
	 */
	public void setTarget(Character target) {
		this.target = target;
	}
	
	/**
	 * Gets the target.
	 *
	 * @return The target.
	 */
	public Character getTarget() {
		return this.target;
	}
	
	/**
	 * Applies this buff's effect to its target Character. The effects are
	 * applied by calling applyEffects(). If this is the first time this buff
	 * is being applied, applyActivationEffects() is also called. If this is
	 * the last time this buff can be applied as determined by the number of
	 * turns it lasts, applyDeactivationEffects() is also called.
	 *
	 * If this buff has already ended after being applied for as many times as
	 * it was initialized with turns, this method has no effect.
	 *
	 * @return True if this Buff is active after being applied to the target.
	 */
	public boolean apply() {
		checkApplication();
		checkDeactivation();
		// check activation last or one-turn buffs are immediately disabled
		checkActivation();
		return isActive();
	}
	
	/**
	 * Checks whether this buff has been activated.
	 *
	 * @return True if this Buff has been activated; false otherwise.
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * Checks whether this buff does not have any turns left.
	 *
	 * @return True if there are no turns left and buff is inactive; false
	 * otherwise.
	 */
	public boolean isExpired() {
		return (!isActive() && turnsLeft == 0);
	}
	
	/**
	 * Gets the amount of effect that this Buff has.
	 *
	 * @return The amount of effect.
	 */
	public double getEffect() {
		return effect;
	}
	
	/**
	 * Checks how many turns are left.
	 *
	 * @return The number of turns remaining for this Buff.
	 */
	public int getTurns() {
		return turnsLeft;
	}
	
	/**
	 * Gets this Buff's name.
	 *
	 * @return This Buff's name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Applies the per-turn effect of this Buff. Called every time this Buff is
	 * applied to its target and has turns left.
	 */
	protected abstract void applyEffect();
	
	/**
	 * Applies the initial effects of this Buff. Called when this Buff is
	 * applied for the first time.
	 */
	protected abstract void applyActivationEffect();
	
	/**
	 * Applies the final effects of this Buff. Called when this Buff is applied
	 * for the last time.
	 */
	protected abstract void applyDeactivationEffect();
	
	/**
	 * Called the first time this Buff is applied. Sets its state to active.
	 */
	private void activate() {
		active = true;
	}
	
	/**
	 * Called the last time this Buff is applied. Sets its state to inactive.
	 */
	private void deactivate() {
		active = false;
	}
	
	/**
	 * Activates this Buff and applies the activation effects. This method only
	 * has an effect if this Buff is inactive and if it has turns left.
	 */
	private void checkActivation() {
		if (!isActive() && turnsLeft > 0) {
			activate();
			applyActivationEffect();
		}
	}
	
	/**
	 * Applies this Buff's per-turn effects and reduces the turn count by one.
	 * This method only has an effect if this Buff is active.
	 */
	private void checkApplication() {
		if (isActive()) {
			applyEffect();
			turnsLeft--;
		}
	}
	
	/**
	 * Deactivates this Buff and applies the deactivation effects. This method
	 * only has an effect if this Buff is active and if it has no more turns
	 * left.
	 */
	private void checkDeactivation() {
		if (isActive() && turnsLeft == 0) {
			deactivate();
			applyDeactivationEffect();
		}
	}

}
