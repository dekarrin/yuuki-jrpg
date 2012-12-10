/**
 * A Character that can be controlled by the interface.
 */

package yuuki.entity;

import java.util.ArrayList;

import yuuki.action.Action;
import yuuki.ui.Interactable;

public class PlayerCharacter extends Character {

	/**
	 * A reference to the user interface for this PC to get its moves from.
	 */
	private Interactable ui;

	/**
	 * Allocates a new Character. Most stats are set manually, but experience
	 * is automatically calculated from the starting level. All stats are the
	 * base stats; all actual stats are calculated by multiplying the stat gain
	 * by the level and adding the base stat.
	 *
	 * @param name The name of the Character.
	 * @param level The level of the new Character. XP is set to match this.
	 * @param moves The moves that this Character knows.
	 * @param hp The health stat of the new Character.
	 * @param mp The mana stat of the new Character.
	 * @param strength The physical strength of the Character.
	 * @param defense The Character's resistance to damage.
	 * @param agility The Character's avoidance of hits.
	 * @param accuracy The Character's ability to hit.
	 * @param magic The magical ability of the Character.
	 * @param luck The ability of the Character to get a critical hit.
	 * @param ui The interface that this Character should get input from.
	 */
	public PlayerCharacter(String name, int level, Action[] moves,
					VariableStat hp, VariableStat mp, Stat strength,
					Stat defense, Stat agility, Stat accuracy, Stat magic,
					Stat luck, Interactable ui) {
		super(name, level, moves, hp, mp, strength, defense, agility, accuracy,
				magic, luck);
		this.ui = ui;
	}
	
	/**
	 * Decides what move to do next based on input from the interface.
	 *
	 * @param fighters The states of the players, including this one.
	 *
	 * @return The move that was selected by the player.
	 */
	protected Action selectAction(ArrayList<ArrayList<Character>> fighters) {
		int index = ui.selectAction(moves);
		return moves[index].clone();
	}
	
	/**
	 * Selects the target of an action based on the other players.
	 *
	 * @param fighters The states of the other players.
	 *
	 * @return The target.
	 */
	protected Character selectTarget(
				ArrayList<ArrayList<Character>> fighters) {
		return ui.selectTarget(fighters);
	}

}
