package yuuki.animation.engine;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import yuuki.animation.Animation;

/**
 * Controls a number of AnimationDrivers and manages their animation.
 */
public class AnimationManager implements Runnable {
	
	/**
	 * The amount of time, in milliseconds, that the animator thread sleeps
	 * for in between each check for additional animations.
	 */
	private static final int ANIMATOR_SLEEP_TIME = 10;
	
	/**
	 * Contains the animation that is to be added and the name of the driver
	 * that is to run it.
	 */
	private static class AnimationQueueItem {
		
		/**
		 * The animation that is to be added.
		 */
		public Animation anim = null;
		
		/**
		 * The name of the animation driver that the animation will be driven
		 * by.
		 */
		public String driver = null;
		
	}
	
	/**
	 * Removes an animation from a driver when it completes.
	 */
	private static class DriverEventHandler implements AnimationListener {
		
		/**
		 * Removes an Animation from its driver once its animation is complete.
		 * 
		 * @param e The animation event object.
		 */
		@Override
		public void animationComplete(AnimationEvent e) {
			Animation anim = (Animation) e.getSource();
			AnimationDriver driver = (AnimationDriver) e.getCause();
			driver.removeAnim(anim);
		}
		
	}
	
	/**
	 * Handles driver animation events.
	 */
	private DriverEventHandler handler = new DriverEventHandler();
	
	/**
	 * Starts an animation and blocks until the animation is complete.
	 * 
	 * @param animator The animator to use for animation.
	 * @param animation The animation to run.
	 * 
	 * @throws InterruptedException If the thread is interrupted while
	 * blocking.
	 */
	public static void animateAndWait(AnimationManager animator,
			Animation animation) throws InterruptedException {
		class AnimationRunner implements AnimationListener {
			public boolean complete = false;
			@Override
			public void animationComplete(AnimationEvent e) {
				this.complete = true;
			}
		};
		AnimationRunner l = new AnimationRunner();
		animation.addAnimationListener(l);
		animator.addAnimation(animation, null);
		while (true) {
			if (l.complete) {
				break;
			}
			Thread.sleep(ANIMATOR_SLEEP_TIME);
		}
	}
	
	/**
	 * The list of animations waiting to be added to an AnimationDriver.
	 */
	private Queue<AnimationQueueItem> animationQueue;
	
	/**
	 * Drives the actual animations.
	 */
	private Map<String, AnimationDriver> drivers;
	
	/**
	 * The FPS that all drivers run at.
	 */
	private final int fps;
	
	/**
	 * Creates a new Animator.
	 * 
	 * @param fps The speed to run animation at.
	 */
	public AnimationManager(int fps) {
		this.fps = fps;
		drivers = new HashMap<String, AnimationDriver>();
		animationQueue = new LinkedList<AnimationQueueItem>();
		createDriver(DEFAULT_DRIVER_NAME);
		(new Thread(this, "MasterAnimator")).start();
	}
	
	/**
	 * The name of the default animation driver.
	 */
	public static final String DEFAULT_DRIVER_NAME = "__DEFAULT";
	
	/**
	 * Creates a new driver in this animation manager. If a driver with the
	 * given name already exists, it is immediately deleted.
	 * 
	 * @param name The name of the driver.
	 */
	public void createDriver(String name) {
		if (drivers.containsKey(name)) {
			destroyDriver(name);
		}
		AnimationDriver ad = new AnimationDriver(fps);
		ad.addListener(handler);
		drivers.put(name, ad);
	}
	
	/**
	 * Destroys an animation driver. It is stopped and immediately removed.
	 * 
	 * @param name The name of the animation driver to destroy.
	 */
	public void destroyDriver(String name) {
		AnimationDriver driver = getDriver(name);
		driver.stop();
		driver.removeListener(handler);
		drivers.remove(name);
	}
	
	/**
	 * Adds an animation to an animation driver.
	 * 
	 * @param a The Animation to add.
	 * @param driver The name of the driver to add the animation to. Set this
	 * to null to use the default animation driver.
	 */
	public void addAnimation(Animation a, String driver) {
		AnimationQueueItem item = new AnimationQueueItem();
		item.anim = a;
		item.driver = driver;
		animationQueue.offer(item);
	}
	
	/**
	 * Gets a driver.
	 * 
	 * @param name The name of the driver. Set this to null to get the default
	 * driver.
	 */
	private AnimationDriver getDriver(String name) {
		name = (name != null) ? name : DEFAULT_DRIVER_NAME;
		return drivers.get(name);
	}
	
	/**
	 * Processes animations added to the queue.
	 */
	@Override
	public void run() {
		while (true) {
			if (!animationQueue.isEmpty()) {
				AnimationQueueItem item = animationQueue.poll();
				Animation a = item.anim;
				getDriver(item.driver).addAnim(a);
			}
			try {
				Thread.sleep(ANIMATOR_SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
