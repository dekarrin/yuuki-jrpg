package yuuki.anim;

/**
 * Provides methods for animating an object.
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
	
}
