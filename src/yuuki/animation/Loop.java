package yuuki.animation;

/**
 * Continuously animates until explicitly stopped by calling halt() on an
 * instance.
 */
public class Loop extends Animation {
	
	/**
	 * The Animation to continuously run.
	 */
	private Animation loopedAnimation;
	
	/**
	 * Allocates a new Loop.
	 * 
	 * @param animation The Animation to continuously run. This should not be a
	 * Loop.
	 */
	public Loop(Animation animation) {
		super(animation.getSprite());
		loopedAnimation = animation;
	}
	
	@Override
	public void finish() {
		loopedAnimation.finish();
	}
	
	@Override
	public void pause() {
		loopedAnimation.pause();
	}
	
	@Override
	public void resetProperties() {
		loopedAnimation.reset();
	}
	
	@Override
	public void resume() {
		loopedAnimation.resume();
	}
	
	@Override
	public void start() {
		super.start();
		loopedAnimation.start();
	}
	
	@Override
	protected void advance(int fps) {
		loopedAnimation.advanceFrame(fps);
		if (loopedAnimation.isComplete()) {
			loopedAnimation.reset();
			loopedAnimation.start();
		}
	}
	
	/**
	 * Always returns false, because a Loop is never complete.
	 * 
	 * @return False.
	 */
	@Override
	protected boolean isAtEnd() {
		return false;
	}
	
}
