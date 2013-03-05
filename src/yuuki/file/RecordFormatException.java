package yuuki.file;

/**
 * Indicates that there is something wrong with the format of a particular
 * record.
 */
public class RecordFormatException extends FormatException {
	
	private static final long serialVersionUID = -2858024429660397352L;
	
	/**
	 * The record number.
	 */
	private int record;
	
	/**
	 * Creates a new InvalidRecordException.
	 * 
	 * @param record The record number.
	 */
	public RecordFormatException(int record) {
		super("Record #" + record + " is invalid");
		this.record = record;
	}
	
	/**
	 * Creates a new InvalidRecordException with an InvalidFieldException as
	 * its cause.
	 * 
	 * @param record The record number.
	 * @param cause The InvalidFieldException that caused this exception.
	 */
	public RecordFormatException(int record, FieldFormatException cause) {
		super("Record #" + record + " is invalid - " + cause.getMessage(),
				cause);
		this.record = record;
	}
	
	/**
	 * Creates a new InvalidRecordException with an InvalidFieldException as
	 * its cause.
	 * 
	 * @param record The record number.
	 * @param message The message to display.
	 */
	public RecordFormatException(int record, String message) {
		super("Record #" + record + ": " + message);
		this.record = record;
	}
	
	/**
	 * Creates a new InvalidRecordException with an InvalidFieldException as
	 * its cause.
	 * 
	 * @param record The record number.
	 * @param message The message to display.
	 * @param cause The InvalidFieldException that caused this exception.
	 */
	public RecordFormatException(int record, String message,
			FieldFormatException cause) {
		super("Record #" + record + ": " + message, cause);
		this.record = record;
	}
	
	/**
	 * Creates a new InvalidRecordException with an InvalidFieldException as
	 * its cause.
	 * 
	 * @param record The record number.
	 * @param message The message to display.
	 * @param cause The exception that caused this exception.
	 */
	public RecordFormatException(int record, String message, Throwable cause) {
		super("Record #" + record + ": " + message, cause);
		this.record = record;
	}
	
	/**
	 * Creates a new InvalidRecordException.
	 * 
	 * @param record The record number.
	 * @param cause The cause of the exception.
	 */
	public RecordFormatException(int record, Throwable cause) {
		super("Record #" + record + " is invalid", cause);
		this.record = record;
	}
	
	/**
	 * Gets the record.
	 * 
	 * @return The record number.
	 */
	public int getRecord() {
		return record;
	}
	
}
