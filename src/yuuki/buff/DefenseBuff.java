package yuuki.buff;

/**
 * A Buff on the Character's defense. A simple multiplying buff.
 */
public class DefenseBuff extends PassiveBuff implements Cloneable {
	
	/**
	 * Creates a new Buff for a Character.
	 *
	 * @param name The display name of this Buff.
	 * @param target The Character that the Buff is on.
	 * @param effect The factor that defense is buffed by.
	 * @param turns The number of turns that this Buff lasts for.
	 */
	public DefenseBuff(String name, double effect, int turns) {
		super(name, effect, turns);
	}
	
	/**
	 * Creates a clone of this DefenseBuff.
	 *
	 * @return The clone.
	 */
	@Override
	public DefenseBuff clone() {
		return (DefenseBuff) super.clone();
	}
	
	@Override
	protected void applyActivationEffect() {
		target.addDefenseMod(effect);
	}
	
	@Override
	protected void applyDeactivationEffect() {
		target.removeDefenseMod(effect);
	}
	
}
