package yuuki.ui;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Displays dialog boxes.
 */
public class DialogHandler {
	
	/**
	 * Shows an error message.
	 * 
	 * @param msg The message to show.
	 */
	public static void showError(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Error",
				JOptionPane.WARNING_MESSAGE);
		String messageTop = "<html>Yuuki encountered an error.<br>" +
				"Thread: '" + Thread.currentThread().getName() + "'</html>";
		String bot = "Continue running Yuuki?";
		Box box = DialogHandler.createContentPanel(messageTop, msg, bot);
		int keepRunning = JOptionPane.showConfirmDialog(null, box, "Error",
				JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
		if (keepRunning != JOptionPane.YES_OPTION) {
			System.exit(1);
		}
	}
	
	/**
	 * Shows an error message for an exception.
	 * 
	 * @param t The Throwable that caused the error.
	 */
	public static void showError(Throwable t) {
		String msg = DialogHandler.getTrace(t);
		DialogHandler.showError(msg);
	}
	
	/**
	 * Shows an error message and immediately terminates the program.
	 * 
	 * @param msg The message to show.
	 */
	public static void showFatalError(String msg) {
		String messageTop = "<html>Yuuki encountered a fatal error!<br>" +
				"Thread: '" + Thread.currentThread().getName() + "'</html>";
		Box box = DialogHandler.createContentPanel(messageTop, msg, null);
		JOptionPane.showMessageDialog(null, box, "Fatal Error",
				JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	}
	
	/**
	 * Creates a panel to display the contents of a dialog.
	 * 
	 * @param top A string to display above the scroll pane. Set to null for no
	 * string.
	 * @param mid A string to display in the scroll pane. Set to null for no
	 * scroll pane.
	 * @param bot A string to display below the scroll pane. Set to null for no
	 * string.
	 * @return The created panel.
	 */
	private static Box createContentPanel(String top, String mid, String bot) {
		Box content = Box.createVerticalBox();
		content.setMaximumSize(new Dimension(500, 400));
		if (top != null) {
			content.add(new JLabel(top));
		}
		if (mid == null) {
			JTextArea midText = new JTextArea(mid);
			JScrollPane scroll = new JScrollPane(midText);
			content.add(scroll);
			midText.setLineWrap(true);
			midText.setWrapStyleWord(true);
		}
		if (bot == null) {
			content.add(new JLabel());
		}
		return content;
	}
	
	/**
	 * Shows an error message for an exception and immediately terminates the
	 * program.
	 * 
	 * @param t The Throwable that caused the error.
	 */
	public static void showFatalError(Throwable t) {
		String msg = DialogHandler.getTrace(t);
		DialogHandler.showFatalError(msg);
	}
	
	/**
	 * Shows a message.
	 * 
	 * @param msg The message to show.
	 */
	public static void showMessage(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Message",
				JOptionPane.PLAIN_MESSAGE);
	}
	
	/**
	 * Shows a message with an exception.
	 * 
	 * @param msg The message to show.
	 * @param t The exception that caused the message.
	 */
	public static void showMessage(String msg, Throwable t) {
		DialogHandler.showMessage(msg + '\n' +
				DialogHandler.getMessageTrace(t));
	}
	
	/**
	 * Gets the trace of messages only from a Throwable.
	 * 
	 * @param t The Throwable to get the messages from.
	 * @return The messages.
	 */
	private static String getMessageTrace(Throwable t) {
		StringBuilder buffer = new StringBuilder();
		Throwable current = t;
		while (true) {
			buffer.append(current.getMessage() + '\n');
			current = current.getCause();
			if (current == null) {
				break;
			}
		}
		return buffer.toString();
	}
	
	/**
	 * Gets the stack trace from a Throwable as a String.
	 * 
	 * @param t The Throwable to get the stack trace from.
	 * @return The stack trace.
	 */
	private static String getTrace(Throwable t) {
		StringBuilder buffer = new StringBuilder();
		Throwable current = t;
		while (true) {
			buffer.append(current.getMessage() + '\n');
			buffer.append(current.getClass().getCanonicalName());
			buffer.append("\n");
			for (StackTraceElement trace : current.getStackTrace()) {
				buffer.append("\t" + trace.toString());
				buffer.append("\n");
			}
			current = current.getCause();
			if (current != null) {
				buffer.append("Caused by: ");
			} else {
				break;
			}
		}
		return buffer.toString();
	}
	
}
