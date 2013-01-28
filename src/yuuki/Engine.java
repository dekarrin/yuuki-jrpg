package yuuki;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import yuuki.action.Action;
import yuuki.battle.Battle;
import yuuki.buff.Buff;
import yuuki.entity.Character;
import yuuki.entity.EntityFactory;
import yuuki.entity.NonPlayerCharacter;
import yuuki.entity.PlayerCharacter;
import yuuki.file.TileLoader;
import yuuki.file.WorldLoader;
import yuuki.ui.GraphicalInterface;
import yuuki.ui.Interactable;
import yuuki.ui.UiExecutor;
import yuuki.world.TileFactory;
import yuuki.world.World;

/**
 * The game engine for the Yuuki JRPG project. This class may be executed
 * directly to run Yuuki.
 */
public class Engine implements Runnable, UiExecutor {
	
	/**
	 * Handles the execution of a battle in its own thread.
	 */
	private class BattleRunner implements Runnable {
		private Battle battle;
		private boolean display;
		public BattleRunner(Battle battle, boolean display) {
			this.battle = battle;
			this.display = display;
		}
		@Override
		public void run() {
			runBattle(battle, display);
			if (display) {
				if (!Thread.currentThread().isInterrupted()) {
					requestBattleEnd();
				}
			}
		}
	}
	
