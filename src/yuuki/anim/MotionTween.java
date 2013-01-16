package yuuki.anim;

import yuuki.sprite.Sprite;

/**
 * Animates movement from one value to another.
 */
public class MotionTween extends Tween {
	
	/**
	 * The amount left to move along the x-axis.
	 */
	private int xRemain;
	
	/**
	 * The amount left to move along the y-axis.
	 */
	private int yRemain;
	
	/**
	 * The total amount of movement along the x-axis.
	 */
	private int xTotal;
	
	/**
	 * The total amount of movement along the y-axis.
	 */
	private int yTotal;

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
		xTotal = xRemain = dx;
		yTotal = yRemain = dy;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean propertiesAtTargets() {
		return (xRemain == 0 && yRemain == 0);
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
		int dx = (int) Math.round(xRemain / (fpms * remaining));
		int dy = (int) Math.round(yRemain / (fpms * remaining));
		dx = (xRemain >= 0) ? Math.min(dx, xRemain) : Math.max(dx, xRemain);
		dy = (yRemain >= 0) ? Math.min(dy, yRemain) : Math.max(dy, yRemain);
		xRemain -= dx;
		yRemain -= dy;
		sprite.move(dx, dy);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void resetProperties() {
		xRemain = xTotal;
		yRemain = yTotal;
	}
	
}
