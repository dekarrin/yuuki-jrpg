package yuuki.ui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;

/**
 * The file menu for the game.
 */
@SuppressWarnings("serial")
public class FileMenu extends Menu<FileMenuListener> implements ActionListener {
	
	/**
	 * Closes the current game when selected.
	 */
	private JMenuItem closeItem;
	
	/**
	 * Exits the game when selected.
	 */
	private JMenuItem exitItem;
	
	/**
	 * Loads a previous game when selected.
	 */
	private JMenuItem loadItem;
	
	/**
	 * Starts a new game when selected.
	 */
	private JMenuItem newItem;
	
	/**
	 * Goes to the options screen when selected.
	 */
	private JMenuItem optionsItem;
	
	/**
	 * Saves the current game when selected.
	 */
	private JMenuItem saveItem;
	
	/**
	 * Creates a new file menu. The child components are created and added.
	 */
	public FileMenu() {
		super("File", "The File menu", KeyEvent.VK_F);
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	protected void addItems() {
		add(newItem);
		add(loadItem);
		add(closeItem);
		add(saveItem);
		addSeparator();
		add(optionsItem);
		addSeparator();
		add(exitItem);
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	protected void buildItems() {
		newItem = new JMenuItem("New Game");
		loadItem = new JMenuItem("Load Game");
		closeItem = new JMenuItem("Close Game");
		saveItem = new JMenuItem("Save Game");
		optionsItem = new JMenuItem("Options");
		exitItem = new JMenuItem("Exit");
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	protected void setItemAccelerators() {
		// TODO: add accelerators
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	protected void setItemDescriptions() {
		String newHelp = "Starts a new game";
		String loadHelp = "Loads a previous game";
		String closeHelp = "Returns to the main menu";
		String saveHelp = "Saves the current game";
		String optHelp = "Brings up the options screen";
		String exitHelp = "Quits the game";
		newItem.getAccessibleContext().setAccessibleDescription(newHelp);
		loadItem.getAccessibleContext().setAccessibleDescription(loadHelp);
		closeItem.getAccessibleContext().setAccessibleDescription(closeHelp);
		saveItem.getAccessibleContext().setAccessibleDescription(saveHelp);
		optionsItem.getAccessibleContext().setAccessibleDescription(optHelp);
		exitItem.getAccessibleContext().setAccessibleDescription(exitHelp);
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	protected void setItemMnemonics() {
		newItem.setMnemonic(KeyEvent.VK_N);
		loadItem.setMnemonic(KeyEvent.VK_L);
		closeItem.setMnemonic(KeyEvent.VK_C);
		saveItem.setMnemonic(KeyEvent.VK_S);
		optionsItem.setMnemonic(KeyEvent.VK_O);
		exitItem.setMnemonic(KeyEvent.VK_X);
	}

	/**
	 * Called when a menu item is selected.
	 * 
	 * @param e The source event.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == exitItem) {
			fireExitItemClicked();
		} else if (e.getSource() == newItem) {
			fireNewItemClicked();
		} else if (e.getSource() == optionsItem) {
			fireOptionsItemClicked();
		} else if (e.getSource() == closeItem) {
			fireCloseItemClicked();
		}
	}
	
	/**
	 * Calls exitItemTriggered() on all listeners.
	 */
	public void fireExitItemClicked() {
		for (FileMenuListener l: getElementListeners()) {
			l.exitItemTriggered();
		}
	}
	
	/**
	 * Calls newItemTriggered() on all listeners.
	 */
	public void fireNewItemClicked() {
		for (FileMenuListener l: getElementListeners()) {
			l.newItemTriggered();
		}
	}
	
	/**
	 * Calls optionsItemTriggered() on all listeners.
	 */
	public void fireOptionsItemClicked() {
		for (FileMenuListener l: getElementListeners()) {
			l.optionsItemTriggered();
		}
	}
	
	/**
	 * Calls closeItemTriggered() on all listeners.
	 */
	public void fireCloseItemClicked() {
		for (FileMenuListener l: getElementListeners()) {
			l.closeItemTriggered();
		}
	}
	
}
