package yuuki.animation.engine;

/**
 * Provides methods for animating an object. Animatable instances are added to
 * Animators to have their animation driven. Animatable instances may only have
 * one owner Animator.
 */
public interface Animatable {
	
	/**
	 * Adds a listener for AnimationEvents.
	 * 
	 * @param l The listener to add.
	 */
	public void addAnimationListener(AnimationListener l);
	
	/**
	 * Advances animation by one frame.
	 * 
	 * @param fps The speed in frames-per-second that the controlling object is
	 * animating at. This can be used for motion calculation so that apparent
	 * speed does not depend on FPS.
	 */
	public void advanceFrame(int fps);
	
	/**
	 * Immediately jumps the animation to its finished position, if there is a
	 * finished position.
	 */
	public void finish();
	
	/**
	 * Checks whether this Animatable is being driven by another object.
	 * 
	 * @return True if this Animatable is already being animated by some
	 * object; otherwise, false.
	 */
	public boolean isControlled();
	
	/**
	 * Pauses the animation clock, if there is one.
	 */
	public void pause();
	
	/**
	 * Removes a listener for AnimationEvents.
	 * 
	 * @param l The listener to remove.
	 */
	public void removeAnimationListener(Object l);
	
	/**
	 * Resets this animation to its initial position.
	 */
	public void reset();
	
	/**
	 * Resumes the animation clock, if there is one.
	 */
	public void resume();
	
	/**
	 * Sets whether this Animatable is being driven by another object.
	 * 
	 * @param controlled Whether this Animatable is being animated.
	 */
	public void setControlled(boolean controlled);
	
	/**
	 * Starts the animation.
	 */
	public void start();
	
	/**
	 * Forces the animation to stop. The next time animation is attempted to be
	 * driven on this Animatable, it will immediately complete.
	 */
	public void stop();
	
}
