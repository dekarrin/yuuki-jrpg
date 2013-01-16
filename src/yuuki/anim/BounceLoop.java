package yuuki.anim;

import yuuki.sprite.Sprite;

/**
 * Bounces a Sprite up and down and/or left and right.
 */
public class BounceLoop extends Loop {

	/**
	 * The amount to bounce on the x-axis. Negative values bounce up.
	 */
	private int xBounce;
	
	/**
	 * The amount to bounce on the y-axis. Negative values bounce left.
	 */
	private int yBounce;
	
	/**
	 * Whether the current bounce is the inverse of the first bounce.
	 */
	private boolean usingInverseBouncer;
	
	/**
	 * The duration of one half of a bounce cycle.
	 */
	private long time;
	
	/**
	 * The animation that is actually driving the bounce.
	 */
	private Animation bouncer;
	
	/**
	 * Creates a new BounceLoop.
	 * 
	 * @param sprite The Sprite to animate.
	 * @param The amount of time of one bounce cycle, in milliseconds.
	 * @param xBounce The amount to bounce the sprite on the x-coordinate.
	 * Negative values start bouncing up, positive values start bouncing down.
	 * @param yBounce The amount to bounce the sprite on the y-coordinate.
	 * Negative values start bouncing left, positive values start bouncing
	 * right.
	 */
	public BounceLoop(Sprite sprite, long time, int xBounce, int yBounce) {
		super(sprite);
		this.xBounce = xBounce;
		this.yBounce = yBounce;
		this.time = time / 2;
		usePrimaryBouncer();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void advance(int fps) {
		bouncer.advanceFrame(fps);
		if (bouncer.isComplete()) {
			if (usingInverseBouncer) {
				usePrimaryBouncer();
			} else {
				useInverseBouncer();
			}
		}
	}
	
	/**
	 * Creates and uses the MotionTween for the initial bounce.
	 */
	private void usePrimaryBouncer() {
		bouncer = new MotionTween(sprite, time, xBounce, yBounce);
		usingInverseBouncer = false;
	}
	
	/**
	 * Creates and uses the MotionTween for the alternate bounce.
	 */
	private void useInverseBouncer() {
		bouncer = new MotionTween(sprite, time, -xBounce, -yBounce);
		usingInverseBouncer = true;
	}
	
}
