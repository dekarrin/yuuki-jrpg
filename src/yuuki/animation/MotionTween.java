package yuuki.animation;

import java.util.Map;

import yuuki.sprite.Sprite;

/**
 * Animates movement from one value to another.
 */
public class MotionTween extends Tween {
	
	/**
	 * Creates a new MotionTween.
	 * 
	 * @param sprite The Sprite to create the animation for.
	 * @param time The duration of the tween, in milliseconds.
	 * @param dx The total amount of movement along the x-axis.
	 * @param dy The total amount of movement along the y-axis.
	 */
	public MotionTween(Sprite sprite, long time, int dx, int dy) {
		super(sprite, time);
		addTweenedProperty("x", dx);
		addTweenedProperty("y", dy);
	}
	
	@Override
	protected void animateSprite(Map<String, Integer> propChanges) {
		int dx = propChanges.get("x");
		int dy = propChanges.get("y");
		sprite.move(dx, dy);
	}
	
}
