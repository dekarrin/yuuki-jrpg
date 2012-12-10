package yuuki.ui;

import java.awt.BorderLayout;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import yuuki.action.Action;
import yuuki.buff.Buff;
import yuuki.entity.Character;
import yuuki.entity.Stat;

/**
 * A user interface that uses the Swing framework.
 */
public class GraphicalInterface implements Interactable, IntroScreenListener {
	
	/**
	 * The message box for the game.
	 */
	private MessageBox messageBox;
	/**
	 * The main window of the program.
	 */
	private JFrame mainWindow;
	
	/**
	 * The main content of the window.
	 */
	private JPanel mainContent;
	
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
	 * Creates the components of the main JFrame and draws the main window on
	 * screen.
	 */
	@Override
	public void initialize() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createComponents();
				refreshWindow();
			}
		});
	}
	
	/**
	 * Empty implementation.
	 */
	@Override
	public void destroy() {
		
	}
	
	@Override
	public String switchToIntroScreen() {
		introScreen.addListener(this);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				switchWindow(introScreen);
			}
		});
                return "Override Corrections";
	}
	
	@Override
	public String switchToOptionsScreen() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				switchWindow(optionsScreen);
			}
		});
                return "Override Corrections";
	}
	
	@Override
	public void switchToBattleScreen(Character[][] fighters, boolean soundMusic, boolean soundEffects) {
		class Runner implements Runnable {
			public Character[][] fighters;
			public void run() {
				battleScreen.startBattle(fighters);
				switchWindow(battleScreen);
				battleScreen.showStart();
			}
		}
		Runner r = new Runner();
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
//				battleScreen.showStatUpdate(fighter);
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
//				battleScreen.showDamage(fighter, stat, damage);
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
//				battleScreen.showDamage(fighter, stat, damage);
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
//				battleScreen.showRecovery(fighter, stat, amount);
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
//				battleScreen.showRecovery(fighter, stat, amount);
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
//				battleScreen.showActionPreparation(action);
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
//				battleScreen.showActionFailure(action);
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
//				battleScreen.showActionUse(action);
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
//				battleScreen.showBuffActivation(buff);
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
//				battleScreen.showBuffApplication(buff);
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
//				battleScreen.showBuffDeactivation(buff);
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
//				battleScreen.showCharacterRemoval(c);
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
//				battleScreen.showCharacterVictory(cs);
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
	public int getChoice(String prompt, String[] options) {
		class StringMessenger implements Runnable {
			public String prompt;
			public String[] options;
			public int value;
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
	public int getChoice(String[] options) {
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
	public int selectAction(Action[] actions) {
		String[] options = new String[actions.length];
		for (int i = 0; i < actions.length; i++) {
			options[i] = actions[i].getName();
		}
		return getChoice("Select an action", options);
	}
	
	@Override
	public Character selectTarget(ArrayList<ArrayList<Character>> fighters) {
		ArrayList<String> charNames = new ArrayList<String>();
		ArrayList<Character> chars = new ArrayList<Character>();
		for (int i = 0; i < fighters.size(); i++) {
			ArrayList<Character> team = fighters.get(i);
			for (int j = 0; j < team.size(); j++) {
				Character c = team.get(j);
				chars.add(c);
				charNames.add(c.getName() + " on team " + i);
			}
		}
		String[] charNamesArr = charNames.toArray(new String[0]);
		int index = getChoice("Select a target", charNamesArr);
		return chars.get(index);
	}
	
	@Override
	public boolean confirm(String prompt, String yes, String no) {
		String[] ops = new String[2];
		ops[0] = yes;
		ops[1] = no;
		int option = getChoice(prompt, ops);
		return (option == 0);
	}
	
	/**
	 * Switches the window to the specified JPanel.
	 * 
	 * @param panel The JPanel to switch to.
	 */
	private void switchWindow(JPanel panel) {
		clearWindow();
		mainContent.add(panel);
		refreshWindow();
	}
	
	/**
	 * Shows the main window with the default screen.
	 */
	private void refreshWindow() {
		mainWindow.pack();
		mainWindow.setVisible(true);
	}
	
	/**
	 * Clears the main window of all components.
	 */
	private void clearWindow() {
		mainWindow.setVisible(false);
		mainContent.removeAll();
	}
	
	/**
	 * Creates the screens used in this GUI.
	 */
	private void createComponents() {
		messageBox = new MessageBox();
		createMainWindow();
		createIntroScreen();
		createOptionsScreen();
		createBattleScreen();
		createOverworldScreen();
		createPauseScreen();
		createEndingScreen();
	}
	
	/**
	 * Creates the primary window.
	 */
	private void createMainWindow() {
		mainContent = new JPanel();
		mainWindow = new JFrame("Yuuki - A JRPG");
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.add(mainContent);
		mainWindow.add(messageBox);
	}
	
	/**
	 * Creates the intro screen.
	 */
	private void createIntroScreen() {
		introScreen = new IntroScreen();
	}
	
	/**
	 * Creates the options screen.
	 */
	private void createOptionsScreen() {
		optionsScreen = new JPanel();
	}
	
	/**
	 * Creates the battle screen.
	 */
	private void createBattleScreen() {
		battleScreen = new BattleScreen();
	}
	
	/**
	 * Creates the overworld screen.
	 */
	private void createOverworldScreen() {
		overworldScreen = new JPanel();
	}
	
	/**
	 * Creates the pause screen.
	 */
	private void createPauseScreen() {
		pauseScreen = new JPanel();
	}
	
	/**
	 * Creates the ending screen.
	 */
	private void createEndingScreen() {
		endingScreen = new JPanel();
	}

	@Override
	public void newGameClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadGameClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void optionsClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String switchToPlayerNameScreen() {
		// TODO Auto-generated method stub
		return "Override Corrections";
	}

	@Override
	public String switchToPlayerNameScreen(boolean soundMusic,
			boolean soundEffects) {
		// TODO Auto-generated method stub
            return "Override Corrections";
		
	}

	@Override
	public String switchToIntroScreen(boolean soundMusic, boolean soundEffects) {
		// TODO Auto-generated method stub
            return "Override Corrections";
		
	}

	@Override
	public String switchToOptionsScreen(boolean soundMusic, boolean soundEffects) {
		// TODO Auto-generated method stub
		return "Override Corrections";
	}
	
}
