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
	
	private Stat[][] originalStats;
	
	/**
	 * Adds the fighters to the screen.
	 * 
	 * @param fighters
	 */
	public void startBattle(Character[][] fighters) {
		this.fighters = fighters;
		setLayout(new BorderLayout());
		extractStats();
		addCharacters();
	}
	
	/**
	 * Shows the transition into the battle.
	 */
	public void showStart() {
		
	}
	
	/**
	 * Shows a stat change on a fighter.
	 * 
	 * @param fighter The fighter to show the stat change for.
	 */
	public void showStatChange(Character fighter) {
		
	}
	
	/**
	 * Extracts the original stats from the fighters for comparison.
	 */
	private void extractStats() {
		originalStats = new Stat[fighters.length][];
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
				fighterGraphics[i][j] = new FighterSprite(fighters[i][j]);
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
