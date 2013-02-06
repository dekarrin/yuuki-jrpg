package yuuki.file;

import java.io.IOException;

import yuuki.action.Action;
import yuuki.entity.ActionFactory;
import yuuki.entity.EntityFactory;

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
	 * @param location The path to the directory containing the definition
	 * files to be loaded, relative to the package structure.
	 * @param actions The ActionFactory to use for creating the definition
	 * actions.
	 */
	public EntityLoader(String location, ActionFactory actions) {
		super(location);
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
	 * @throws IOException If an IOException occurs.
	 */
	public EntityFactory load(String resource) throws IOException {
		EntityFactory factory = new EntityFactory();
		String[][] records = loadRecords(resource);
		for (String[] r : records) {
			String name = r[0];
			char disp = r[1].charAt(0);
			Action[] moves = parseMoves(r[2]);
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
			factory.addDefinition(name, hp, hpg, mp, mpg, str, strg, def, defg,
					agl, aglg, acc, accg, mag, magg, luk, lukg, moves, disp,
					xp);
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
	 */
	private Action[] parseMoves(String value) {
		String[] moves = splitMultiValue(value);
		int[] actionIds = parseIntArray(moves, 0);
		Action[] actions = new Action[actionIds.length];
		for (int i = 0; i < actionIds.length; i++) {
			actions[i] = actionFactory.createAction(actionIds[i]);
		}
		return actions;
	}
	
}
