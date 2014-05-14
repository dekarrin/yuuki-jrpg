package yuuki.ui.menu;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JMenuBar;

import yuuki.ui.Listenable;

/**
 * The menu bar used by the game.
 */
@SuppressWarnings("serial")
public class MenuBar extends JMenuBar implements Listenable<MenuBarListener>,
MenuListener {
	
	/**
	 * Describes which menus and items are to be enabled/disabled.
	 */
	public static class MenuBarEnableInfo {
		
		/**
		 * Constructs a new MenuBarEnableInfo with all menus and items enabled.
		 */
		public MenuBarEnableInfo() {
			this(true);
		}
		
		/**
		 * Constructs a new MenuBarEnableInfo.
		 * 
		 * @param defaultEnable Whether all menus and items are enabled by
		 * default.
		 */
		public MenuBarEnableInfo(boolean defaultEnable) {
			
		}
	}
	
	/**
	 * The ID of the actions menu.
	 */
	public static final int MENU_ID_ACTIONS = 1;
	
	/**
	 * The ID of the file menu.
	 */
	public static final int MENU_ID_FILE = 0;
	
	/**
	 * Contains actions related to the game.
	 */
	private ActionsMenu actionsMenu;
	
	/**
	 * The File menu. Contains main menu operations.
	 */
	private FileMenu fileMenu;
	
	/**
	 * The list of listeners.
	 */
	private Set<MenuBarListener> listeners;
	
	/**
	 * Creates a new MenuBar. The child components are created and added.
	 */
	public MenuBar() {
		listeners = new HashSet<MenuBarListener>();
		createMenus();
		setListeners();
		addMenus();
	}
	
	@Override
	public boolean addListener(MenuBarListener listener) {
		return listeners.add(listener);
	}
	
	@Override
	public List<MenuBarListener> getElementListeners() {
		List<MenuBarListener> listenersList =
				new LinkedList<MenuBarListener>();
		for (MenuBarListener listener : listeners) {
			listenersList.add(listener);
		}
		return listenersList;
	}
	
	@Override
	public void menuItemTriggered(Menu<?> source, int itemId) {
		int menuId = -1;
		if (source == fileMenu) {
			menuId = MENU_ID_FILE;
		} else if (source == actionsMenu) {
			menuId = MENU_ID_ACTIONS;
		}
		fireMenuItemTriggered(menuId, itemId);
	}
	
	@Override
	public boolean removeListener(Object listener) {
		return listeners.remove(listener);
	}
	
	/**
	 * Sets the enabled status of a menu.
	 * 
	 * @param id An index that identifies the menu. Should be of the MENU_ID_*
	 * constants from this class.
	 * @param enabled Whether the menu should be enabled.
	 */
	public void setMenuEnabled(int id, boolean enabled) {
		switch (id) {
			case MenuBar.MENU_ID_FILE:
				fileMenu.setEnabled(enabled);
				break;
				
			case MenuBar.MENU_ID_ACTIONS:
				actionsMenu.setEnabled(enabled);
				break;
		}
	}
	
	/**
	 * Sets the enabled status of a menu item.
	 * 
	 * @param menuId An index that identifies the menu. Should be of the
	 * MENU_ID_* constants from this class.
	 * @param itemId An index that identifies the menu item. Should be a
	 * constant taken from the appropriate class.
	 * @param enabled Whether the menu should be enabled.
	 */
	public void setMenuItemEnabled(int menuId, int itemId, boolean enabled) {
		switch (menuId) {
			case MenuBar.MENU_ID_FILE:
				fileMenu.setItemEnabled(itemId, enabled);
				break;
				
			case MenuBar.MENU_ID_ACTIONS:
				actionsMenu.setItemEnabled(itemId, enabled);
				break;
		}
	}
	
	/**
	 * Adds the menus to this menu bar.
	 */
	private void addMenus() {
		add(fileMenu);
		add(actionsMenu);
	}
	
	/**
	 * Creates the menus in this menu bar.
	 */
	private void createMenus() {
		fileMenu = new FileMenu();
		actionsMenu = new ActionsMenu();
	}
	
	/**
	 * Calls menuItemTriggered() on all listeners.
	 * 
	 * @param menuId The ID of the menu that the item is in.
	 * @param itemId The ID of the item that was triggered.
	 */
	private void fireMenuItemTriggered(int menuId, int itemId) {
		for (MenuBarListener l : getElementListeners()) {
			l.menuItemTriggered(menuId, itemId);
		}
	}
	
	/**
	 * Sets this menu bar as a listener to all menus.
	 */
	private void setListeners() {
		fileMenu.addListener(this);
		actionsMenu.addListener(this);
	}
	
}
