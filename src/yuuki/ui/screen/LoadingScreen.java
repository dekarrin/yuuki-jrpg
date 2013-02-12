package yuuki.ui.screen;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import yuuki.animation.engine.AnimationManager;

/**
 * Shows the loading progress and splash screen.
 */
@SuppressWarnings("serial")
public class LoadingScreen extends Screen<ScreenListener> {
	
	/**
	 * The amount of ticks in the loading bar. More ticks allow for more
	 * precision in progress displayed.
	 */
	private static final int LOADING_BAR_TICKS = 1000;
	
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
	private JProgressBar progress;
	
	/**
	 * The text label.
	 */
	private JLabel label;
	
	/**
	 * Creates a new screen of a specified size.
	 * 
	 * @param width The width of the new screen.
	 * @param height The height of the new screen.
	 * @param animator The animation engine.
	 */
	public LoadingScreen(int width, int height, AnimationManager animator) {
		super(width, height);
		setLayout(new FlowLayout());
		int progWidth = (int) (width * PROGRESS_WIDTH_MULTIPLIER);
		int progHeight = PROGRESS_HEIGHT;
		Dimension progSize = new Dimension(progWidth, progHeight);
		progress = new JProgressBar(0, LOADING_BAR_TICKS);
		progress.setValue(0);
		progress.setStringPainted(true);
		progress.setPreferredSize(progSize);
		progress.setMaximumSize(progSize);
		progress.setMinimumSize(progSize);
		Box b = Box.createVerticalBox();
		label = new JLabel("Loading...");
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
		b.add(Box.createVerticalStrut(80));
		b.add(label);
		b.add(Box.createVerticalStrut(20));
		b.add(progress);
		add(b);
	}
	
	@Override
	public void setInitialProperties() {}
	
	/**
	 * Updates the progress bar.
	 * 
	 * @param percent The percent to update it to.
	 * @param text What to show in the label.
	 */
	public void updateProgress(double percent, String text) {
		int val = (int) Math.round(percent * LOADING_BAR_TICKS);
		progress.setValue(val);
		setText(text);
	}
	
	/**
	 * Sets the text of this LoadingScreen.
	 * 
	 * @param text The text to change it to.
	 */
	public void setText(String text) {
		class Runner implements Runnable {
			private String text;
			public Runner(String text) {
				this.text = text;
			}
			public void run() {
				label.setText(text);
			}
		}
		SwingUtilities.invokeLater(new Runner(text));
	}
	
}
