package yuuki.ui;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 * Shows mods that have been enabled, and allows the user to enable/disable
 * them.
 */
public class ModPanel extends JPanel {
	
	private static final long serialVersionUID = 6077820699428016095L;

	/**
	 * Creates a new ModPanel.
	 * 
	 * @param width The width of the panel.
	 * @param height The height of the panel.
	 */
	public ModPanel(int width, int height) {
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		final int SIZE = 11;
		Integer[] nums = new Integer[SIZE];
		for (int i = 0; i < SIZE; i++) {
			nums[i] = i + 1;
		}
		@SuppressWarnings({"rawtypes", "unchecked"})
		JList list = new JList(nums);
		list.setPreferredSize(size);
		list.setMinimumSize(new Dimension(width, 1));
		Box layout = Box.createVerticalBox();
		layout.setPreferredSize(size);
		layout.setMinimumSize(size);
		layout.setMaximumSize(size);
		layout.setSize(size);
	//	layout.add(new JLabel("Mods:"));
		add(layout);
		layout.add(list);
	}
	
}
