package yuuki.entity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import yuuki.action.Action;
import yuuki.action.BasicAttack;
import yuuki.action.BasicDefense;
import yuuki.action.Flee;
import yuuki.ui.Interactable;

/**
 * Generates entities based on their names.
 */
public class EntityFactory {
	
	/**
	 * Holds the information needed to create an instance of an Action.
	 */
	private static class ActionDefinition {
		
		/**
		 * The arguments to the getInstance() method of the Action.
		 */
		public String[] args;
		
		/**
		 * This particular ActionDefinition's ID in the list of all
		 * definitions.
		 */
		public int id;
		
		/**
		 * The name of this ActionDefinition.
		 */
		public String name;
		
	}
	
	/**
	 * Holds the information needed to create the arguments for creating an
	 * instance of a Character.
	 */
	private static class EntityDefinition {
		
		/**
		 * The base accuracy.
		 */
		public int acc = 0;
		
		/**
		 * The accuracy gained per level.
		 */
		public int accg = 0;
		
		/**
		 * The base agility.
		 */
		public int agl = 0;
		
		/**
		 * The agility gained per level.
		 */
		public int aglg = 0;
		
		/**
		 * The IDs of the ActionDefinitions that define this entity's actions.
		 */
		public int[] attacks;
		
		/**
		 * The base defense.
		 */
		public int def = 0;
		
		/**
		 * The defense gained per level.
		 */
		public int defg = 0;
		
		/**
		 * The base hit points.
		 */
		public int hp = 0;
		
		/**
		 * The hit points gained per level.
		 */
		public int hpg = 0;
		
		/**
		 * The base luck.
		 */
		public int luk = 0;
		
		/**
		 * The luck gained per level.
		 */
		public int lukg = 0;
		
		/**
		 * The base magic.
		 */
		public int mag = 0;
		
		/**
		 * The magic gained per level.
		 */
		public int magg = 0;
		
		/**
		 * The base mana points.
		 */
		public int mp = 0;
		
		/**
		 * The mana points gained per level.
		 */
		public int mpg = 0;
		
		/**
		 * The name of the entity. This will be inaccurate for the player
		 * character, who is stored as an arbitrary string to allow the player
		 * to select his own name.
		 */
		public String name;
		
		/**
		 * The base strength.
		 */
		public int str = 0;
		
		/**
		 * The strength gained per level.
		 */
		public int strg = 0;
		
		/**
		 * The amount of experience gained when this entity is defeated. This
		 * is not used by the player character.
		 */
		public int xp = 0;
		
	}
	
	/**
	 * Holds the arguments to creating an actual instance of a Character.
	 */
	private static class StatModel {
		
		/**
		 * The accuracy of the Character.
		 */
		public Stat acc;
		
		/**
		 * The agility of the Character.
		 */
		public Stat agl;
		
		/**
		 * The defense of the Character.
		 */
		public Stat def;
		
		/**
		 * The hit points of the Character.
		 */
		public VariableStat hp;
		
		/**
		 * The luck of the Character.
		 */
		public Stat luk;
		
		/**
		 * The magic of the Character.
		 */
		public Stat mag;
		
		/**
		 * The actions that the Character may perform.
		 */
		public Action[] moves;
		
		/**
		 * The mana points of the Character.
		 */
		public VariableStat mp;
		
		/**
		 * The name of the Character.
		 */
		public String name;
		
		/**
		 * The strength of the Character.
		 */
		public Stat str;
		
		/**
		 * The experience points gained when this Character is defeated.
		 */
		public int xp;
		
	}
	
	/**
	 * The location of the file containing the action definitions. The location
	 * is relative to the package structure.
	 */
	public static final String ACTIONS_FILE = "/yuuki/resource/actions.csv";
	
	/**
	 * The location of the file containing the monster definitions. The
	 * location is relative to the package structure.
	 */
	public static final String MONSTERS_FILE = "/yuuki/resource/monsters.csv";
	
	/**
	 * The bases for creating instances of Action. Used to get an instance
	 * factory from a String without using reflections.
	 */
	private HashMap<String, Action> actionBases;
	
	/**
	 * All defined actions as read from the action definitions file.
	 */
	private HashMap<Integer, ActionDefinition> actions;
	
	/**
	 * All defined entities as read from the entity definitions file.
	 */
	private HashMap<String, EntityDefinition> entities;
	
