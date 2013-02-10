package yuuki.battle;

import java.util.ArrayList;

import yuuki.action.Action;
import yuuki.buff.Buff;
import yuuki.entity.Character;
import yuuki.ui.Interactable;
import yuuki.ui.UiExecutor;

/**
 * Handles the execution of a battle in its own thread.
 */
public class BattleRunner implements Runnable {
	
	/**
	 * Whether the battle thread should pause.
	 */
	public volatile boolean paused;
	
	/**
	 * The battle being run.
	 */
	private Battle battle;
	
	/**
	 * The thread that this BattleRunner runs in.
	 */
	private Thread battleThread;
	
	/**
	 * The handler for executing tasks for the UI.
	 */
	private UiExecutor main;
	
	/**
	 * The interface to run the battle on.
	 */
	private Interactable ui;
	
	/**
	 * Creates the new BattleRunner.
	 * 
	 * @param battle The battle to run.
	 * @param ui The UI to display the battle on. Set to null to not display
	 * the battle.
	 * @param main The object that executes UI tasks.
	 */
	public BattleRunner(Battle battle, Interactable ui, UiExecutor main) {
		this.battle = battle;
		this.ui = ui;
		this.main = main;
		this.paused = false;
	}
	
	@Override
	public void run() {
		battleThread = Thread.currentThread();
		try {
			runBattle(battle);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		if (ui != null) {
			if (!Thread.currentThread().isInterrupted()) {
				main.requestBattleEnd();
			}
		}
	}
	
	/**
	 * Sets whether or not this battle is paused.
	 */
	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	/**
	 * Ends the battle currently running. If no battle is currently running,
	 * this method has no effect.
	 */
	public void stop() {
		if (battleThread != null && battleThread.isAlive()) {
			battleThread.interrupt();
		}
	}
	
	/**
	 * Checks whether this battle has been paused or halted.
	 * 
	 * @throws InterruptedException If the thread is interrupted while paused.
	 */
	private void checkHalted() throws InterruptedException {
		checkPause();
		checkInterrupted();
	}
	
	/**
	 * Blocks until this battle is not paused.
	 */
	private void checkPause() throws InterruptedException {
		while (paused) {
			Thread.sleep(50);
			checkInterrupted();
		}
	}
	
	/**
	 * Throws an exception if this thread has been interrupted.
	 * 
	 * @throws InterruptedException 
	 */
	private void checkInterrupted() throws InterruptedException {
		if (Thread.currentThread().isInterrupted()) {
			throw new InterruptedException();
		}
	}
	
	/**
	 * Outputs the results of a battle's action application phase to the user
	 * interface.
	 *
	 * @param battle The battle to output the state of.
	 */
	private void outputActionApplication(Battle battle) throws
	InterruptedException {
		Action a = battle.getLastAction();
		if (a.wasSuccessful()) {
			if (a.getCostStat() != null) {
				outputActionCost(a);
			}
			ui.showActionUse(a);
			checkHalted();
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
		checkHalted();
	}
	
	/**
	 * Outputs the results of an action cost to the user interface.
	 *
	 * @param action The Action to output.
	 */
	private void outputActionCost(Action a) throws InterruptedException {
		ui.showDamage(a.getOrigin(), a.getCostStat(), (int)a.getCost());
		checkHalted();
		ui.showStatUpdate(a.getOrigin());
		checkHalted();
	}
	
	/**
	 * Outputs the effects of an action to the user interface.
	 *
	 * @param a The Action to output.
	 */
	private void outputActionEffects(Action a) throws InterruptedException {
		int[] effects = a.getActualEffects();
		ArrayList<Character> targets = a.getTargets();
		for (int i = 0; i < effects.length; i++) {
			Character t = targets.get(i);
			int damage = effects[i];
			ui.showDamage(t, a.getEffectStat(), damage);
			checkHalted();
			ui.showStatUpdate(t);
			checkHalted();
		}
	}
	
	/**
	 * Outputs the results of a battle's action get phase to the user
	 * interface.
	 *
	 * @param battle The battle to output the state of.
	 */
	private void outputActionGet(Battle battle) throws InterruptedException {
		Action a = battle.getLastAction();
		ui.showActionPreperation(a);
		checkHalted();
	}
	
	/**
	 * Outputs the results of a battle's buff application phase to the user
	 * interface.
	 *
	 * @param battle The battle to output the state of.
	 */
	private void outputBuffApplication(Battle battle) throws
	InterruptedException {
		Character currentFighter = battle.getCurrentFighter();
		ArrayList<Buff> buffs = currentFighter.getBuffs();
		for (Buff b : buffs) {
			ui.showBuffApplication(b);
			checkHalted();
			ui.showStatUpdate(currentFighter);
			checkHalted();
		}
	}
	
	/**
	 * Outputs the results of a battle's death check phase to the user
	 * interface.
	 *
	 * @param battle The battle to output the state of.
	 */
	private void outputDeathCheck(Battle battle) throws InterruptedException {
		ArrayList<Character> removed = battle.getRemovedFighters();
		for (Character c : removed) {
			ui.showCharacterRemoval(c);
			checkHalted();
		}
	}
	
	/**
	 * Outputs the results of a battle's loot calculation phase to the user
	 * interface.
	 *
	 * @param battle The battle to output the state of.
	 */
	private void outputLoot(Battle battle) {}
	
	/**
	 * Outputs the results of a battle's team death check phase to the user
	 * interface.
	 *
	 * @param battle The battle to output the state of.
	 */
	private void outputTeamDeathCheck(Battle battle) {}
	
	/**
	 * Outputs the results of a battle's turn start phase to the user
	 * interface.
	 *
	 * @param battle The battle to output the state of.
	 */
	private void outputTurnStart(Battle battle) throws InterruptedException {
		Character c = battle.getCurrentFighter();
		int recoveredMana = battle.getRegeneratedMana();
		ui.display(c, "It looks like I'm up next.", true);
		ArrayList<Buff> expiredBuffs = c.getExpiredBuffs();
		for (Buff expired : expiredBuffs) {
			ui.showBuffDeactivation(expired);
			checkHalted();
		}
		if (recoveredMana != 0) {
			ui.showRecovery(c, c.getMPStat(), recoveredMana);
			checkHalted();
		}
		ui.showStatUpdate(c);
		checkHalted();
		ui.waitForDisplay();
		checkHalted();
	}
	
	/**
	 * Outputs the results of a battle's victory check phase to the user
	 * interface.
	 *
	 * @param battle The battle to output the state of.
	 */
	private void outputVictory(Battle battle) {}
	
	/**
	 * Runs a battle to completion.
	 *
	 * @param battle The battle to run.
	 */
	private void runBattle(Battle battle) throws InterruptedException {
		while (battle.advance()) {
			if (Thread.currentThread().isInterrupted()) {
				break;
			}
			if (ui != null) {
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
	
}
