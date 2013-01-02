package yuuki.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import yuuki.action.Action;
import yuuki.buff.Buff;
import yuuki.entity.Character;
import yuuki.entity.Stat;
import yuuki.ui.screen.*;

/**
 * A user interface that uses the Swing framework.
 */
public class GraphicalInterface implements Interactable, IntroScreenListener,
CharacterCreationScreenListener, OverworldScreenListener {
	
	/**
	 * The width of the game window.
	 */
	public static final int WINDOW_WIDTH = 800;
	
	/**
	 * The height of the game window.
	 */
	public static final int WINDOW_HEIGHT = 600;
	
	/**
	 * The height of the message box within the window.
	 */
	public static final int MESSAGE_BOX_HEIGHT = 100;
	
	/**
	 * The message box for the game.
	 */
	private MessageBox messageBox;
	/**
	 * The main window of the program.
	 */
	private JFrame mainWindow;
	
	/**
	 * The intro screen.
	 */
	private IntroScreen introScreen;
	
	/**
	 * The options screen.
	 */
	private Screen optionsScreen;
	
	/**
	 * The battle screen.
	 */
	private BattleScreen battleScreen;
	
	/**
	 * The overworld screen.
	 */
	private OverworldScreen overworldScreen;
	
	/**
	 * The pause screen.
	 */
	private Screen pauseScreen;
	
	/**
	 * The ending screen.
	 */
	private Screen endingScreen;
	
	/**
	 * The object performing the actual work.
	 */
	private UiExecutor mainProgram;

	/**
	 * The screen where character creation is done.
	 */
	private CharacterCreationScreen charCreationScreen;
	
	/**
	 * Allocates a new GraphicalInterface. Its components are created.
	 */
	public GraphicalInterface(UiExecutor mainProgram) {
		this.mainProgram = mainProgram;
		createComponents();
	}
	
	/**
	 * Creates the components of the main JFrame and draws the main window on
	 * screen.
	 */
	@Override
	public void initialize() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				refreshWindow();
				mainWindow.setVisible(true);
			}
		});
	}
	
	/**
	 * Empty implementation.
	 */
	@Override
	public void destroy() {
		mainWindow.dispose();
	}
	
	@Override
	public void switchToIntroScreen() {
		introScreen.addListener(this);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				switchWindow(introScreen);
			}
		});
	}
	
	@Override
	public void switchToOptionsScreen() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				switchWindow(optionsScreen);
			}
		});
	}
	
	@Override
	public void switchToCharacterCreationScreen() {
		charCreationScreen.addListener(this);
		class Runner implements Runnable {
			public void run() {
				switchWindow(charCreationScreen);
			}
		}
		Runner r = new Runner();
		SwingUtilities.invokeLater(r);
	}
	
	@Override
	public void switchToBattleScreen(Character[][] fighters) {
		class Runner implements Runnable {
			public Character[][] fighters;
			public void run() {
				battleScreen.initBattle(fighters);
				switchWindow(battleScreen);
				battleScreen.showStart();
				mainProgram.requestBattleStart();
			}
		}
		Runner r = new Runner();
		r.fighters = fighters;
		SwingUtilities.invokeLater(r);
	}
	
	@Override
	public void switchToOverworldScreen() {
		overworldScreen.addListener(this);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				switchWindow(overworldScreen);
			}
		});
	}
	
	@Override
	public void switchToPauseScreen() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				switchWindow(pauseScreen);
			}
		});
	}
	
	@Override
	public void switchToEndingScreen() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				switchWindow(endingScreen);
			}
		});
	}
	
	@Override
	public void showStatUpdate(Character fighter) {
		class Runner implements Runnable {
			public Character fighter;
			public void run() {
				battleScreen.showStatUpdate(fighter);
			}
		}
		Runner r = new Runner();
		r.fighter = fighter;
		SwingUtilities.invokeLater(r);
	}
	
	@Override
	public void showDamage(Character fighter, Stat stat, int damage) {
		class Runner implements Runnable {
			public Character fighter;
			public Stat stat;
			public int damage;
			public void run() {
				battleScreen.showDamage(fighter, stat, damage);
			}
		}
		Runner r = new Runner();
		r.fighter = fighter;
		r.stat = stat;
		r.damage = damage;
		SwingUtilities.invokeLater(r);
	}
	
	@Override
	public void showDamage(Character fighter, Stat stat, double damage) {
		class Runner implements Runnable {
			public Character fighter;
			public Stat stat;
			public double damage;
			public void run() {
				battleScreen.showDamage(fighter, stat, damage);
			}
		}
		Runner r = new Runner();
		r.fighter = fighter;
		r.stat = stat;
		r.damage = damage;
		SwingUtilities.invokeLater(r);
	}
	
	@Override
	public void showRecovery(Character fighter, Stat stat, int amount) {
		class Runner implements Runnable {
			public Character fighter;
			public Stat stat;
			public int amount;
			public void run() {
				battleScreen.showRecovery(fighter, stat, amount);
			}
		}
		Runner r = new Runner();
		r.fighter = fighter;
		r.stat = stat;
		r.amount = amount;
		SwingUtilities.invokeLater(r);
	}
	
	@Override
	public void showRecovery(Character fighter, Stat stat, double amount) {
		class Runner implements Runnable {
			public Character fighter;
			public Stat stat;
			public double amount;
			public void run() {
				battleScreen.showRecovery(fighter, stat, amount);
			}
		}
		Runner r = new Runner();
		r.fighter = fighter;
		r.stat = stat;
		r.amount = amount;
		SwingUtilities.invokeLater(r);
	}
	
	@Override
	public void showActionPreperation(Action action) {
		class Runner implements Runnable {
			public Action action;
			public void run() {
				battleScreen.showActionPreparation(action);
			}
		}
		Runner r = new Runner();
		r.action = action;
		SwingUtilities.invokeLater(r);
	}
	
	@Override
	public void showActionFailure(Action action) {
		class Runner implements Runnable {
			public Action action;
			public void run() {
				battleScreen.showActionFailure(action);
			}
		}
		Runner r = new Runner();
		r.action = action;
		SwingUtilities.invokeLater(r);
	}
	
	@Override
	public void showActionUse(Action action) {
		class Runner implements Runnable {
			public Action action;
			public void run() {
				battleScreen.showActionUse(action);
			}
		}
		Runner r = new Runner();
		r.action = action;
		SwingUtilities.invokeLater(r);
	}
	
	@Override
	public void showBuffActivation(Buff buff) {
		class Runner implements Runnable {
			public Buff buff;
			public void run() {
				battleScreen.showBuffActivation(buff);
			}
		}
		Runner r = new Runner();
		r.buff = buff;
		SwingUtilities.invokeLater(r);
	}
	
	@Override
	public void showBuffApplication(Buff buff) {
		class Runner implements Runnable {
			public Buff buff;
			public void run() {
				battleScreen.showBuffApplication(buff);
			}
		}
		Runner r = new Runner();
		r.buff = buff;
		SwingUtilities.invokeLater(r);
	}
	
	@Override
	public void showBuffDeactivation(Buff buff) {
		class Runner implements Runnable {
			public Buff buff;
			public void run() {
				battleScreen.showBuffDeactivation(buff);
			}
		}
		Runner r = new Runner();
		r.buff = buff;
		SwingUtilities.invokeLater(r);
	}
	
	@Override
	public void showCharacterRemoval(Character c) {
		class Runner implements Runnable {
			public Character c;
			public void run() {
				battleScreen.showCharacterRemoval(c);
			}
		}
		Runner r = new Runner();
		r.c = c;
		SwingUtilities.invokeLater(r);
	}
	
	@Override
	public void showCharacterVictory(Character[] cs) {
		class Runner implements Runnable {
			public Character[] cs;
			public void run() {
				battleScreen.showCharacterVictory(cs);
			}
		}
		Runner r = new Runner();
		r.cs = cs;
		SwingUtilities.invokeLater(r);
	}
	
	@Override
	public String getString(String prompt) {
		class Runner implements Runnable, MessageBoxInputListener {
			public String prompt;
			public String value = null;
			public void run() {
				messageBox.addListener(this);
				messageBox.getString(prompt);
			}
			public void enterClicked(String input) {
				value = input;
				messageBox.removeListener(this);
			}
			public void optionClicked(Object option) {}
		};
		Runner r = new Runner();
		r.prompt = prompt;
		SwingUtilities.invokeLater(r);
		while (r.value == null) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return r.value;
	}
	
	@Override
	public String getString() {
		return getString("Enter a value");
	}
	
	@Override
	public int getInt(String prompt) {
		Integer answer = null;
		while (answer == null) {
			answer = Integer.parseInt(getString(prompt));
		}
		return answer.intValue();
	}
	
	@Override
	public int getInt() {
		return getInt("Enter an integer");
	}
	
	@Override
	public int getInt(String prompt, int min, int max) {
		int answer = 0;
		boolean answerIsGood = false;
		while (!answerIsGood) {
			answer = getInt(prompt);
			if (answer >= min && answer <= max) {
				answerIsGood = true;
			}
		}
		return answer;
	}
	
	@Override
	public int getInt(int min, int max) {
		return getInt("Enter an integer", min, max);
	}
	
	@Override
	public double getDouble(String prompt) {
		Double answer = null;
		while (answer == null) {
			answer = Double.parseDouble(getString(prompt));
		}
		return answer.doubleValue();
	}
	
	@Override
	public double getDouble() {
		return getDouble("Enter an integer");
	}
	
	@Override
	public double getDouble(String prompt, double min, double max) {
		double answer = 0;
		boolean answerIsGood = false;
		while (!answerIsGood) {
			answer = getDouble(prompt);
			if (answer >= min && answer <= max) {
				answerIsGood = true;
			}
		}
		return answer;
	}
	
	@Override
	public double getDouble(double min, double max) {
		return getDouble("Enter an integer", min, max);
	}
	
	@Override
	public Object getChoice(String prompt, Object[] options) {
		class StringMessenger implements Runnable, MessageBoxInputListener {
			public String prompt;
			public Object[] options;
			public Object value = null;
			public void run() {
				messageBox.addListener(this);
				messageBox.getChoice(prompt, options);
			}
			public void enterClicked(String s) {}
			public void optionClicked(Object option) {
				value = option;
				messageBox.removeListener(this);
			}
		};
		StringMessenger runner = new StringMessenger();
		runner.prompt = prompt;
		runner.options = options;
		SwingUtilities.invokeLater(runner);
		while (runner.value == null) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return runner.value;
	}
	
	@Override
	public Object getChoice(Object[] options) {
		return getChoice("Select an option", options);
	}
	
	@Override
	public void display(Character speaker, String message) {
		class StringMessenger implements Runnable {
			public Character speaker;
			public String message;
			public void run() {
				messageBox.display(speaker, message);
			}
		};
		StringMessenger runner = new StringMessenger();
		runner.speaker = speaker;
		runner.message = message;
		SwingUtilities.invokeLater(runner);
	}
	
	@Override
	public void playSound(String path) {
	
	}
	
	@Override
	public Action selectAction(Action[] actions) {
		return (Action) getChoice("Select an action", actions);
	}
	
	@Override
	public Character selectTarget(ArrayList<ArrayList<Character>> fighters) {
		ArrayList<Character> chars = new ArrayList<Character>();
		for (int i = 0; i < fighters.size(); i++) {
			ArrayList<Character> team = fighters.get(i);
			for (int j = 0; j < team.size(); j++) {
				chars.add(team.get(j));
			}
		}
		Character[] charsArr = chars.toArray(new Character[0]);
		return (Character) getChoice("Select a target", charsArr);
	}
	
	@Override
	public boolean confirm(String prompt, String yes, String no) {
		String[] ops = {yes, no};
		String choice = (String) getChoice(prompt, ops);
		return (choice.equals(yes));
	}
	
	/**
	 * Switches the window to display the specified screen.
	 * 
	 * @param screen The screen to switch to.
	 */
	private void switchWindow(Screen screen) {
		clearWindow();
		mainWindow.add(screen, BorderLayout.CENTER);
		mainWindow.add(messageBox, BorderLayout.SOUTH);
		refreshWindow();
		screen.setInitialFocus();
	}
	
	/**
	 * Shows the main window with the default screen.
	 */
	private void refreshWindow() {
		mainWindow.pack();
	}
	
	/**
	 * Clears the main window of all components.
	 */
	private void clearWindow() {
		mainWindow.getContentPane().removeAll();
	}
	
	/**
	 * Creates the screens used in this GUI.
	 */
	private void createComponents() {
		createMainWindow();
		createMessageBox();
		createIntroScreen();
		createOptionsScreen();
		createBattleScreen();
		createOverworldScreen();
		createPauseScreen();
		createEndingScreen();
		createPlayerCreationScreen();
	}
	
	/**
	 * Creates the primary window.
	 */
	private void createMainWindow() {
		WindowListener l = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				mainProgram.requestQuit();
			}
		};
		mainWindow = new JFrame("Yuuki - A JRPG");
		mainWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainWindow.setResizable(false);
		mainWindow.addWindowListener(l);
	}
	
	/**
	 * Creates the player creation screen.
	 */
	private void createPlayerCreationScreen() {
		int height = WINDOW_HEIGHT - MESSAGE_BOX_HEIGHT;
		charCreationScreen = new CharacterCreationScreen(WINDOW_WIDTH, height);
	}
	
	/**
	 * Creates the message box.
	 */
	private void createMessageBox() {
		Dimension size = new Dimension(WINDOW_WIDTH, MESSAGE_BOX_HEIGHT);
		messageBox = new MessageBox();
		messageBox.setPreferredSize(size);
	}
	
	/**
	 * Creates the intro screen.
	 */
	private void createIntroScreen() {
		int height = WINDOW_HEIGHT - MESSAGE_BOX_HEIGHT;
		introScreen = new IntroScreen(WINDOW_WIDTH, height);
	}
	
	/**
	 * Creates the options screen.
	 */
	private void createOptionsScreen() {
		int height = WINDOW_HEIGHT - MESSAGE_BOX_HEIGHT;
		optionsScreen = Screen.getInstance(WINDOW_WIDTH, height);
	}
	
	/**
	 * Creates the battle screen.
	 */
	private void createBattleScreen() {
		int height = WINDOW_HEIGHT - MESSAGE_BOX_HEIGHT;
		battleScreen = new BattleScreen(WINDOW_WIDTH, height);
		battleScreen.setVisible(true);
	}
	
	/**
	 * Creates the overworld screen.
	 */
	private void createOverworldScreen() {
		int height = WINDOW_HEIGHT - MESSAGE_BOX_HEIGHT;
		overworldScreen = new OverworldScreen(WINDOW_WIDTH, height);
	}
	
	/**
	 * Creates the pause screen.
	 */
	private void createPauseScreen() {
		int height = WINDOW_HEIGHT - MESSAGE_BOX_HEIGHT;
		pauseScreen = Screen.getInstance(WINDOW_WIDTH, height);
	}
	
	/**
	 * Creates the ending screen.
	 */
	private void createEndingScreen() {
		int height = WINDOW_HEIGHT - MESSAGE_BOX_HEIGHT;
		endingScreen = Screen.getInstance(WINDOW_WIDTH, height);
	}

	@Override
	public void newGameClicked() {
		mainProgram.requestNewGame();
	}

	@Override
	public void loadGameClicked() {
		mainProgram.requestLoadGame();
	}

	@Override
	public void optionsClicked() {
		mainProgram.requestOptionsScreen();
	}

	@Override
	public void exitClicked() {
		mainProgram.requestQuit();
	}
	
	@Override
	public void createCharacterClicked() {
		String name = charCreationScreen.getEnteredName();
		int level = charCreationScreen.getEnteredLevel();
		if (!name.equals("")) {
			mainProgram.requestCharacterCreation(name, level);
		} else {
			alert("You must enter a name!");
		}
	}
	
	@Override
	public void startBattleClicked() {
		mainProgram.requestBattle(true);
	}
	
	public void showUnimpMsg() {
		display(null, "That feature has not yet been implemented.");
	}
	
	private void alert(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}
}
