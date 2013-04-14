package yuuki.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import yuuki.graphic.ImageComponent;
import yuuki.graphic.ImageFactory;
import yuuki.item.InventoryPouch;
import yuuki.item.Item;
import yuuki.util.InvalidIndexException;

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
	public class ItemCell extends ImageComponent {
		
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
			setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
			try {
				Image img = images.createImage(item.getImage());
				setBackgroundImage(img);
			} catch (InvalidIndexException e) {
				// do nothing; simply keep the image blank
			}
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					fireClicked(e);
				}
			});
		}
		
		/**
		 * Highlights this cell.
		 */
		public void brighten() {
			setBorder(BorderFactory.createLineBorder(Color.BLUE));
		}
		
		/**
		 * Dims this cell.
		 */
		public void dim() {
			setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		}
		
		/**
		 * Fires the clicked event on the InvenPanel.
		 */
		private void fireClicked(MouseEvent e) {
			fireItemCellClicked(e, item);
		}
		
	}
	
	/**
	 * The minimum amount of padding to use between each cell.
	 */
	public static final int ITEM_CELL_MIN_SPACE = 3;
	
	/**
	 * The height and width of a cell in the items list.
	 */
	public static final int ITEM_CELL_SIZE = 50;
	
	/**
	 * Used for generating tile images.
	 */
	private ImageFactory images;
	
	/**
	 * The number of items added.
	 */
	private int itemCount = 0;
	
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
	 * Adds a new item to the list.
	 * 
	 * @param item The item to add.
	 */
	public void addItem(Item item) {
		itemCount++;
		ItemCell cell = new ItemCell(item, ITEM_CELL_SIZE);
		setCellBounds(cell);
		itemList.add(cell);
		int actualCellHeight = ITEM_CELL_SIZE + cellSpacing();
		int listHeight = cellSpacing() + getRowCount() * actualCellHeight;
		Dimension listSize = new Dimension(itemList.viewerWidth, listHeight);
		itemList.setPreferredSize(listSize);
		itemList.setMinimumSize(listSize);
	}
	
	/**
	 * Adds all items in a pouch.
	 * 
	 * @param pouch The InventoryPouch to show the contents of.
	 */
	public void addPouch(InventoryPouch pouch) {
		Item[] items = pouch.getItems();
		for (Item i : items) {
			addItem(i);
		}
	}
	
	/**
	 * Clears all items from the list.
	 */
	public void clearItems() {
		itemList.removeAll();
		itemCount = 0;
	}
	
	/**
	 * Sets the image factory for this screen.
	 * 
	 * @param factory The image factory to use.
	 */
	public void setImageFactory(ImageFactory factory) {
		this.images = factory;
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
	 * Calculates the spacing between each item cell. The spacing will always
	 * be at least the amount specified by ITEM_CELL_MIN_SPACE, but if there is
	 * enough room in this InvenPanel, spacing will stretch so that the item
	 * cells are uniformly distributed about the width of this container.
	 * 
	 * @return The calculated padding for an item cell.
	 */
	private int cellSpacing() {
		int cols = getColCount();
		int colsWidth = (ITEM_CELL_SIZE + ITEM_CELL_MIN_SPACE) * cols;
		int usedWidth = colsWidth + ITEM_CELL_MIN_SPACE;
		int extraWidth = itemList.viewerWidth - usedWidth;
		int extraSpace = extraWidth / (cols + 1);
		int totalSpace = ITEM_CELL_MIN_SPACE + extraSpace;
		return totalSpace;
	}
	
	/**
	 * Creates the components in this panel.
	 */
	private void createComponents(int width, int height) {
		itemList = new VerticalScrollPaneClient(width, height, ITEM_CELL_SIZE);
		itemList.setLayout(null);
	}
	
	/**
	 * Calls invenItemCellClicked() on the listener.
	 * 
	 * @param item The item represented by the clicked-on cell.
	 */
	private void fireItemCellClicked(MouseEvent e, Item item) {
		if (listener != null) {
			listener.itemCellClicked(e, item);
		}
	}
	
	/**
	 * Gets the number of columns that fit in a row.
	 * 
	 * @return The number of cells.
	 */
	private int getColCount() {
		int useableWidth = itemList.viewerWidth - ITEM_CELL_MIN_SPACE;
		int colCount = useableWidth / (ITEM_CELL_SIZE + ITEM_CELL_MIN_SPACE);
		return colCount;
	}
	
	/**
	 * Gets the number of rows in this panel.
	 * 
	 * @return The number of cells.
	 */
	private int getRowCount() {
		return (itemCount - 1) / getColCount() + 1;
	}
	
	/**
	 * Sets the bounds for a new item cell.
	 * 
	 * @param cell The cell to set the bounds for.
	 */
	private void setCellBounds(ItemCell cell) {
		int relX = (itemCount - 1) % getColCount();
		int relY = (itemCount - 1) / getColCount();
		int x = cellSpacing() + relX * (ITEM_CELL_SIZE + cellSpacing());
		int y = cellSpacing() + relY * (ITEM_CELL_SIZE + cellSpacing());
		cell.setBounds(x, y, ITEM_CELL_SIZE, ITEM_CELL_SIZE);
	}
	
}
