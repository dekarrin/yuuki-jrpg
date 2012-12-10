/**
 * The battle engine for all fights in the Yuuki system. It is not
 * independently-driven. It must be explicitly instructed to advance its
 * execution. This allows it to be decoupled from any interfaces.
 *
 * @version 10/20/12
 */

package yuuki.battle;
import java.util.ArrayList;
import yuuki.action.Action;
import yuuki.entity.Character;

public class Battle {

	/**
	 * The potential states of a Battle.
	 */
	public static enum State {
		STARTING_TURN,
		GETTING_ACTION,
		APPLYING_ACTION,
		APPLYING_BUFFS,
		CHECKING_DEATH,
		ENDING_TURN,
		CHECKING_VICTORY,
		LOOTING,
		ENDING;
	}

	/**
	 * The percent of total mana gained in a turn.
	 */
	private static final double MANA_GEN = 0.05;

	/**
	 * The current state of this Battle. This determines what action is
	 * taken the next time advance() is called.
	 */
	private State state;
	
	/**
	 * The last state of this Battle. This is the state that it was last in.
	 * This is useful for users to know what was last done.
	 */
	private State lastState;

	/**
	 * The last Action that a Character selected.
	 */
	private Action lastAction;

	/**
	 * The currently active fighters. The first array is the list of teams and
	 * the second array is the fighter on that team. For example, you would use
	 * the notation fighters.get(1).get(0) to get the first fighter of the
	 * second team.
	 */
	private ArrayList<ArrayList<Character>> fighters;

	/**
	 * The player whose turn it currently is.
	 */
	private int currentFighter;
	
	/**
	 * The fighters that are to be removed.
	 */
	private ArrayList<Character> removedFighters;
	
	/**
	 * The Characters arranged in the order that they take their turns.
	 */
	private ArrayList<Character> turnOrder;
	
	/**
	 * Amount of mana last regenerated.
	 */
	private int regeneratedMana;
	
	/**
	 * Whether the current fighter has fled.
	 */
	private boolean fled = false;

	/**
	 * Begins a new battle with the given participants.
	 *
	 * @param participants The Characters involved in the battle. The array
	 * is the teams of the Characters; each of these is an array containing
	 * the Characters on that team.
	 */
	public Battle(Character[][] participants) {
		removedFighters = new ArrayList<Character>();
		assignToFighters(participants);
		orderFighters();
		currentFighter = 0;
		state = State.STARTING_TURN;
	}
	
	/**
	 * Converts this Battle to a String.
	 *
	 * @return The String version.
	 */
	public String toString() {
		String str = "";
		str += state + "/" + lastState + ":" + lastAction + " - \n";
		str += currentFighter + " - " + turnOrder + "\n";
		str += fighters;
		return str;
	}

	/**
	 * Advances battle through the current execution state. Each state
	 * consists of only a few operations that are carried out before the
	 * next state is reached. In between execution states the user is free
	 * to query this Battle for information about the fight.
	 *
	 * @return True if more calls to advance() are required before the battle
	 * is over; otherwise, false.
	 */
	public boolean advance() {
		lastState = state;
		boolean moreCallsNeeded = true;
		switch (state) {
			case STARTING_TURN:
				getCurrentFighter().removeExpiredBuffs();
				regenerateMana();
				state = State.GETTING_ACTION;
				break;

			case GETTING_ACTION:
				getCurrentFighter().emptyExpiredBuffs();
				lastAction = getCurrentFighter().getNextAction(fighters);
				state = State.APPLYING_ACTION;
				break;

			case APPLYING_ACTION:
				lastAction.apply();
				state = State.APPLYING_BUFFS;
				break;

			case APPLYING_BUFFS:
				checkFlee();
				if (!fled) {
					getCurrentFighter().applyBuffs();
				}
				state = State.CHECKING_DEATH;
				break;

			case CHECKING_DEATH:
				checkDeath();
				state = State.ENDING_TURN;
				break;

			case ENDING_TURN:
				emptyRemovedFighters();
				checkTeamStatus();
				state = State.CHECKING_VICTORY;
				break;

			case CHECKING_VICTORY:
				if (battleIsOver()) {
					state = State.LOOTING;
				} else {
					setNextPlayer();
					state = State.STARTING_TURN;
				}
				break;

			case LOOTING:
				calculateLoot();
				state = State.ENDING;
				break;

			case ENDING:
				moreCallsNeeded = false;
				break;
		}
		return moreCallsNeeded;
	}
	
	/**
	 * Gets the amount of mana last regenerated.
	 *
	 * @param The amount of mana.
	 */
	public int getRegeneratedMana() {
		return regeneratedMana;
	}
	
	/**
	 * Gets the fighters that are on a team.
	 *
	 * @param team The team to get the fighters for.
	 *
	 * @return The fighters on the given team.
	 */
	public ArrayList<Character> getFighters(int team) {
		return fighters.get(team);
	}
	
	/**
	 * Gets the removed fighters.
	 *
	 * @return The fighters removed during the last advancement.
	 */
	public ArrayList<Character> getRemovedFighters() {
		return removedFighters;
	}
	
	/**
	 * Gets the fighter whose turn it currently is.
	 *
	 * @return The current fighter.
	 */
	public Character getCurrentFighter() {
		return turnOrder.get(currentFighter);
	}
	
	/**
	 * Gets the last action that a Character chose. This will be null if the
	 * last advancement did not produce an Action.
	 *
	 * @return The last Action that a fighter chose, or null if the fighter did
	 * not just choose an Action.
	 */
	public Action getLastAction() {
		return lastAction;
	}
	
