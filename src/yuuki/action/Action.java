/**
 * A character's action during the fight. This gives information on what type
 * of effect it has and who its target is.
 *
 * Derived classes should set the actual effects, as well as the cost stat and
 * the effect stat.
 */

package yuuki.action;

import java.util.ArrayList;
import java.util.HashSet;
import yuuki.entity.Character;
import yuuki.entity.Stat;
import yuuki.buff.Buff;

public abstract class Action implements Cloneable {

	/**
	 * The teams that were affected by this Action.
	 */
	private HashSet<Integer> affectedTeams;

	/**
	 * The Buff that is applied to the target.
	 */
	protected Buff targetBuff;
	
	/**
	 * The Buff that is applied to the origin.
	 */
	protected Buff originBuff;
	
	/**
	 * Where this Action came from; who is doing the Action.
	 */
	protected Character origin;
	
	/**
	 * The targets to whom the Action is to be applied.
	 */
	protected ArrayList<Character> targets;
	
	/**
	 * The actual effects after application of this action.
	 */
	protected int[] actualEffects;
	
	/**
	 * The amount of effect of this Action.
	 */
	protected double effect;
	
	/**
	 * The amount of cost of this Action.
	 */
	protected double cost;
	
	/**
	 * Whether the last application of this Action was successful.
	 */
	protected boolean successful;
	
	/**
	 * The stat that this Action applies cost to.
	 */
	protected Stat costStat;
	
	/**
	 * The stat that this Action applied the effect to.
	 */
	protected Stat effectStat;
	
	/**
	 * The name of this Action; used for display purposes.
	 */
	private String name;
	
	/**
	 * Creates a new Action.
	 *
	 * @param name The display name of this Action.
	 * @param effect The amount of effect that the new Action is to have.
	 * @param cost The amount of cost that the new Action will take.
	 * @param targetBuff The buff that is applied on application.
	 * @param originBuff The buff that is applied to the origin.
	 */
	public Action(String name, double effect, double cost, Buff targetBuff,
					Buff originBuff) {
		this.name = name;
		this.effect = effect;
		this.cost = cost;
		this.targetBuff = targetBuff;
		this.originBuff = originBuff;
		targets = new ArrayList<Character>();
		actualEffects = new int[0];
		origin = null;
		affectedTeams = new HashSet<Integer>();
	}
	
