package yuuki.animation.engine;
/**
 * A listener for animation to be complete.
 */
public interface AnimationListener {
	
	/**
	 * Fired when the animation is complete.
	 * 
	 * @param e Information on the event that occurred.
	 */
	public void animationComplete(AnimationEvent e);
	
}
