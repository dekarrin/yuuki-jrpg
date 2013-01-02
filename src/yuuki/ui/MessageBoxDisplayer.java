package yuuki.ui;

import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.SwingUtilities;

import yuuki.entity.Character;

public class MessageBoxDisplayer implements Runnable {
	
	private static enum MessageType {
		MESSAGE,
		STRING_PROMPT,
		CHOICE_PROMPT
	}
	
	private static class MessageData {
		private String prompt;
		private Object[] options;
		private MessageType type;
		public MessageData(String prompt, Object[] options,
				MessageType type) {
			this.prompt = prompt;
			this.options = options;
			this.type = type;
		}
		public Character getSpeaker() {
			return (Character) options[0];
		}
		public String getPrompt() {
			return prompt;
		}
		public Object[] getOptions() {
			return options;
		}
		public MessageType getType() {
			return type;
		}
	}
	
	private LinkedBlockingQueue<MessageData> displayQueue;
	
	private MessageBox box;
	
	private boolean displayingMessage;
	
	private boolean isBlocking;
	
	public MessageBoxDisplayer(MessageBox box) {
		displayQueue = new LinkedBlockingQueue<MessageData>();
		this.box = box;
		displayingMessage = false;
		isBlocking = false;
	}
	
	public void run() {
		while (true) {
			try {
				if (isBlocking) {
					Thread.sleep(50);
				} else {
					if (displayingMessage) {
						Thread.sleep(5000);
						clearMessage();
						displayingMessage = false;
					} else {
						displayNextMessage();
					}
				}
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void resetPrompt() {
		isBlocking = false;
		Runnable r = new Runnable() {
			public void run() {
				box.showTextBox();
			}
		};
		SwingUtilities.invokeLater(r);
	}
	
	public void queueMessage(Character speaker, String message) {
		Object[] options = {speaker};
		MessageType type = MessageType.MESSAGE;
		MessageData md = new MessageData(message, options, type);
		queueMessageData(md);
	}
	
	public void queueStringPrompt(String prompt) {
		MessageType type = MessageType.STRING_PROMPT;
		MessageData md = new MessageData(prompt, null, type);
		queueMessageData(md);
	}
	
	public void queueChoicePrompt(String prompt, Object[] options) {
		MessageType type = MessageType.CHOICE_PROMPT;
		MessageData md = new MessageData(prompt, options, type);
		queueMessageData(md);
	}
	
	private void queueMessageData(MessageData data) {
		try {
			displayQueue.put(data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
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
	
	private void displayMessage(Character speaker, String message) {
		String fullMessage = "";
		if (speaker != null) {
			fullMessage = speaker + ": ";
		}
		fullMessage += message;
		class Runner implements Runnable {
			public String msg;
			public void run() {
				box.setText(msg);
			}
		}
		Runner r = new Runner();
		r.msg = fullMessage;
		displayingMessage = true;
		SwingUtilities.invokeLater(r);
	}
	
	private void displayStringPrompt(String prompt) {
		class Runner implements Runnable {
			public String prompt;
			public void run() {
				box.showTextPrompt(prompt);
			}
		}
		Runner r = new Runner();
		r.prompt = prompt;
		SwingUtilities.invokeLater(r);
		isBlocking = true;
	}
	
	private void displayChoicePrompt(String prompt, Object[] options) {
		class Runner implements Runnable {
			public String prompt;
			public Object[] options;
			public void run() {
				box.showChoicePrompt(prompt, options);
			}
		}
		Runner r = new Runner();
		r.prompt = prompt;
		r.options = options;
		SwingUtilities.invokeLater(r);
		isBlocking = true;
	}

	private void clearMessage() {
		Runnable r = new Runnable() {
			public void run() {
				box.setText("");
			}
		};
		SwingUtilities.invokeLater(r);
	}
}
