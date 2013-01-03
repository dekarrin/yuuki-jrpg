package yuuki.ui;

import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.SwingUtilities;

import yuuki.entity.Character;

/**
 * Handles message queuing and displaying for a MessageBox. MessageBoxDisplayer
 * should run in a separate thread from its associated MessageBox so as not to
 * cause blocking on the EDT.
 */
public class MessageBoxDisplayer implements Runnable {
	
	/**
	 * Holds information on a message. Messages in this sense can be normal
	 * messages, text prompts, or choice prompts.
	 */
	private static class MessageData {
		
		/**
		 * The options that the user may choose from. As a shortcut, if this
		 * MessageData instance contains normal message data, the speaker of
		 * the message is stored here.
		 */
		private Object[] options;
		
		/**
		 * The actual message String.
		 */
		private String prompt;
		
		/**
		 * The type of message that this MessageData contains data for.
		 */
		private MessageType type;
		
		/**
		 * Creates a new MessageData and populates its members.
		 * 
		 * @param prompt The actual message String.
		 * @param options The options that the user may choose from.
		 * @param type The type of message that this MessageData is to contain
		 * data for.
		 */
		public MessageData(String prompt, Object[] options,
				MessageType type) {
			this.prompt = prompt;
			this.options = options;
			this.type = type;
		}
		
		/**
		 * Gets the options array of this MessageData.
		 * 
		 * @return The options array.
		 */
		public Object[] getOptions() {
			return options;
		}
		
		/**
		 * Gets the string prompt of this MessageData.
		 * 
		 * @return The string prompt.
		 */
		public String getPrompt() {
			return prompt;
		}
		
		/**
		 * Gets the speaker of a normal message.
		 * 
		 * @return The speaker.
		 */
		public Character getSpeaker() {
			return (Character) options[0];
		}
		
		/**
		 * Gets the type of message that this MessageData is holding data for.
		 * 
		 * @return The message type.
		 */
		public MessageType getType() {
			return type;
		}
		
	}
	
	/**
	 * The types of messages that this MessageBoxDisplayer can queue.
	 */
	private static enum MessageType {
		
		/**
		 * A prompt that asks the user to select one of several options.
		 */
		CHOICE_PROMPT,
		
		/**
		 * A normal message with a speaker.
		 */
		MESSAGE,
		
		/**
		 * A prompt that asks the user to enter a string.
		 */
		STRING_PROMPT
		
	}
	
	/**
	 * The MessageBox that this MessageBoxDisplayer is controlling message
	 * queuing and displaying for.
	 */
	private MessageBox box;
	
	/**
	 * The message queue.
	 */
	private LinkedBlockingQueue<MessageData> displayQueue;
	
	/**
	 * Whether this MessageBoxDisplayer is currently displaying a message and
	 * is paused to let the user read the message.
	 */
	private boolean isBlockingForDisplay;
	
	/**
	 * Whether this MessageBoxDisplayer's thread is blocking while waiting for
	 * user input.
	 */
	private boolean isBlockingForInput;
	
	/**
	 * Allocates a new MessageBoxDisplayer and creates the message queue.
	 * 
	 * @param box The MessageBox that this MessageBoxDisplayer is handling
	 * messages for.
	 */
	public MessageBoxDisplayer(MessageBox box) {
		displayQueue = new LinkedBlockingQueue<MessageData>();
		this.box = box;
		isBlockingForDisplay = false;
		isBlockingForInput = false;
	}
	
	/**
	 * Adds a choice prompt to the message queue.
	 * 
	 * @param prompt The prompt to show the user.
	 * @param options The options that the user may choose from.
	 */
	public void queueChoicePrompt(String prompt, Object[] options) {
		MessageType type = MessageType.CHOICE_PROMPT;
		MessageData md = new MessageData(prompt, options, type);
		queueMessageData(md);
	}
	
	/**
	 * Adds a normal message to the message queue.
	 * 
	 * @param speaker The speaker of the message. Set to null for no speaker.
	 * @param message The actual message.
	 */
	public void queueMessage(Character speaker, String message) {
		Object[] options = {speaker};
		MessageType type = MessageType.MESSAGE;
		MessageData md = new MessageData(message, options, type);
		queueMessageData(md);
	}
	
