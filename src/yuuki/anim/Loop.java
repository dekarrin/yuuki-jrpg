package yuuki.anim;

import yuuki.sprite.Sprite;

/**
 * Continuously animates until explicitly stopped by calling halt() on an
 * instance.
 */
public abstract class Loop extends Animation {
	
	/**
	 * Allocates a new Loop.
	 * 
	 * @param sprite The Sprite to animate.
	 */
	public Loop(Sprite sprite) {
		super(sprite);
	}
	
	/**
	 * Always returns false, because an AdvancementLoop is never complete.
	 * 
	 * @return False.
	 */
	@Override
	protected boolean isAtEnd() {
		return false;
	}
	
}
