package yuuki.ui.menu;

/**
 * Listens for events fired from a Menu.
 */
public interface MenuListener extends yuuki.ui.ElementListener {
	
	/**
	 * Fired when an item from the menu is selected. The ID of items can be
	 * obtained from the menu classes themselves as static finals.
	 * 
	 * @param source The element that the event started in.
	 * @param itemId The ID of the item that was selected.
	 */
	public void menuItemTriggered(Menu<?> source, int itemId);
	
}
