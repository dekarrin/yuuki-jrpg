package yuuki.animation;

import yuuki.sprite.Sprite;

/**
 * Calls advanceFrame() on the animated sprite. Advancement essentially
 * delegates the task of animation to the Sprite itself.
 */
public class Advancement extends Animation {
	
	/**
	 * Whether this advancement has advanced the animation of the owned sprite.
	 */
	private boolean hasAdvanced;
	
	/**
	 * Creates a new AdvancementLoop that animates the specified Sprite.
	 * 
	 * @param Sprite The Sprite that this animation animates.
	 */
	public Advancement(Sprite sprite) {
		super(sprite);
		hasAdvanced = false;
	}
	
	/**
	 * Advances the owned Sprite through one of its frames.
	 */
	@Override
	protected void advance(int fps) {
		sprite.advanceFrame(fps);
		hasAdvanced = true;
	}
	
	/**
	 * Always returns false, because advancement is never complete.
	 */
	@Override
	protected boolean isAtEnd() {
		return hasAdvanced;
	}
	
	@Override
	protected void resetProperties() {
		hasAdvanced = false;
	}
	
}