	/**
	 * Clones this Action.
	 *
	 * @return A clone of this Action.
	 */
	@SuppressWarnings("unchecked")
	public Action clone() {
		Action a2 = null;
		try {
			a2 = (Action) super.clone();
		} catch(CloneNotSupportedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		if (this.targetBuff != null) {
			a2.targetBuff = this.targetBuff.clone();
		}
		if (this.originBuff != null) {
			a2.originBuff = this.originBuff.clone();
		}
		// targets contents shallow-copied
		a2.targets = (ArrayList<Character>) this.targets.clone();
		// origin shallow-copied
		// name shallow-copied
		a2.actualEffects = new int[this.actualEffects.length];
		for (int i = 0; i < this.actualEffects.length; i++) {
			a2.actualEffects[i] = this.actualEffects[i];
		}
		return a2;
	}
	
	/**
	 * Applies this Action to targets. The cost is taken from the origin and
	 * the effects are attempted to be put on the targets. If the origin cannot
	 * supply the cost, the application will fail. No effects are put on the
	 * targets and this method returns false.
	 *
	 * Note that there are multiple situations that could cause this method to
	 * return false; the origin could lack the required stat to perform the
	 * Action, or it could have failed some other check. Returning false simply
	 * indicates that no effects were attempted to be put on the targets.
	 *
	 * Also note that returning true does not necessarily mean that the targets
	 * were affected; they could have blocked the effects. The return value
	 * only indicates whether effects were attempted to be put onto the
	 * targets, not whether it was successful.
	 *
	 * @return True if the effects were attempted to be put on the targets;
	 * otherwise, false.
	 */
	public boolean apply() {
		setAffectedTeams(); // only now can we be sure that the fighter id was set
		successful = applyCost();
		if (successful) {
			applyEffect();
			applyBuffs();
		}
		return successful;
	}
	
	/**
	 * Gets the name of this Action.
	 *
	 * @return This Action's name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the effect of this Action.
	 *
	 * @return The effect value of this Action.
	 */
	public double getEffect() {
		return effect;
	}
	
	/**
	 * Gets the cost of this Action.
	 *
	 * @return The amount that this Action costs.
	 */
	public double getCost() {
		return cost;
	}
	
	/**
	 * Sets the performer of this Action.
	 *
	 * @param performer The Character performing the action.
	 */
	public void setOrigin(Character performer) {
		origin = performer;
		setCostAndEffectStats(performer);
	}
	
	/**
	 * Gets the performer of this Action.
	 *
	 * @return The Character performing this Action if origin was set;
	 * otherwise, null.
	 */
	public Character getOrigin() {
		return origin;
	}
	
	/**
	 * Adds a target to the list.
	 *
	 * @param t The fighter to add to the target list.
	 */
	public void addTarget(Character t) {
		targets.add(t);
		int[] newEffects = new int[actualEffects.length + 1];
		for (int i = 0; i < actualEffects.length; i++) {
			newEffects[i] = actualEffects[i];
		}
		newEffects[newEffects.length - 1] = 0;
		actualEffects = newEffects;
		setCostAndEffectStats(t);
	}
	
	/**
	 * Gets the targeted fighters.
	 *
	 * @return An array containing the targets, which will be empty if no
	 * targets have been set.
	 */
	public ArrayList<Character> getTargets() {
		return targets;
	}
	
	/**
	 * Gets the targeted fighters' team's IDs.
	 *
	 * @return An array containing the teams with Characters who are targets,
	 * which will be empty if no targets have been set.
	 */
	public int[] getAffectedTeams() {
		int[] teams = new int[affectedTeams.size()];
		int k = 0;
		for (Integer i: affectedTeams) {
			teams[k++] = i.intValue();
		}
		return teams;
	}
	
	/**
	 * Checks whether the last application of this Action was successful.
	 *
	 * @return True if it was successful; false if it was not.
	 */
	public boolean wasSuccessful() {
		return successful;
	}
	
	/**
	 * Removes all current targets.
	 */
	public void clearTargets() {
		this.targets.clear();
	}
	
	/**
	 * Gets the actual damage caused by this Action, in the order of the
	 * targets.
	 *
	 * @return The actual caused damages.
	 */
	public int[] getActualEffects() {
		return actualEffects;
	}
	
	/**
	 * Gets the stat that is affected by the cost.
	 *
	 * @return The cost stat.
	 */
	public Stat getCostStat() {
		return costStat;
	}
	
	/**
	 * Gets the stat that is affected by the effect.
	 *
	 * @return The effect stat.
	 */
	public Stat getEffectStat() {
		return effectStat;
	}
	
	/**
	 * Gets the origin buff.
	 *
	 * @return The origin buff.
	 */
	public Buff getOriginBuff() {
		return originBuff;
	}
	 
	/**
	 * Gets the target buff.
	 *
	 * @return the target buff.
	 */
	public Buff getTargetBuff() {
		return targetBuff;
	}
	
	/**
	 * Applies any applicable Buffs to either the targets, origin, or both.
	 */
	protected abstract void applyBuffs();
	
	/**
	 * Applies the cost to the origin.
	 *
	 * @return True if the cost was successfully applied; otherwise, false.
	 */
	protected abstract boolean applyCost();
	
	/**
	 * Applies the effects to the targets. Overriding classes should set the
	 * actual applied effects of each target in actualEffects.
	 */
	protected abstract void applyEffect();
	
	/**
	 * Sets the cost stat. The stat is not actually directly used; it should be
	 * cloned to ensure that it is not.
	 *
	 * @param c The character to get the stats from.
	 */
	protected abstract void setCostStat(Character c);
	
	/**
	 * Sets the effect stat. The stat is not actually directly used; it should
	 * be cloned to ensure that it is not.
	 *
	 * @param c The character to get the stats from.
	 */
	protected abstract void setEffectStat(Character c);
	
	/**
	 * Sets the cost stats if they are not set and sets the effect stats if
	 * they are not set.
	 *
	 * @param c The character to set the stats from.
	 */
	private void setCostAndEffectStats(Character c) {
		if (costStat == null) {
			setCostStat(c);
		}
		if (effectStat == null) {
			setEffectStat(c);
		}
	}
	
	/**
	 * Sets the teams affected by this action.
	 */
	private void setAffectedTeams() {
		for (Character t: targets) {
			affectedTeams.add(t.getTeamId());
		}
	}

}