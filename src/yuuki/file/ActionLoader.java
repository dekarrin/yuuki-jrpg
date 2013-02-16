package yuuki.file;

import java.io.IOException;

import yuuki.action.ActionFactory;

/**
 * Loads action definition files into an ActionFactory.
 */
public class ActionLoader extends CsvResourceLoader {
	
	/**
	 * Creates a new ActionLoader for action definition files at the specified
	 * location.
	 * 
	 * @param location The path to the directory containing the action
	 * definition files to be loaded, relative to the package structure.
	 */
	public ActionLoader(String location) {
		super(location);
	}
	
	/**
	 * Loads the action definitions from a file.
	 * 
	 * @param resource The location of the file to load, relative to the
	 * resource root.
	 * 
	 * @return An ActionFactory with the definitions from the file.
	 * 
	 * @throws ResourceNotFoundException If the resource could not be found.
	 * @throws ResourceFormatException If the resource has invalid contents.
	 * @throws IOException If an IOException occurs.
	 */
	public ActionFactory load(String resource) throws
	ResourceNotFoundException, ResourceFormatException, IOException {
		ActionFactory factory = new ActionFactory();
		String[][] records = loadRecords(resource);
		for (int i = 0; i < records.length; i++) {
			try {
				parseRecord(records, i, factory);
			} catch (RecordFormatException e) {
				throw new ResourceFormatException(resource, e);
			}
			advanceProgress(1.0 / records.length);
		}
		return factory;
	}
	
	/**
	 * Parses a record and adds it to the factory.
	 * 
	 * @param records The record list to get the record from.
	 * @param num The index of the record being parsed.
	 * @param factory The factory to add the record to.
	 * 
	 * @throws RecordFormatException If the given record is invalid.
	 */
	private void parseRecord(String[][] records, int num,
			ActionFactory factory) throws RecordFormatException {
		String[] r = records[num];
		int id = 0;
		try {
			id = parseIntField("ID", r[0]);
		} catch (FieldFormatException e) {
			throw new RecordFormatException(num, e);
		}
		String name = r[1];
		String[] args = splitMultiValue(r[2]);
		factory.addDefinition(id, name, args);
	}
	
}
