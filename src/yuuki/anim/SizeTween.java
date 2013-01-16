package yuuki.anim;

import java.util.ArrayList;

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
		addTweenedProperty(dw);
		addTweenedProperty(dh);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void animateSprite(ArrayList<Integer> properties) {
		int dw = properties.get(0);
		int dh = properties.get(1);
		sprite.setSize(sprite.getWidth() + dw, sprite.getHeight() + dh);
	}
	
}
