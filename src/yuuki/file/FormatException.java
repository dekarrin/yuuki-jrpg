package yuuki.file;

/**
 * Indicates a problem with formatting.
 */
public class FormatException extends Exception {
	
	private static final long serialVersionUID = 8454597720033038305L;
	
	/**
	 * Constructs a new FormatException with null as its detail message.
	 */
	public FormatException() {
		super();
	}
	
	/**
	 * Constructs a new FormatException with the specified detail message.
	 * 
	 * @param message The detail message.
	 */
	public FormatException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a new FormatException with the specified detail message and
	 * cause.
	 * 
	 * @param message The detail message.
	 * @param cause The cause of the exception.
	 */
	public FormatException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Constructs a new FormatException with the specified cause.
	 * 
	 * @param cause The cause of the exception.
	 */
	public FormatException(Throwable cause) {
		super(cause);
	}
	
}
