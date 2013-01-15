package yuuki.anim;

import java.util.LinkedList;

/**
 * Continuously runs animations.
 */
public class Animator implements Runnable, AnimationListener {
	
	/**
	 * The amount of time, in milliseconds, that the animator thread sleeps
	 * for in between each check for additional animations.
	 */
	private static final int ANIMATOR_SLEEP_TIME = 10;
	
	/**
	 * Drives the actual animations.
	 */
	private AnimationDriver driver;
	
	/**
	 * The list of animations that are to be run.
	 */
	private LinkedList<Animation> animations;
	
	/**
	 * Creates a new Animator.
	 * 
	 * @param fps The speed to run animation at.
	 */
	public Animator(int fps) {
		driver = new AnimationDriver(fps);
		animations = new LinkedList<Animation>();
		(new Thread(this, "MasterAnimator")).start();
	}
	
	/**
	 * Adds an animation to the queue.
	 * 
	 * @param a The Animation to add.
	 */
	public void addAnimation(Animation a) {
		animations.offer(a);
	}
	
	/**
	 * Processes animations added to the queue.
	 */
	public void run() {
		while (true) {
			if (!animations.isEmpty()) {
				Animation a = animations.poll();
				a.addListener(this);
				driver.addAnim(a);
			}
			try {
				Thread.sleep(ANIMATOR_SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Removes an Animation from the driver once its animation is complete.
	 * 
	 * @param a the animation that is complete.
	 */
	public void animationComplete(Animation a) {
		a.removeListener(this);
		driver.removeAnim(a);
	}
	
}
