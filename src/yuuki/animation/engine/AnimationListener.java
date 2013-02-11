package yuuki.animation.engine;
/**
 * A listener for animation to be complete.
 */
public interface AnimationListener {
	
	/**
	 * Fired when the animation is complete.
	 * 
	 * @param e Information about the event that occurred.
	 */
	public void animationComplete(AnimationEvent e);
	
	/**
	 * Fired when the animation is stopped.
	 * 
	 * @param e Information about the event that occurred.
	 */
	public void animationStopped(AnimationEvent e);
	
}
