package yuuki.anim;

/**
 * A listener for animation to be complete.
 */
public interface AnimationListener {
	
	/**
	 * Fired when the animation is complete.
	 * 
	 * @param animation The animation that is complete.
	 */
	public void animationComplete(Animation animation);
	
}
