package yuuki.ui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;

/**
 * Holds items related to game play.
 */
@SuppressWarnings("serial")
public class ActionsMenu extends Menu<MenuListener> implements ActionListener {
	
	/**
	 * The ID of the get item.
	 */
	public static final int ITEM_ID_GET = 1;
	
	/**
	 * The ID of the inventory item.
	 */
	public static final int ITEM_ID_INVENTORY = 0;
	
	/**
	 * The menu item for getting an item.
	 */
	private JMenuItem menuItemGet;
	
	/**
	 * The menu item for the inventory.
	 */
	private JMenuItem menuItemInventory;
	
	/**
	 * Creates a new ActionsMenu.
	 */
	public ActionsMenu() {
		super("Actions", "The Actions menu", KeyEvent.VK_A);
		setListeners();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int id = -1;
		if (e.getSource() == menuItemGet) {
			id = ITEM_ID_GET;
		} else if (e.getSource() == menuItemInventory) {
			id = ITEM_ID_INVENTORY;
		}
		fireItemTriggered(id);
	}
	
	/**
	 * Calls menuItemTriggered() on all listeners.
	 * 
	 * @param id The ID of the item that was selected.
	 */
	public void fireItemTriggered(int id) {
		for (MenuListener l : getElementListeners()) {
			l.menuItemTriggered(this, id);
		}
	}
	
	/**
	 * Sets this ActionsMenu as the listener for all of its items.
	 */
	private void setListeners() {
		menuItemGet.addActionListener(this);
		menuItemInventory.addActionListener(this);
	}
	
	@Override
	protected void addItems() {
		add(menuItemGet);
		add(menuItemInventory);
	}
	
	@Override
	protected void buildItems() {
		menuItemInventory = new JMenuItem("Inventory");
		menuItemGet = new JMenuItem("Get Item");
	}
	
	@Override
	protected void setItemAccelerators() {
		// TODO: add accelerators
	}
	
	@Override
	protected void setItemDescriptions() {
		String getHelp = "Picks up an item";
		String invenHelp = "Displays the inventory";
		menuItemGet.getAccessibleContext().setAccessibleDescription(getHelp);
		menuItemInventory.getAccessibleContext().setAccessibleDescription(invenHelp);
	}
	
	@Override
	protected void setItemMnemonics() {
		menuItemGet.setMnemonic(KeyEvent.VK_G);
		menuItemInventory.setMnemonic(KeyEvent.VK_I);
	}
	
}
