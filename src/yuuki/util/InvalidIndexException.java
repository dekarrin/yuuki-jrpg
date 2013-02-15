package yuuki.util;

/**
 * Thrown from factories when a given index for instance creation does not
 * refer to any instance.
 */
public class InvalidIndexException extends Exception {
	
	private static final long serialVersionUID = 7070998669198800095L;

	/**
	 * Creates a new InvalidIndexException.
	 * 
	 * @param index The index that caused the exception.
	 */
	public InvalidIndexException(Object index) {
		super(index.toString());
	}
	
	/**
	 * Creates a new InvalidIndexException.
	 * 
	 * @param index The index that caused the exception.
	 */
	public InvalidIndexException(int index) {
		super(index + "");
	}
	
	/**
	 * Creates a new InvalidIndexException.
	 * 
	 * @param index The index that caused the exception.
	 */
	public InvalidIndexException(short index) {
		super(index + "");
	}
	
	/**
	 * Creates a new InvalidIndexException.
	 * 
	 * @param index The index that caused the exception.
	 */
	public InvalidIndexException(byte index) {
		super(index + "");
	}
	
	/**
	 * Creates a new InvalidIndexException.
	 * 
	 * @param index The index that caused the exception.
	 */
	public InvalidIndexException(long index) {
		super(index + "");
	}
	
	/**
	 * Creates a new InvalidIndexException.
	 * 
	 * @param index The index that caused the exception.
	 */
	public InvalidIndexException(float index) {
		super(index + "");
	}
	
	/**
	 * Creates a new InvalidIndexException.
	 * 
	 * @param index The index that caused the exception.
	 */
	public InvalidIndexException(char index) {
		super(index + "");
	}
	
	/**
	 * Creates a new InvalidIndexException.
	 * 
	 * @param index The index that caused the exception.
	 */
	public InvalidIndexException(boolean index) {
		super(index + "");
	}
	
}
