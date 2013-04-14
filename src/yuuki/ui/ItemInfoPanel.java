package yuuki.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
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
import yuuki.item.Item;
import yuuki.item.UsableItem;
import yuuki.util.InvalidIndexException;

/**
 * Shows information on an item.
 */
@SuppressWarnings("serial")
public class ItemInfoPanel extends JPanel {
	
	/**
	 * A listener for events fired from this class.
	 */
	public interface InfoPanelListener {
		public void dropItemClicked(Item item);
		public void useItemClicked(Item item);
	}
	
	/**
	 * The size of the description font.
	 */
	private static final int DESC_FONT_SIZE = 10;
	
	/**
	 * The size of an image in the info panel.
	 */
	private static final int IMAGE_SIZE = 100;
	
	/**
	 * The size of the usability and value fonts.
	 */
	private static final int INFO_FONT_SIZE = 10;
	
	/**
	 * The size of the title font.
	 */
	private static final int TITLE_FONT_SIZE = 12;
	
	/**
	 * The description of the item.
	 */
	private JTextArea description;
	
	/**
	 * Drops the item when clicked.
	 */
	private JButton dropButton;
	
	/**
	 * The large picture of the currently selected item.
	 */
	private ImageComponent image;
	
	/**
	 * Generates image data from indexes.
	 */
	private ImageFactory imageFactory = null;
	
	/**
	 * The listener for events.
	 */
	private InfoPanelListener listener;
	
	/**
	 * The item being shown.
	 */
	private Item shownItem = null;
	
	/**
	 * The title of the currently selected item.
	 */
	private JLabel title;
	
	/**
	 * The line that explains the use of the item.
	 */
	private JLabel usability;
	
	/**
	 * Uses the item when clicked.
	 */
	private JButton useButton;
	
	/**
	 * Shows the number of uses remaining.
	 */
	private JLabel uses;
	
	/**
	 * The line that tells the value of the item.
	 */
	private JLabel value;
	
	/**
	 * Creates a new InfoPanel.
	 * 
	 * @param width The width of the info panel.
	 * @param height The height of the info panel.
	 */
	public ItemInfoPanel(int width, int height) {
		createComponents();
		addComponents();
	}
	
	/**
	 * Sets the image factory.
	 * 
	 * @param factory The image factory to use.
	 */
	public void setImageFactory(ImageFactory factory) {
		imageFactory = factory;
	}
	
	/**
	 * Sets the listener for this panel.
	 * 
	 * @param l The listener.
	 */
	public void setListener(InfoPanelListener l) {
		listener = l;
	}
	
	/**
	 * Sets the info panel to show information on an item.
	 * 
	 * @param item The item to show information on.
	 */
	public void showInfo(Item item) {
		shownItem = item;
		setInfo();
	};
	
	/**
	 * Adds child components to this panel.
	 */
	private void addComponents() {
		Box layout = new Box(BoxLayout.Y_AXIS);
		Box valueLine = new Box(BoxLayout.X_AXIS);
		layout.add(image);
		layout.add(title);
		layout.add(usability);
		valueLine.add(value);
		valueLine.add(uses);
		layout.add(valueLine);
		layout.add(new JScrollPane(description));
		Box buttons = new Box(BoxLayout.X_AXIS);
		buttons.add(dropButton);
		buttons.add(useButton);
		layout.add(buttons);
		add(layout);
	}
	
	/**
	 * Creates all child components.
	 */
	private void createComponents() {
		image = new ImageComponent();
		image.setBackgroundImage(null);
		image.setPreferredSize(new Dimension(IMAGE_SIZE, IMAGE_SIZE));
		title = new JLabel("No item selected");
		title.setFont(new Font(Font.SERIF, Font.ITALIC, TITLE_FONT_SIZE));
		usability = new JLabel("No item selected");
		usability.setFont(new Font(Font.SANS_SERIF, Font.ITALIC,
				INFO_FONT_SIZE));
		value = new JLabel("No item selected");
		value.setFont(new Font(Font.SANS_SERIF, Font.ITALIC,
				INFO_FONT_SIZE));
		uses = new JLabel("No item selected");
		uses.setFont(new Font(Font.SANS_SERIF, Font.ITALIC,
				INFO_FONT_SIZE));
		description = new JTextArea("No item selected");
		description.setFont(new Font(Font.SERIF, Font.ITALIC,
				DESC_FONT_SIZE));
		description.setEditable(false);
		dropButton = new JButton("Drop");
		dropButton.setEnabled(false);
		dropButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.dropItemClicked(shownItem);
			}
		});
		useButton = new JButton("Use");
		useButton.setEnabled(false);
		useButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.useItemClicked(shownItem);
			}
		});
	}
	
	/**
	 * Enables buttons based on the current item.
	 */
	private void setButtons() {
		dropButton.setEnabled(shownItem != null);
		useButton.setEnabled(shownItem != null && shownItem.isExternal());
	}
	
	/**
	 * Sets the description based on the current item.
	 */
	private void setDescription() {
		String text = "";
		if (shownItem != null) {
			text = "Descriptions are not yet implemented.";
		}
		description.setText(text);
	}
	
	/**
	 * Sets the image from the current item.
	 */
	private void setImage() {
		Image img = null;
		if (shownItem != null) {
			try {
				img = imageFactory.createImage(shownItem.getImage());
			} catch (InvalidIndexException e) {
				e.printStackTrace();
			}
		}
		image.setBackgroundImage(img);
	}
	
	/**
	 * Sets the info panel for all information based on the current item.
	 */
	private void setInfo() {
		setImage();
		setTitle();
		setValueLine();
		setUsability();
		setDescription();
		setButtons();
	}
	
	/**
	 * Sets the title from the current item.
	 */
	private void setTitle() {
		String titleText = "";
		if (shownItem != null) {
			titleText = shownItem.getName();
		}
		title.setText(titleText);
	}
	
	/**
	 * Sets the usability line from the current item.
	 */
	private void setUsability() {
		String usabilityText = "";
		if (shownItem != null) {
			if (shownItem.isUsable()) {
				usabilityText = "May be used";
				if (!shownItem.isExternal()) {
					usabilityText += " in battle only";
				}
			} else {
				usabilityText = "Not usable";
			}
		}
		usability.setText(usabilityText);
	}
	
	/**
	 * Sets the value and uses line from the current item.
	 */
	private void setValueLine() {
		String usesText = "";
		String valueText = "No item selected";
		if (shownItem != null) {
			if (shownItem.isUsable()) {
				UsableItem item = ((UsableItem) shownItem);
				usesText = item.getUses() + " uses left";
			}
			valueText = shownItem.getValue() + "cp";
		}
		value.setText(valueText);
		uses.setText(usesText);
	}
	
}