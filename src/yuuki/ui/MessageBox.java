package yuuki.ui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import yuuki.entity.Character;

@SuppressWarnings("serial")
public class MessageBox extends JPanel implements MouseListener {
	
	private Thread clearThread;

	private JTextArea textBox;
	
	private JTextField input;
	
	private JButton enterButton;
	
	private ArrayList<MessageBoxInputListener> listeners;
	
	private HashMap<JButton, Object> optionValues;
	
	public MessageBox() {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		listeners = new ArrayList<MessageBoxInputListener>();
		textBox = new JTextArea("", 5, 70);
		input = new JTextField(30);
		enterButton = new JButton("Enter");
		add(textBox);
	}
	
	public void addListener(MessageBoxInputListener l) {
		listeners.add(l);
	}
	
	public void removeListener(MessageBoxInputListener l) {
		listeners.remove(l);
	}
	
	public void getString(String prompt) {
		System.out.println("Hit getString");
		showTextPrompt(prompt);
	}
	
	public void getChoice(String prompt, Object[] options) {
		System.out.println("Hit getChoice");
		optionValues = new HashMap<JButton, Object>(options.length);
		showChoicePrompt(prompt, options);
	}
	
	public void display(Character speaker, String message) {
		class Pauser implements Runnable {
			public String message;
			public void run() {
				textBox.setText(message);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {}
				textBox.setText("");
			}
		}
		Pauser p = new Pauser();
		p.message = message;
		if (clearThread != null) {
			clearThread.interrupt();
		}
		clearThread = new Thread(p);
		clearThread.start();
	}
	
	private void showTextPrompt(String prompt) {
		removeAll();
		add(new JLabel(prompt));
		add(input);
		add(enterButton);
	}
	
	private void showChoicePrompt(String prompt, Object[] options) {
		removeAll();
		add(new JLabel(prompt));
		for (Object opt: options) {
			JButton button = new JButton(opt.toString());
			optionValues.put(button, opt);
			button.addMouseListener(this);
			add(button);
		}
	}
	
	private void showTextBox() {
		removeAll();
		add(textBox);
	}
	
	private void fireEnterClicked() {
		showTextBox();
		String rawInput = input.getText();
		for (MessageBoxInputListener l: listeners) {
			l.enterClicked(rawInput);
		}
	}
	
	private void fireOptionClicked(JButton option) {
		showTextBox();
		Object optValue = optionValues.get(option);
		for (MessageBoxInputListener l: listeners) {
			l.optionClicked(optValue);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getComponent() == enterButton) {
			fireEnterClicked();
		} else {
			JButton hitButton = (JButton) e.getComponent();
			fireOptionClicked(hitButton);
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
	
}
