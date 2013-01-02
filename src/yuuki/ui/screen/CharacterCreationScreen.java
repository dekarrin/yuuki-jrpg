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
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

@SuppressWarnings("serial")
public class CharacterCreationScreen extends Screen implements MouseListener {
	
	private JButton createCharacterButton;
	
	private final String DEFAULT_NAME = "Dekarrin";
	
	private JSpinner levelField;
	
	private ArrayList<CharacterCreationScreenListener> listeners;
	
	private JTextField nameField;
	
	public CharacterCreationScreen(int width, int height) {
		super(width, height);
		setLayout(new FlowLayout());
		listeners = new ArrayList<CharacterCreationScreenListener>();
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
	
	public void addListener(CharacterCreationScreenListener l) {
		listeners.add(l);
	}
	
	public int getEnteredLevel() {
		Integer obj = (Integer) levelField.getValue();
		return obj.intValue();
	}
	
	public String getEnteredName() {
		return nameField.getText();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Component c = e.getComponent();
		if (c == createCharacterButton) {
			fireCreateCharacterClicked();
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void mousePressed(MouseEvent e) {}
	
	@Override
	public void mouseReleased(MouseEvent e) {}
	
	@Override
	public void setInitialFocus() {
		nameField.requestFocus();
	}
	
	private void fireCreateCharacterClicked() {
		for (CharacterCreationScreenListener l: listeners) {
			l.createCharacterClicked();
		}
	}
	
}
