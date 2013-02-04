package yuuki.world;

/**
 * Thrown when a portal links to a land that hasn't been loaded.
 */
@SuppressWarnings("serial")
public class InvalidLinkNameException extends RuntimeException {
	
	public InvalidLinkNameException(String linkName) {
		super(linkName);
	}
	
}
