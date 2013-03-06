package yuuki.ui.screen;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import yuuki.Options;
import yuuki.ui.ModPanel;

/**
 * The screen containing the options for the game.
 */
@SuppressWarnings("serial")
public class OptionsScreen extends Screen<OptionsScreenListener> implements
ChangeListener {
	
	/**
	 * The slider for background music volume.
	 */
	private JSlider bgmVolumeSlider;
	
	/**
	 * The panel for mod loading.
	 */
	private ModPanel modPanel;
	
	/**
	 * Listens for enter presses.
	 */
	private KeyListener enterListener = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (e.getSource() == submitButton) {
					fireEnterClicked();
				} else if (e.getSource() == sfxTestButton) {
					fireSfxClicked();
				}
			}
		}
	};
	
	/**
	 * Button for testing SFX.
	 */
	private JButton sfxTestButton;
	
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
		MouseListener mAdapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getSource() == submitButton) {
					fireEnterClicked();
				} else if (e.getSource() == sfxTestButton) {
					fireSfxClicked();
				}
			}
		};
		Box form = new Box(BoxLayout.Y_AXIS);
		bgmVolumeSlider = createPercentSlider();
		sfxVolumeSlider = createPercentSlider();
		sfxTestButton = new JButton("SFX Test");
		sfxTestButton.addMouseListener(mAdapter);
		sfxTestButton.addKeyListener(enterListener);
		submitButton = new JButton("OK");
		submitButton.addMouseListener(mAdapter);
		submitButton.addKeyListener(enterListener);
		modPanel = new ModPanel(width - 80, 200);
		form.add(createLabeledComponent("BGM: ", bgmVolumeSlider));
		form.add(createLabeledComponent("SFX: ", sfxVolumeSlider));
		form.add(sfxTestButton);
		form.add(submitButton);
		form.add(modPanel);
		add(form);
	}
	
	/**
	 * Sets the initial focus to the primary element in the options screen.
	 */
	@Override
	public void setInitialProperties() {
		submitButton.requestFocus();
	}
	
	/**
	 * Sets all fields to their current values.
	 * 
	 * @param options The options object.
	 */
	public void setValues(Options options) {
		bgmVolumeSlider.setValue(options.bgmVolume);
		sfxVolumeSlider.setValue(options.sfxVolume);
	}
	
	/**
	 * Called when a slider is moved.
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		if (!source.getValueIsAdjusting()) {
			if (source == bgmVolumeSlider) {
				fireBgmLevelChanged();
			} else if (source == sfxVolumeSlider) {
				fireSfxLevelChanged();
			}
		}
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
	
	/**
	 * Creates a slider for discrete values in the range [0, 100].
	 * 
	 * @return The slider.
	 */
	private JSlider createPercentSlider() {
		JSlider slider = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 0);
		slider.addChangeListener(this);
		slider.addKeyListener(enterListener);
		return slider;
	}
	
	/**
	 * Calls bgmVolumeChanged() on all listeners.
	 */
	private void fireBgmLevelChanged() {
		int volume = bgmVolumeSlider.getValue();
		for (OptionsScreenListener l : getElementListeners()) {
			l.bgmVolumeChanged(volume);
		}
	}
	
	/**
	 * Calls optionsSubmitted() on all listeners.
	 */
	private void fireEnterClicked() {
		for (OptionsScreenListener l : getElementListeners()) {
			l.optionsSubmitted();
		}
	}
	
	/**
	 * Calls sfxTestClicked() on all listeners.
	 */
	private void fireSfxClicked() {
		for (OptionsScreenListener l : getElementListeners()) {
			l.sfxTestClicked();
		}
	}
	
	/**
	 * Calls sfxVolumeChanged() on all listeners.
	 */
	private void fireSfxLevelChanged() {
		int volume = sfxVolumeSlider.getValue();
		for (OptionsScreenListener l : getElementListeners()) {
			l.sfxVolumeChanged(volume);
		}
	}
	
}
