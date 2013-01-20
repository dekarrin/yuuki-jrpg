package yuuki.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import yuuki.animation.TextTween;
import yuuki.animation.engine.Animator;
import yuuki.entity.Character;
import yuuki.sprite.Sprite;

/**
 * Displays messages and prompts the user for input.
 */
public class MessageBox extends Sprite implements MouseListener {
	
	/**
	 * The thread that clears the box after the display time is over.
	 */
	private class Cleaner implements Runnable {
		
		/**
		 * The time that thread's waiting started at.
		 */
		private long startTime;
		
		/**
		 * The amount of time to wait before cleaning.
		 */
		private long waitTime;
		
		/**
		 * Creates a new Cleaner.
		 * 
		 * @param time The amount of time to wait before cleaning.
		 */
		public Cleaner(long time) {
			waitTime = time;
		}
		
		/**
		 * Waits, and then cleans the text area.
		 */
		@Override
		public void run() {
			startTime = System.currentTimeMillis();
			while (System.currentTimeMillis() - startTime < waitTime) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					setText("");
				}
			}
			setText("");
		}
		
	}
	
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
	 * The thread that waits to clear the text after it has been displayed.
	 */
	private Thread textCleaner;
	
	/**
	 * Allocates a new MessageBox. The child components are created and the
	 * message displayer is started on its own thread.
	 * 
	 * @param animator The handler for this MessageBox's animation.
	 * @param width The width of this MessageBox.
	 * @param height The height of this MessageBox.
	 */
	public MessageBox(Animator animator, int width, int height) {
		super(animator, width, height);
		component.setLayout(new FlowLayout());
		component.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
	 * Adds one character to the text of this MessageBox.
	 * 
	 * @param c The character to add.
	 */
	public void addChar(char c) {
		textBox.setText(textBox.getText() + c);
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
	 * Clears this MessageBox of all text. If a clean-up thread is waiting, it
	 * is interrupted.
	 */
	public void clear() {
		if (textCleaner != null && textCleaner.isAlive()) {
			textCleaner.interrupt();
		} else {
			setText("");
		}
	}
	
	/**
	 * Queues a text message for displaying.
	 * 
	 * @param speaker The character doing the speaking. This is used for
	 * styling the message. Set this to null for no styling.
	 * @param message The message to display.
	 * @param letterDelay The time between each letter. Set to 0 for instant
	 * display.
	 * @param displayTime The amount of time to display the message after it
	 * has finished being displayed.
	 */
	public void display(Character speaker, String message, long letterDelay,
			long displayTime) {
		String msg = composeMessage(speaker, message);
		clear();
		showMessage(msg, letterDelay);
		spawnCleanerThread(displayTime);
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
	 * {@inheritDoc}
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
	 * {@inheritDoc}
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {}
	
	/**
	 * {@inheritDoc}
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
	 * Sets the text of this MessageBox's text box.
	 * 
	 * @param t The String to set the text box's contents to.
	 */
	public void setText(String t) {
		class Runner implements Runnable {
			private String t;
			public Runner(String t) {
				this.t = t;
			}
			public void run() {
				textBox.setText(t);
			}
		}
		Runner r = new Runner(t);
		SwingUtilities.invokeLater(r);
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
		component.removeAll();
		add(new JLabel(prompt));
		for (Object opt: options) {
			JButton button = new JButton(opt.toString());
			optionValues.put(button, opt);
			button.addMouseListener(this);
			add(button);
		}
		component.revalidate();
		component.repaint();
	}
	
	/**
	 * Shows the text box. This should never be called directly by any class
	 * except for MessageBoxDisplayer.
	 */
	public void showTextBox() {
		component.removeAll();
		add(textBox);
		component.revalidate();
		component.repaint();
	}
	
	/**
	 * Shows a text prompt. This should never be called directly by any class
	 * but MessageBoxDisplayer; to display a prompt with proper queuing, use
	 * the getString() method.
	 * 
	 * @param prompt The text prompt to show the user.
	 */
	public void showTextPrompt(String prompt) {
		component.removeAll();
		add(new JLabel(prompt));
		add(input);
		add(enterButton);
		component.revalidate();
		component.repaint();
	}
	
	/**
	 * Joins the current thread with the cleaner thread.
	 */
	public void waitForClean() {
		if (textCleaner != null && textCleaner.isAlive()) {
			try {
				textCleaner.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Animates the message and waits for it to complete.
	 * 
	 * @param letterDelay The time between each letter.
	 * @param message The message to display.
	 */
	private void animateMessage(long letterDelay, String message) {
		TextTween tween = new TextTween(this, letterDelay, message);
		try {
			Animator.animateAndWait(animator, tween);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the message from the message text and the speaker.
	 * 
	 * @param speaker The character speaking the message.
	 * @param message The text of the message.
	 * 
	 * @return The fully-composed message.
	 */
	private String composeMessage(Character speaker, String message) {
		String msg = "";
		if (speaker != null) {
			msg = speaker.getName() + ": ";
		}
		msg += message;
		return msg;
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
	
	/**
	 * Shows a message on the text area. If letter delay is not 0, it will be
	 * animated.
	 * 
	 * @param message The message to display.
	 * @param letterDelay The time between each letter. Set to 0 for instant
	 * display.
	 */
	private void showMessage(String message, long letterDelay) {
		if (letterDelay != 0) {
			animateMessage(letterDelay, message);
		} else {
			setText(message);
		}
	}
	
	/**
	 * Spawns a thread that waits a certain amount of time before clearing the
	 * display.
	 * 
	 * @param time The time to display the message before clearing it.
	 */
	private void spawnCleanerThread(long displayTime) {
		Cleaner c = new Cleaner(displayTime);
		textCleaner = new Thread(c, "Message Cleaner");
		textCleaner.start();
	}
	
}
