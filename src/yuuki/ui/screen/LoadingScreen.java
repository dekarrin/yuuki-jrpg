package yuuki.ui.screen;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.JLabel;

import yuuki.animation.engine.Animator;
import yuuki.sprite.ProgressBar;

/**
 * Shows the loading progress and splash screen.
 */
@SuppressWarnings("serial")
public class LoadingScreen extends Screen<ScreenListener> {
	
	/**
	 * The color of the progress bar.
	 */
	private static final Color PROGRESS_COLOR = new Color(0x87, 0xf5, 0x00);
	
	/**
	 * The height of the loading bar.
	 */
	private static final int PROGRESS_HEIGHT = 30;
	
	/**
	 * The percentage of the screen that the loading bar takes up in width.
	 */
	private static final double PROGRESS_WIDTH_MULTIPLIER = 0.6;
	
	/**
	 * The loading bar.
	 */
	private ProgressBar progress;
	
	/**
	 * Creates a new screen of a specified size.
	 * 
	 * @param width
	 * @param height
	 * @param Animator The animation engine.
	 */
	public LoadingScreen(int width, int height, Animator animator) {
		super(width, height);
		setLayout(new FlowLayout());
		Color color = PROGRESS_COLOR;
		int progWidth = (int) (width * PROGRESS_WIDTH_MULTIPLIER);
		int progHeight = PROGRESS_HEIGHT;
		progress = new ProgressBar(animator, progWidth, progHeight, color,
				100);
		Box b = Box.createVerticalBox();
		JLabel label = new JLabel("Loading...");
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
		b.add(Box.createVerticalStrut(80));
		b.add(label);
		b.add(Box.createVerticalStrut(20));
		b.add(progress.getComponent());
		add(b);
		progress.setValue(0);
	}
	
	@Override
	public void setInitialProperties() {}
	
	/**
	 * Updates the progress bar.
	 * 
	 * @param percent The percent to update it to.
	 */
	public void updateProgress(int percent) {
		progress.setValue(percent);
	}
	
}
