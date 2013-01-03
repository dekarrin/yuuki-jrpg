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

/**
 * Displays messages and prompts the user for input.
 */
@SuppressWarnings("serial")
public class MessageBox extends JPanel implements MouseListener {
	
	/**
	 * The enter button used when a text prompt is displayed.
	 */
	private JButton enterButton;
	
	/**
	 * The input field used when a text prompt is displayed.
	 */
	private JTextField input;
	
	/**
	 * The list of listeners for events fired from this MessageBox.
	 */
	private ArrayList<MessageBoxInputListener> listeners;
	
	/**
	 * Handles message queuing and displaying.
	 */
	private MessageBoxDisplayer messageDisplayer;
	
	/**
	 * The values of the options shown during a choice prompt.
	 */
	private HashMap<JButton, Object> optionValues;
	
	/**
	 * The text area used for normal message displaying.
	 */
	private JTextArea textBox;
	
	/**
	 * Allocates a new MessageBox. The child components are created and the
	 * message displayer is started on its own thread.
	 */
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
	
	/**
	 * Adds a listener for MessageBox input events.
	 * 
	 * @param l The listener to add.
	 */
	public void addListener(MessageBoxInputListener l) {
		listeners.add(l);
	}
	
	/**
	 * Queues a text message for displaying.
	 * 
	 * @param speaker The character doing the speaking. This is used for
	 * styling the message. Set this to null for no styling.
	 * @param message The message to display.
	 */
	public void display(Character speaker, String message) {
		messageDisplayer.queueMessage(speaker, message);
	}
	
	/**
	 * Queues a choice prompt for displaying. When it is displayed, the user is
	 * presented with several options shown as buttons. Once the user clicks an
	 * option, the optionClicked() method is called on all listeners.
	 * 
	 * @param prompt The text prompt to show to the user.
	 * @param options The options that the user may choose from. For best
	 * results, each element's polymorphic type must have its toString() method
	 * overridden.
	 */
	public void getChoice(String prompt, Object[] options) {
		optionValues = new HashMap<JButton, Object>(options.length);
		messageDisplayer.queueChoicePrompt(prompt, options);
	}
	
	/**
	 * Queues a string prompt for displaying. When it is displayed, the user is
	 * presented with an input field and an enter button. Once the user enters
	 * a value, the enterClicked() method is called on all listeners.
	 * 
	 * @param prompt The text prompt to show to the user.
	 */
	public void getString(String prompt) {
		messageDisplayer.queueStringPrompt(prompt);
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getComponent() == enterButton) {
			fireEnterClicked();
		} else {
			JButton hitButton = (JButton) e.getComponent();
			fireOptionClicked(hitButton);
		}
		
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {}
	
	/**
	 * Clears a listener from this MessageBox's list.
	 * 
	 * @param l The listener to remove.
	 */
	public void removeListener(MessageBoxInputListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Sets the text of this MessageBox's text box. This should never be called
	 * directly by any class but MessageBoxDisplayer; to display a message with
	 * proper queuing, use the display() method.
	 * 
	 * @param t The String to set the text box's contents to.
	 */
	public void setText(String t) {
		textBox.setText(t);
	}
	
	/**
	 * Shows a choice prompt. This should never be called directly by any class
	 * but MessageBoxDisplayer; to display a prompt with proper queuing, use
	 * the getChoice() method.
	 * 
	 * @param prompt The text prompt to show the user.
	 * @param options The options that the user is to pick from.
	 */
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
	
	/**
	 * Shows the text box. This should never be called directly by any class
	 * except for MessageBoxDisplayer.
	 */
	public void showTextBox() {
		removeAll();
		add(textBox);
		revalidate();
		repaint();
	}
	
	/**
	 * Shows a text prompt. This should never be called directly by any class
	 * but MessageBoxDisplayer; to display a prompt with proper queuing, use
	 * the getString() method.
	 * 
	 * @param prompt The text prompt to show the user.
	 */
	public void showTextPrompt(String prompt) {
		removeAll();
		add(new JLabel(prompt));
		add(input);
		add(enterButton);
		revalidate();
		repaint();
	}
	
	/**
	 * Calls the enterClicked() method on all listeners. This method is called
	 * when the user presses the enter button in a text prompt.
	 */
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
	
	/**
	 * Calls the optionClicked() method on all listeners. This method is called
	 * when the user selects an option in a choice prompt.
	 * 
	 * @param option The button that the user clicked.
	 */
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
	
}