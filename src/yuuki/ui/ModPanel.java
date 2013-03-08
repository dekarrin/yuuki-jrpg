package yuuki.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

/**
 * Shows mods that have been enabled, and allows the user to enable/disable
 * them.
 */
@SuppressWarnings("serial")
public class ModPanel extends JPanel {
	
	/**
	 * The minimum height of a mod sub-panel.
	 */
	public static final int MOD_HEIGHT = 50;
	
	/**
	 * The list that contains all individual mods.
	 */
	private JPanel modList;
	
	/**
	 * Displays the mods.
	 */
	private static class ModList extends JPanel implements Scrollable {
		
		/**
		 * The height of the view.
		 */
		private final int viewerHeight;
		
		/**
		 * The width of the view.
		 */
		private final int viewerWidth;
		
		/**
		 * Creates a new ModList.
		 * @param width The width of the view.
		 * @param height The height of the view.
		 */
		public ModList(int width, int height) {
			viewerWidth = width;
			viewerHeight = height;
		}
		
		@Override
		public int getScrollableUnitIncrement(Rectangle visibleRect,
				int orientation, int direction) {
			final int y = visibleRect.y;
			final int h = visibleRect.height;
			if (orientation == SwingConstants.HORIZONTAL) {
				return 1;
			} else {
				if (direction > 0) {
					int viewRemainder = getInitial(y, h) + getFullRows(y, h);
					return ModPanel.MOD_HEIGHT - (h - viewRemainder);
				} else {
					return ModPanel.MOD_HEIGHT - getInitial(y, h);
				}
			}
		}
		
		@Override
		public int getScrollableBlockIncrement(Rectangle visibleRect,
				int orientation, int direction) {
			final int y = visibleRect.y;
			final int h = visibleRect.height;
			if (orientation == SwingConstants.HORIZONTAL) {
				return 1;
			} else {
				if (direction > 0) {
					return getInitial(y, h) + getFullRows(y, h);
				} else {
					return (h - getInitial(y, h));
				}
			}
		}
		
		@Override
		public Dimension getPreferredScrollableViewportSize() {
			return new Dimension(viewerWidth, viewerHeight);
		}
		
		@Override
		public boolean getScrollableTracksViewportHeight() {
			return false;
		}
		
		@Override
		public boolean getScrollableTracksViewportWidth() {
			return true;
		}
		
		/**
		 * Gets the number of full rows contained inside the view.
		 * 
		 * @param scroll The amount, in pixels, that the view is scrolled down.
		 * @param height The height, in pixels, of the view.
		 * @return The number of rows that are completely exposed within the
		 * view.
		 */
		private int getFullRows(int scroll, int height) {
			int pixelsAfterInitial = height - getInitial(scroll, height);
			return pixelsAfterInitial / ModPanel.MOD_HEIGHT;
		}
		
		/**
		 * Gets the number of pixels exposed of the first unit in the view, if
		 * and only if the first unit is partially exposed. If the first unit
		 * is fully exposed, this method will return 0.
		 * 
		 * @param scroll The amount, in pixels, that the view is scrolled down.
		 * @param height The height, in pixels, of the view.
		 * @return The number of exposed pixels of the first unit in the view,
		 * or 0 if the first unit is fully exposed.
		 */
		private int getInitial(int scroll, int height) {
			int remaining = scroll - (scroll / height);
			return ModPanel.MOD_HEIGHT - remaining;
		}
		
	}
	
	/**
	 * Creates a new ModPanel.
	 * 
	 * @param width The width of the scroll view.
	 * @param height The height of the scroll view.
	 */
	public ModPanel(int width, int height) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		createComponents(width, height);
		addComponents(width, height);
		//addMod("Super cool awesome funtime");
	}
	
	/**
	 * Adds a mod to the displayed list.
	 * 
	 * @param name The name of the mod.
	 */
	public void addMod(String name) {
		JPanel modPanel = createModPanel(name);
		modList.add(modPanel);
	}
	
	/**
	 * Adds all child components to this ModPanel.
	 * 
	 * @param width The width of this ModPanel.
	 * @param height The height of this ModPanel.
	 */
	private void addComponents(int width, int height) {
		//JPanel labelPanel = createLabelPanel(width, "Mods:");
		JScrollPane modListView = createViewPort(width, height);
		//add(labelPanel);
		//add(Box.createVerticalStrut(2));
		add(modListView);
	}
	
	/**
	 * Creates the child components of this ModPanel that are properties of it.
	 * 
	 * @param width The width of this ModPanel.
	 */
	private void createComponents(int width, int height) {
		modList = new ModList(width, height);
		modList.setBackground(Color.LIGHT_GRAY);
		modList.setLayout(new BoxLayout(modList, BoxLayout.Y_AXIS));
	//	Dimension max = modList.getMaximumSize();
	//	max.width = width;
	//	modList.setMaximumSize(max);
	}
	
	/**
	 * Creates a panel with center alignment containing a label with left
	 * alignment.
	 * 
	 * @param width The width of the panel.
	 * @param text The text of the label.
	 * @return The created panel.
	 */
	private JPanel createLabelPanel(int width, String text) {
		JPanel panel = new JPanel();
		JLabel label = new JLabel(text);
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		panel.setPreferredSize(new Dimension(width, 1));
		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.WEST);
		return panel;
	}
	
	/**
	 * Creates a panel with an enabler check box for a mod.
	 * 
	 * @return The created enabler panel.
	 */
	private JPanel createModEnablerPanel() {
		JPanel panel = new JPanel();
		JLabel label = new JLabel("Enable:");
		JCheckBox box = new JCheckBox();
		box.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setOpaque(false);
		panel.add(label);
		panel.add(Box.createHorizontalStrut(2));
		panel.add(box);
		panel.add(Box.createHorizontalStrut(10));
		return panel;
	}
	
	/**
	 * Creates a panel for a mod.
	 * 
	 * @param name The name of the mod.
	 * @return The created mod panel.
	 */
	private JPanel createModPanel(String name) {
		JPanel panel = new JPanel();
		JPanel title = createModTitlePanel(name);
		JPanel enabler = createModEnablerPanel();
		Dimension s = getPreferredSize();
		Dimension pSize = new Dimension(s.width, MOD_HEIGHT);
		//panel.setPreferredSize(pSize);
		panel.setMinimumSize(new Dimension(1, MOD_HEIGHT));
		panel.setMaximumSize(pSize);
		panel.setLayout(new BorderLayout());
		panel.add(title, BorderLayout.WEST);
		panel.add(enabler, BorderLayout.EAST);
	//	panel.setBackground();
		return panel;
	}
	
	/**
	 * Creates a title panel for a mod.
	 * 
	 * @param name The name of the mod.
	 * @return The created title panel.
	 */
	private JPanel createModTitlePanel(String name) {
		JPanel panel = new JPanel();
		JLabel label = new JLabel(name);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setOpaque(false);
		panel.add(Box.createHorizontalStrut(10));
		panel.add(label);
		return panel;
	}
	
	/**
	 * Creates the view port for the mod list.
	 * 
	 * @param width The width of the view port.
	 * @param height The height of the view port.
	 * @return The created view port.
	 */
	private JScrollPane createViewPort(int width, int height) {
		JScrollPane view = new JScrollPane(modList);
		//view.setPreferredSize(new Dimension(1, height));
		return view;
	}
	
}
