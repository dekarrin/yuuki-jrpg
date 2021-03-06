package yuuki.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
	 * The color of a mod with the alternate color scheme.
	 */
	private static final Color MOD_COLOR_ALT = new Color(0xdadada);
	
	/**
	 * The color of a mod with the main color scheme.
	 */
	private static final Color MOD_COLOR_MAIN = new Color(0xc9c9c9);
	
	/**
	 * Calls methods on this object when a mod is enabled/disabled.
	 */
	private ModControlListener listener = null;
	
	/**
	 * Whether the next mod to be added should have alt colors.
	 */
	private boolean modAltScheme;
	
	/**
	 * Maps the enablers to mod identifiers.
	 */
	private Map<Object, String> modIndexes = new HashMap<Object, String>();
	
	/**
	 * The list that contains all individual mods.
	 */
	private VerticalScrollPaneClient modList;
	
	/**
	 * Whether any mods have been added to the list.
	 */
	private boolean modsAdded;
	
	/**
	 * Creates a new ModPanel.
	 * 
	 * @param width The width of the scroll view.
	 * @param height The height of the scroll view.
	 */
	public ModPanel(int width, int height) {
		modsAdded = false;
		modAltScheme = false;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		createComponents(width, height);
		addComponents();
	}
	
	/**
	 * Adds a mod to the displayed list.
	 * 
	 * @param name The name of the mod.
	 * @param id A string that uniquely identifies the mod.
	 */
	public void addMod(String name, String id) {
		if (!modsAdded) {
			modList.removeAll();
			modsAdded = true;
		}
		JPanel modPanel = createModPanel(name, id);
		modList.add(modPanel);
	}
	
	/**
	 * Clears all mods from the list.
	 */
	public void clearMods() {
		modIndexes.clear();
		modList.removeAll();
		modList.add(createNoModsPanel());
		modsAdded = false;
		modAltScheme = false;
	}
	
	/**
	 * Sets the listener for mod control events.
	 * 
	 * @param listener The listener.
	 */
	public void setModListener(ModControlListener listener) {
		this.listener = listener;
	}
	
	/**
	 * Adds all child components to this ModPanel.
	 */
	private void addComponents() {
		JPanel labelPanel = createLabelPanel("Mods:");
		JScrollPane modListView = createViewPort();
		JPanel noMods = createNoModsPanel();
		modList.add(noMods);
		add(labelPanel);
		add(Box.createVerticalStrut(2));
		add(modListView);
	}
	
	/**
	 * Creates the child components of this ModPanel that are properties of it.
	 * 
	 * @param width The width of this ModPanel.
	 * @param height The height of this ModPanel.
	 */
	private void createComponents(int width, int height) {
		modList = new VerticalScrollPaneClient(width, height, MOD_HEIGHT);
		modList.setLayout(new BoxLayout(modList, BoxLayout.Y_AXIS));
	}
	
	/**
	 * Creates a listener for a mod enabler check box.
	 * 
	 * @return The created listener.
	 */
	private ItemListener createEnablerListener() {
		return new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				String id = modIndexes.get(e.getSource());
				if (id != null) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						fireModEnabled(id);
					} else {
						fireModDisabled(id);
					}
				}
			}
		};
	}
	
	/**
	 * Creates a panel with center alignment containing a label with left
	 * alignment.
	 * 
	 * @param text The text of the label.
	 * @return The created panel.
	 */
	private JPanel createLabelPanel(String text) {
		JPanel panel = new JPanel();
		JLabel label = new JLabel(text);
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.WEST);
		return panel;
	}
	
	/**
	 * Creates a panel with an enabler check box for a mod.
	 * 
	 * @param id A unique identifier for the mod.
	 * @return The created enabler panel.
	 */
	private JPanel createModEnablerPanel(String id) {
		JPanel panel = new JPanel();
		JLabel label = new JLabel("Enable:");
		JCheckBox box = new JCheckBox();
		modIndexes.put(box, id);
		box.addItemListener(createEnablerListener());
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
	 * @param id A unique identifier for the mod.
	 * @return The created mod panel.
	 */
	private JPanel createModPanel(String name, String id) {
		JPanel panel = new JPanel();
		JPanel title = createModTitlePanel(name);
		JPanel enabler = createModEnablerPanel(id);
		Dimension s = modList.getPreferredScrollableViewportSize();
		panel.setPreferredSize(new Dimension(s.width, MOD_HEIGHT));
		panel.setLayout(new BorderLayout());
		panel.add(title, BorderLayout.WEST);
		panel.add(enabler, BorderLayout.EAST);
		if (modAltScheme) {
			panel.setBackground(MOD_COLOR_MAIN);
		} else {
			panel.setBackground(MOD_COLOR_ALT);
		}
		modAltScheme = ! modAltScheme;
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
	 * Creates a panel with a message indicated that no mods have been loaded.
	 * 
	 * @return The created panel.
	 */
	private JPanel createNoModsPanel() {
		JPanel panel = new JPanel();
		JLabel label = new JLabel("No mods detected.");
		label.setFont(new Font(Font.SERIF, Font.ITALIC, 12));
		panel.add(label);
		panel.setOpaque(false);
		return panel;
	}
	
	/**
	 * Creates the view port for the mod list.
	 * 
	 * @return The created view port.
	 */
	private JScrollPane createViewPort() {
		JScrollPane view = new JScrollPane(modList);
		view.getViewport().setBackground(new Color(0xb8b8b8));
		return view;
	}
	
	/**
	 * Calls modDisabled() on the listener.
	 * 
	 * @param id The ID of the disabled mod.
	 */
	private void fireModDisabled(String id) {
		if (listener != null) {
			listener.modDisabled(id);
		}
	}
	
	/**
	 * Calls modEnabled() on the listener.
	 * 
	 * @param id The ID of the enabled mod.
	 */
	private void fireModEnabled(String id) {
		if (listener != null) {
			listener.modEnabled(id);
		}
	}
	
}
