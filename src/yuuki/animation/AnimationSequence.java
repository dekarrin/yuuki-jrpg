package yuuki.animation;

import java.util.ArrayList;

import yuuki.sprite.Sprite;

/**
 * An animation that is made up of a series of other animations. Each animation
 * is run in sequence.
 */
public class AnimationSequence extends Animation {
	
	/**
	 * The index of the currently running animation.
	 */
	private int position;
	
	/**
	 * The animations to run.
	 */
	private ArrayList<Animation> sequence;
	
	/**
	 * Allocates a new AnimationSequence.
	 * 
	 * @param sprite The Sprite to animate.
	 */
	public AnimationSequence(Sprite sprite) {
		super(sprite);
		sequence = new ArrayList<Animation>();
		position = 0;
	}
	
	/**
	 * Adds an animation to the sequence of animations.
	 * 
	 * @param animation The animation to add.
	 */
	public void add(Animation animation) {
		sequence.add(animation);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void advance(int fps) {
		Animation current = sequence.get(position);
		if (!current.isComplete()) {
			current.advanceFrame(fps);
		}
		if (current.isComplete() && position < sequence.size() - 1) {
			position++;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isAtEnd() {
		boolean atLastAnimation = (position == (sequence.size() - 1));
		boolean currentIsComplete = sequence.get(position).isComplete();
		return (atLastAnimation && currentIsComplete);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void resetProperties() {
		for (int i = 0; i <= position; i++) {
			sequence.get(i).reset();
		}
		position = 0;
	}
	
}
