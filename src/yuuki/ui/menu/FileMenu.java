package yuuki.ui.menu;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.accessibility.AccessibleContext;
import javax.swing.JMenuItem;

/**
 * The file menu for the game.
 */
@SuppressWarnings("serial")
public class FileMenu extends Menu<MenuListener> {
	
	/**
	 * The ID of the 'Close' item.
	 */
	public static final int ITEM_ID_CLOSE = 1;
	
	/**
	 * The ID of the 'Exit' item.
	 */
	public static final int ITEM_ID_EXIT = 5;
	
	/**
	 * The ID of the 'Load' item.
	 */
	public static final int ITEM_ID_LOAD = 3;
	
	/**
	 * The ID of the 'New' item.
	 */
	public static final int ITEM_ID_NEW = 0;
	
	/**
	 * The ID of the 'Options' item.
	 */
	public static final int ITEM_ID_OPTIONS = 4;
	
	/**
	 * The ID of the 'Save' item.
	 */
	public static final int ITEM_ID_SAVE = 2;
	
	/**
	 * Creates a new file menu. The child components are created and added.
	 */
	public FileMenu() {
		super("File", "The File menu", KeyEvent.VK_F);
	}
	
	@Override
	protected List<Integer> getSeparatorIndexes() {
		List<Integer> l = new ArrayList<Integer>();
		l.add(4);
		l.add(6);
		return l;
	}
	
	@Override
	protected List<JMenuItem> buildItems() {
		List<JMenuItem> itemList = new ArrayList<JMenuItem>();
		itemList.add(new JMenuItem("New Game"));
		itemList.add(new JMenuItem("Load Game"));
		itemList.add(new JMenuItem("Close Game"));
		itemList.add(new JMenuItem("Save Game"));
		itemList.add(new JMenuItem("Options"));
		itemList.add(new JMenuItem("Exit"));
		return itemList;
	}
	
	@Override
	protected void setItemAccelerators() {
		// TODO: add accelerators
	}
	
	@Override
	protected void setItemDescriptions() {
		String newHelp = "Starts a new game";
		String loadHelp = "Loads a previous game";
		String closeHelp = "Returns to the main menu";
		String saveHelp = "Saves the current game";
		String optHelp = "Brings up the options screen";
		String exitHelp = "Quits the game";
		AccessibleContext nc, lc, cc, sc, oc, ec;
		nc = itemAt(ITEM_ID_NEW).getAccessibleContext();
		lc = itemAt(ITEM_ID_LOAD).getAccessibleContext();
		cc = itemAt(ITEM_ID_CLOSE).getAccessibleContext();
		sc = itemAt(ITEM_ID_SAVE).getAccessibleContext();
		oc = itemAt(ITEM_ID_OPTIONS).getAccessibleContext();
		ec = itemAt(ITEM_ID_EXIT).getAccessibleContext();
		nc.setAccessibleDescription(newHelp);
		lc.setAccessibleDescription(loadHelp);
		cc.setAccessibleDescription(closeHelp);
		sc.setAccessibleDescription(saveHelp);
		oc.setAccessibleDescription(optHelp);
		ec.setAccessibleDescription(exitHelp);
	}
	
	@Override
	protected void setItemMnemonics() {
		itemAt(ITEM_ID_NEW).setMnemonic(KeyEvent.VK_N);
		itemAt(ITEM_ID_LOAD).setMnemonic(KeyEvent.VK_L);
		itemAt(ITEM_ID_CLOSE).setMnemonic(KeyEvent.VK_C);
		itemAt(ITEM_ID_SAVE).setMnemonic(KeyEvent.VK_S);
		itemAt(ITEM_ID_OPTIONS).setMnemonic(KeyEvent.VK_O);
		itemAt(ITEM_ID_EXIT).setMnemonic(KeyEvent.VK_Q);
	}
	
}
