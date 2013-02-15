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
	 * @throws IOException If an IOException occurs.
	 */
	public ActionFactory load(String resource) throws
	ResourceNotFoundException, IOException {
		ActionFactory factory = new ActionFactory();
		String[][] records = loadRecords(resource);
		for (String[] r : records) {
			int id = Integer.parseInt(r[0]);
			String name = r[1];
			String[] args = splitMultiValue(r[2]);
			factory.addDefinition(id, name, args);
			advanceProgress(1.0 / records.length);
		}
		return factory;
	}
	
}
