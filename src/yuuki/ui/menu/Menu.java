package yuuki.ui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * A menu in the game.
 * 
 * @param <L> The type of listener that can be registered on this Menu.
 */
@SuppressWarnings("serial")
public abstract class Menu<L extends MenuListener> extends JMenu implements
yuuki.ui.Listenable<L> {
	
	/**
	 * Allows menu items to be accessed by some index that identifies them.
	 */
	private List<JMenuItem> items;
	
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
		createItems();
		setItemMnemonics();
		setItemDescriptions();
		setItemAccelerators();
		addItems();
		setListeners();
	}
	
	/**
	 * Adds listeners to each menu item.
	 */
	private void setListeners() {
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedId = -1;
				for (int i = 0; i < items.size(); i++) {
					JMenuItem item = items.get(i);
					if (e.getSource() == item) {
						selectedId = i;
						break;
					}
				}
				fireItemTriggered(selectedId);
			}
		};
		for (JMenuItem item : items) {
			item.addActionListener(listener);
		}
	}
	
	/**
	 * Calls menuItemTriggered() on all listeners.
	 * 
	 * @param id The ID of the item that was selected.
	 */
	private void fireItemTriggered(int id) {
		for (MenuListener l : getElementListeners()) {
			l.menuItemTriggered(this, id);
		}
	}
	
	@Override
	public boolean addListener(L listener) {
		return listeners.add(listener);
	}
	
	@Override
	public List<L> getElementListeners() {
		List<L> listenersList = new LinkedList<L>();
		for (L listener : listeners) {
			listenersList.add(listener);
		}
		return listenersList;
	}
	
	@Override
	public boolean removeListener(Object listener) {
		return listeners.remove(listener);
	}
	
	/**
	 * Sets the enabled status of an item.
	 * 
	 * @param id An index that identifies the menu item. Should be provided from
	 * a constant in the Menu subclass.
	 * @param enabled Whether the menu item should be enabled.
	 */
	public void setItemEnabled(int id, boolean enabled) {
		items.get(id).setEnabled(enabled);
	}
	
	/**
	 * Adds the child components to this menu.
	 */
	private void addItems() {
		List<Integer> separatorLocations = getSeparatorIndexes();
		int sepsAdded = 0;
		for (int i = 0; i < items.size(); i++) {
			if (separatorLocations.contains(i)) {
				addSeparator();
				sepsAdded++;
			} else {
				JMenuItem item = items.get(i - sepsAdded);
				add(item);
			}
		}
	}
	
	/**
	 * Creates the menu items for this menu and adds them to the list of items.
	 */
	private void createItems() {
		items = buildItems();
	}
	
	/**
	 * Creates the menu items for this menu.
	 */
	protected abstract List<JMenuItem> buildItems();
	
	/**
	 * Gets the indexes of the separators.
	 * 
	 * @return A list of indexes that give where in the menu separators should
	 * be located at.
	 */
	protected abstract List<Integer> getSeparatorIndexes();
	
	/**
	 * Returns the menu item that the index identifies.
	 * 
	 * @param id The index of the menu item.
	 */
	protected JMenuItem itemAt(int index) {
		return items.get(index);
	}
	
	/**
	 * Gets the number of items currently in this menu.
	 */
	protected int itemCount() {
		return items.size();
	}
	
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
