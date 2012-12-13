package yuuki.ui;

import java.awt.BorderLayout;

import javax.swing.*;

import yuuki.entity.Character;
import yuuki.entity.Stat;

@SuppressWarnings("serial")
public class BattleScreen extends JPanel {
	
	public static final int BOTTOM_TEAM_INDEX = 0;
	
	public static final int TOP_TEAM_INDEX = 1;

	private Character[][] fighters;
	
	private FighterSprite[][] fighterGraphics;
	/**
	 * Adds the fighters to the screen.
	 * 
	 * @param fighters
	 */
	public void startBattle(Character[][] fighters) {
		this.fighters = fighters;
		setLayout(new BorderLayout());
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
	 * Adds the Characters to the screen.
	 */
	private void addCharacters() {
		// TODO: optimize for more than one team
		createAllGraphics();
		addBottomTeam();
		addTopTeam();
	}
	
	/**
	 * Creates the graphics for each character.
	 */
	private void createAllGraphics() {
		fighterGraphics = new FighterSprite[fighters.length][];
		for (int i = 0; i < fighters.length; i++) {
			fighterGraphics[i] = new FighterSprite[fighters[i].length];
			for (int j = 0; j < fighters[i].length; j++) {
				FighterSprite fs = new FighterSprite(fighters[i][j]);
				fighterGraphics[i][j] = fs;
				fighters[i][j].setSprite(fs);
			}
		}
	}
	
	/**
	 * Adds a team to the bottom row.
	 */
	private void addBottomTeam() {
		Box team = new Box(BoxLayout.X_AXIS);
		for (FighterSprite fs: fighterGraphics[BOTTOM_TEAM_INDEX]) {
			team.add(fs);
		}
		add(team, BorderLayout.SOUTH);
	}
	
	/**
	 * Adds a team to the top row.
	 */
	private void addTopTeam() {
		Box team = new Box(BoxLayout.X_AXIS);
		for (FighterSprite fs: fighterGraphics[TOP_TEAM_INDEX]) {
			team.add(fs);
		}
		add(team, BorderLayout.NORTH);
	}
}
