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
		 * The thread that waits to clear the text after it has been displayed.
		 */
		private Thread cleanerThread;
		
		/**
		 * Whether this cleaner has been paused.
		 */
		private volatile boolean paused;
		
		/**
		 * The time that this cleaner was paused at.
		 */
		private long pauseTime;
		
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
		 */
		public Cleaner() {}
		
		/**
		 * Checks whether this cleaner is running.
		 */
		public boolean isWaiting() {
			return (cleanerThread != null && cleanerThread.isAlive());
		}
		
		/**
		 * Joins with the cleaner thread.
		 */
		public void joinWithThread() throws InterruptedException {
			cleanerThread.join();
		}
		
		/**
		 * Resumes the cleaner.
		 */
		public void resume() {
			if (paused) {
				paused = false;
				long pauseDuration = System.currentTimeMillis() - pauseTime;
				startTime += pauseDuration;
			}
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
					checkPause();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			setText("");
		}
		
		/**
		 * Starts this cleaner.
		 * 
		 * @param time The amount of time to wait before cleaning.
		 */
		public void start(long time) {
			waitTime = time;
			cleanerThread = new Thread(this, "MessageCleaner");
			cleanerThread.start();
		}
		
		/**
		 * Stops this cleaner immediately.
		 */
		public void stop() {
			cleanerThread.interrupt();
		}
		
		/**
		 * Pauses the cleaner.
		 */
		public void suspend() {
			paused = true;
			pauseTime = System.currentTimeMillis();
		}
		
		/**
		 * Checks whether this cleaner has been paused, and if so, waits until
		 * it is unpaused.
		 */
		private void checkPause() throws InterruptedException {
			while (paused) {
				Thread.sleep(10);
			}
		}
		
	}
	
	/**
	 * The name of this MessageBox's animation driver.
	 */
	private static final String DRIVER = "MessageBox";
	
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
	 * The cleaner.
	 */
	private Cleaner cleaner;
	
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
	 * The label for a prompt.
	 */
	private JLabel label;
	
	/**
	 * The list of listeners for events fired from this MessageBox.
	 */
	private ArrayList<MessageBoxInputListener> listeners;
	
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
	 * 
	 * @param animator The handler for this MessageBox's animation.
	 * @param width The width of this MessageBox.
	 * @param height The height of this MessageBox.
	 */
	public MessageBox(AnimationManager animator, int width, int height) {
		super(animator, width, height);
		cleaner = new Cleaner();
		animator.createDriver(DRIVER);
		animator.startDriver(DRIVER);
		component.setLayout(new FlowLayout());
		listeners = new ArrayList<MessageBoxInputListener>();
		textBox = new JTextArea("", 5, 70);
		textBox.setEditable(false);
		textBox.setFocusable(false);
		input = new JTextField(30);
		enterButton = new JButton("Enter");
		label = new JLabel();
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
		if (cleaner.isWaiting()) {
			cleaner.stop();
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
	 * Freezes any animations on this box and disables everything on it.
	 */
	public void freeze() {
		frozen = true;
		animator.suspendDriver(DRIVER);
		cleaner.suspend();
		MessageBox.invokeNow(new Runnable() {
			@Override
			public void run() {
				setComponentsEnabled(false);
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
				setComponentsEnabled(true);
			}
		});
		cleaner.resume();
		animator.resumeDriver(DRIVER);
		frozen = false;
	}
	
	/**
	 * Joins the current thread with the cleaner thread.
	 */
	public void waitForClean() {
		checkFreeze();
		if (cleaner.isWaiting()) {
			try {
				cleaner.joinWithThread();
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
		try {
			animator.animateAndWait(tween, DRIVER);
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
	 * Sets the enabled status of each child component.
	 */
	private void setComponentsEnabled(boolean enabled) {
		getComponent().setEnabled(enabled);
		input.setEnabled(enabled);
		label.setEnabled(enabled);
		enterButton.setEnabled(enabled);
		textBox.setEnabled(enabled);
		if (optionValues != null) {
			for (JButton b : optionValues.keySet()) {
				b.setEnabled(enabled);
			}
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
		label.setText(prompt);
		add(label);
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
		label.setText(prompt);
		add(label);
		add(input);
		add(enterButton);
		component.revalidate();
		component.repaint();
	}
	
	/**
	 * Spawns a thread that waits a certain amount of time before clearing the
	 * display.
	 * 
	 * @param displayTime The time to display the message before clearing it.
	 */
	private void spawnCleanerThread(long displayTime) {
		cleaner.start(displayTime);
	}
	
}
