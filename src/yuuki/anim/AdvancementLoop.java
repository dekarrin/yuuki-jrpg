package yuuki.anim;

import yuuki.sprite.Sprite;

/**
 * Continuously calls advanceFrame() on the animated sprite. AdvancementLoop
 * essentially delegates the task of animation to the Sprite itself. It runs
 * continuously until it is explicitly told to halt.
 */
public class AdvancementLoop extends Animation {
	
	/**
	 * Creates a new AdvancementLoop that animates the specified Sprite.
	 * 
	 * @param Sprite The Sprite that this animation animates.
	 */
	public AdvancementLoop(Sprite sprite) {
		super(sprite);
	}
	
	/**
	 * Always returns false, because an AdvancementLoop is never complete.
	 * 
	 * @return False.
	 */
	@Override
	public boolean isComplete() {
		return false;
	}
	
	/**
	 * Advances the owned Sprite through one of its frames.
	 */
	@Override
	protected void advance(int fps) {
		sprite.advanceFrame(fps);
	}
	
}
