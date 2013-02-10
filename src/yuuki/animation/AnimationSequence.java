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
	 * Resumes the current animation.
	 */
	public void resume() {
		Animation current = sequence.get(position);
		current.resume();
	}
	
	/**
	 * Pauses the current animation.
	 */
	public void pause() {
		Animation current = sequence.get(position);
		current.pause();
	}
	
	/**
	 * Starts animation.
	 */
	public void start() {}
	
	/**
	 * Finishes every remaining animation in the sequence.
	 */
	public void finish() {
		for (int i = position; i < sequence.size(); i++) {
			sequence.get(i).finish();
		}
		position = (sequence.size() - 1);
	}
	
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
	
	@Override
	protected boolean isAtEnd() {
		boolean atLastAnimation = (position == (sequence.size() - 1));
		boolean currentIsComplete = sequence.get(position).isComplete();
		return (atLastAnimation && currentIsComplete);
	}
	
	@Override
	protected void resetProperties() {
		for (int i = 0; i <= position; i++) {
			sequence.get(i).reset();
		}
		position = 0;
	}
	
}
