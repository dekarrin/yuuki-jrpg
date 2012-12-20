/**
 * The game engine for the Yuuki JRPG project. This class may be executed
 * directly to run Yuuki.
 */

package yuuki;

import java.util.ArrayList;

import yuuki.ui.GraphicalInterface;
import yuuki.ui.Interactable;
import yuuki.ui.UiListener;
import yuuki.battle.Battle;
import yuuki.action.*;
import yuuki.buff.*;
import yuuki.entity.Character;
import yuuki.entity.*;

public class YuukiEngine implements Runnable, UiListener {

	/**
	 * The user interface.
	 */
	private Interactable ui;
	
	/**
	 * The player character.
	 */
	private PlayerCharacter player;
	
	/**
	 * The creator of all entities.
	 */
	private EntityFactory entityMaker;
	
	/**
	 * The current battle.
	 */
	private Battle battle;
	
	/**
	 * Program execution hook. Creates a new thread in which to execute the
	 * game engine in and then starts the thread.
	 *
	 * @param args Command line arguments. Not used.
	 */
	public static void main(String[] args) {
		YuukiEngine gameEngine = new YuukiEngine();
		gameEngine.run();
	}
	
	/**
	 * Creates a new YuukiEngine with a Swing-based GUI.
	 */
	public YuukiEngine() {
		ui = new GraphicalInterface(this);
		entityMaker = new EntityFactory();
	}
	
	/**
	 * Runs the engine.
	 */
	public void run() {
		ui.initialize();
		ui.switchToIntroScreen();
		/*
		while (stillFighting) {
			battleOneOnOne(player, createJill());
			stillFighting = ui.confirm("Battle again?", "Yes", "No");
		}
		ui.switchToEndingScreen();
		ui.destroy();*/
	}
	
	/**
	 * Starts a battle between two characters. Switches to the battle screen,
	 * runs the battle, then switches to the overworld screen.
	 *
	 * @param f1 The first fighter.
	 * @param f2 The second fighter.
	 *
	 * @return The winner of the battle.
	 */
	private Character battleOneOnOne(Character f1, Character f2) {
		Character[] t1 = {f1};
		Character[] t2 = {f2};
		Character[][] fighters = new Character[2][1];
		fighters[0] = t1;
		fighters[1] = t2;
		ui.display(null, "Oh no! Random monsters!");
		Battle b = new Battle(fighters);
		showBattle(fighters, b);
		Character winner = b.getFighters(0).get(0);
		return winner;
	}
	
	/**
	 * Switches to the battle screen, runs through a battle, then switches to
	 * the overworld screen.
	 *
	 * @param fighters The characters in the battle.
	 * @param battle The battle to show.
	 */
	private void showBattle(Character[][] fighters, Battle b) {
		ui.switchToBattleScreen(fighters);
		runBattle(b);
		ui.switchToOverworldScreen();
	}
	
	/**
	 * Runs a battle to completion. If the UI is switched to the battle screen,
	 * it is displayed there; otherwise it is not displayed at all.
	 *
	 * @param battle The battle to run
	 */
	private void runBattle(Battle battle) {
		while (battle.advance()) {
			switch (battle.getLastState()) {
				case STARTING_TURN:
					outputTurnStart(battle);
					break;
					
				case GETTING_ACTION:
					outputActionGet(battle);
					break;
					
				case APPLYING_ACTION:
					outputActionApplication(battle);
					break;
					
				case APPLYING_BUFFS:
					outputBuffApplication(battle);
					break;
					
				case CHECKING_DEATH:
					outputDeathCheck(battle);
					break;
					
				case ENDING_TURN:
					outputTeamDeathCheck(battle);
					break;
					
				case CHECKING_VICTORY:
					// not sure we care about this state
					break;
					
				case LOOTING:
					outputLoot(battle);
					break;
					
				default:
					break;
			}
			if (battle.getState() == Battle.State.ENDING) {
				outputVictory(battle);
			}
		}
	}
	
