package yuuki.ui.menu;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JMenu;

/**
 * A menu in the game.
 * 
 * @param <L> The type of listener that can be registered on this Menu.
 */
@SuppressWarnings("serial")
public abstract class Menu<L extends MenuListener> extends JMenu implements
yuuki.ui.Listenable<L> {
	
	/**
	 * Keeps track of all registered listeners.
	 */
	private Set<L> listeners;
	
	/**
	 * Creates a new GameMenu.
	 * 
	 * @param name The name of the menu as displayed in its menu bar.
	 * @param description The description of the menu.
	 * @param mnemonic The key used to access the menu.
	 */
	public Menu(String name, String description, int mnemonic) {
		super(name);
		listeners = new HashSet<L>();
		setMnemonic(mnemonic);
		getAccessibleContext().setAccessibleDescription(description);
		buildItems();
		setItemMnemonics();
		setItemDescriptions();
		setItemAccelerators();
		addItems();
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public boolean addListener(L listener) {
		return listeners.add(listener);
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public List<L> getElementListeners() {
		List<L> listenersList = new LinkedList<L>();
		for (L listener: listeners) {
			listenersList.add(listener);
		}
		return listenersList;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public boolean removeListener(Object listener) {
		return listeners.remove(listener);
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
