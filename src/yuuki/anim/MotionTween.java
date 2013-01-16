package yuuki.anim;

import yuuki.sprite.Sprite;

/**
 * Animates movement from one value to another.
 */
public class MotionTween extends Tween {
	
	/**
	 * The amount to move along the x-axis each step.
	 */
	private int xDistance;
	
	/**
	 * The amount to move along the y-axis each step.
	 */
	private int yDistance;

	/**
	 * Creates a new MotionTween.
	 * 
	 * @param sprite The Sprite to create the animation for.
	 * @param time The duration of the tween, in milliseconds.
	 * @param dx The total amount of movement along the x-axis.
	 * @param dy The total amount of movement along the y-axis.
	 */
	public MotionTween(Sprite sprite, long time, int dx, int dy) {
		super(sprite, time);
		xDistance = dx;
		yDistance = dy;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean propertiesAtTargets() {
		return (xDistance == 0 && yDistance == 0);
	}

	/**
	 * Moves the sprite by as much as has been requested. If this is the last
	 * advancement, the remainder is added to the movement.
	 * 
	 * @param fps The speed of animation, in frames per second.
	 */
	@Override
	protected void advanceTween(int fps) {
		double fpms = (double) fps / 1000;
		long remaining = getRemainingTime();
		int dx = (int) Math.round(xDistance / (fpms * remaining));
		int dy = (int) Math.round(yDistance / (fpms * remaining));
		dx = Math.min(dx, xDistance);
		dy = Math.min(dy, yDistance);
		xDistance -= dx;
		yDistance -= dy;
		sprite.move(dx, dy);
	}
	
}
