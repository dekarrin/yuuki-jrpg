package yuuki.ui;

import javax.swing.JOptionPane;

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
				JOptionPane.ERROR_MESSAGE);
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
		showError(msg);
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
	 * Gets the stack trace from a Throwable as a String.
	 * 
	 * @param t The Throwable to get the stack trace from.
	 * @return The stack trace.
	 */
	private static String getTrace(Throwable t) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(t.getClass().getCanonicalName());
		buffer.append(": ");
		buffer.append(t.getMessage());
		buffer.append("\n\n");
		for (StackTraceElement trace : t.getStackTrace()) {
			buffer.append(trace.toString());
			buffer.append("\n");
		}
		return buffer.toString();
	}
	
}
