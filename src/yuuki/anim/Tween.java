package yuuki.anim;

import yuuki.sprite.Sprite;

/**
 * Animates some property of a sprite by interpolating values given a current
 * value and an end value.
 */
public abstract class Tween extends Animation {

	/**
	 * The number of steps remaining in this Tween.
	 */
	private int remainingSteps;
	
	/**
	 * Creates a new Tween.
	 * 
	 * @param sprite The Sprite to create the animation for.
	 * @param steps The number of steps that this Tween should last.
	 */
	public Tween(Sprite sprite, int steps) {
		super(sprite);
		this.remainingSteps = steps;
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
		remainingSteps--;
	}
	
	/**
	 * Checks whether there are any more steps left in this animation.
	 * 
	 * @return True if the animation has run for as many steps as have been
	 * requested; false otherwise.
	 */
	@Override
	protected boolean isComplete() {
		return (remainingSteps == 0);
	}
	
	/**
	 * Gets the number of steps remaining in this Tween.
	 * 
	 * @return The number of remaining steps.
	 */
	protected int getRemainingSteps() {
		return remainingSteps;
	}
	
}
