package yuuki.ui.menu;

import javax.swing.JMenuBar;

/**
 * The menu bar used by the game.
 */
@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {
	
	/**
	 * The File menu. Contains main menu operations.
	 */
	private FileMenu fileMenu;
	
	/**
	 * Creates a new GameMenuBar. The child components are created and added.
	 */
	public MenuBar() {
		createMenus();
		addMenus();
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
	
}
