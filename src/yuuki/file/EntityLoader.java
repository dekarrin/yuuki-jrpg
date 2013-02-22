package yuuki.file;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

import yuuki.action.Action;
import yuuki.action.ActionFactory;
import yuuki.entity.EntityFactory;
import yuuki.util.InvalidIndexException;

/**
 * Loads entity definition files.
 */
public class EntityLoader extends CsvResourceLoader {
	
	/**
	 * Creates Action instances in EntityFactory instances loaded by this
	 * EntityLoader.
	 */
	private ActionFactory actionFactory;
	
	/**
	 * Creates a new EntityLoader for entity definition files at the specified
	 * location.
	 * 
	 * @param directory The directory containing the definition files to be
	 * loaded.
	 * @param actions The ActionFactory to use for creating the definition
	 * actions.
	 */
	public EntityLoader(File directory, ActionFactory actions) {
		super(directory);
		this.actionFactory = actions;
	}
	
	/**
	 * Creates a new EntityLoader for resource files in the given ZIP file.
	 *
	 * @param archive The ZIP file containing the resource files to be loaded.
	 * @param zipRoot The root within the ZIP file of all files to be loaded.
	 * @param actions The ActionFactory to use for creating the definition
	 * actions.
	 */
	public EntityLoader(ZipFile archive, String zipRoot, ActionFactory
			actions) {
		super(archive, zipRoot);
		this.actionFactory = actions;
	}
	
	/**
	 * Loads the data from an entity definitions resource file into an
	 * EntityFactory object.
	 * 
	 * @param resource The path to the entity definitions file to load,
	 * relative to the resource root.
	 * 
	 * @return The EntityFactory object if the resource file exists; otherwise,
	 * null.
	 * 
	 * @throws ResourceNotFoundException If the resource does not exist.
	 * @throws ResourceFormatException
	 * @throws IOException If an IOException occurs.
	 */
	public EntityFactory load(String resource) throws
	ResourceNotFoundException, ResourceFormatException, IOException {
		EntityFactory factory = new EntityFactory();
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
	 * Parses the attacks value into valid Actions by using the ActionFactory
	 * in this EntityLoader.
	 * 
	 * @param value The exact value of the field containing the moves.
	 * 
	 * @return The Actions that the value refers to.
	 * 
	 * @throws FieldFormatException If the moves field contains an invalid
	 * value.
	 */
	private Action[] parseMoves(String value) throws FieldFormatException {
		String[] moves = splitMultiValue(value);
		int[] actionIds = parseIntArray(moves, 0);
		Action[] actions = new Action[actionIds.length];
		for (int i = 0; i < actionIds.length; i++) {
			try {
				actions[i] = actionFactory.createAction(actionIds[i]);
			} catch (InvalidIndexException e) {
				throw new FieldFormatException("moves", value);
			}
		}
		return actions;
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
			EntityFactory factory) throws RecordFormatException {
		String[] r = records[num];
		String name = r[0];
		//char disp = r[1].charAt(0);
		Action[] moves;
		try {
			moves = parseMoves(r[2]);
		} catch (FieldFormatException e) {
			throw new RecordFormatException(num, e);
		}
		int hp = Integer.parseInt(r[3]);
		int mp = Integer.parseInt(r[4]);
		int str = Integer.parseInt(r[5]);
		int def = Integer.parseInt(r[6]);
		int agl = Integer.parseInt(r[7]);
		int acc = Integer.parseInt(r[8]);
		int mag = Integer.parseInt(r[9]);
		int luk = Integer.parseInt(r[10]);
		int hpg = Integer.parseInt(r[11]);
		int mpg = Integer.parseInt(r[12]);
		int strg = Integer.parseInt(r[13]);
		int defg = Integer.parseInt(r[14]);
		int aglg = Integer.parseInt(r[15]);
		int accg = Integer.parseInt(r[16]);
		int magg = Integer.parseInt(r[17]);
		int lukg = Integer.parseInt(r[18]);
		int xp = Integer.parseInt(r[19]);
		String overArt = r[20];
		factory.addDefinition(name, hp, hpg, mp, mpg, str, strg, def, defg,
				agl, aglg, acc, accg, mag, magg, luk, lukg, moves, overArt,
				xp);
	}
	
}
