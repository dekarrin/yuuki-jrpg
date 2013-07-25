package yuuki.ui.menu;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.accessibility.AccessibleContext;
import javax.swing.JMenuItem;

/**
 * Holds items related to game play.
 */
@SuppressWarnings("serial")
public class ActionsMenu extends Menu<MenuListener> {
	
	/**
	 * The ID of the get item.
	 */
	public static final int ITEM_ID_GET = 1;
	
	/**
	 * The ID of the inventory item.
	 */
	public static final int ITEM_ID_INVENTORY = 0;
	
	/**
	 * Creates a new ActionsMenu.
	 */
	public ActionsMenu() {
		super("Actions", "The Actions menu", KeyEvent.VK_A);
	}
	
	@Override
	protected List<JMenuItem> buildItems() {
		List<JMenuItem> list = new ArrayList<JMenuItem>();
		list.add(new JMenuItem("Inventory"));
		list.add(new JMenuItem("Get Item"));
		return list;
	}
	
	@Override
	protected List<Integer> getSeparatorIndexes() {
		return new ArrayList<Integer>();
	}
	
	@Override
	protected void setItemAccelerators() {
		// TODO: add accelerators
	}
	
	@Override
	protected void setItemDescriptions() {
		String getHelp = "Picks up an item";
		String invenHelp = "Displays the inventory";
		AccessibleContext gc, ic;
		gc = itemAt(ITEM_ID_GET).getAccessibleContext();
		ic = itemAt(ITEM_ID_INVENTORY).getAccessibleContext();
		gc.setAccessibleDescription(getHelp);
		ic.setAccessibleDescription(invenHelp);
	}
	
	@Override
	protected void setItemMnemonics() {
		itemAt(ITEM_ID_GET).setMnemonic(KeyEvent.VK_G);
		itemAt(ITEM_ID_INVENTORY).setMnemonic(KeyEvent.VK_I);
	}
	
}
