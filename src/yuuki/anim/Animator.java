package yuuki.anim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Animates a number of objects.
 */
public class Animator implements Runnable {
	
	/**
	 * The speed that animation is occurring at.
	 */
	private int fps;
	
	/**
	 * The list of objects to animate.
	 */
	private List<Animatable> anims;
	
	/**
	 * The thread running the animation.
	 */
	private Thread animationThread;
	
	/**
	 * Allocates a new Animator.
	 * 
	 * @param fps The speed that the Animator is to animate at.
	 */
	public Animator(int fps) {
		anims = Collections.synchronizedList(new ArrayList<Animatable>());
		this.fps = fps;
		animationThread = null;
	}
	
	/**
	 * Animates each of the items on the animation list, then sleeps for
	 * an appropriate amount of time depending on the parent's FPS.
	 */
	public void run() {
		while (fps != 0) {
			int ms = (int) Math.round((double) 1 / fps);
			try {
				Thread.sleep(ms);
			} catch (InterruptedException e) {
				break;
			}
			advanceAnimation();
		}
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
	 * Sets the speed of animation in frames per second. It is not gaurenteed
	 * that animation speed will change immediately if the FPS is changed
	 * during animation.
	 * 
	 * @param fps The new speed of animation.
	 */
	public void setFps(int fps) {
		this.fps = fps;
	}
	
	/**
	 * Adds an Animatable to the list of animated objects.
	 * 
	 * @param a The animatable to add.
	 * 
	 * @throws IllegalArgumentException If the Animatable already has an owner.
	 */
	public void addAnimatable(Animatable a) {
		if (a.isControlled()) {
			String error = "Animatable is already controlled!";
			throw new IllegalArgumentException(error);
		}
		a.setControlled(true);
		anims.add(a);
	}
	
	/**
	 * Removes an Animatable from the list of animated objects.
	 * 
	 * @param a The animatable to remove.
	 */
	public void removeAnimatable(Animatable a) {
		if (anims.remove(a)) {
			a.setControlled(false);
		}
	}
	
	/**
	 * Starts animating the objects that this Animator controls. They are
	 * animated at a speed determined by this Animator's FPS.
	 */
	public void start() {
		if (!isAnimating()) {
			animationThread = new Thread(this, "Animator");
			animationThread.start();
		}
	}
	
	/**
	 * Checks whether animation is currently occurring.
	 * 
	 * @return True if animation is occurring; false otherwise.
	 */
	public boolean isAnimating() {
		return (animationThread != null);
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
		synchronized (anims) {
			for (Animatable a: anims) {
				a.advanceFrame(fps);
			}
		}
	}

}