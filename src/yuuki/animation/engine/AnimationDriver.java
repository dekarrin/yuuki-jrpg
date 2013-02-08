package yuuki.animation.engine;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import yuuki.animation.Animation;

/**
 * Animates a number of objects.
 */
public class AnimationDriver implements Runnable, AnimationOwner {
	
	/**
	 * Listens for animation events and sends them to the listeners on this
	 * driver.
	 */
	private class EventHandler implements AnimationListener {
		
		@Override
		public void animationComplete(AnimationEvent e) {
			AnimationEvent firedEvent = getRefiredEvent(e);
			fireAnimationComplete(firedEvent);
		}
		
		/**
		 * Gets the AnimationEvent that adds this AnimationDriver as the cause
		 * of the event.
		 * 
		 * @param e The event to be fired.
		 * 
		 * @return An AnimationEvent with the same source as the given event
		 * and the cause set to this AnimationDriver.
		 */
		private AnimationEvent getRefiredEvent(AnimationEvent e) {
			AnimationEvent firedEvent;
			Animation source = (Animation) e.getSource();
			AnimationDriver cause = AnimationDriver.this;
			firedEvent = new AnimationEvent(source, cause);
			return firedEvent;
		}
		
	}
	
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
	 * Handles animation events.
	 */
	private EventHandler handler = new EventHandler();
	
	/**
	 * Listeners for animation events.
	 */
	private Set<AnimationListener> listeners;
	
	/**
	 * Allocates a new Animator.
	 * 
	 * @param fps The speed that the Animator is to animate at.
	 */
	public AnimationDriver(int fps) {
		anims = new CopyOnWriteArrayList<Animatable>();
		listeners = new CopyOnWriteArraySet<AnimationListener>();
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
		a.addAnimationListener(handler);
		anims.add(a);
	}
	
	/**
	 * Adds a listener to this driver.
	 * 
	 * @param l The listener to add.
	 */
	public void addListener(AnimationListener l) {
		listeners.add(l);
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
			a.removeAnimationListener(handler);
		}
	}
	
	/**
	 * Removes a listener from this driver.
	 * 
	 * @param l The listener to remove.
	 */
	public void removeListener(Object l) {
		listeners.remove(l);
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
	
	/**
	 * Calls animationComplete() on all listeners.
	 * 
	 * @param e The original event.
	 */
	private void fireAnimationComplete(AnimationEvent e) {
		AnimationListener[] ls = new AnimationListener[listeners.size()];
		listeners.toArray(ls);
		for (AnimationListener l : ls) {
			l.animationComplete(e);
		}
	}
	
}