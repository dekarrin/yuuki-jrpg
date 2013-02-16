package yuuki.util;

/**
 * Thrown from factories when a given index for instance creation does not
 * refer to any instance.
 */
public class InvalidIndexException extends Exception {
	
	private static final long serialVersionUID = 7070998669198800095L;
	
	/**
	 * The invalid index.
	 */
	private final String index;
	
	/**
	 * Creates a new InvalidIndexException.
	 * 
	 * @param index The index that caused the exception.
	 */
	public InvalidIndexException(boolean index) {
		this(index + "");
	}
	
	/**
	 * Creates a new InvalidIndexException.
	 * 
	 * @param index The index that caused the exception.
	 */
	public InvalidIndexException(byte index) {
		this(index + "");
	}
	
	/**
	 * Creates a new InvalidIndexException.
	 * 
	 * @param index The index that caused the exception.
	 */
	public InvalidIndexException(char index) {
		this(index + "");
	}
	
	/**
	 * Creates a new InvalidIndexException.
	 * 
	 * @param index The index that caused the exception.
	 */
	public InvalidIndexException(float index) {
		this(index + "");
	}
	
	/**
	 * Creates a new InvalidIndexException.
	 * 
	 * @param index The index that caused the exception.
	 */
	public InvalidIndexException(int index) {
		this(index + "");
	}
	
	/**
	 * Creates a new InvalidIndexException.
	 * 
	 * @param index The index that caused the exception.
	 */
	public InvalidIndexException(long index) {
		this(index + "");
	}
	
	/**
	 * Creates a new InvalidIndexException.
	 * 
	 * @param index The index that caused the exception.
	 */
	public InvalidIndexException(Object index) {
		super("'" + ((index != null) ? (index.toString()) : ("null")) +
				"' is an invalid index");
		if (this.index == null) {
			this.index = null;
		} else {
			this.index = index.toString();
		}
	}
	
	/**
	 * Creates a new InvalidIndexException.
	 * 
	 * @param index The index that caused the exception.
	 */
	public InvalidIndexException(short index) {
		this(index + "");
	}
	
	/**
	 * Gets the string value of the index that caused the exception.
	 * 
	 * @return The index.
	 */
	public String getIndex() {
		return index;
	}
	
}
