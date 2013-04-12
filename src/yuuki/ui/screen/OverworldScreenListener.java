package yuuki.ui.screen;

/**
 * A listener for events from the overworld screen.
 */
public interface OverworldScreenListener extends ScreenListener {
	
	/**
	 * Fired when the stats button is pushed.
	 */
	public void invenButtonClicked();
	
	/**
	 * Fired when the get item button is pushed.
	 */
	public void getItemClicked();
	
}
