package yuuki.entity;

import java.awt.Point;
import java.util.ArrayList;

import yuuki.action.Action;
import yuuki.action.ItemUse;
import yuuki.item.Item;
import yuuki.item.UsableItem;
import yuuki.ui.Interactable;
import yuuki.world.Land;
import yuuki.world.WalkGraph;

/**
 * A Character that can be controlled by the interface.
 */
public class PlayerCharacter extends Character {
	
	/**
	 * The direction that a player is facing.
	 */
	public static enum Orientation {
		EAST,
		NORTH,
		NORTHEAST,
		NORTHWEST,
		SOUTH,
		SOUTHEAST,
		SOUTHWEST,
		WEST
	}
	
	/**
	 * The direction that the player character is facing.
	 */
	public Orientation orientation;
	
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
	 * @param overworldArt The path to the overworld art.
	 * @param ui The interface that this Character should get input from.
	 */
	public PlayerCharacter(String name, int level, Action[] moves,
			VariableStat hp, VariableStat mp, Stat strength,
			Stat defense, Stat agility, Stat accuracy, Stat magic,
			Stat luck, String overworldArt, Interactable ui) {
		super(name, level, moves, hp, mp, strength, defense, agility, accuracy,
				magic, luck, overworldArt);
		this.ui = ui;
		orientation = Orientation.SOUTH;
	}
	
	@Override
	public String getBattleImage() {
		return null;
	}
	
	@Override
	public Point getNextMove(Land land) throws InterruptedException {
		WalkGraph graph = land.getWalkGraph(getLocation(), true);
		return ui.selectMove(graph, orientation);
	}
	
	@Override
	public String getOverworldImage() {
		String baseName = super.getOverworldImage();
		String ext = null;
		switch (orientation) {
			case NORTH:
				ext = "N";
				break;
				
			case SOUTH:
				ext = "S";
				break;
				
			case WEST:
				ext = "W";
				break;
				
			case EAST:
				ext = "E";
				break;
				
			case NORTHEAST:
				ext = "NE";
				break;
				
			case NORTHWEST:
				ext = "NW";
				break;
				
			case SOUTHEAST:
				ext = "SE";
				break;
				
			case SOUTHWEST:
				ext = "SW";
				break;
		}
		return baseName + "_" + ext;
	}
	
	@Override
	public boolean isTransferrable() {
		return true;
	}
	
	/**
	 * Decides what move to do next based on input from the interface.
	 *
	 * @param fighters The states of the players, including this one.
	 *
	 * @return The move that was selected by the player.
	 */
	@Override
	protected Action selectAction(ArrayList<ArrayList<Character>> fighters) {
		Action m = ui.selectAction(moves);
		if (m == null) {
			// should never happen unless current thread has been interrupted
			return null;
		} else if (m == itemUseAction) {
			Item item = ui.selectItem(inventory.getUsableItems());
			if (item == null) {
				return null;
			} else {
				Action a = m.clone();
				((ItemUse) a).setItem((UsableItem) item);
				return a;
			}
		} else {
			return m.clone();
		}
	}
	
	/**
	 * Selects the target of an action based on the other players.
	 *
	 * @param fighters The states of the other players.
	 *
	 * @return The target.
	 */
	@Override
	protected Character selectTarget(
			ArrayList<ArrayList<Character>> fighters) {
		return ui.selectTarget(fighters);
	}

	/**
	 * Gets the location of the point that the player is facing.
	 */
	public Point getFacingPoint() {
		Point loc = new Point(getLocation());
		int dx = 0;
		int dy = 0;
		switch (orientation) {
			case NORTH:
				dy = -1;
				break;
				
			case NORTHEAST:
				dy = -1;
				dx = 1;
				break;
				
			case EAST:
				dx = 1;
				break;
				
			case SOUTHEAST:
				dy = 1;
				dx = 1;
				break;
				
			case SOUTH:
				dy = 1;
				break;
				
			case SOUTHWEST:
				dy = 1;
				dx = -1;
				break;
				
			case WEST:
				dx = -1;
				break;
				
			case NORTHWEST:
				dx = -1;
				dy = -1;
				break;
		}
		loc.x += dx;
		loc.y += dy;
		return loc;
	}
	
}
