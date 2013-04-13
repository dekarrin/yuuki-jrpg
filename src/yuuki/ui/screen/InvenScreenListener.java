package yuuki.ui.screen;

import yuuki.item.Item;

/**
 * For listeners to events coming from the inventory screen.
 */
public interface InvenScreenListener extends ScreenListener {
	
	/**
	 * Fired when the close button is pressed.
	 */
	public void closeInvenClicked();
	
	/**
	 * Fired when the use button for an item is pressed.
	 * 
	 * @param item The item that was selected when the button was pressed.
	 */
	public void useItemClicked(Item item);
	
	/**
	 * Fired when the drop button for an item is pressed.
	 * 
	 * @param item The item that was selected when the button was pressed.
	 */
	public void dropItemClicked(Item item);
	
}
