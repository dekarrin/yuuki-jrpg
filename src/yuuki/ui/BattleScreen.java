package yuuki.ui;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import yuuki.entity.Character;

@SuppressWarnings("serial")
public class BattleScreen extends JPanel {

	private Character[][] fighters;
	
	private FighterSprite[][] fighterGraphics;
	
	/**
	 * Adds the fighters to the screen.
	 * 
	 * @param fighters
	 */
	public void startBattle(Character[][] fighters) {
		this.fighters = fighters;
		addCharacters();
	}
	
	/**
	 * Shows the transition into the battle.
	 */
	public void showStart() {
		
	}
	
	/**
	 * Adds the Characters to the screen.
	 */
	private void addCharacters() {
		// TODO: optimize for more than one team
		createAllGraphics();
		addTopTeam();
		addBottomTeam();
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
}
