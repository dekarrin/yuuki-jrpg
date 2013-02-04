package yuuki.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import yuuki.action.Action;
import yuuki.ui.Interactable;

/**
 * Generates entities based on their names.
 */
public class EntityFactory {
	
	/**
	 * Holds the arguments to creating an actual instance of a Character.
	 */
	private static class EntityDefinition implements Cloneable {
		
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
		 * The name of the Character. Not used when creating a PlayerCharacter.
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
		
		/**
		 * Performs a deep-copy on this EntityDef.
		 * 
		 * @return the deep-copied EntityDef.
		 */
		@Override
		public EntityDefinition clone() {
			EntityDefinition d2 = null;
			try {
				d2 = (EntityDefinition) super.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			d2.hp = hp.clone();
			d2.mp = mp.clone();
			d2.str = str.clone();
			d2.def = def.clone();
			d2.agl = agl.clone();
			d2.acc = acc.clone();
			d2.mag = mag.clone();
			d2.luk = luk.clone();
			d2.moves = new Action[moves.length];
			for (int i = 0; i < moves.length; i++) {
				d2.moves[i] = moves[i].clone();
			}
			return d2;
		}
		
	}
	
	/**
	 * The name of the EntityDefinition for a player character.
	 */
	private static final String PLAYER_CHARACTER_NAME = "__PLAYER";
	
	/**
	 * All defined entities as read from the entity definitions file. When a
	 * definition is used to create an instance, it must be cloned, or else
	 * multiple instances of characters will share the same references to
	 * stats.
	 */
	private Map<String, EntityDefinition> definitions;
	
	/**
	 * Allocates a new EntityFactory. The definition files are read and the
	 * list of base actions is populated.
	 */
	public EntityFactory() {
		definitions = new HashMap<String, EntityDefinition>();
	}
	
	/**
	 * Adds a definition to this EntityFactory.
	 * 
	 * @param name The character's name.
	 * @param hp The base hit points.
	 * @param hpg The hit points gained per level.
	 * @param mp The base mana points.
	 * @param mpg The mana points gained per level.
	 * @param str The base strength.
	 * @param strg The strength gained per level.
	 * @param def The base defense.
	 * @param defg The defense gained per level.
	 * @param agl The base agility.
	 * @param aglg The agility gained per level.
	 * @param acc The base accuracy.
	 * @param accg The accuracy gained per level.
	 * @param mag The base magic ability.
	 * @param magg The magic ability gained per level.
	 * @param luk The base luck.
	 * @param lukg The luck gained per level.
	 * @param moves The actions that this character may perform.
	 * @param xp The experienced gained when this character is defeated.
	 */
	public void addDefinition(String name, int hp, int hpg, int mp, int mpg,
			int str, int strg, int def, int defg, int agl, int aglg, int acc,
			int accg, int mag, int magg, int luk, int lukg, Action[] moves,
			int xp) {
		EntityDefinition ed = new EntityDefinition();
		ed.name = name;
		ed.hp = new VariableStat("health", hp, hpg);
		ed.mp = new VariableStat("mana", mp, mpg);
		ed.str = new Stat("strength", str, strg);
		ed.def = new Stat("defense", def, defg);
		ed.agl = new Stat("agility", agl, aglg);
		ed.acc = new Stat("accuracy", acc, accg);
		ed.mag = new Stat("magic", mag, magg);
		ed.luk = new Stat("luck", luk, lukg);
		ed.moves = moves;
		ed.xp = xp;
		definitions.put(name.toLowerCase(), ed);
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
		EntityDefinition d = getDefinition(name);
		NonPlayerCharacter m;
		m = new NonPlayerCharacter(d.name, level, d.moves, d.hp, d.mp, d.str,
				d.def, d.agl, d.acc, d.mag, d.luk, d.xp);
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
		EntityDefinition d = getDefinition(PLAYER_CHARACTER_NAME);
		PlayerCharacter m;
		m = new PlayerCharacter(name, level, d.moves, d.hp, d.mp, d.str, d.def,
				d.agl, d.acc, d.mag, d.luk, ui);
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
		Set<String> validNames = definitions.keySet();
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
		String[] names = definitions.keySet().toArray(new String[0]);
		return names;
	}
	
	/**
	 * Gets an entity definition. The name is normalized and the definition is
	 * deep-cloned to ensure that its contents are never shared between two
	 * instances.
	 * 
	 * @param name The name of the entity.
	 * 
	 * @return The definition for the named entity.
	 */
	private EntityDefinition getDefinition(String name) {
		EntityDefinition d = definitions.get(name.toLowerCase());
		return d.clone();
	}
	
}
