package yuuki.ui.screen;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The screen containing the options for the game.
 */
@SuppressWarnings("serial")
public class OptionsScreen extends Screen implements ChangeListener {
	
	/**
	 * The slider for background music volume.
	 */
	private JSlider bgmVolumeSlider;
	
	/**
	 * The slider for sound effects volume.
	 */
	private JSlider sfxVolumeSlider;
	
	/**
	 * The form submission button.
	 */
	private JButton submitButton;
	
	/**
	 * Creates a new OptionsScreen. The child components are created and added.
	 * 
	 * @param width The width of the screen.
	 * @param height the height of the screen.
	 */
	public OptionsScreen(int width, int height) {
		super(width, height);
		Box form = new Box(BoxLayout.Y_AXIS);
		bgmVolumeSlider = createPercentSlider();
		sfxVolumeSlider = createPercentSlider();
		submitButton = new JButton("OK");
		form.add(createLabeledComponent("BGM: ", bgmVolumeSlider));
		form.add(createLabeledComponent("SFX: ", sfxVolumeSlider));
		form.add(submitButton);
		add(form);
	}
	
	/**
	 * Sets the initial focus to the primary element in the options screen.
	 */
	public void setInitialFocus() {
		
	}
	
	/**
	 * Called when a slider is moved.
	 */
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		if (!source.getValueIsAdjusting()) {
			
		}
	}
	
	/**
	 * Creates a slider for discrete values in the range [0, 100].
	 * 
	 * @return The slider.
	 */
	private JSlider createPercentSlider() {
		JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		slider.addChangeListener(this);
		return slider;
	}
	
	/**
	 * Creates a panel containing a label and a component.
	 * 
	 * @param label The String that the label should contain.
	 * @param component The component that is to be labeled.
	 * 
	 * @return The panel.
	 */
	private JPanel createLabeledComponent(String label, JComponent component) {
		JPanel panel = new JPanel();
		panel.add(new JLabel(label));
		panel.add(component);
		return panel;
	}
	
}
