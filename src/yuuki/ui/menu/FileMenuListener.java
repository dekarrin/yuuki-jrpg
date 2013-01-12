package yuuki.ui.menu;

/**
 * Listens for events fired from a FileMenu.
 */
public interface FileMenuListener extends MenuListener {
	
	/**
	 * Fired when the 'Exit' item is selected.
	 */
	public void exitItemTriggered();
	
	/**
	 * Fired when the 'New' item is selected.
	 */
	public void newItemTriggered();
	
	/**
	 * Fired when the 'Close' item is selected.
	 */
	public void closeItemTriggered();
	
	/**
	 * Fired when the 'Options' item is selected.
	 */
	public void optionsItemTriggered();
	
}
