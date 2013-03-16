package yuuki.file;

/**
 * Indicates that a field in a resource file has invalid contents.
 */
public class FieldFormatException extends FormatException {
	
	private static final long serialVersionUID = 3414066625971692004L;
	
	/**
	 * The name of the field.
	 */
	private String field;
	
	/**
	 * The value of the field.
	 */
	private String value;
	
	/**
	 * Creates a new InvalidFieldException.
	 * 
	 * @param field The name of the invalid field.
	 * @param value The value of the invalid field. This is converted to a
	 * string value using toString().
	 */
	public FieldFormatException(String field, int value) {
		this(field, new Integer(value));
	}
	
	/**
	 * Creates a new InvalidFieldException.
	 * 
	 * @param field The name of the invalid field.
	 * @param value The value of the invalid field. This is converted to a
	 * string value using toString().
	 */
	public FieldFormatException(String field, Object value) {
		super("'"+field+"' contains invalid value '"+value+"'");
		this.field = field;
		this.value = value.toString();
	}
	
	/**
	 * Creates a new InvalidFieldException.
	 * 
	 * @param field The name of the invalid field.
	 * @param value The value of the invalid field. This is converted to a
	 * string value using toString().
	 * @param cause The exception that caused this exception to be thrown.
	 */
	public FieldFormatException(String field, String value, Throwable cause) {
		super("'"+field+"' contains invalid value '"+value+"'", cause);
		this.field = field;
		this.value = value.toString();
	}
	
	/**
	 * Gets the field.
	 * 
	 * @return The field.
	 */
	public String getField() {
		return field;
	}
	
	/**
	 * Gets the value.
	 * 
	 * @return The value.
	 */
	public String getValue() {
		return value;
	}
	
}
