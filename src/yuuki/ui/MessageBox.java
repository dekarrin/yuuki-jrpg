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
	
	private MessageBoxDisplayer messageDisplayer;

	private JTextArea textBox;
	
	private JTextField input;
	
	private JButton enterButton;
	
	private ArrayList<MessageBoxInputListener> listeners;
	
	private HashMap<JButton, Object> optionValues;
	
	public MessageBox() {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		listeners = new ArrayList<MessageBoxInputListener>();
		textBox = new JTextArea("", 5, 70);
		textBox.setEditable(false);
		textBox.setFocusable(false);
		input = new JTextField(30);
		enterButton = new JButton("Enter");
		showTextBox();
		messageDisplayer = new MessageBoxDisplayer(this);
		(new Thread(messageDisplayer, "MessageDisplay")).start();
	}
	
	public void addListener(MessageBoxInputListener l) {
		listeners.add(l);
	}
	
	public void removeListener(MessageBoxInputListener l) {
		listeners.remove(l);
	}
	
	public void getString(String prompt) {
		messageDisplayer.queueStringPrompt(prompt);
	}
	
	public void getChoice(String prompt, Object[] options) {
		optionValues = new HashMap<JButton, Object>(options.length);
		messageDisplayer.queueChoicePrompt(prompt, options);
	}
	
	public void display(Character speaker, String message) {
		messageDisplayer.queueMessage(speaker, message);
	}
	
	public void setText(String t) {
		textBox.setText(t);
	}
	
	public void showTextPrompt(String prompt) {
		removeAll();
		add(new JLabel(prompt));
		add(input);
		add(enterButton);
		revalidate();
		repaint();
	}
	
	public void showChoicePrompt(String prompt, Object[] options) {
		removeAll();
		add(new JLabel(prompt));
		for (Object opt: options) {
			JButton button = new JButton(opt.toString());
			optionValues.put(button, opt);
			button.addMouseListener(this);
			add(button);
		}
		revalidate();
		repaint();
	}
	
	public void showTextBox() {
		removeAll();
		add(textBox);
		revalidate();
		repaint();
	}
	
	private void fireEnterClicked() {
		messageDisplayer.resetPrompt();
		String rawInput = input.getText();
		// make a copy in case listeners remove themselves during iteration
		MessageBoxInputListener[] ls = new MessageBoxInputListener[0];
		MessageBoxInputListener[] listenersList = listeners.toArray(ls);
		for (MessageBoxInputListener l: listenersList) {
			l.enterClicked(rawInput);
		}
	}
	
	private void fireOptionClicked(JButton option) {
		messageDisplayer.resetPrompt();
		Object optValue = optionValues.get(option);
		// make a copy in case listeners remove themselves during iteration
		MessageBoxInputListener[] ls = new MessageBoxInputListener[0];
		MessageBoxInputListener[] listenersList = listeners.toArray(ls);
		for (MessageBoxInputListener l: listenersList) {
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
