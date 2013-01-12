package yuuki.ui.menu;

import yuuki.ui.ElementListener;

/**
 * Listens for events fired from a menu bar.
 */
public interface MenuBarListener extends ElementListener {
	
	/**
	 * Indicates that a menu item was triggered.
	 * 
	 * @param menuId The menu that the item is in.
	 * @param itemId The item that was clicked.
	 */
	public void menuItemTriggered(int menuId, int itemId);
	
}
