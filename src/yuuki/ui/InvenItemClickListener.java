package yuuki.ui;

import java.awt.event.MouseEvent;

import yuuki.item.Item;

/**
 * A listener for click events on an InvenPanel.
 */
public interface InvenItemClickListener {
	
	/**
	 * Called when an item cell is clicked.
	 * 
	 * @param item The item that the clicked cell represented.
	 */
	public void itemCellClicked(MouseEvent e, Item item);
	
	/**
	 * Called when an item is deselected.
	 */
	public void itemDeselected();
	
}
