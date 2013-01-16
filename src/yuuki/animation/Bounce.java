package yuuki.animation;

import yuuki.sprite.Sprite;

/**
 * Bounces a Sprite up and down and/or left and right.
 */
public class Bounce extends AnimationSequence {
	
	/**
	 * Creates a new Bounce.
	 * 
	 * @param sprite The Sprite to animate.
	 * @param The amount of time of one bounce, in milliseconds.
	 * @param xBounce The amount to bounce the sprite on the x-coordinate.
	 * Negative values start bouncing up, positive values start bouncing down.
	 * @param yBounce The amount to bounce the sprite on the y-coordinate.
	 * Negative values start bouncing left, positive values start bouncing
	 * right.
	 */
	public Bounce(Sprite sprite, long time, int xBounce, int yBounce) {
		super(sprite);
		addAnimation(new MotionTween(sprite, time / 2, xBounce, yBounce));
		addAnimation(new MotionTween(sprite, time / 2, -xBounce, -yBounce));
	}
	
}
