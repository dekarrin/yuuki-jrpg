package yuuki.ui.screen;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import yuuki.graphic.ImageComponent;
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
	 * Shows the inventory.
	 */
	private InvenPanel invenPanel;
	
	/**
	 * Shows information on an item.
	 */
	private InfoPanel infoPanel;
	
	/**
	 * Shows information on an item.
	 */
	private static class InfoPanel extends JPanel {
		
		/**
		 * The large picture of the currently selected item.
		 */
		private ImageComponent image;
		
		/**
		 * The title of the currently selected item.
		 */
		private JLabel title;
		
		/**
		 * The line that explains the use of the item.
		 */
		private JLabel usability;
		
		/**
		 * The line that tells the value of the item.
		 */
		private JLabel value;
		
		/**
		 * The description of the item.
		 */
		private JTextArea description;
		
		/**
		 * Drops the item when clicked.
		 */
		private JButton dropButton;
		
		/**
		 * Uses the item when clicked.
		 */
		private JButton useButton;
		
		/**
		 * Creates a new InfoPanel.
		 * 
		 * @param width The width of the info panel.
		 * @param height The height of the info panel.
		 */
		public InfoPanel(int width, int height) {
			createComponents();
			addComponents();
		}
		
		/**
		 * Adds child components to this panel.
		 */
		private void addComponents() {
			Box layout = new Box(BoxLayout.Y_AXIS);
			layout.add(image);
			layout.add(value);
			layout.add(usability);
			layout.add(new JScrollPane(description));
			Box buttons = new Box(BoxLayout.X_AXIS);
			buttons.add(dropButton);
			buttons.add(useButton);
			layout.add(buttons);
			add(layout);
		}
		
		/**
		 * The size of the title font.
		 */
		private static final int TITLE_FONT_SIZE = 10;
		
		/**
		 * The size of the usability and value fonts.
		 */
		private static final int INFO_FONT_SIZE = 8;
		
		/**
		 * The size of the description font.
		 */
		private static final int DESC_FONT_SIZE = 8;
		
		/**
		 * Creates all child components.
		 */
		private void createComponents() {
			image = new ImageComponent();
			image.setBackgroundImage(null);
			title = new JLabel("No item selected");
			title.setFont(new Font(Font.SERIF, Font.ITALIC, TITLE_FONT_SIZE));
			usability = new JLabel("No item selected");
			usability.setFont(new Font(Font.SANS_SERIF, Font.ITALIC,
					INFO_FONT_SIZE));
			value = new JLabel("No item selected");
			value.setFont(new Font(Font.SANS_SERIF, Font.ITALIC,
					INFO_FONT_SIZE));
			description = new JTextArea("No item selected");
			description.setFont(new Font(Font.SERIF, Font.ITALIC,
					DESC_FONT_SIZE));
			dropButton = new JButton("Drop");
			dropButton.setEnabled(false);
			useButton = new JButton("Use");
			useButton.setEnabled(false);
		}
		
	}
	
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
		closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireCloseInvenClicked();
			}
		});
		closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		infoPanel = new InfoPanel(INVEN_PANEL_WIDTH_OFFSET - 10, height);
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
