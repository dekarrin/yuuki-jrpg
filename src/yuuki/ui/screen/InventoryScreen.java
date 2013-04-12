package yuuki.ui.screen;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import yuuki.graphic.ImageFactory;
import yuuki.item.InventoryPouch;
import yuuki.item.Item;
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
	
	/**
	 * Adds an item to the inventory panel.
	 * 
	 * @param item The item to add.
	 */
	public void addItem(Item item) {
		invenPanel.addItem(item);
	}
	
	/**
	 * Adds all items in a pouch to the inventory panel.
	 * 
	 * @param pouch The pouch whose items to add.
	 */
	public void addPouch(InventoryPouch pouch) {
		invenPanel.addPouch(pouch);
	}
	
	/**
	 * Clears the inventory panel.
	 */
	public void clearInventory() {
		invenPanel.clearItems();
	}
	
	/**
	 * Sets the image factory for this screen.
	 * 
	 * @param factory The image factory to use.
	 */
	public void setImageFactory(ImageFactory factory) {
		invenPanel.setImageFactory(factory);
	}
	
	@Override
	public void setInitialProperties() {}
	
	/**
	 * Adds all child components to this InventoryScreen.
	 */
	private void addComponents() {
		Box form = new Box(BoxLayout.Y_AXIS);
		form.add(invenPanel);
		form.add(closeButton);
		add(form);
	}
	
	/**
	 * Creates the child components.
	 * 
	 * @param width The width of this screen.
	 * @param height The height of this screen.
	 */
	private void createComponents(int width, int height) {
		invenPanel = new InvenPanel(width - 50, height - 100);
		closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireCloseInvenClicked();
			}
		});
		closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
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
