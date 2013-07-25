package yuuki.ui.screen;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import yuuki.graphic.ImageFactory;
import yuuki.item.InventoryPouch;
import yuuki.item.Item;
import yuuki.ui.InvenItemClickListener;
import yuuki.ui.InvenPanel;
import yuuki.ui.ItemInfoPanel;

/**
 * Shows the user's inventory and stats.
 */
@SuppressWarnings("serial")
public class InventoryScreen extends Screen<InvenScreenListener> {
	
	/**
	 * The height of the inventory panel is the screen width minus this amount.
	 */
	private static final int INVEN_PANEL_HEIGHT_OFFSET = 100;
	
	/**
	 * The width of the inventory panel is the screen width minus this amount.
	 */
	private static final int INVEN_PANEL_WIDTH_OFFSET = 400;
	
	/**
	 * Closes the inventory screen.
	 */
	private JButton closeButton;
	
	/**
	 * Shows information on an item.
	 */
	private ItemInfoPanel infoPanel;
	
	/**
	 * Shows the inventory.
	 */
	private InvenPanel invenPanel;
	
	/**
	 * Listens for key pushes.
	 */
	private KeyListener keyListener = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_ESCAPE:
					fireCloseInvenClicked();
					break;
			}
		}
	};
	
	/**
	 * The itemCell that is currently lit.
	 */
	private InvenPanel.ItemCell litCell = null;
	
	/**
	 * Creates a new Inventory Screen.
	 * 
	 * @param width The width of the screen.
	 * @param height The height of the screen.
	 */
	public InventoryScreen(int width, int height) {
		super(width, height);
		addKeyListener(keyListener);
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
	 * Sets the enabled status of the Drop button. By default, the enabled
	 * status is 'true'.
	 * 
	 * @param b What to set the enabled status to.
	 */
	public void setDropButtonEnabled(boolean b) {
		infoPanel.setDropButtonEnabled(b);
	}
	
	/**
	 * Sets the image factory for this screen.
	 * 
	 * @param factory The image factory to use.
	 */
	public void setImageFactory(ImageFactory factory) {
		infoPanel.setImageFactory(factory);
		invenPanel.setImageFactory(factory);
	}
	
	@Override
	public void setInitialProperties() {
		dimSelection();
		infoPanel.showInfo(null);
		this.requestFocus();
	}
	
	/**
	 * Adds all child components to this InventoryScreen.
	 */
	private void addComponents() {
		Box form = new Box(BoxLayout.Y_AXIS);
		Box row1 = new Box(BoxLayout.X_AXIS);
		row1.add(invenPanel);
		row1.add(infoPanel);
		form.add(row1);
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
		invenPanel = new InvenPanel(width - INVEN_PANEL_WIDTH_OFFSET,
				height - INVEN_PANEL_HEIGHT_OFFSET);
		invenPanel.setListener(new InvenItemClickListener() {
			@Override
			public void itemCellClicked(MouseEvent e, Item item) {
				selectItem(e, item);
			}
			@Override
			public void itemDeselected() {
				deselectItem();
			}
		});
		closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireCloseInvenClicked();
			}
		});
		closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		infoPanel = new ItemInfoPanel(INVEN_PANEL_WIDTH_OFFSET - 10, height);
		infoPanel.setListener(new ItemInfoPanel.InfoPanelListener() {
			@Override
			public void dropItemClicked(Item item) {
				fireDropItemClicked(item);
			}
			@Override
			public void useItemClicked(Item item) {
				fireUseItemClicked(item);
			}
		});
	}
	
	/**
	 * Deselects an item.
	 */
	private void deselectItem() {
		dimSelection();
		infoPanel.showInfo(null);
	}
	
	/**
	 * Dims the currently selected cell, if there is one.
	 */
	private void dimSelection() {
		if (litCell != null) {
			litCell.dim();
		}
	}
	
	/**
	 * Calls closeInvenClicked() on all listeners.
	 */
	private void fireCloseInvenClicked() {
		for (InvenScreenListener l : getElementListeners()) {
			l.closeInvenClicked();
		}
	}
	
	/**
	 * Calls dropItemClicked() on all listeners.
	 * 
	 * @param item The item that was selected when the button was clicked.
	 */
	private void fireDropItemClicked(Item item) {
		for (InvenScreenListener l : getElementListeners()) {
			l.dropItemClicked(item);
		}
	}
	
	/**
	 * Calls useItemClicked() on all listeners.
	 * 
	 * @param item The item that was selected when the button was clicked.
	 */
	private void fireUseItemClicked(Item item) {
		for (InvenScreenListener l : getElementListeners()) {
			l.useItemClicked(item);
		}
	}
	
	/**
	 * Called when an inventory item is clicked.
	 * 
	 * @param e The MouseEvent that caused it.
	 * @param item The item that was clicked.
	 */
	private void selectItem(MouseEvent e, Item item) {
		dimSelection();
		InvenPanel.ItemCell cell = (InvenPanel.ItemCell) e.getSource();
		litCell = cell;
		cell.brighten();
		infoPanel.showInfo(item);
		repaint();
	}
	
}
