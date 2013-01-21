package yuuki.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yuuki.sprite.Sprite;

/**
 * Animates some property of a sprite by interpolating values given a current
 * value and an end value. Tween automatically makes up for lost time caused by
 * animation speed changing and long pauses between animation pulses. Note that
 * this means that if a Tween that is to last 5 seconds is pulsed with a call
 * to advanceFrame() and 6 seconds later is pulsed again, the animation will
 * instantly complete.
 * 
 * Tween does not guarantee that the animation will happen in exactly the
 * specified time. If the time is up, and the tween still hasn't completed, it
 * will jump the remaining amount on the next pulse after time is up.
 */
public abstract class Tween extends TimedAnimation {
	
	/**
	 * The amount left to change each property by.
	 */
	private List<Integer> propertyRemainings;
	
	/**
	 * The total amount to change each property by.
	 */
	private List<Integer> propertyTotals;
	
	/**
	 * Maps property indexes to property names.
	 */
	private Map<Integer, String> propertyNames;
	
	/**
	 * Creates a new Tween.
	 * 
	 * @param sprite The Sprite to create the animation for.
	 * @param time The length of time in milliseconds that this Tween should
	 * last.
	 */
	public Tween(Sprite sprite, long time) {
		super(sprite, time);
		propertyRemainings = new ArrayList<Integer>();
		propertyTotals = new ArrayList<Integer>();
		propertyNames = new HashMap<Integer, String>();
	}
	
	/**
	 * Gets the amount that a property should change.
	 * 
	 * @param prop The amount of property remaining.
	 * @param fpms The number of frames per millisecond.
	 * @param time The amount of time remaining.
	 */
	private int getPropertyDifference(int prop, double fpms, long time) {
		int dp = 0;
		if (time == 0) {
			dp = prop;
		} else {
			dp = (int) Math.round(prop / (fpms * time));
			dp = (prop >= 0) ? Math.min(dp, prop) : Math.max(dp, prop);
		}
		return dp;
	}
	
	/**
	 * Checks whether the tween is complete.
	 * 
	 * @return True if the tweened properties are at their target values;
	 * otherwise, false.
	 */
	private boolean propertiesAtTargets() {
		boolean atTargets = true;
		for (Integer p: propertyRemainings) {
			if (p.intValue() != 0) {
				atTargets = false;
				break;
			}
		}
		return atTargets;
	}
	
	/**
	 * Adds a property to this tween at the specified index. The property's
	 * name is the same as its index in the map passed to animateSprite().
	 * 
	 * @param name The name of the new property.
	 * @param total The total amount that this property is to change by.
	 */
	protected void addTweenedProperty(String name, int total) {
		Integer propIndex = propertyTotals.size();
		propertyTotals.add(total);
		propertyRemainings.add(total);
		propertyNames.put(propIndex, name);
	}
	
	/**
	 * Advances the animation by as much as has been requested.
	 * 
	 * @param fps The speed of animation, in frames per second.
	 */
	@Override
	protected void advanceAnimation(int fps) {
		double fpms = (double) fps / 1000;
		long remaining = getRemainingTime();
		HashMap<String, Integer> propChanges = new HashMap<String, Integer>();
		for (int i = 0; i < propertyTotals.size(); i++) {
			int pRemain = propertyRemainings.get(i);
			int dp = getPropertyDifference(pRemain, fpms, remaining);
			pRemain -= dp;
			propertyRemainings.set(i, pRemain);
			propChanges.put(propertyNames.get(i), dp);
		}
		animateSprite(propChanges);
	}
	
	/**
	 * Animates the owned sprite with the values calculated by advancing the
	 * tween.
	 * 
	 * @param propChanges The amount that each property should change.
	 */
	protected abstract void animateSprite(Map<String, Integer> propChanges);
	
	/**
	 * Checks whether there is any more time left in this animation and
	 * whether the tween is complete.
	 * 
	 * @return True if the animation has run for the requested time and the
	 * tween is over.
	 */
	@Override
	protected boolean isAtEnd() {
		return (super.isAtEnd() && propertiesAtTargets());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void resetProperties() {
		for (int i = 0; i < propertyTotals.size(); i++) {
			int prop = propertyTotals.get(i);
			propertyRemainings.set(i, prop);
		}
	}
	
}
