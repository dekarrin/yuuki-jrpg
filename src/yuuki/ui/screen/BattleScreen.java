package yuuki.ui.screen;

import java.util.ArrayList;

import yuuki.action.Action;
import yuuki.animation.engine.AnimationManager;
import yuuki.buff.Buff;
import yuuki.entity.Character;
import yuuki.entity.Stat;
import yuuki.sprite.FighterSprite;

/**
 * The screen shown for a battle.
 */
@SuppressWarnings("serial")
public class BattleScreen extends Screen<ScreenListener> {
	
	/**
	 * The index of the team that should be shown on the bottom of the screen.
	 */
	public static final int BOTTOM_TEAM_INDEX = 0;
	
	/**
	 * The index of the team that should be shown on the top of the team.
	 */
	public static final int TOP_TEAM_INDEX = 1;
	
	/**
	 * The horizontal spacing between each sprite.
	 */
	private static final int SPRITE_HGAP = 30;
	
	/**
	 * The animation engine for the game.
	 */
	private AnimationManager animationEngine;
	
	/**
	 * The graphics displayed on the screen.
	 */
	private ArrayList<ArrayList<FighterSprite>> fighterGraphics;
	
	/**
	 * Creates this BattleScreen and makes it visible.
	 */
	public BattleScreen(int width, int height,
			AnimationManager animationEngine) {
		super(width, height);
		setLayout(null); // can't be fighting an LM for control of sprites
		this.animationEngine = animationEngine;
	}
	
	/**
	 * Adds the fighters to the screen.
	 * 
	 * @param fighters The fighters to add.
	 */
	public void initBattle(Character[][] fighters) {
		createAllGraphics(fighters);
		refreshSprites();
	}
	
	@Override
	public void setInitialProperties() {}
	
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
	 * Removes a sprite from the screen.
	 * 
	 * @param fighter The fighter whose sprite to remove.
	 */
	public void showCharacterRemoval(Character fighter) {
		FighterSprite sprite = (FighterSprite) fighter.getSprite();
		// TODO: Search that runs better than O(n) time
		for (ArrayList<FighterSprite> teamGraphics: fighterGraphics) {
			if (teamGraphics.remove(sprite)) {
				break;
			}
		}
		refreshSprites();
	}
	
	/**
	 * Shows the victory animation for the given characters.
	 * 
	 * @param cs The characters to show the animation for.
	 */
	public void showCharacterVictory(Character[] cs) {
		for (Character element : cs) {
			FighterSprite sprite = (FighterSprite) element.getSprite();
			sprite.showCharacterVictory();
		}
	}
	
	/**
	 * Shows that a fighter took damage. This method is thread-safe and does
	 * not directly manipulate Swing components.
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
	 * Shows that a fighter took damage. This method is thread-safe and does
	 * not directly manipulate Swing components.
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
	 * Shows that a fighter recovered. This method is thread-safe and does not
	 * directly manipulate Swing components.
	 * 
	 * @param fighter The fighter that recovered.
	 * @param stat The stat that was recovered.
	 * @param amount The amount of recovery.
	 */
	public void showRecovery(Character fighter, Stat stat, double amount) {
		FighterSprite sprite = (FighterSprite) fighter.getSprite();
		sprite.showRecovery(stat, amount);
	}
	
	/**
	 * Shows that a fighter recovered. This method is thread-safe and does not
	 * directly manipulate Swing components.
	 * 
	 * @param fighter The fighter that recovered.
	 * @param stat The stat that was recovered.
	 * @param amount The amount of recovery.
	 */
	public void showRecovery(Character fighter, Stat stat, int amount) {
		FighterSprite sprite = (FighterSprite) fighter.getSprite();
		sprite.showRecovery(stat, amount);
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
		FighterSprite fs = (FighterSprite) fighter.getSprite();
		fs.showStatUpdate(fighter);
	}
	
	/**
	 * Adds a team to the bottom row.
	 */
	private void addBottomTeam() {
		int x = 0, y = 0;
		for (FighterSprite fs : fighterGraphics.get(BOTTOM_TEAM_INDEX)) {
			y = getSetHeight() - fs.getHeight();
			fs.moveTo(x, y);
			add(fs.getComponent());
			x += fs.getWidth() + SPRITE_HGAP;
		}
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
	 * Adds a team to the top row.
	 */
	private void addTopTeam() {
		int x = 0, y = 0;
		for (FighterSprite fs : fighterGraphics.get(TOP_TEAM_INDEX)) {
			fs.moveTo(x, y);
			add(fs.getComponent());
			x += fs.getWidth() + SPRITE_HGAP;
		}
	}
	
	/**
	 * Creates the graphics for each character.
	 * 
	 * @param fighters The fighters to make the graphics for.
	 */
	private void createAllGraphics(Character[][] fighters) {
		fighterGraphics =
				new ArrayList<ArrayList<FighterSprite>>(fighters.length);
		for (int i = 0; i < fighters.length; i++) {
			ArrayList<FighterSprite> teamGraphics =
					new ArrayList<FighterSprite>(fighters[i].length);
			for (int j = 0; j < fighters[i].length; j++) {
				AnimationManager a = animationEngine;
				FighterSprite fs = new FighterSprite(fighters[i][j], a);
				teamGraphics.add(fs);
				fighters[i][j].setSprite(fs);
			}
			fighterGraphics.add(i, teamGraphics);
		}
	}
	
	/**
	 * Removes all sprites on the screen and adds the ones in this
	 * BattleScreen's list.
	 */
	private void refreshSprites() {
		removeCharacters();
		addCharacters();
		repaint();
	}
	
	/**
	 * Removes all sprites from the screen.
	 */
	private void removeCharacters() {
		removeAll();
	}
	
}
