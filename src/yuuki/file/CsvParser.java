package yuuki.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Reads a CSV file.
 */
public class CsvParser {
	
	/**
	 * Gets a CsvParser that uses ',' for the field separator, '\n' for the
	 * record separator, and '"' for the field delimiter.
	 * 
	 * @param stream The InputStream to read the CSV data from.
	 * 
	 * @return A CsvParser that uses the above common characters.
	 */
	public static CsvParser defaultParser(InputStream stream) {
		CsvParser parser = new CsvParser(stream, '\n', ',', '"');
		return parser;
	}
	
	/**
	 * The character that delimits field.
	 */
	private char fieldDelimiter;
	
	/**
	 * The character that separates fields.
	 */
	private char fieldSeparator;
	
	/**
	 * The character that separates records.
	 */
	private char recordSeparator;
	
	/**
	 * The stream being read for CSV data.
	 */
	private BufferedReader stream;
	
	/**
	 * Creates a new CsvParser.
	 * 
	 * @param stream The stream that has CSV data.
	 * @param recordSeparator The character that separates records.
	 * @param fieldSeparator The character that separates fields.
	 * @param fieldDelimiter The character that delimits field data.
	 */
	public CsvParser(InputStream stream, char recordSeparator,
			char fieldSeparator, char fieldDelimiter) {
		this.stream = new BufferedReader(new InputStreamReader(stream));
		this.recordSeparator = recordSeparator;
		this.fieldSeparator = fieldSeparator;
		this.fieldDelimiter = fieldDelimiter;
	}
	
	/**
	 * Closes the stream.
	 */
	public void close() {
		try {
			stream.close();
		} catch (IOException e) {
			System.err.println("Stream couldn't close");
		}
	}
	
	/**
	 * Parses all the CSV data at once.
	 * 
	 * @return A two-dimensional array containing all records.
	 */
	public String[][] read() throws IOException {
		ArrayList<String[]> records = new ArrayList<String[]>();
		String recordLine = null;
		while ((recordLine = readRecord()) != null) {
			if (recordLine.charAt(0) != '#') {
				records.add(parseLine(recordLine));
			}
		}
		return records.toArray(new String[0][]);
	}
	
	/**
	 * Parses a field to only contain the delimited data.
	 * 
	 * @param rawField The field data directly from the split.
	 * 
	 * @return The delimited field data only.
	 */
	private String parseField(String rawField) {
		StringBuffer buffer = new StringBuffer("");
		boolean isWithinDelimiters = false;
		for (int i = 0; i < rawField.length(); i++) {
			char currentChar = rawField.charAt(i);
			if (currentChar == fieldDelimiter) {
				if (isWithinDelimiters) {
					break;
				} else {
					isWithinDelimiters = true;
				}
			} else if (isWithinDelimiters) {
				buffer.append(currentChar);
			}
		}
		return buffer.toString();
	}
	
	/**
	 * Parses a record line into a series of fields.
	 * 
	 * @param line The String containing the record.
	 * 
	 * @return The fields from the record.
	 */
	private String[] parseLine(String line) {
		ArrayList<String> fields = new ArrayList<String>();
		String[] rawFields = line.split("" + fieldSeparator);
		for (String rf : rawFields) {
			fields.add(parseField(rf));
		}
		return fields.toArray(new String[0]);
	}
	
	/**
	 * Reads the next record. The stream is read until either the record
	 * separator character or the end of the stream is reached.
	 * 
	 * @return The next record, or null if the end of the stream is reached.
	 */
	private String readRecord() throws IOException {
		StringBuffer buffer = null;
		int nextByte = -1;
		char nextChar = '\0';
		do {
			nextByte = stream.read();
			if (nextByte != -1) {
				if (buffer == null) {
					buffer = new StringBuffer("");
				}
				nextChar = (char) nextByte;
				if (nextChar != recordSeparator) {
					buffer.append(nextChar);
				}
			}
		} while (nextByte != -1 && nextChar != recordSeparator);
		return (buffer != null) ? buffer.toString() : null;
	}
	
}
