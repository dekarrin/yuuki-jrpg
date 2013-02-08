package yuuki.animation;

import yuuki.sprite.Sprite;

/**
 * An animation that runs for a specified amount of time.
 */
public abstract class TimedAnimation extends Animation {
	
	/**
	 * The amount of time that it takes to complete the animation.
	 */
	private long duration;
	
	/**
	 * The time that animation started at.
	 */
	private long startTime;
	
	/**
	 * Creates a new TimedAnimation.
	 * 
	 * @param sprite The sprite being animated.
	 * @param time The amount of time in milliseconds that the animation takes.
	 */
	public TimedAnimation(Sprite sprite, long time) {
		super(sprite);
		duration = time;
		startTime = 0;
	}
	
	/**
	 * Immediately finishes animation.
	 */
	public void finish() {
		startTime = (System.currentTimeMillis() - duration - 1000);
	}
	
	@Override
	protected void advance(int fps) {
		if (isOnFirstPulse()) {
			startTime = System.currentTimeMillis();
		}
		advanceAnimation(fps);
	}
	
	/**
	 * Advances animation by one frame.
	 * 
	 * @param fps The speed of animation in frames per second.
	 */
	protected abstract void advanceAnimation(int fps);
	
	/**
	 * Gets the amount of time remaining in this animation.
	 * 
	 * @return The number of milliseconds until this animation is done.
	 */
	protected long getRemainingTime() {
		return (duration - (System.currentTimeMillis() - startTime));
	}
	
	/**
	 * Checks whether the animation is complete.
	 * 
	 * @return True if there is no more time left in this animation.
	 */
	@Override
	protected boolean isAtEnd() {
		return (getRemainingTime() <= 0);
	}
	
}
