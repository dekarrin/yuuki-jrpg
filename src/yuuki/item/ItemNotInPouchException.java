package yuuki.item;

/**
 * Indicates that an operation failed because an item was not in the pouch.
 */
public class ItemNotInPouchException extends Exception {
	
	private static final long serialVersionUID = 1282739105253952810L;

	/**
	 * Creates a new exception.
	 * 
	 * @param id The ID of the item that was not in the pouch.
	 */
	public ItemNotInPouchException(long id) {
		super("pouch does not have an item with ID " + id);
	}
	
	/**
	 * Creates a new exception.
	 * 
	 * @param item The Item that was not in the pouch.
	 */
	public ItemNotInPouchException(Item item) {
		super("pouch does not have specific item with ID " + item.getId());
	}
	
}
