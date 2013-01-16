package yuuki.anim;

import yuuki.sprite.Sprite;

/**
 * Animates some property of a sprite by interpolating values given a current
 * value and an end value. Tween automatically makes up for lost time caused by
 * animation speed changing and long pauses between animation pulses. Note that
 * this means that if a Tween that is to last 5 seconds is pulsed with a call
 * to advanceFrame() and 6 seconds later is pulsed again, the animation will
 * instantly complete.
 * 
 * Tween does not guarantee that the animation will happen in exactly the
 * specified time. If the time is up, and the tween still hasn't completed, it
 * will jump the remaining amount on the next pulse after time is up.
 */
public abstract class Tween extends Animation {

	/**
	 * The amount of time that this Tween takes to complete its animation.
	 */
	private long duration;
	
	/**
	 * The time that animation started at.
	 */
	private long startTime;
	
	/**
	 * Creates a new Tween.
	 * 
	 * @param sprite The Sprite to create the animation for.
	 * @param time The length of time in milliseconds that this Tween should
	 * last.
	 */
	public Tween(Sprite sprite, long time) {
		super(sprite);
		this.duration = time;
		this.startTime = 0;
	}
	
	/**
	 * Advances the animation by as much as has been requested.
	 * 
	 * @param fps The speed of animation, in frames per second.
	 */
	protected abstract void advanceTween(int fps);
	
	/**
	 * {@inheritDoc}
	 */
	protected void advance(int fps) {
		advanceTween(fps);
	}
	
	/**
	 * Checks whether there is any more time left in this animation and
	 * whether the tween is complete.
	 * 
	 * @return True if the animation has run for the requested time and the
	 * tween is over.
	 */
	@Override
	public boolean isComplete() {
		return (getRemainingTime() <= 0 && propertiesAtTargets());
	}
	
	/**
	 * Checks whether the tween is complete.
	 * 
	 * @return True if the tweened properties are at their target values;
	 * otherwise, false.
	 */
	protected abstract boolean propertiesAtTargets();
	
	/**
	 * Gets the amount of time remaining in this Tween.
	 * 
	 * @return The number of milliseconds until this Tween is done.
	 */
	protected long getRemainingTime() {
		return (duration - (System.currentTimeMillis() - startTime));
	}
	
}
