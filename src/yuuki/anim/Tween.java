package yuuki.anim;

import java.util.ArrayList;

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
	private ArrayList<Integer> propertyRemainings;
	
	/**
	 * The total amount to change each property by.
	 */
	private ArrayList<Integer> propertyTotals;
	
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
	}
	
	/**
	 * Adds a property to this tween.
	 * 
	 * @param total The total amount that this property is to change by.
	 */
	protected void addTweenedProperty(int total) {
		propertyTotals.add(total);
		propertyRemainings.add(total);
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
	
	/**
	 * Animates the owned sprite with the values calculated by advancing the
	 * tween.
	 * 
	 * @param properties The values of the properties.
	 */
	protected abstract void animateSprite(ArrayList<Integer> properties);
	
	/**
	 * Advances the animation by as much as has been requested.
	 * 
	 * @param fps The speed of animation, in frames per second.
	 */
	@Override
	protected void advanceAnimation(int fps) {
		double fpms = (double) fps / 1000;
		long remaining = getRemainingTime();
		ArrayList<Integer> propValues = new ArrayList<Integer>();
		for (int i = 0; i < propertyTotals.size(); i++) {
			int pRemain = propertyRemainings.get(i);
			int dp = getPropertyDifference(pRemain, fpms, remaining);
			pRemain -= dp;
			propertyRemainings.set(i, pRemain);
			propValues.add(dp);
		}
		animateSprite(propValues);
	}
	
	/**
	 * Gets the amount that a property should change.
	 * 
	 * @param prop The amount of property remaining.
	 * @param fpms The number of frames per millisecond.
	 * @param time The amount of time remaining.
	 */
	private int getPropertyDifference(int prop, double fpms, long time) {
		int dp = (int) Math.round(prop / (fpms * time));
		dp = (prop >= 0) ? Math.min(dp, prop) : Math.max(dp, prop);
		return dp;
	}
	
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
	
}
