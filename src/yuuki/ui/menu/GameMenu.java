package yuuki.ui.menu;

import javax.swing.JMenu;

/**
 * A menu in the game.
 */
@SuppressWarnings("serial")
public abstract class GameMenu extends JMenu {
	
	/**
	 * Creates a new GameMenu.
	 * 
	 * @param name The name of the menu as displayed in its menu bar.
	 * @param description The description of the menu.
	 * @param mnemonic The key used to access the menu.
	 */
	public GameMenu(String name, String description, int mnemonic) {
		super(name);
		setMnemonic(mnemonic);
		getAccessibleContext().setAccessibleDescription(description);
		buildItems();
		setItemMnemonics();
		setItemDescriptions();
		setItemAccelerators();
		addItems();
	}
	
	/**
	 * Adds the child components to this menu.
	 */
	protected abstract void addItems();
	
	/**
	 * Creates the menu items for this menu.
	 */
	protected abstract void buildItems();
	
	/**
	 * Sets the execution keystrokes for this menu's items.
	 */
	protected abstract void setItemAccelerators();
	
	/**
	 * Sets the descriptions for this menu's items.
	 */
	protected abstract void setItemDescriptions();
	
	/**
	 * Sets the mnemonics for this menu's items.
	 */
	protected abstract void setItemMnemonics();
	
}
