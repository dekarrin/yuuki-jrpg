/**
 * The game engine for the Yuuki JRPG project. This class may be executed
 * directly to run Yuuki.
 */

package yuuki;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import yuuki.ui.GraphicalInterface;
import yuuki.ui.Interactable;
import yuuki.ui.UiExecutor;
import yuuki.battle.Battle;
import yuuki.action.*;
import yuuki.buff.*;
import yuuki.entity.Character;
import yuuki.entity.*;

public class YuukiEngine implements Runnable, UiExecutor {

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
	private Battle mainBattle;
	
	/**
	 * Handles the execution of a battle.
	 */
	private class BattleRunner implements Runnable {
		private Battle battle;
		private boolean display;
		public BattleRunner(Battle battle, boolean display) {
			this.battle = battle;
			this.display = display;
		}
		public void run() {
			runBattle(battle, display);
		}
	};
	
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
	 * Spawns a thread that runs a battle to completion.
	 * 
	 * @param battle The battle to run through.
	 * @param display Whether the battle should be displayed on the GUI.
	 */
	private void spawnBattleThread(Battle battle, boolean display) {
		BattleRunner r = new BattleRunner(battle, display);
		(new Thread(r)).start();
	}
	
	/**
	 * Runs a battle to completion.
	 *
	 * @param battle The battle to run
	 * @param display Whether the battle should be displayed.
	 */
	private void runBattle(Battle battle, boolean display) {
		while (battle.advance()) {
			if (display) {
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
	public void requestNewGame() {
		ui.switchToCharacterCreationScreen();
	}

	@Override
	public void requestLoadGame() {
		// TODO Auto-generated method stub
	}

	@Override
	public void requestOptionsScreen() {
		// TODO Auto-generated method stub
	}

	@Override
	public void requestQuit() {
		int quit = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to quit?", "Quit Confirmation",
				JOptionPane.YES_NO_OPTION);
		if (quit == JOptionPane.YES_OPTION) {
			ui.destroy();
			System.exit(0);
		}
	}
	
	@Override
	public void requestCharacterCreation(String name, int level) {
		player = entityMaker.createPlayer(name, level, ui);
		ui.switchToOverworldScreen();
	}
	
	@Override
	public void requestBattle(boolean display) {
		NonPlayerCharacter slime = entityMaker.createNpc("slime", 2);
		Character[][] fighters = {{player}, {slime}};
		Battle battle = new Battle(fighters);
		if (display) {
			mainBattle = battle;
			ui.switchToBattleScreen(fighters);
		}
	}
	
	@Override
	public void requestBattleStart() {
		spawnBattleThread(mainBattle, true);
	}
}