	/**
	 * Adds a text prompt to the message queue.
	 * 
	 * @param prompt The text to show the user.
	 */
	public void queueStringPrompt(String prompt) {
		MessageType type = MessageType.STRING_PROMPT;
		MessageData md = new MessageData(prompt, null, type);
		queueMessageData(md);
	}
	
	/**
	 * Resets the MessageBox to show the default message area.
	 */
	public void resetPrompt() {
		isBlockingForInput = false;
		Runnable r = new Runnable() {
			@Override
			public void run() {
				box.showTextBox();
			}
		};
		SwingUtilities.invokeLater(r);
	}
	
	/**
	 * Constantly checks for messages to display. If there are messages queued,
	 * they are displayed. If a prompt is being displayed, this thread blocks
	 * until the user enters something in the prompt.
	 */
	@Override
	public void run() {
		while (true) {
			try {
				if (isBlockingForInput) {
					Thread.sleep(50);
				} else {
					if (isBlockingForDisplay) {
						Thread.sleep(5000);
						clearMessage();
						isBlockingForDisplay = false;
					} else {
						displayNextMessage();
					}
				}
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Clears the text in the message box.
	 */
	private void clearMessage() {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				box.setText("");
			}
		};
		SwingUtilities.invokeLater(r);
	}
	
	/**
	 * Displays a choice prompt. The directive is passed to the associated
	 * MessageBox on the EDT.
	 * 
	 * @param prompt The text to show the user.
	 * @param options The options that the user is to choose from.
	 */
	private void displayChoicePrompt(String prompt, Object[] options) {
		class Runner implements Runnable {
			public Object[] options;
			public String prompt;
			@Override
			public void run() {
				box.showChoicePrompt(prompt, options);
			}
		}
		Runner r = new Runner();
		r.prompt = prompt;
		r.options = options;
		SwingUtilities.invokeLater(r);
		isBlockingForInput = true;
	}
	
	/**
	 * Displays a normal message. The directive is passed to the associated
	 * MessageBox on the EDT.
	 * 
	 * @param speaker The one doing the speaking. Set to null for no speaker.
	 * @param message The message that the character says.
	 */
	private void displayMessage(Character speaker, String message) {
		String fullMessage = "";
		if (speaker != null) {
			fullMessage = speaker + ": ";
		}
		fullMessage += message;
		class Runner implements Runnable {
			public String msg;
			@Override
			public void run() {
				box.setText(msg);
			}
		}
		Runner r = new Runner();
		r.msg = fullMessage;
		isBlockingForDisplay = true;
		SwingUtilities.invokeLater(r);
	}
	
	/**
	 * Displays the next queued message. If there is no queued message, this
	 * method causes the current thread to block until there is one.
	 * 
	 * @throws InterruptedException If this thread is interrupted while waiting
	 * for a message.
	 */
	private void displayNextMessage() throws InterruptedException {
		MessageData message = displayQueue.take();
		switch (message.getType()) {
			case MESSAGE:
				displayMessage(message.getSpeaker(), message.getPrompt());
				break;
				
			case STRING_PROMPT:
				displayStringPrompt(message.getPrompt());
				break;
				
			case CHOICE_PROMPT:
				displayChoicePrompt(message.getPrompt(), message.getOptions());
				break;
		}
	}
	
	/**
	 * Displays a text prompt. The directive is passed to the associated
	 * MessageBox on the EDT.
	 * 
	 * @param prompt The text to prompt the user with.
	 */
	private void displayStringPrompt(String prompt) {
		class Runner implements Runnable {
			public String prompt;
			@Override
			public void run() {
				box.showTextPrompt(prompt);
			}
		}
		Runner r = new Runner();
		r.prompt = prompt;
		SwingUtilities.invokeLater(r);
		isBlockingForInput = true;
	}
	
	/**
	 * Adds a MessageData instance to the message queue.
	 * 
	 * @param data The MessageData instance to add.
	 */
	private void queueMessageData(MessageData data) {
		try {
			displayQueue.put(data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
