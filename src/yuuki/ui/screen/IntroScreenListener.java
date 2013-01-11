package yuuki.ui.screen;

/**
 * A listener for events fired from an IntroScreen.
 */
public interface IntroScreenListener extends ScreenListener {
	
	/**
	 * Fired when the exit button is clicked.
	 */
	public void exitClicked();
	
	/**
	 * Fired when the load button is clicked.
	 */
	public void loadGameClicked();
	
	/**
	 * Fired when the new game button is clicked.
	 */
	public void newGameClicked();
	
	/**
	 * Fired when the options button is clicked.
	 */
	public void optionsClicked();
	
}
