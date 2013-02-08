package yuuki.ui;

import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import yuuki.animation.TextTween;
import yuuki.animation.TimedAnimation;
import yuuki.animation.engine.AnimationManager;
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
		 * Waits, and then cleans the text area. If interrupted, the text area
		 * will not be cleaned.
		 */
		@Override
		public void run() {
			startTime = System.currentTimeMillis();
			while (System.currentTimeMillis() - startTime < waitTime) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			setText("");
		}
		
	}
	
	/**
	 * Executes a Runnable synchronously on the EDT. This method is
	 * thread-safe; if the current thread is not the EDT, the Runnable is sent
	 * to the EDT.
	 * 
	 * If an Exception is thrown by the executing code, the stack trace is
	 * printed to stderr and this method returns immediately.
	 * 
	 * If the current thread is interrupted while waiting for the code to be
	 * executed, the thread's interrupted flag is set and this method returns
	 * immediately.
	 * 
	 * @param doRun The Runnable to be executed.
	 */
	private static void invokeNow(Runnable doRun) {
		if (!SwingUtilities.isEventDispatchThread()) {
			try {
				SwingUtilities.invokeAndWait(doRun);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		} else {
			doRun.run();
		}
	}
	
	/**
	 * The enter button used when a text prompt is displayed.
	 */
	private JButton enterButton;
	
	/**
	 * Whether this MessageBox is inactive and non-responsive.
	 */
	private boolean frozen;
	
	/**
	 * The input field used when a text prompt is displayed.
	 */
	private JTextField input;
	
	/**
	 * The list of listeners for events fired from this MessageBox.
	 */
	private ArrayList<MessageBoxInputListener> listeners;
	
	/**
	 * The animation that is tweening the text display.
	 */
	private TimedAnimation messageDisplayAnimation;
	
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
	public MessageBox(AnimationManager animator, int width, int height) {
		super(animator, width, height);
		component.setLayout(new FlowLayout());
		listeners = new ArrayList<MessageBoxInputListener>();
		textBox = new JTextArea("", 5, 70);
		textBox.setEditable(false);
		textBox.setFocusable(false);
		input = new JTextField(30);
		enterButton = new JButton("Enter");
		showTextArea();
	}
	
	/**
	 * Adds one character to the text of this MessageBox.
	 * 
	 * @param c The character to add.
	 */
	public void addChar(char c) {
		checkFreeze();
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
		checkFreeze();
		if (textCleaner != null && textCleaner.isAlive()) {
			textCleaner.interrupt();
		}
		setText("");
	}
	
	/**
	 * Displays a message. This method is safe to call from outside the EDT. It
	 * may NOT be called from the EDT if animation is used.
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
		checkFreeze();
		String msg = composeMessage(speaker, message);
		clear();
		showMessage(msg, letterDelay);
		spawnCleanerThread(displayTime);
	}
	
	/**
	 * Exits the current prompt immediately. Any inputed text is removed and
	 * the button values are cleared. This method can be used to switch back
	 * after the user has entered data at a prompt, but care should be taken to
	 * get the entered data first, as this method will remove it.
	 */
	public void exitPrompt() {
		checkFreeze();
		Runnable r = new Runnable() {
			@Override
			public void run() {
				showTextArea();
				input.setText("");
			}
		};
		MessageBox.invokeNow(r);
		optionValues = null;
	}
	
	/**
	 * Immediately finishes the message being tweened.
	 */
	public void finishAnimating() {
		if (messageDisplayAnimation != null) {
			messageDisplayAnimation.finish();
		}
	}
	
	/**
	 * Freezes any animations on this box and disables everything on it.
	 */
	public void freeze() {
		frozen = true;
		MessageBox.invokeNow(new Runnable() {
			@Override
			public void run() {
				getComponent().setEnabled(false);
			}
		});
	}
	
	/**
	 * Displays a choice prompt. When it is displayed, the user is presented
	 * with several options shown as buttons. Once the user clicks an option,
	 * the optionClicked() method is called on all listeners.
	 * 
	 * @param prompt The text prompt to show to the user.
	 * @param options The options that the user may choose from. For best
	 * results, each element's polymorphic type must have its toString() method
	 * overridden.
	 */
	public void getChoice(String prompt, Object[] options) {
		checkFreeze();
		optionValues = new HashMap<JButton, Object>(options.length);
		showChoicePrompt(prompt, options);
	}
	
	/**
	 * Displays a text prompt. When it is displayed, the user is presented with
	 * an input field and an enter button. Once the user enters a value, the
	 * enterClicked() method is called on all listeners.
	 * 
	 * @param prompt The text prompt to show to the user.
	 */
	public void getString(String prompt) {
		checkFreeze();
		showTextPrompt(prompt);
	}
	
	/**
	 * Checks whether this message box is frozen.
	 * 
	 * @return True if this MessageBox is frozen; otherwise, false.
	 */
	public boolean isFrozen() {
		return frozen;
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
		checkFreeze();
		class Runner implements Runnable {
			private String t;
			public Runner(String t) {
				this.t = t;
			}
			@Override
			public void run() {
				textBox.setText(t);
			}
		}
		Runner r = new Runner(t);
		SwingUtilities.invokeLater(r);
	}
	
	/**
	 * Unfreezes any animations on this box and enables it.
	 */
	public void unfreeze() {
		MessageBox.invokeNow(new Runnable() {
			@Override
			public void run() {
				getComponent().setEnabled(true);
			}
		});
		frozen = false;
	}
	
	/**
	 * Joins the current thread with the cleaner thread.
	 */
	public void waitForClean() {
		checkFreeze();
		if (textCleaner != null && textCleaner.isAlive()) {
			try {
				textCleaner.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
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
		messageDisplayAnimation = tween;
		try {
			AnimationManager.animateAndWait(animator, tween);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	/**
	 * Checks whether this message box has been frozen, and if it has, throws
	 * an exception.
	 * 
	 * @throws FrozenException If this message box has been frozen.
	 */
	private void checkFreeze() throws FrozenException {
		if (frozen) {
			throw new FrozenException();
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
		String rawInput = input.getText();
		exitPrompt();
		// make a copy in case listeners remove themselves during iteration
		MessageBoxInputListener[] ls = new MessageBoxInputListener[0];
		MessageBoxInputListener[] listenersList = listeners.toArray(ls);
		for (MessageBoxInputListener l : listenersList) {
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
		Object optValue = optionValues.get(option);
		exitPrompt();
		// make a copy in case listeners remove themselves during iteration
		MessageBoxInputListener[] ls = new MessageBoxInputListener[0];
		MessageBoxInputListener[] listenersList = listeners.toArray(ls);
		for (MessageBoxInputListener l : listenersList) {
			l.optionClicked(optValue);
		}
	}
	
	/**
	 * Shows a choice prompt.
	 * 
	 * @param prompt The text prompt to show the user.
	 * @param options The options that the user is to pick from.
	 */
	private void showChoicePrompt(String prompt, Object[] options) {
		component.removeAll();
		add(new JLabel(prompt));
		for (Object opt : options) {
			JButton button = new JButton(opt.toString());
			optionValues.put(button, opt);
			button.addMouseListener(this);
			add(button);
		}
		component.revalidate();
		component.repaint();
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
	 * Shows the read-only text box.
	 */
	private void showTextArea() {
		component.removeAll();
		add(textBox);
		component.revalidate();
		component.repaint();
	}
	
	/**
	 * Shows a text prompt.
	 * 
	 * @param prompt The text prompt to show the user.
	 */
	private void showTextPrompt(String prompt) {
		component.removeAll();
		add(new JLabel(prompt));
		add(input);
		add(enterButton);
		component.revalidate();
		component.repaint();
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
