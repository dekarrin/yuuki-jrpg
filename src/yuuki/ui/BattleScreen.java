package yuuki.ui;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import java.util.ArrayList;

import yuuki.action.Action;
import yuuki.buff.Buff;
import yuuki.entity.Character;
import yuuki.entity.Stat;

@SuppressWarnings("serial")
public class BattleScreen extends JPanel {
	
	public static final int BOTTOM_TEAM_INDEX = 0;
	
	public static final int TOP_TEAM_INDEX = 1;

	private Character[][] fighters;
	
	private ArrayList<ArrayList<FighterSprite>> fighterGraphics;
	/**
	 * Adds the fighters to the screen.
	 * 
	 * @param fighters
	 */
	public void startBattle(Character[][] fighters) {
		this.fighters = fighters;
		setLayout(new BorderLayout());
		createAllGraphics();
		addCharacters();
	}
	
	/**
	 * Shows the transition into the battle.
	 */
	public void showStart() {
		// TODO empty
	}
	
	/**
	 * Shows a stat change on a fighter.
	 * 
	 * @param fighter The fighter to show the stat change for.
	 */
	public void showStatUpdate(Character fighter) {
		System.out.println(fighter.getName());
		System.out.println("STR: " + fighter.getStrength());
		System.out.println("DEF: " + fighter.getDefense());
		System.out.println("AGT: " + fighter.getAgility());
		System.out.println("ACC: " + fighter.getAccuracy());
		System.out.println("MAG: " + fighter.getMagic());
		System.out.println("LUK: " + fighter.getLuck());
		System.out.println();
	}
	
	/**
	 * Shows that a fighter took damage.
	 * 
	 * @param fighter The fighter that took damage.
	 * @param stat The stat that took damage.
	 * @param damage The amount of damage.
	 */
	public void showDamage(Character fighter, Stat stat, int damage) {
		FighterSprite sprite = (FighterSprite) fighter.getSprite();
		sprite.showDamage(stat, damage);
	}
	
	/**
	 * Shows that a fighter took damage.
	 * 
	 * @param fighter The fighter that took damage.
	 * @param stat The stat that took damage.
	 * @param damage The amount of damage.
	 */
	public void showDamage(Character fighter, Stat stat, double damage) {
		FighterSprite sprite = (FighterSprite) fighter.getSprite();
		sprite.showDamage(stat, damage);
	}
	
	/**
	 * Shows that a fighter recovered.
	 * 
	 * @param fighter The fighter that recovered.
	 * @param stat The stat that was recovered.
	 * @param damage The amount of recovery.
	 */
	public void showRecovery(Character fighter, Stat stat, int amount) {
		FighterSprite sprite = (FighterSprite) fighter.getSprite();
		sprite.showRecovery(stat, amount);
	}
	
	/**
	 * Shows that a fighter recovered.
	 * 
	 * @param fighter The fighter that recovered.
	 * @param stat The stat that was recovered.
	 * @param damage The amount of recovery.
	 */
	public void showRecovery(Character fighter, Stat stat, double amount) {
		FighterSprite sprite = (FighterSprite) fighter.getSprite();
		sprite.showRecovery(stat, amount);
	}
	
	/**
	 * Shows a sprite preparing to do an action.
	 * 
	 * @param action The action to show.
	 */
	public void showActionPreparation(Action action) {
		Character fighter = action.getOrigin();
		FighterSprite sprite = (FighterSprite) fighter.getSprite();
		sprite.showActionPreparation(action);
	}
	
	/**
	 * Shows a sprite failing at an action.
	 * 
	 * @param action The action to show.
	 */
	public void showActionFailure(Action action) {
		Character fighter = action.getOrigin();
		FighterSprite sprite = (FighterSprite) fighter.getSprite();
		sprite.showActionFailure(action);
	}
	
