package yuuki.animation;

import java.util.Map;

import yuuki.sprite.Sprite;

/**
 * Changes the dimensions of a Sprite over time.
 */
public class SizeTween extends Tween {
	
	/**
	 * Creates a new SizeTween.
	 * 
	 * @param sprite The Sprite to animate.
	 * @param time The duration in milliseconds of the tween.
	 * @param dw The amount that width should change.
	 * @param dh The amount that height should change.
	 */
	public SizeTween(Sprite sprite, long time, int dw, int dh) {
		super(sprite, time);
		addTweenedProperty("width", dw);
		addTweenedProperty("height", dh);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void animateSprite(Map<String, Integer> propChanges) {
		int dw = propChanges.get("width");
		int dh = propChanges.get("height");
		sprite.resize(dw, dh);
	}
	
}
