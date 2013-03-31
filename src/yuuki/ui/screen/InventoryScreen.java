package yuuki.ui.screen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import yuuki.ui.InvenPanel;

/**
 * Shows the user's inventory and stats.
 */
@SuppressWarnings("serial")
public class InventoryScreen extends Screen<InvenScreenListener> {
	
	/**
	 * Closes the inventory screen.
	 */
	private JButton closeButton;
	
	/**
	 * Shows the inventory.
	 */
	private InvenPanel invenPanel;
	
	/**
	 * Creates a new Inventory Screen.
	 * 
	 * @param width The width of the screen.
	 * @param height The height of the screen.
	 */
	public InventoryScreen(int width, int height) {
		super(width, height);
		createComponents(width, height);
		addComponents();
	}
	
	@Override
	public void setInitialProperties() {}
	
	/**
	 * Adds all child components to this InventoryScreen.
	 */
	private void addComponents() {
		add(invenPanel);
		add(closeButton);
	}
	
	/**
	 * Creates the child components.
	 * 
	 * @param width The width of this screen.
	 * @param height The height of this screen.
	 */
	private void createComponents(int width, int height) {
		invenPanel = new InvenPanel(width, height);
		closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireCloseInvenClicked();
			}
		});
	}
	
	/**
	 * Calls closeInvenClicked() on all listeners.
	 */
	private void fireCloseInvenClicked() {
		for (InvenScreenListener l : getElementListeners()) {
			l.closeInvenClicked();
		}
	}
	
}
