package yuuki.ui;

/**
 * A listener for events fired from a MessageBox.
 */
public interface MessageBoxInputListener {
	
	/**
	 * Fired when the user enters text into a text prompt.
	 * 
	 * @param input The text that the user entered.
	 */
	public void enterClicked(String input);
	
	/**
	 * Fired when the user selects a choice from a choice prompt.
	 * 
	 * @param option The option that the user selected.
	 */
	public void optionClicked(Object option);
	
}
