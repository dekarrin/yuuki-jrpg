package yuuki.ui.screen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

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
	 * Creates a new Inventory Screen.
	 * 
	 * @param width The width of the screen.
	 * @param height The height of the screen.
	 */
	public InventoryScreen(int width, int height) {
		super(width, height);
		closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireCloseInvenClicked();
			}
		});
		addComponents();
	}
	
	@Override
	public void setInitialProperties() {}
	
	/**
	 * Adds all child components to this InventoryScreen.
	 */
	private void addComponents() {
		add(closeButton);
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
