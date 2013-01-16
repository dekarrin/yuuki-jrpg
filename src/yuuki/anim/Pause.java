package yuuki.anim;

import yuuki.sprite.Sprite;

/**
 * An animation that deliberately does nothing. This can be used to fill up
 * space in an AnimationSequence.
 */
public class Pause extends TimedAnimation {
	
	/**
	 * Creates a new Pause.
	 * 
	 * @param sprite The sprite that is being animated.
	 * @param time The length of time that the pause should last.
	 */
	public Pause(Sprite sprite, long time) {
		super(sprite, time);
	}

	/**
	 * Has no effect, as a pause does not do anything.
	 */
	@Override
	protected void advanceAnimation(int fps) {}
	
	/**
	 * Has no effect.
	 */
	@Override
	protected void resetProperties() {}
	
}