	/**
	 * Handles the execution of a world in its own thread.
	 */
	private class WorldRunner implements Runnable {
		private volatile boolean paused = false;
		@Override
		public void run() {
			while (true) {
				try {
					while (paused) {
						Thread.sleep(10);
					}
					advanceWorld();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
		public void setPaused(boolean paused) {
			this.paused = paused;
		}
	}
	
	/**
	 * The path to definitions files.
	 */
	public static final String DEFINITIONS_PATH = "/yuuki/resource/data/";
	
	/**
	 * The path to land files.
	 */
	public static final String LAND_PATH = "/yuuki/resource/land/";
	
	/**
	 * The name of the tile definitions file.
	 */
	public static final String TILE_FILE = "tiles.csv";
	
	/**
	 * The name of the world definitions file.
	 */
	public static final String WORLD_FILE = "world.csv";
	
	/**
	 * Program execution hook. Creates a new instance of Engine and then runs
	 * it.
	 *
	 * @param args Command line arguments. Not used.
	 */
	public static void main(String[] args) {
		Engine game = new Engine();
		game.run();
	}
	
	/**
	 * Creates all entities.
	 */
	private EntityFactory entityMaker;
	
	/**
	 * The current battle.
	 */
	private Battle mainBattle;
	
	/**
	 * The thread running the currently displayed battle.
	 */
	private Thread mainBattleThread;
	
	/**
	 * The options for the game.
	 */
	private Options options;
	
	/**
	 * The player character.
	 */
	private PlayerCharacter player;
	
	/**
	 * The user interface.
	 */
	private Interactable ui;
	
	/**
	 * The world controller.
	 */
	private World world;
	
	/**
	 * The thread running the world advancement.
	 */
	private WorldRunner worldRunner;
	
	/**
	 * Creates a new Engine with a Swing-based GUI.
	 */
	public Engine() {
		options = new Options();
		ui = new GraphicalInterface(this, options);
		entityMaker = new EntityFactory();
		world = loadWorld();
		String[] lands = world.getAllLandNames();
		world.changeLand(lands[0]); // always load the first land in world
		applyOptions();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestBattle(boolean display) {
		NonPlayerCharacter slime = entityMaker.createNpc("slime", 2);
		Character[][] fighters = {{player}, {slime}};
		Battle battle = new Battle(fighters);
		if (display) {
			mainBattle = battle;
			exitOverworldMode();
			ui.switchToBattleScreen(fighters);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestBattleEnd() {
		Character winner = mainBattle.getFighters(0).get(0);
		ui.getChoice(winner.getName() + " won", new String[]{"Continue"});
		enterOverworldMode();
		ui.display(null, "Your health has been restored.", false);
		player.restoreHP();
		player.restoreMP();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestBattleStart() {
		spawnBattleThread(mainBattle, true);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestCharacterCreation(String name, int level) {
		player = entityMaker.createPlayer(name, level, ui);
		enterOverworldMode();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestCloseGame() {
		exitOverworldMode();
		ui.switchToIntroScreen();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestLoadGame() {
		ui.display(null, "Loading hasn't yet been implemented", false);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestNewGame() {
		exitOverworldMode();
		ui.switchToCharacterCreationScreen();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestOptionApplication() {
		applyOptions();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestOptionsScreen() {
		ui.switchToOptionsScreen();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestOptionsSubmission() {
		// TODO: do not depend on Interactable to set options
		ui.switchToLastScreen();
	}
	
	/**
	 * {@inheritDoc}
	 */
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestSaveGame() {
		ui.display(null, "Saving hasn't yet been implemented", false);
	}
	
	/**
	 * Initializes the engine. The UI is started and the intro screen is shown.
	 */
	@Override
	public void run() {
		ui.initialize();
		ui.switchToIntroScreen();
	}
	
	/**
	 * Advances the world by one tick and updates the GUI with the new world
	 * data.
	 */
	private void advanceWorld() {
		world.advance();
		//		ui.updateWorldView(player.getLocation());
	}
	
	/**
	 * Applies each of the options in the game options object to obtain their
	 * respective effects.
	 */
	private void applyOptions() {
		ui.applyOptions(options);
	}
	
	/**
	 * Switches to the overworld screen and begins overworld advancement.
	 */
	private void enterOverworldMode() {
		ui.switchToOverworldScreen();
		startWorldThread();
	}
	
	/**
	 * Sets the world advancer to stop clicking through the world.
	 */
	private void exitOverworldMode() {
		if (worldRunner != null) {
			pauseWorldThread();
		}
	}
	
	/**
	 * Loads the world from disk.
	 * 
	 * @return The loaded World.
	 */
	private World loadWorld() {
		TileLoader tileLoader = null;
		TileFactory tf = null;
		WorldLoader worldLoader = null;
		World world = null;
		try {
			tileLoader = new TileLoader(DEFINITIONS_PATH);
			tf = tileLoader.loadTiles(TILE_FILE);
			worldLoader = new WorldLoader(DEFINITIONS_PATH, LAND_PATH, tf);
			world = worldLoader.load(WORLD_FILE);
		} catch (IOException e) {
			System.err.println("Could not load world file!");
		}
		return world;
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
	private void outputTurnStart(Battle battle) {
		Character c = battle.getCurrentFighter();
		int recoveredMana = battle.getRegeneratedMana();
		ui.display(c, "It looks like I'm up next.", true);
		ArrayList<Buff> expiredBuffs = c.getExpiredBuffs();
		for (Buff expired: expiredBuffs) {
			ui.showBuffDeactivation(expired);
		}
		if (recoveredMana != 0) {
			ui.showRecovery(c, c.getMPStat(), recoveredMana);
		}
		ui.showStatUpdate(c);
		ui.waitForDisplay();
	}
	
	/**
	 * Outputs the results of a battle's victory check phase to the user
	 * interface.
	 *
	 * @param battle The battle to output the state of.
	 */
	private void outputVictory(Battle battle) {}
	
	/**
	 * Pauses the thread running the world.
	 */
	private void pauseWorldThread() {
		worldRunner.setPaused(true);
	}
	
	/**
	 * Runs a battle to completion.
	 *
	 * @param battle The battle to run
	 * @param display Whether the battle should be displayed.
	 */
	private void runBattle(Battle battle, boolean display) {
		while (battle.advance()) {
			if (Thread.currentThread().isInterrupted()) {
				break;
			}
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
	 * Spawns a thread that runs a battle to completion.
	 * 
	 * @param battle The battle to run through.
	 * @param display Whether the battle should be displayed on the GUI.
	 */
	private void spawnBattleThread(Battle battle, boolean display) {
		BattleRunner r = new BattleRunner(battle, display);
		Thread t = new Thread(r);
		if (display) {
			if (mainBattleThread != null && mainBattleThread.isAlive()) {
				mainBattleThread.interrupt();
			}
			mainBattleThread = t;
			t.setName("MainBattle");
		} else {
			t.setName("Battle");
		}
		t.start();
	}
	
	/**
	 * Starts the thread running the world. If the thread has not yet been
	 * created, it is created.
	 */
	private void startWorldThread() {
		if (worldRunner == null) {
			worldRunner = new WorldRunner();
			(new Thread(worldRunner, "World")).start();
		}
		worldRunner.setPaused(false);
		ui.setWorldView(world.getTiles());
	}
	
}