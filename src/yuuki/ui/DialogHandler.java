package yuuki.ui;

import java.awt.Dimension;
import java.awt.Font;

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
		String top = "Yuuki encountered an error.\n" +
				"Thread: '" + Thread.currentThread().getName() + "'\n";
		JScrollPane mid = DialogHandler.createScrollPane(msg);
		String bot = "Continue running Yuuki?";
		Object[] msgs = {top, mid, bot};
		int keepRunning = JOptionPane.showConfirmDialog(null, msgs, "Error",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
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
		//String messageTop = "<html>Yuuki encountered a fatal error!<br>" +
		//		"Thread: '" + Thread.currentThread().getName() + "'</html>";
		//Box box = DialogHandler.createContentPanel(messageTop, msg, null);
		String topMessage = "Yuuki encountered a fatal error!\n" +
				"Thread: '" + Thread.currentThread().getName() + "'\n";
		JScrollPane scrollPane = createScrollPane(msg);
		Object[] msgs = {topMessage, scrollPane};
		JOptionPane.showMessageDialog(null, msgs, "Fatal Error",
				JOptionPane.ERROR_MESSAGE);
		System.exit(1);
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
	 * Creates a panel to display the contents of a dialog.
	 * 
	 * @param text The text to put in the scroll pane.
	 * @return The created panel.
	 */
	private static JScrollPane createScrollPane(String text) {
		JTextArea area = new JTextArea(text);
		Font old = area.getFont();
		area.setFont(new Font(old.getName(), old.getStyle(), old.getSize()-2));
		JScrollPane scroll = new JScrollPane(area);
		scroll.setPreferredSize(new Dimension(400, 250));
		return scroll;
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
			String msg = current.getMessage();
			if (msg != null) {
				buffer.append(current.getMessage() + '\n');
			}
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
