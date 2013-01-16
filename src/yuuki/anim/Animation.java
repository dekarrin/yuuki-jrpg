package yuuki.anim;

import java.util.HashSet;
import java.util.Set;

import yuuki.sprite.Sprite;

/**
 * Drives the animation of a single Sprite. An Animation has parameters for a
 * single type of animation for a single Sprite. It must itself be driven by
 * some other AnimationOwner.
 */
public abstract class Animation implements Animatable {
	
	/**
	 * Whether the animation ended event has been fired.
	 */
	private boolean endEventFired;

	/**
	 * Whether this Animation has a driver.
	 */
	private boolean controlled;
	
	/**
	 * Whether this Animation is being forced to end.
	 */
	private boolean forcedComplete;
	
	/**
	 * Whether this animation is on its first pulse.
	 */
	private boolean firstPulse;
	
	/**
	 * The sprite that this Animation has the parameters for animating.
	 */
	protected Sprite sprite;
	
	/**
	 * The objects listening for the animation to be complete.
	 */
	private Set<AnimationListener> listeners;
	
	/**
	 * Allocates a new Animation.
	 * 
	 * @param sprite The Sprite that this Animation animates.
	 */
	public Animation(Sprite sprite) {
		this.sprite = sprite;
		this.listeners = new HashSet<AnimationListener>();
		this.controlled = false;
		this.forcedComplete = false;
		this.endEventFired = false;
		this.firstPulse = true;
	}
	
	/**
	 * Forces the animation to stop. The next time animation is attempted to be
	 * driven on this Animation, it will immediately complete.
	 */
	public void halt() {
		forcedComplete = true;
	}
	
	/**
	 * Whether animation has started.
	 * 
	 * @return True if advanceFrame() has already been called on this Animation
	 * at least once and if the animation is not complete; otherwise, false.
	 */
	public boolean isRunning() {
		return !(isOnFirstPulse() || isComplete());
	}
	
	/**
	 * Whether the animation is on its first pulse.
	 * 
	 * @return True if advance() has already been called at least once before;
	 * otherwise, false.
	 */
	protected boolean isOnFirstPulse() {
		return firstPulse;
	}
	
	/**
	 * Adds a listener to this Animation if it hasn't already been added.
	 * 
	 * @param l The listener to add.
	 */
	public void addListener(AnimationListener l) {
		listeners.add(l);
	}
	
	/**
	 * Removes a listener from this Animation if it has been added.
	 * 
	 * @param l The listener to remove.
	 */
	public void removeListener(AnimationListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Checks whether this animation has reached the end.
	 * 
	 * @return True if the animation has run to the end; otherwise, false.
	 */
	protected abstract boolean isAtEnd();
	
	/**
	 * Advances the animation by one frame. The method isComplete() is checked
	 * before advance() is called, and if isComplete() returns true, advance()
	 * will not be called.
	 * 
	 * @param fps The speed of animation.
	 */
	protected abstract void advance(int fps);
	
	/**
	 * Calls animationComplete on all listeners.
	 */
	private void fireAnimationComplete() {
		AnimationListener[] list = listeners.toArray(new AnimationListener[0]);
		for (AnimationListener l: list) {
			l.animationComplete(this);
		}
	}
	
	/**
	 * Restores this animation to its beginning so it is ready to play again.
	 * This will not restore the original properties of the animated sprite.
	 */
	public void reset() {
		forcedComplete = false;
		endEventFired = false;
		firstPulse = true;
		resetProperties();
	}
	
	/**
	 * Resets the properties of this animation back to their original values so
	 * that the animation can be run again.
	 */
	protected abstract void resetProperties();
	
	/**
	 * Gets this Animation's Sprite.
	 * 
	 * @return The sprite.
	 */
	public Sprite getSprite() {
		return sprite;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void advanceFrame(int fps) {
		if (!isComplete()) {
			advance(fps);
			firstPulse = false;
		} else if (!endEventFired) {
			fireAnimationComplete();
			endEventFired = true;
		}
	}
	
	/**
	 * Checks whether this animation is done running.
	 * 
	 * @param True if the animation is no longer running; false otherwise.
	 */
	public boolean isComplete() {
		return (isAtEnd() || forcedComplete);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isControlled() {
		return controlled;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setControlled(boolean controlled) {
		this.controlled = controlled;
	}	
	
}
