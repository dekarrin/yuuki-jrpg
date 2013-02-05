package yuuki.ui.screen;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 * The screen displayed during character creation.
 */
@SuppressWarnings("serial")
public class CharacterCreationScreen extends
Screen<CharacterCreationScreenListener> implements MouseListener {
	
	/**
	 * The string that the text in the name field is initially set to.
	 */
	private static final String DEFAULT_NAME = "Dekarrin";
	
	/**
	 * The button that creates the character and advances to the next screen.
	 */
	private JButton createCharacterButton;
	
	/**
	 * The field for selecting character level in.
	 */
	private JSpinner levelField;
	
	/**
	 * The field to enter the character's name into.
	 */
	private JTextField nameField;
	
	/**
	 * Creates a new CharacterCreationScreen. The child components are created
	 * and added to this screen.
	 * 
	 * @param width The width of the screen.
	 * @param height The height of the screen.
	 */
	public CharacterCreationScreen(int width, int height) {
		super(width, height);
		setLayout(new FlowLayout());
		createCharacterButton = new JButton("Create Character");
		nameField = new JTextField(40);
		levelField = new JSpinner(new SpinnerNumberModel(10, 1, 50, 1));
		ActionListener enterActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireCreateCharacterClicked();
			}
		};
		KeyListener enterKeyListener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					fireCreateCharacterClicked();
				}
			}
		};
		nameField.addActionListener(enterActionListener);
		nameField.setText(DEFAULT_NAME);
		createCharacterButton.addMouseListener(this);
		createCharacterButton.addKeyListener(enterKeyListener);
		add(new JLabel("Name: "));
		add(nameField);
		add(new JLabel("Level: "));
		add(levelField);
		add(createCharacterButton);
	}
	
	/**
	 * Gets the level entered by the user.
	 * 
	 * @return The level.
	 */
	public int getEnteredLevel() {
		Integer obj = (Integer) levelField.getValue();
		return obj.intValue();
	}
	
	/**
	 * Gets the name entered by the user.
	 * 
	 * @return The name.
	 */
	public String getEnteredName() {
		return nameField.getText();
	}
	
	/**
	 * Calls createCharacterClicked() on each of the listeners. This method is
	 * called when the user clicks the create character button.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		Component c = e.getComponent();
		if (c == createCharacterButton) {
			fireCreateCharacterClicked();
		}
	}
	
	/**
	 * Not used.
	 */
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	/**
	 * Not used.
	 */
	@Override
	public void mouseExited(MouseEvent e) {}
	
	/**
	 * Not used.
	 */
	@Override
	public void mousePressed(MouseEvent e) {}
	
	/**
	 * Not used.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {}
	
	/**
	 * Sets the initial focus of this screen to the name field.
	 */
	@Override
	public void setInitialProperties() {
		nameField.requestFocus();
	}
	
	/**
	 * Calls createCharacterClicked() on each of the listeners registered to
	 * this screen.
	 */
	private void fireCreateCharacterClicked() {
		for (CharacterCreationScreenListener l : getElementListeners()) {
			l.createCharacterClicked();
		}
	}
	
}
