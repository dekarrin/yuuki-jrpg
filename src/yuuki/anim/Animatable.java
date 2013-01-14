package yuuki.anim;

/**
 * Provides methods for animating an object. Animatable instances are added to
 * Animators to have their animation driven. Animatable instances may only have
 * one owner Animator.
 */
public interface Animatable {
	
	/**
	 * Advances animation by one frame.
	 * 
	 * @param fps The speed in frames-per-second that the controlling object is
	 * animating at. This can be used for motion calculation so that apparent
	 * speed does not depend on FPS.
	 */
	public void advanceFrame(int fps);
	
	/**
	 * Checks whether this Animatable is being driven by another object.
	 * 
	 * @return True if this Animatable is already being animated by some
	 * object; otherwise, false.
	 */
	public boolean isControlled();
	
	/**
	 * Sets whether this Animatable is being driven by another object.
	 * 
	 * @param controlled Whether this Animatable is being animated.
	 */
	public void setControlled(boolean controlled);
	
}
