package yuuki.animation;

import java.util.Map;

import yuuki.ui.MessageBox;

/**
 * Types up text on a message box, one letter at a time.
 */
public class TextTween extends Tween {
	
	/**
	 * The position of the next character to be added to the message box.
	 */
	private int position;
	
	/**
	 * The complete message that is being displayed on the message box.
	 */
	private String text;
	
	/**
	 * Creates a new TextChange for a MessageBox.
	 * 
	 * @param box The MessageBox to animate.
	 * @param time The amount of time to take before each character.
	 * @param text The text to display.
	 */
	public TextTween(MessageBox box, long time, String text) {
		super(box, time * text.length());
		addTweenedProperty("position", text.length() - 1);
		this.text = text;
		this.position = 0;
	}
	
	/**
	 * Adds the next character(s) to the message box.
	 * 
	 * @param propChanges Index "position" contains the number of characters to
	 * add.
	 */
	@Override
	protected void animateSprite(Map<String, Integer> propChanges) {
		int dp = propChanges.get("position");
		for (int i = 0; i < dp; i++) {
			((MessageBox) sprite).addChar(text.charAt(position));
			position++;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void resetProperties() {
		super.resetProperties();
		position = 0;
	}
	
}
