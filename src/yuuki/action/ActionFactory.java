package yuuki.action;

import java.util.HashMap;
import java.util.Map;

import yuuki.util.InvalidIndexException;

/**
 * Generates Action instances.
 */
public class ActionFactory {
	
	/**
	 * Holds the information needed to create an instance of an Action.
	 */
	private static class ActionDefinition {
		
		/**
		 * The arguments to the createInstance() method of the Action.
		 */
		public String[] args;
		
		/**
		 * The name of this ActionDefinition.
		 */
		public String name;
		
	}
	
	/**
	 * The bases for creating instances of Action. Used to get an instance
	 * factory from a String without using reflections.
	 */
	private Map<String, Action> bases;
	
	/**
	 * The definitions in this ActionFactory.
	 */
	private Map<Integer, ActionDefinition> definitions;
	
	/**
	 * Creates a new ActionFactory and the associated base Action instances.
	 */
	public ActionFactory() {
		definitions = new HashMap<Integer, ActionDefinition>();
		bases = new HashMap<String, Action>();
		createBaseActions();
	}
	
	/**
	 * Adds an action definition.
	 * 
	 * @param id The ID of the Action definition to create.
	 * @param name The class name of the base Action.
	 * @param args The arguments to the createInstance() method of the base
	 * Action.
	 */
	public void addDefinition(int id, String name, String[] args) {
		ActionDefinition def = new ActionDefinition();
		def.name = name;
		def.args = args;
		definitions.put(id, def);
	}
	
	/**
	 * Creates an instance of an Action from a definition ID.
	 * 
	 * @param id The definition ID of the Action to create.
	 * 
	 * @return The newly-created Action instance.
	 * 
	 * @throws InvalidIndexException If the given index ID does not exist.
	 */
	public Action createAction(int id) throws InvalidIndexException {
		ActionDefinition ad = definitions.get(id);
		if (ad == null) {
			throw new InvalidIndexException(id);
		}
		Action base = bases.get(ad.name);
		Action actualAction = base.createInstance(ad.args);
		return actualAction;
	}
	
	/**
	 * Creates one instance of each concrete subclass of Action and stores it
	 * in the action bases map indexed under the class' name.
	 */
	private void createBaseActions() {
		bases.put("BasicAttack", new BasicAttack(0));
		bases.put("BasicDefense", new BasicDefense(0));
		bases.put("Flee", new Flee());
		bases.put("ItemUse", new ItemUse());
	}
	
}
