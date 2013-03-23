package yuuki.ui;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import yuuki.item.Item;

/**
 * Shows all items in the user's possession. Exactly one item may register
 * itself for click events on items in this InvenPanel.
 */
@SuppressWarnings("serial")
public class InvenPanel extends JPanel {
	
	/**
	 * A single cell in this InvenPanel. Each cell represents exactly one item
	 * in the user's inventory.
	 */
	private static class ItemCell extends JPanel {
		
		/**
		 * The item that this cell represents.
		 */
		private Item item;
		
		/**
		 * Creates a new ItemCell.
		 * 
		 * @param item The item that the new cell represents.
		 * @param size The size of the new cell.
		 */
		public ItemCell(Item item, int size) {
			this.item = item;
			setPreferredSize(new Dimension(size, size));
			addMouseListener(new MouseAdapter() {
				
			});
		}
		
	}
	
	/**
	 * The height of a cell in the items list.
	 */
	public static final int ITEM_CELL_SIZE = 40;
	
	/**
	 * Shows the items.
	 */
	private VerticalScrollPaneClient itemList;
	
	/**
	 * The listener to be notified of click events that occur on the cells of
	 * items in this InvenPanel.
	 */
	private InvenItemClickListener listener = null;
	
	/**
	 * Creates a new InvenPanel.
	 * 
	 * @param width The width of the scroll view.
	 * @param height The height of the scroll view.
	 */
	public InvenPanel(int width, int height) {
		createComponents(width, height);
		addComponents();
	}
	
	/**
	 * Clears all items from the list.
	 */
	public void clearItems() {
		itemList.removeAll();
	}
	
	/**
	 * Adds a new item to the list.
	 * 
	 * @param item The item to add.
	 */
	public void addItem(Item item) {
		ItemCell cell = new ItemCell(item, ITEM_CELL_SIZE);
		itemList.add(cell);
	}
	
	/**
	 * Registers a listener for click events on this panel.
	 * 
	 * @param listener The listener to add. Set to null for no listener.
	 */
	public void setListener(InvenItemClickListener listener) {
		this.listener = listener;
	}
	
	/**
	 * Adds the components to the panel.
	 */
	private void addComponents() {
		JScrollPane viewer = new JScrollPane(itemList);
		add(viewer);
	}
	
	/**
	 * Creates the components in this panel.
	 */
	private void createComponents(int width, int height) {
		itemList = new VerticalScrollPaneClient(width, height, ITEM_CELL_SIZE);
	}
	
	/**
	 * Calls invenItemCellClicked() on the listener.
	 * 
	 * @param item The item represented by the clicked-on cell.
	 */
	private void fireItemCellClicked(Item item) {
		if (listener != null) {
			listener.itemCellClicked(item);
		}
	}
	
}
