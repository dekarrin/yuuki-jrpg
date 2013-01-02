package yuuki.ui;

import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.SwingUtilities;

import yuuki.entity.Character;

public class MessageBoxDisplayer implements Runnable {
	
	private static class MessageData {
		private Object[] options;
		private String prompt;
		private MessageType type;
		public MessageData(String prompt, Object[] options,
				MessageType type) {
			this.prompt = prompt;
			this.options = options;
			this.type = type;
		}
		public Object[] getOptions() {
			return options;
		}
		public String getPrompt() {
			return prompt;
		}
		public Character getSpeaker() {
			return (Character) options[0];
		}
		public MessageType getType() {
			return type;
		}
	}
	
	private static enum MessageType {
		CHOICE_PROMPT,
		MESSAGE,
		STRING_PROMPT
	}
	
	private MessageBox box;
	
	private boolean displayingMessage;
	
	private LinkedBlockingQueue<MessageData> displayQueue;
	
	private boolean isBlocking;
	
	public MessageBoxDisplayer(MessageBox box) {
		displayQueue = new LinkedBlockingQueue<MessageData>();
		this.box = box;
		displayingMessage = false;
		isBlocking = false;
	}
	
	public void queueChoicePrompt(String prompt, Object[] options) {
		MessageType type = MessageType.CHOICE_PROMPT;
		MessageData md = new MessageData(prompt, options, type);
		queueMessageData(md);
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
	
	public void resetPrompt() {
		isBlocking = false;
		Runnable r = new Runnable() {
			@Override
			public void run() {
				box.showTextBox();
			}
		};
		SwingUtilities.invokeLater(r);
	}
	
	@Override
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
	
	private void clearMessage() {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				box.setText("");
			}
		};
		SwingUtilities.invokeLater(r);
	}
	
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
		isBlocking = true;
	}
	
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
		displayingMessage = true;
		SwingUtilities.invokeLater(r);
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
		isBlocking = true;
	}
	
	private void queueMessageData(MessageData data) {
		try {
			displayQueue.put(data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