	/**
	 * Gets where in the battle process this Battle currently is.
	 *
	 * @return The state of the battle.
	 */
	public State getState() {
		return state;
	}
	
	/**
	 * Gets where in the battle process this Battle was before the current step.
	 *
	 * @return The state of the battle.
	 */
	public State getLastState() {
		return lastState;
	}
	
	/**
	 * Gets the number of teams in this Battle.
	 *
	 * @return The number of teams.
	 */
	public int getTeamCount() {
		return fighters.size();
	}
	
	/**
	 * Checks if the last move was a flee.
	 * 
	 * @return Whether it was a flee.
	 */
	public boolean fleeOccured() {
		return fled;
	}

	/**
	 * Regenerates the mana of the current Character.
	 */
	private void regenerateMana() {
		Character c = getCurrentFighter();
		int oldMana = c.getMP();
		int manaMax = c.getMaxMP();
		int amount = (int) Math.floor(manaMax * MANA_GEN);
		c.gainMP(amount);
		regeneratedMana = c.getMP() - oldMana;
	}
	
	/**
	 * Removes the origin fighter if he has fled.
	 */
	private void checkFlee() {
		if (lastAction instanceof yuuki.action.Flee) {
			if (lastAction.wasSuccessful()) {
				fled = true;
				removeFighter(lastAction.getOrigin());
			}
		}
	}

	/**
	 * Removes the targeted fighter if he is now dead.
	 */
	private void checkDeath() {
		ArrayList<Character> targets = lastAction.getTargets();
		for (Character c: targets) {
			if (!c.isAlive()) {
				removeFighter(c);
			}
		}
	}

	/**
	 * Removes the targeted fighter's team if there is no longer anyone on
	 * it.
	 */
	private void checkTeamStatus() {
		int[] affectedTeams = lastAction.getAffectedTeams();
		for (int t: affectedTeams) {
			if (fighters.get(t).size() == 0) {
				removeTeam(t);
			}
		}
	}

	/**
	 * Checks whether the battle is over.
 	 *
	 * @return True if only one team remains in active play; otherwise false.
	 */
	private boolean battleIsOver() {
		return (fighters.size() == 1);
	}

	/**
	 * Sets the current player to the player whose turn is next.
	 */
	private void setNextPlayer() {
		fled = false;
		currentFighter++;
		if (currentFighter >= turnOrder.size()) {
			currentFighter = 0;
		}
	}

	/**
	 * Calculates the loot for the battle. The result is kept in this
	 * Battle's state.
	 */
	private void calculateLoot() {
		// TODO: loot calculation
	}

	/**
	 * Assigns characters to the fighters list. Each character's fighter ID and
	 * team ID is set and they are added to the internal array. The team ID is
	 * arbitrary; as long as Characters on the same team have the same team ID,
	 * the actual number doesn't matter. The fighter ID is simply set in the
	 * order that the participants are given.
	 */
	private void assignToFighters(Character[][] teams) {
		this.fighters = new ArrayList<ArrayList<Character>>(teams.length);
		for (Character[] t: teams) {
			ArrayList<Character> team = new ArrayList<Character>(t.length);
			for (Character c: t) {
				c.startFighting(team.size(), fighters.size());
				team.add(c);
			}
			fighters.add(team);
		}
	}
	
	/**
	 * Sets the play order for the battle.
	 */
	private void orderFighters() {
		turnOrder = new ArrayList<Character>();
		for (ArrayList<Character> team: fighters) {
			for (Character c: team) {
				turnOrder.add(c);
			}
		}
	}
	
	/**
	 * Removes a fighter from the field. All of the Character's battle params
	 * are reset and it is removed from the list of fighters.
	 *
	 * @param f The fighter to remove.
	 */
	private void removeFighter(Character f) {
		int team = f.getTeamId();
		int id = f.getFighterId();
		int turnId = turnOrder.indexOf(f);
		turnOrder.remove(turnId);
		if (currentFighter > turnId) {
			currentFighter--;
		}
		fighters.get(team).remove(id);
		reassignIds(team, id);
		removedFighters.add(f);
	}
	
	/**
	 * Empties the removed fighters array.
	 */
	private void emptyRemovedFighters() {
		for (int i = 0; i < removedFighters.size(); i++) {
			Character f = removedFighters.get(i);
			f.stopFighting();
		}
		removedFighters.clear();
	}
	
	/**
	 * Removes a team from the field.
	 *
	 * @param teamId The id of the team to remove.
	 */
	private void removeTeam(int teamId) {
		fighters.remove(teamId);
		reassignTeams(teamId);
	}
	
	/**
	 * Decrements the ID of all fighters on the given team with an higher than
	 * the given one.
	 *
	 * @param teamId The ID of the team whose fighters to reassign.
	 * @param id The ID to shift all fighters left towards.
	 */
	private void reassignIds(int teamId, int id) {
		ArrayList<Character> team = fighters.get(teamId);
		for (Character c: team) {
			if (c.getFighterId() > id) {
				c.setFighterId(c.getFighterId() - 1);
			}
		}
	}
	
	/**
	 * Decrements the team ID of all fighters on teams higher than the given
	 * team.
	 *
	 * @param teamId The ID to shift all fighters left towards.
	 */
	private void reassignTeams(int teamId) {
		for (int i = teamId; i < fighters.size(); i++) {
			ArrayList<Character> team = fighters.get(i);
			for (Character c: team) {
				c.setTeamId(i);
			}
		}
	}
}
