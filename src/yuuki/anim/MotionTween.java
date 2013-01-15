package yuuki.anim;

import yuuki.sprite.Sprite;

/**
 * Animates movement from one value to another.
 */
public class MotionTween extends Tween {
	
	/**
	 * The amount to move along the x-axis each step.
	 */
	private int dx;
	
	/**
	 * The amount to move along the y-axis each step.
	 */
	private int dy;
	
	/**
	 * The remainder of total movement along the x-axis divided by steps.
	 */
	private int rx;
	
	/**
	 * The remainder of total movement along the y-axis divided by steps.
	 */
	private int ry;

	/**
	 * Creates a new MotionTween.
	 * 
	 * @param sprite The Sprite to create the animation for.
	 * @param steps The number of steps to complete the tween in.
	 * @param dx The total amount of movement along the x-axis.
	 * @param dy The total amount of movement along the y-axis.
	 */
	public MotionTween(Sprite sprite, int steps, int dx, int dy) {
		super(sprite, steps);
		this.dx = dx / getRemainingSteps();
		this.dy = dy / getRemainingSteps();
		rx = dx % getRemainingSteps();
		ry = dy % getRemainingSteps();
	}

	/**
	 * Moves the sprite by as much as has been requested. If this is the last
	 * advancement, the remainder is added to the movement.
	 * 
	 * @param fps The speed of animation, in frames per second.
	 */
	@Override
	protected void advanceTween(int fps) {
		int totalDx = dx;
		int totalDy = dy;
		if (getRemainingSteps() == 1) {
			totalDx += rx;
			totalDy += ry;
		}
		sprite.move(totalDx, totalDy);
	}
	
}
