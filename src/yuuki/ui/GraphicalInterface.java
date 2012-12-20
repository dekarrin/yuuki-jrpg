package yuuki.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import yuuki.action.Action;
import yuuki.buff.Buff;
import yuuki.entity.Character;
import yuuki.entity.Stat;
import yuuki.ui.screen.BattleScreen;
import yuuki.ui.screen.CharacterCreationScreen;
import yuuki.ui.screen.CharacterCreationScreenListener;
import yuuki.ui.screen.IntroScreen;
import yuuki.ui.screen.IntroScreenListener;

/**
 * A user interface that uses the Swing framework.
 */
public class GraphicalInterface implements Interactable, IntroScreenListener,
CharacterCreationScreenListener {
	
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
	private JPanel optionsScreen;
	
	/**
	 * The battle screen.
	 */
	private BattleScreen battleScreen;
	
	/**
	 * The overworld screen.
	 */
	private JPanel overworldScreen;
	
	/**
	 * The pause screen.
	 */
	private JPanel pauseScreen;
	
	/**
	 * The ending screen.
	 */
	private JPanel endingScreen;
	
	/**
	 * The object performing the actual work.
	 */
	private UiListener mainProgram;

	/**
	 * The screen where character creation is done.
	 */
	private CharacterCreationScreen charCreationScreen;
	
	/**
	 * Allocates a new GraphicalInterface. Its components are created.
	 */
	public GraphicalInterface(UiListener mainProgram) {
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
				battleScreen.startBattle(fighters);
				switchWindow(battleScreen);
				battleScreen.showStart();
			}
		}
		Runner r = new Runner();
		r.fighters = fighters;
		SwingUtilities.invokeLater(r);
	}
	
	@Override
	public void switchToOverworldScreen() {
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
		class Runner implements Runnable {
			public String prompt;
			public String value;
			public void run() {
				value = messageBox.getString(prompt);
			}
		};
		Runner r = new Runner();
		r.prompt = prompt;
		try {
			SwingUtilities.invokeAndWait(r);
		} catch(InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
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
		class StringMessenger implements Runnable {
			public String prompt;
			public Object[] options;
			public Object value;
			public void run() {
				value = messageBox.getChoice(prompt, options);
			}
		};
		StringMessenger runner = new StringMessenger();
		runner.prompt = prompt;
		runner.options = options;
		try {
			SwingUtilities.invokeAndWait(runner);
		} catch(InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
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
	 * Switches the window to the specified JPanel.
	 * 
	 * @param panel The JPanel to switch to.
	 */
	private void switchWindow(JPanel panel) {
		clearWindow();
		mainWindow.add(panel, BorderLayout.CENTER);
		mainWindow.add(messageBox, BorderLayout.SOUTH);
		refreshWindow();
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
		createMessageBox();
		createMainWindow();
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
		mainWindow = new JFrame("Yuuki - A JRPG");
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Creates the player creation screen.
	 */
	private void createPlayerCreationScreen() {
		int height = WINDOW_HEIGHT - MESSAGE_BOX_HEIGHT;
		Dimension size = new Dimension(WINDOW_WIDTH, height);
		charCreationScreen = new CharacterCreationScreen();
		charCreationScreen.setPreferredSize(size);
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
		Dimension size = new Dimension(WINDOW_WIDTH, height);
		introScreen = new IntroScreen();
		introScreen.setPreferredSize(size);
	}
	
	/**
	 * Creates the options screen.
	 */
	private void createOptionsScreen() {
		int height = WINDOW_HEIGHT - MESSAGE_BOX_HEIGHT;
		Dimension size = new Dimension(WINDOW_WIDTH, height);
		optionsScreen = new JPanel();
		optionsScreen.setPreferredSize(size);
	}
	
	/**
	 * Creates the battle screen.
	 */
	private void createBattleScreen() {
		int height = WINDOW_HEIGHT - MESSAGE_BOX_HEIGHT;
		Dimension size = new Dimension(WINDOW_WIDTH, height);
		battleScreen = new BattleScreen();
		battleScreen.setPreferredSize(size);
		battleScreen.setVisible(true);
	}
	
	/**
	 * Creates the overworld screen.
	 */
	private void createOverworldScreen() {
		int height = WINDOW_HEIGHT - MESSAGE_BOX_HEIGHT;
		Dimension size = new Dimension(WINDOW_WIDTH, height);
		overworldScreen = new JPanel();
		overworldScreen.setPreferredSize(size);
	}
	
	/**
	 * Creates the pause screen.
	 */
	private void createPauseScreen() {
		int height = WINDOW_HEIGHT - MESSAGE_BOX_HEIGHT;
		Dimension size = new Dimension(WINDOW_WIDTH, height);
		pauseScreen = new JPanel();
		pauseScreen.setPreferredSize(size);
	}
	
	/**
	 * Creates the ending screen.
	 */
	private void createEndingScreen() {
		int height = WINDOW_HEIGHT - MESSAGE_BOX_HEIGHT;
		Dimension size = new Dimension(WINDOW_WIDTH, height);
		endingScreen = new JPanel();
		endingScreen.setPreferredSize(size);
	}

	@Override
	public void newGameClicked() {
		mainProgram.onNewGameRequested();
	}

	@Override
	public void loadGameClicked() {
		showUnimpMsg();
	}

	@Override
	public void optionsClicked() {
		showUnimpMsg();
	}

	@Override
	public void exitClicked() {
		mainProgram.onQuitRequested();
	}
	
	@Override
	public void createCharacterClicked() {
		String name = charCreationScreen.getName();
		if (!name.equals("")) {
			mainProgram.onCreateCharacter(name, charCreationScreen.getLevel());
			showUnimpMsg();
		} else {
			alert("You must enter a name!");
		}
	}
	
	private void showUnimpMsg() {
		alert("Feature not implmented!");
	}
	
	private void alert(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}
}
