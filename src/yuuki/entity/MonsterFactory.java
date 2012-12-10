package yuuki.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import yuuki.action.*;

/**
 * Generates monsters based on their names.
 */
public class MonsterFactory {

	private class MonsterDefinition {
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
		public Action[] attacks;
	}
	
	private HashMap<String, MonsterDefinition> monsters;
	
	public MonsterFactory() {
		monsters = new HashMap<String, MonsterDefinition>();
		defineMonsters();
	}
	
	public NonPlayerCharacter create(String name, int level) {
		MonsterDefinition md = monsters.get(name);
		VariableStat hp = new VariableStat("health", md.hp, md.hpg);
		VariableStat mp = new VariableStat("mana", md.mp, md.mpg);
		Stat str = new Stat("strength", md.str, md.strg);
		Stat def = new Stat("defense", md.def, md.defg);
		Stat mag = new Stat("magic", md.mag, md.magg);
		Stat agl = new Stat("agility", md.agl, md.aglg);
		Stat acc = new Stat("accuracy", md.acc, md.accg);
		Stat luk = new Stat("luck", md.luk, md.lukg);
		Action[] mvs = new Action[md.attacks.length];
		for (int i = 0; i < md.attacks.length; i++) {
			mvs[i] = md.attacks[i].clone();
		}
		NonPlayerCharacter m;
		m = new NonPlayerCharacter(name, level, mvs, hp, mp, str, def, agl,
									acc, mag, luk, md.xp);
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
	public NonPlayerCharacter[] createRandomTeam(int size, int levelMin,
			int levelMax, String... names) {
		NonPlayerCharacter[] monsters = new NonPlayerCharacter[size];
		for (int i = 0; i < size; i++) {
			monsters[i] = createRandom(levelMin, levelMax, names);
		}
		return monsters;
	}
	
	/**
	 * @param levelMin
	 * @param levelMax
	 * @param names Leave null for any random monster.
	 * @return
	 */
	public NonPlayerCharacter createRandom(int levelMin, int levelMax,
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
		NonPlayerCharacter monster = create(name, level);
		return monster;
	}
	
	private void defineMonsters() {
		defineSlime();
	}
	
	private void defineSlime() {
		MonsterDefinition md = new MonsterDefinition();
		md.str = 5;
		md.def = 5;
		md.mag = 2;
		md.agl = 3;
		md.acc = 6;
		md.luk = 4;
		md.xp = 3;
		md.hp = 80;
		md.hpg = 15;
		md.attacks = new Action[1];
		// set monster attacks
		md.attacks[0] = new BasicAttack(4);
		// add to definitions list
		monsters.put("slime", md);
	}
	
}