	/**
	 * Allocates a new EntityFactory. The definition files are read and the
	 * list of base actions is populated.
	 */
	public EntityFactory() {
		entities = new HashMap<String, EntityDefinition>();
		actions = new HashMap<Integer, ActionDefinition>();
		actionBases = new HashMap<String, Action>();
		createBaseActions();
		try {
			readMonsterDefinitions();
			readActionDefinitions();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a NonPlayerCharacter of a specified level.
	 * 
	 * @param name The name of the NPC; must match one in the definitions file.
	 * @param level The level of the NPC. This must be at least 1.
	 * 
	 * @return An NPC with the given name and level.
	 */
	public NonPlayerCharacter createNpc(String name, int level) {
		StatModel sm = getStatModel(name);
		NonPlayerCharacter m;
		m = new NonPlayerCharacter(	sm.name, level, sm.moves, sm.hp, sm.mp,
				sm.str, sm.def, sm.agl, sm.acc, sm.mag,
				sm.luk, sm.xp);
		return m;
	}
	
	/**
	 * Creates the player character.
	 * 
	 * @param name The name to give the player character.
	 * @param level The starting level of the player character.
	 * @param ui A reference to the game's user interface.
	 * 
	 * @return An instance of PlayerCharacter with the given name and level.
	 */
	public PlayerCharacter createPlayer(String name, int level,
			Interactable ui) {
		StatModel sm = getStatModel("__PLAYER");
		PlayerCharacter m;
		m = new PlayerCharacter(	name, level, sm.moves, sm.hp, sm.mp,
				sm.str, sm.def, sm.agl, sm.acc, sm.mag,
				sm.luk, ui);
		return m;
	}
	
	/**
	 * Creates a random NPC from a set of names.
	 * 
	 * @param levelMin The minimum (inclusive) level of the NPC.
	 * @param levelMax The maximum (inclusive) level of the NPC.
	 * @param names The names to select from. One is chosen randomly from this
	 * array. Each element must match one name from the definitions file. Set
	 * this argument to null for any random entity.
	 * 
	 * @return A random NPC with one of the names given and with a level in the
	 * given range.
	 */
	public NonPlayerCharacter createRandomNpc(int levelMin, int levelMax,
			String... names) {
		Set<String> validNames = entities.keySet();
		if (names != null) {
			Set<String> wantedNames = new HashSet<String>();
			for (String n: names) {
				wantedNames.add(n);
			}
			validNames.retainAll(wantedNames);
		}
		if (validNames.size() == 0) {
			throw new IllegalArgumentException("no valid names given");
		}
		String[] possibleNames =  validNames.toArray(new String[0]);
		int nameInd = (int) Math.floor(Math.random() * possibleNames.length);
		int diff = levelMax - levelMin;
		int level = levelMin + (int) Math.floor(Math.random() * diff);
		String name = possibleNames[nameInd];
		NonPlayerCharacter entity = createNpc(name, level);
		return entity;
	}
	
	/**
	 * Creates a number of random NPCs from a set of names.
	 * 
	 * @param size The number of NPCs to create.
	 * @param levelMin The minimum (inclusive) level of the NPCs.
	 * @param levelMax The maximum (inclusive) level of the NPCs.
	 * @param names The names to select from. The NPC types are chosen randomly
	 * from this array. Each element must match one name from the definitions
	 * file. Set this argument to null for any random entities.
	 * 
	 * @return An array containing the specified number of random NPCs of the
	 * given types and with levels in the given range.
	 */
	public NonPlayerCharacter[] createRandomNpcTeam(int size, int levelMin,
			int levelMax, String... names) {
		NonPlayerCharacter[] entities = new NonPlayerCharacter[size];
		for (int i = 0; i < size; i++) {
			entities[i] = createRandomNpc(levelMin, levelMax, names);
		}
		return entities;
	}
	
	/**
	 * Returns an array containing the names of all defined entities.
	 * 
	 * @return The entities.
	 */
	public String[] getEntityNames() {
		String[] names = entities.keySet().toArray(new String[0]);
		return names;
	}
	
	/**
	 * Creates one instance of each concrete subclass of Action and stores it
	 * in the action bases map indexed under the class' name.
	 */
	private void createBaseActions() {
		actionBases.put("BasicAttack", new BasicAttack(0));
		actionBases.put("BasicDefense", new BasicDefense(0));
		actionBases.put("Flee", new Flee());
	}
	
	/**
	 * Gets a stat model for an entity name. The stat model contains all the
	 * arguments necessary for instantiation.
	 * 
	 * @param name The name of the entity.
	 * 
	 * @return The stat model for the given entity name.
	 */
	private StatModel getStatModel(String name) {
		StatModel sm = new StatModel();
		EntityDefinition md = entities.get(name.toLowerCase());
		sm.name = md.name;
		sm.hp = new VariableStat("health", md.hp, md.hpg);
		sm.mp = new VariableStat("mana", md.mp, md.mpg);
		sm.str = new Stat("strength", md.str, md.strg);
		sm.def = new Stat("defense", md.def, md.defg);
		sm.mag = new Stat("magic", md.mag, md.magg);
		sm.agl = new Stat("agility", md.agl, md.aglg);
		sm.acc = new Stat("accuracy", md.acc, md.accg);
		sm.luk = new Stat("luck", md.luk, md.lukg);
		sm.moves = new Action[md.attacks.length];
		for (int i = 0; i < md.attacks.length; i++) {
			ActionDefinition ad = actions.get(md.attacks[i]);
			Action base = actionBases.get(ad.name);
			Action actualAction = base.createInstance(ad.args);
			sm.moves[i] = actualAction;
		}
		sm.xp = md.xp;
		return sm;
	}
	
	/**
	 * Parses a line from the action definitions file into an ActionDefinition
	 * instance.
	 * 
	 * @param line The line to parse.
	 * 
	 * @return The ActionDefinition parsed from the line.
	 */
	private ActionDefinition parseActionDefinition(String line) {
		ActionDefinition ad = new ActionDefinition();
		String[] parts = readCsv(line);
		ad.id = Integer.parseInt(parts[0]);
		ad.name = parts[1];
		ad.args = new String[0];
		if (!parts[2].equals("")) {
			ad.args = parts[2].split(":");
		}
		return ad;
	}
	
	/**
	 * Parses a line from the monster definitions file into an EntityDefinition
	 * instance.
	 * 
	 * @param line The line to parse.
	 * 
	 * @return The EntityDefinition parsed from the line.
	 */
	private EntityDefinition parseMonsterDefinition(String line) {
		EntityDefinition md = new EntityDefinition();
		String[] parts = readCsv(line);
		md.name	= parts[0];
		md.attacks = parseToInts(parts[1].split(":"), 0);
		int[] stats = parseToInts(parts, 2);
		md.hp	= stats[0];
		md.mp	= stats[1];
		md.str	= stats[2];
		md.def	= stats[3];
		md.agl	= stats[4];
		md.acc	= stats[5];
		md.mag	= stats[6];
		md.luk	= stats[7];
		md.hpg	= stats[8];
		md.mpg	= stats[9];
		md.strg	= stats[10];
		md.defg	= stats[11];
		md.aglg	= stats[12];
		md.accg	= stats[13];
		md.magg	= stats[14];
		md.lukg	= stats[15];
		md.xp	= stats[16];
		return md;
	}
	
	/**
	 * Parses an array of strings into an array of ints.
	 * 
	 * @param toParse The array of strings to parse.
	 * @param start The element to start parsing at.
	 * 
	 * @return An array of ints parsed from the given starting element of the
	 * given array.
	 */
	private int[] parseToInts(String[] toParse, int start) {
		int[] parsed = new int[toParse.length-start];
		for (int i = start; i < toParse.length; i++) {
			parsed[i - start] = Integer.parseInt(toParse[i]);
		}
		return parsed;
	}
	
	/**
	 * Reads and parses the action definitions file and stores the result in
	 * the action definitions map.
	 * 
	 * @throws FileNotFoundException If the action definitions file cannot be
	 * found.
	 * @throws IOException If an IOException occurs.
	 */
	private void readActionDefinitions() throws FileNotFoundException,
	IOException {
		BufferedReader r = null;
		InputStream file = getClass().getResourceAsStream(ACTIONS_FILE);
		r = new BufferedReader(new InputStreamReader(file));
		String line = null;
		int num = 1;
		while ((line = r.readLine()) != null) {
			if (line.charAt(0) != '#') {
				try {
					ActionDefinition ad = parseActionDefinition(line);
					actions.put(ad.id, ad);
				} catch(RuntimeException e) {
					System.err.println("Error parsing line #" + num + " of " +
							"action definitions file.");
				}
			}
			num++;
		}
		r.close();
	}
	
	/**
	 * Reads a CSV-formatted line into an array of Strings. The line is assumed
	 * to use a comma to delimit fields and double quotes to contain all
	 * values. Everything in a field before the first double quote and after
	 * the second double quote is ignored.
	 * 
	 * @param line The line to read.
	 * 
	 * @return An array containing the CSV fields in the line.
	 */
	private String[] readCsv(String line) {
		String[] parts = line.split(",");
		for (int i = 0; i < parts.length; i++) {
			String field = parts[i];
			StringBuffer f = new StringBuffer("");
			boolean reading = false;
			boolean began = false;
			for (int j = 0; j < field.length(); j++) {
				char c = field.charAt(j);
				if (reading) {
					if (c == '"') {
						reading = false;
						break;
					} else {
						f.append(c);
					}
				} else {
					if (c == '"') {
						reading = true;
						began = true;
					}
				}
			}
			if (!(began && !reading)) {
				throw new RuntimeException();
			} else {
				parts[i] = f.toString();
			}
		}
		return parts;
	}
	
	/**
	 * Reads and parses the entity definitions file and stores the result in
	 * the entity definitions map.
	 * 
	 * @throws FileNotFoundException If the entity definitions file cannot be
	 * found.
	 * @throws IOException If an IOException occurs.
	 */
	private void readMonsterDefinitions() throws FileNotFoundException,
	IOException {
		BufferedReader r = null;
		InputStream file = getClass().getResourceAsStream(MONSTERS_FILE);
		r = new BufferedReader(new InputStreamReader(file));
		String line = null;
		int num = 1;
		while ((line = r.readLine()) != null) {
			if (line.charAt(0) != '#') {
				try {
					// entity names are case-insensitive
					EntityDefinition md = parseMonsterDefinition(line);
					entities.put(md.name.toLowerCase(), md);
				} catch(RuntimeException e) {
					System.err.println("Error parsing line #" + num + " of " +
							"monster definitions file.");
				}
			}
			num++;
		}
		r.close();
	}
	
}