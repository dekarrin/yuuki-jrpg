package yuuki.file;

/**
 * Indicates that a resource contains invalid formatting.
 */
public class ResourceFormatException extends FormatException {
	
	private static final long serialVersionUID = 8919791404378326402L;
	
	/**
	 * The name of the resource that is invalid.
	 */
	private String resource;
	
	/**
	 * Creates a new InvalidResourceException.
	 * 
	 * @param resource The name of the invalid resource.
	 */
	public ResourceFormatException(String resource) {
		super("Resource '" + resource + "' is invalid");
		this.resource = resource;
	}
	
	/**
	 * Creates a new InvalidResourceException.
	 * 
	 * @param resource The name of the invalid resource.
	 * @param cause The InvalidRecordException that caused this exception to be
	 * thrown.
	 */
	public ResourceFormatException(String resource,
			RecordFormatException cause) {
		super("Resource '" + resource + "' is invalid - " + cause.getMessage(),
				cause);
		this.resource = resource;
	}
	
	/**
	 * Creates a new InvalidResourceException.
	 * 
	 * @param resource The name of the invalid resource.
	 * @param cause The Exception that caused this exception to be thrown.
	 */
	public ResourceFormatException(String resource, Throwable cause) {
		super("Resource '" + resource + "' is invalid", cause);
		this.resource = resource;
	}
	
	/**
	 * Gets the resource that caused the Exception.
	 * 
	 * @param resource
	 */
	public String getResource() {
		return resource;
	}
	
}
