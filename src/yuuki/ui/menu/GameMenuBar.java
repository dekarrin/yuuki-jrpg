package yuuki.ui.menu;

import javax.swing.JMenuBar;

/**
 * The menu bar used by the game.
 */
@SuppressWarnings("serial")
public class GameMenuBar extends JMenuBar {
	
	/**
	 * The File menu. Contains main menu operations.
	 */
	private GameMenu fileMenu;
	
	/**
	 * Creates a new GameMenuBar. The child components are created and added.
	 */
	public GameMenuBar() {
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
