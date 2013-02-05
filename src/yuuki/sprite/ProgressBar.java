package yuuki.sprite;

import java.awt.Color;

import javax.swing.JLayeredPane;

import yuuki.animation.engine.Animator;

/**
 * Shows progress.
 */
public class ProgressBar extends Sprite {
	
	/**
	 * The color of the unfilled portion of all ProgressBar instances.
	 */
	public static final Color BACKGROUND_COLOR = new Color(79, 76, 0);
	
	/**
	 * The color of the border of all ProgressBar instances.
	 */
	public static final Color BORDER_COLOR = new Color(0, 0, 0);
	
	/**
	 * The background portion of this ProgressBar.
	 */
	private Rectangle background;
	
	/**
	 * The filled portion of this ProgressBar.
	 */
	private Rectangle foreground;
	
	/**
	 * The maximum value of this ProgressBar.
	 */
	private int maximum;
	
	/**
	 * The currently-displayed value of this ProgressBar.
	 */
	private int value;
	
	/**
	 * Creates a new ProgressBar with the given dimensions and whose filled
	 * portion is the given color.
	 * 
	 * @param animator The handler for this ProgressBar's animations.
	 * @param width The total width of the ProgressBar in pixels.
	 * @param height The total height of the ProgressBar in pixels.
	 * @param barColor The color of the filled portion of the ProgressBar.
	 * @param max The maximum value.
	 */
	public ProgressBar(Animator animator, int width, int height,
			Color barColor, int max) {
		super(animator, width, height);
		this.value = 0;
		this.maximum = max;
		background = new Rectangle(animator, getWidth(), getHeight());
		foreground = new Rectangle(animator, getWidth(), getHeight());
		background.setBorderColor(BORDER_COLOR);
		background.setFillColor(BACKGROUND_COLOR);
		foreground.setBorderColor(null);
		foreground.setFillColor(barColor);
		JLayeredPane pane = new JLayeredPane();
		pane.add(background.getComponent(), new Integer(0));
		pane.add(foreground.getComponent(), new Integer(1));
		pane.setBounds(0, 0, getWidth(), getHeight());
		add(pane);
	}
	
	/**
	 * Gets the percent of this ProgressBar that is currently filled.
	 * 
	 * @return The percent filled.
	 */
	public double getPercent() {
		return (double) value / maximum;
	}
	
	/**
	 * Updates this ProgressBar to show a certain value and animates it.
	 * 
	 * @param value The value to show.
	 */
	public void setValue(int value) {
		this.value = value;
		int targetWidth = (int) Math.round(getWidth() * getPercent());
		foreground.setWidth(targetWidth);
	}
	
}
