package yuuki.entity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import yuuki.action.*;
import yuuki.ui.Interactable;

/**
 * Generates monsters based on their names.
 */
public class EntityFactory {
	
	public static final String MONSTER_DEFINITIONS_FILE = "data/monsters.csv";
	
	public static final String ACTION_DEFINITIONS_FILE = "data/actions.csv";
	
	private class ActionDefinition {
		public String name;
		public int id;
		public String[] args;
	}

	private class MonsterDefinition {
		public String name;
		public int str = 0;		// strength
		public int strg = 0;	// strength gain
		public int def = 0;		// defense
		public int defg = 0;	// defense gain
		public int mag = 0;
		public int magg = 0;
		public int agl = 0;
		public int aglg = 0;
		public int acc = 0;
		public int accg = 0;
		public int luk = 0;
		public int lukg = 0;
		public int hp = 0;
		public int hpg = 0;
		public int mp = 0;
		public int mpg = 0;
		public int xp = 0;
		public int[] attacks;
	}
	
	private class StatModel {
		public String name;
		public VariableStat hp;
		public VariableStat mp;
		public Stat str;
		public Stat def;
		public Stat agl;
		public Stat acc;
		public Stat mag;
		public Stat luk;
		public Action[] moves;
		public int xp;
	}
	
	private HashMap<String, MonsterDefinition> monsters;
	
	private HashMap<Integer, ActionDefinition> actions;
	
	private HashMap<String, Action> actionBases;
	
	public EntityFactory() {
		monsters = new HashMap<String, MonsterDefinition>();
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
	
	public PlayerCharacter createPlayer(String name, int level,
			Interactable ui) {
		StatModel sm = getStatModel(name);
		PlayerCharacter m;
		m = new PlayerCharacter(	sm.name, level, sm.moves, sm.hp, sm.mp,
									sm.str, sm.def, sm.agl, sm.acc, sm.mag,
									sm.luk, ui);
		return m;
	}
	
	public NonPlayerCharacter createNpc(String name, int level) {
		StatModel sm = getStatModel(name);
		NonPlayerCharacter m;
		m = new NonPlayerCharacter(	sm.name, level, sm.moves, sm.hp, sm.mp,
									sm.str, sm.def, sm.agl, sm.acc, sm.mag,
									sm.luk, sm.xp);
		return m;
	}
	
	/**
	 * 
	 * @param size
	 * @param levelMin
	 * @param levelMax
	 * @param names Leave null for any random monster.
	 * @return
	 */
	public NonPlayerCharacter[] createRandomNpcTeam(int size, int levelMin,
			int levelMax, String... names) {
		NonPlayerCharacter[] monsters = new NonPlayerCharacter[size];
		for (int i = 0; i < size; i++) {
			monsters[i] = createRandomNpc(levelMin, levelMax, names);
		}
		return monsters;
	}
	
	/**
	 * @param levelMin
	 * @param levelMax
	 * @param names Leave null for any random monster.
	 * @return
	 */
	public NonPlayerCharacter createRandomNpc(int levelMin, int levelMax,
			String... names) {
		Set<String> validNames = monsters.keySet();
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
		NonPlayerCharacter monster = createNpc(name, level);
		return monster;
	}
	
	private StatModel getStatModel(String name) {
		StatModel sm = new StatModel();
		MonsterDefinition md = monsters.get(name);
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
	
	private void createBaseActions() {
		actionBases.put("BasicAttack", new BasicAttack(0));
		actionBases.put("BasicDefense", new BasicDefense(0));
		actionBases.put("Flee", new Flee());
	}
	
	private void readMonsterDefinitions() throws FileNotFoundException,
	IOException {
		BufferedReader r = null;
		r = new BufferedReader(new FileReader(MONSTER_DEFINITIONS_FILE));
		String line = null;
		int num = 1;
		while ((line = r.readLine()) != null) {
			if (line.charAt(0) != '#') {
				try {
					MonsterDefinition md = parseMonsterDefinition(line);
					monsters.put(md.name, md);
				} catch(RuntimeException e) {
					System.err.println("Error parsing line #" + num + " of " +
							"monster definitions file.");
				}
			}
			num++;
		}
		r.close();
	}
	
	private void readActionDefinitions() throws FileNotFoundException,
	IOException {
		BufferedReader r = null;
		r = new BufferedReader(new FileReader(ACTION_DEFINITIONS_FILE));
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
	
	private int[] parseToInts(String[] toParse, int start) {
		int[] parsed = new int[toParse.length-start];
		for (int i = start; i < toParse.length; i++) {
			parsed[i] = Integer.parseInt(toParse[i]);
		}
		return parsed;
	}
	
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
	
	private MonsterDefinition parseMonsterDefinition(String line) {
		MonsterDefinition md = new MonsterDefinition();
		String[] parts = readCsv(line);
		md.name	= parts[0];
		md.attacks = parseToInts(parts[1].split(":"), 0);
		int[] stats = parseToInts(parts, 2);
		md.hp	= stats[0];
		md.hpg	= stats[1];
		md.mp	= stats[2];
		md.mpg	= stats[3];
		md.str	= stats[4];
		md.def	= stats[5];
		md.agl	= stats[6];
		md.acc	= stats[7];
		md.mag	= stats[8];
		md.luk	= stats[9];
		md.strg	= stats[10];
		md.defg	= stats[11];
		md.aglg	= stats[12];
		md.accg	= stats[13];
		md.magg	= stats[14];
		md.lukg	= stats[15];
		md.xp	= stats[16];
		return md;
	}
	
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
	
}
