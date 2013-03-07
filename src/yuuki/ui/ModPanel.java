package yuuki.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

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
	private static final int MOD_HEIGHT = 20;
	
	/**
	 * The list that contains all individual mods.
	 */
	private JPanel modList;
	
	/**
	 * Creates a new ModPanel.
	 * 
	 * @param width The width of the panel.
	 * @param height The height of the panel.
	 */
	public ModPanel(int width, int height) {
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		createComponents(width);
		addComponents(width, height);
		addMod("Super cool awesome funtime");
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
		JPanel labelPanel = createLabelPanel(width, "Mods:");
		JScrollPane modListView = createViewPort(width, height);
		add(labelPanel);
		add(Box.createVerticalStrut(2));
		add(modListView);
	}
	
	/**
	 * Creates the child components of this ModPanel that are properties of it.
	 * 
	 * @param width The width of this ModPanel.
	 */
	private void createComponents(int width) {
		modList = new JPanel();
		modList.setBackground(Color.LIGHT_GRAY);
		modList.setLayout(new BoxLayout(modList, BoxLayout.Y_AXIS));
		Dimension max = modList.getMaximumSize();
		max.width = width;
		modList.setMaximumSize(max);
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
		panel.setMinimumSize(new Dimension(width, 1));
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
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(label);
		panel.add(box);
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
	//	JPanel title = createModTitlePanel(name);
	//	JPanel enabler = createModEnablerPanel();
		Dimension s = getPreferredSize();
		Dimension pSize = new Dimension(s.width, MOD_HEIGHT);
		panel.setPreferredSize(pSize);
		panel.setMinimumSize(pSize);
		panel.setMaximumSize(pSize);
		panel.setLayout(new BorderLayout());
	//	panel.add(title, BorderLayout.WEST);
	//	panel.add(enabler, BorderLayout.EAST);
		panel.setBackground(Color.RED);
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
		view.setPreferredSize(new Dimension(1, height));
		//view.setMinimumSize(new Dimension(width, 1));
		return view;
	}
	
}