	/**
	 * Outputs the results of a battle's turn start phase to the user
	 * interface.
	 *
	 * @param battle The battle to output the state of.
	 */
	private void outputTurnStart(Battle battle) {
		Character c = battle.getCurrentFighter();
		int recoveredMana = battle.getRegeneratedMana();
		ui.display(c, "It looks like I'm up next.");
		ArrayList<Buff> expiredBuffs = c.getExpiredBuffs();
		for (Buff expired: expiredBuffs) {
			ui.showBuffDeactivation(expired);
		}
		if (recoveredMana != 0) {
			ui.showRecovery(c, c.getMPStat(), recoveredMana);
		}
		ui.showStatUpdate(c);
	}
	
	/**
	 * Outputs the results of a battle's action get phase to the user
	 * interface.
	 *
	 * @param battle The battle to output the state of.
	 */
	private void outputActionGet(Battle battle) {
		Action a = battle.getLastAction();
		ui.showActionPreperation(a);
	}
	
	/**
	 * Outputs the results of a battle's action application phase to the user
	 * interface.
	 *
	 * @param battle The battle to output the state of.
	 */
	private void outputActionApplication(Battle battle) {
		Action a = battle.getLastAction();
		if (a.wasSuccessful()) {
			if (a.getCostStat() != null) {
				outputActionCost(a);
			}
			ui.showActionUse(a);
			if (a.getEffectStat() != null) {
				outputActionEffects(a);
			}
			if (a.getOriginBuff() != null) {
				ui.showBuffActivation(a.getOriginBuff());
			}
			if (a.getTargetBuff() != null) {
				ui.showBuffActivation(a.getTargetBuff());
			}
		} else {
			ui.showActionFailure(a);
		}
	}
	
	/**
	 * Outputs the results of an action cost to the user interface.
	 *
	 * @param action The Action to output.
	 */
	private void outputActionCost(Action a) {
		ui.showDamage(a.getOrigin(), a.getCostStat(), (int)a.getCost());
		ui.showStatUpdate(a.getOrigin());
	}
	
	/**
	 * Outputs the effects of an action to the user interface.
	 *
	 * @param a The Action to output.
	 */
	private void outputActionEffects(Action a) {
		int[] effects = a.getActualEffects();
		ArrayList<Character> targets = a.getTargets();
		for (int i = 0; i < effects.length; i++) {
			Character t = targets.get(i);
			int damage = effects[i];
			ui.showDamage(t, a.getEffectStat(), damage);
			ui.showStatUpdate(t);
		}
	}
	
	/**
	 * Outputs the results of a battle's buff application phase to the user
	 * interface.
	 *
	 * @param battle The battle to output the state of.
	 */
	private void outputBuffApplication(Battle battle) {
		Character currentFighter = battle.getCurrentFighter();
		ArrayList<Buff> buffs = currentFighter.getBuffs();
		for (Buff b: buffs) {
			ui.showBuffApplication(b);
			ui.showStatUpdate(currentFighter);
		}
	}
	
	/**
	 * Outputs the results of a battle's death check phase to the user
	 * interface.
	 *
	 * @param battle The battle to output the state of.
	 */
	private void outputDeathCheck(Battle battle) {
		ArrayList<Character> removed = battle.getRemovedFighters();
		for (Character c: removed) {
			ui.showCharacterRemoval(c);
		}
	}
	
	/**
	 * Outputs the results of a battle's team death check phase to the user
	 * interface.
	 *
	 * @param battle The battle to output the state of.
	 */
	private void outputTeamDeathCheck(Battle battle) {}
	
	/**
	 * Outputs the results of a battle's loot calculation phase to the user
	 * interface.
	 *
	 * @param battle The battle to output the state of.
	 */
	private void outputLoot(Battle battle) {}
	
	/**
	 * Outputs the results of a battle's victory check phase to the user
	 * interface.
	 *
	 * @param battle The battle to output the state of.
	 */
	private void outputVictory(Battle battle) {}

	@Override
	public void onNewGameRequested() {
		ui.switchToCharacterCreationScreen();
	}

	@Override
	public void onLoadGameRequested() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onOptionsScreenRequested() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onQuitRequested() {
		ui.destroy();
		System.exit(0);
	}
	
	@Override
	public void onCreateCharacter(String name, int level) {
		player = entityMaker.createPlayer(name, level, ui);
		ui.switchToOverworldScreen();
	}
	
	@Override
	public void onBattleStarted() {
		NonPlayerCharacter slime = entityMaker.createNpc("slime", 2);
		Character[][] fighters = {{player}, {slime}};
		ui.switchToBattleScreen(fighters);
	}
}
