package yuuki.animation.engine;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Animates a number of objects.
 */
public class AnimationDriver implements Runnable, AnimationOwner {
	
	/**
	 * The thread running the animation.
	 */
	private Thread animationThread;
	
	/**
	 * The list of objects to animate.
	 */
	private List<Animatable> anims;
	
	/**
	 * The speed that animation is occurring at.
	 */
	private int fps;
	
	/**
	 * Allocates a new Animator.
	 * 
	 * @param fps The speed that the Animator is to animate at.
	 */
	public AnimationDriver(int fps) {
		anims = new CopyOnWriteArrayList<Animatable>();
		this.fps = fps;
		animationThread = null;
	}
	
	@Override
	public void addAnim(Animatable a) {
		if (a.isControlled()) {
			String error = "Animatable is already controlled!";
			throw new IllegalArgumentException(error);
		}
		a.setControlled(true);
		anims.add(a);
	}
	
	/**
	 * Gets the speed of animation in frames per second.
	 * 
	 * @return The speed of animation.
	 */
	public int getFps() {
		return fps;
	}
	
	/**
	 * Checks whether animation is currently occurring.
	 * 
	 * @return True if animation is occurring; false otherwise.
	 */
	public boolean isAnimating() {
		return (animationThread != null);
	}
	
	@Override
	public void removeAnim(Animatable a) {
		if (anims.remove(a)) {
			a.setControlled(false);
		}
	}
	
	/**
	 * Animates each of the items on the animation list, then sleeps for
	 * an appropriate amount of time depending on the parent's FPS.
	 */
	@Override
	public void run() {
		while (fps != 0) {
			int ms = (int) Math.round((double) 1000 / fps);
			try {
				Thread.sleep(ms);
			} catch (InterruptedException e) {
				break;
			}
			advanceAnimation();
		}
	}
	
	/**
	 * Sets the speed of animation in frames per second. It is not guaranteed
	 * that animation speed will change immediately if the FPS is changed
	 * during animation.
	 * 
	 * @param fps The new speed of animation.
	 */
	public void setFps(int fps) {
		this.fps = fps;
	}
	
	/**
	 * Starts animating the objects that this Animator controls. They are
	 * animated at a speed determined by this Animator's FPS.
	 */
	public void start() {
		if (!isAnimating()) {
			animationThread = new Thread(this, "AnimationDriver");
			animationThread.start();
		}
	}
	
	/**
	 * Stops animating objects.
	 */
	public void stop() {
		if (isAnimating()) {
			animationThread.interrupt();
			animationThread = null;
		}
	}
	
	/**
	 * Animates all objects in the animation list.
	 */
	private void advanceAnimation() {
		for (Animatable a : anims) {
			a.advanceFrame(fps);
		}
	}
	
}