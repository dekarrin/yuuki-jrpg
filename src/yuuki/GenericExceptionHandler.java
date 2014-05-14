package yuuki;

/**
 * Shows an exception to the user with the option to immediately terminate
 * the program.
 */
public class GenericExceptionHandler implements Thread.UncaughtExceptionHandler {

	public void uncaughtException(Thread t, Throwable e) {
		e.printStackTrace();
		yuuki.ui.DialogHandler.showError(e);
	}
	
}
