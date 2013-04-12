package yuuki.item;

/**
 * Indicates that an operation failed on an InventoryPouch because there were
 * too many items in it.
 */
public class PouchFullException extends Exception {
	
	private static final long serialVersionUID = 3206802860180717159L;

	/**
	 * Creates a new PouchFullException.
	 */
	public PouchFullException() {
		super("The pouch is full");
	}
	
}
