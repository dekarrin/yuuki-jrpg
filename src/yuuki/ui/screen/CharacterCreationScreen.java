package yuuki.ui.screen;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CharacterCreationScreen extends JPanel implements MouseListener {
	
	private ArrayList<CharacterCreationScreenListener> listeners;
	
	private JButton createCharacterButton;
	
	private JTextField nameField;

	public CharacterCreationScreen() {
		setLayout(new FlowLayout());
		listeners = new ArrayList<CharacterCreationScreenListener>();
		createCharacterButton = new JButton("Create Character");
		nameField = new JTextField(40);
		createCharacterButton.addMouseListener(this);
		add(new JLabel("Enter Character name"));
		add(nameField);
		add(createCharacterButton);
	}
	
	public String getData() {
		return nameField.getText();
	}
	
	public void mouseClicked(MouseEvent e) {
		Component c = e.getComponent();
		if (c == createCharacterButton) {
			fireCreateCharacterClicked();
		}
	}
	
	public void mouseReleased(MouseEvent e) {}
	
	public void mousePressed(MouseEvent e) {}
	
	public void mouseExited(MouseEvent e) {}
	
	public void mouseEntered(MouseEvent e) {}
	
	private void fireCreateCharacterClicked() {
		for (CharacterCreationScreenListener l: listeners) {
			l.createCharacterClicked();
		}
	}
	
}
