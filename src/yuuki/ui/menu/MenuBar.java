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
	 * The ID of the file menu.
	 */
	public static final int FILE_MENU_ID = 0;
	
	/**
	 * The File menu. Contains main menu operations.
	 */
	private FileMenu fileMenu;
	
	/**
	 * The list of listeners.
	 */
	private Set<MenuBarListener> listeners;
	
	/**
	 * Creates a new GameMenuBar. The child components are created and added.
	 */
	public MenuBar() {
		listeners = new HashSet<MenuBarListener>();
		createMenus();
		setListeners();
		addMenus();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addListener(MenuBarListener listener) {
		return listeners.add(listener);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MenuBarListener> getElementListeners() {
		List<MenuBarListener> listenersList =
				new LinkedList<MenuBarListener>();
		for (MenuBarListener listener: listeners) {
			listenersList.add(listener);
		}
		return listenersList;
	}
	
	/**
	 * {{@inheritDoc}}
	 */
	@Override
	public void menuItemTriggered(Menu<?> source, int itemId) {
		int menuId = -1;
		if (source == fileMenu) {
			menuId = FILE_MENU_ID;
		}
		fireMenuItemTriggered(menuId, itemId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeListener(Object listener) {
		return listeners.remove(listener);
	}
	
	/**
	 * Adds the menus to this menu bar.
	 */
	private void addMenus() {
		add(fileMenu);
	}
	
	/**
	 * Creates the menus in this menu bar.
	 */
	private void createMenus() {
		fileMenu = new FileMenu();
	}
	
	/**
	 * Calls menuItemTriggered() on all listeners.
	 * 
	 * @param menuId The ID of the menu that the item is in.
	 * @param itemId The ID of the item that was triggered.
	 */
	private void fireMenuItemTriggered(int menuId, int itemId) {
		for (MenuBarListener l: getElementListeners()) {
			l.menuItemTriggered(menuId, itemId);
		}
	}
	
	/**
	 * Sets this menu bar as a listener to all menus.
	 */
	private void setListeners() {
		fileMenu.addListener(this);
	}
	
}