	/**
	 * Shows a sprite using an action.
	 * 
	 * @param action The action to show.
	 */
	public void showActionUse(Action action) {
		Character fighter = action.getOrigin();
		FighterSprite sprite = (FighterSprite) fighter.getSprite();
		sprite.showActionUse(action);
	}
	
	/**
	 * Shows a buff being activated on a sprite.
	 * 
	 * @param buff The buff to show the activation of.
	 */
	public void showBuffActivation(Buff buff) {
		Character fighter = buff.getTarget();
		FighterSprite sprite = (FighterSprite) fighter.getSprite();
		sprite.showBuffActivation(buff);
	}
	
	/**
	 * Shows a buff being deactivated on a sprite.
	 * 
	 * @param buff The buff to show the deactivation of.
	 */
	public void showBuffDeactivation(Buff buff) {
		Character fighter = buff.getTarget();
		FighterSprite sprite = (FighterSprite) fighter.getSprite();
		sprite.showBuffDeactivation(buff);
	}
	
	/**
	 * Shows a buff being applied to a sprite.
	 * 
	 * @param buff The buff to the activation of.
	 */
	public void showBuffApplication(Buff buff) {
		Character fighter = buff.getTarget();
		FighterSprite sprite = (FighterSprite) fighter.getSprite();
		sprite.showBuffApplication(buff);
	}
	
	/**
	 * Removes a sprite from the screen.
	 * 
	 * @param fighter The fighter whose sprite to remove.
	 */
	public void showCharacterRemoval(Character fighter) {
		FighterSprite sprite =  (FighterSprite) fighter.getSprite();
		// TODO: Search algo that runs in better than O(n) time
		for (ArrayList<FighterSprite> teamGraphics: fighterGraphics) {
			if (teamGraphics.remove(sprite)) {
				break;
			}
		}
		refreshSprites();
	}
	
	/**
	 * Removes all sprites on the screen and adds the ones in this
	 * BattleScreen's list.
	 */
	private void refreshSprites() {
		removeCharacters();
		addCharacters();
	}
	
	/**
	 * Removes all sprites from the screen.
	 */
	private void removeCharacters() {
		removeAll();
	}
	
	/**
	 * Adds the Characters to the screen.
	 */
	private void addCharacters() {
		// TODO: optimize for more than one team
		addBottomTeam();
		addTopTeam();
	}
	
	/**
	 * Creates the graphics for each character.
	 */
	private void createAllGraphics() {
		fighterGraphics =
				new ArrayList<ArrayList<FighterSprite>>(fighters.length);
		for (int i = 0; i < fighters.length; i++) {
			ArrayList<FighterSprite> teamGraphics =
					new ArrayList<FighterSprite>(fighters[i].length);
			for (int j = 0; j < fighters[i].length; j++) {
				FighterSprite fs = new FighterSprite(fighters[i][j]);
				teamGraphics.add(fs);
				fighters[i][j].setSprite(fs);
			}
			fighterGraphics.add(i, teamGraphics);
		}
	}
	
	/**
	 * Adds a team to the bottom row.
	 */
	private void addBottomTeam() {
		Box team = new Box(BoxLayout.X_AXIS);
		for (FighterSprite fs: fighterGraphics.get(BOTTOM_TEAM_INDEX)) {
			team.add(fs);
		}
		add(team, BorderLayout.SOUTH);
	}
	
	/**
	 * Adds a team to the top row.
	 */
	private void addTopTeam() {
		Box team = new Box(BoxLayout.X_AXIS);
		for (FighterSprite fs: fighterGraphics.get(TOP_TEAM_INDEX)) {
			team.add(fs);
		}
		add(team, BorderLayout.NORTH);
	}

	/**
	 * Shows the victory animation for the given characters.
	 * 
	 * @param cs The characters to show the animation for.
	 */
	public void showCharacterVictory(Character[] cs) {
		for (int i = 0; i < cs.length; i++) {
			FighterSprite sprite = (FighterSprite) cs[i].getSprite();
			sprite.showCharacterVictory();
		}
	}
}
