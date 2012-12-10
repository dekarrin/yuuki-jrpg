/**
 * A very simple interface that only uses simple streams for input and output.
 * Note that this class will not throw exceptions; if a stream operation
 * throws an exception, it is simply caught and the stack trace is printed.
 */

package yuuki.ui;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import yuuki.entity.Character;
import yuuki.entity.Stat;
import yuuki.entity.VariableStat;
import yuuki.buff.Buff;
import yuuki.action.Action;

public class StreamInterface implements Interactable {

	/**
	 * The stream that is the source of input.
	 */
	private InputStream input;
	
	/**
	 * The stream that is the destination of normal output.
	 */
	private OutputStream output;
	
	/**
	 * The stream that is the destination of error output.
	 */
	private OutputStream error;

	/**
	 * Reads from stdin.
	 */
	private BufferedReader stdin;
	
	/**
	 * Writes to stdout.
	 */
	private BufferedWriter stdout;
	 
	/**
	 * Writes to stderr.
	 */
	private BufferedWriter stderr;
	
	/**
	 * A screen this interface could be on.
	 */
	private enum Screen {
		INTRO,
		OPTIONS,
		BATTLE,
		OVERWORLD,
		PAUSE,
		ENDING,
                PLAYERNAME;
	}
	
	/**
	 * The screen that this interface is on.
	 */
	private Screen screen;
	
	/**
	 * Creates a new StreamInterface.
	 *
	 * @param input The stream to use as input.
	 * @param output The stream to use for normal output.
	 * @param error The stream to use for error output.
	 */
	public StreamInterface(InputStream input, OutputStream output,
							OutputStream error) {
		this.input = input;
		this.output = output;
		this.error = error;
		stdin = null;
		stdout = null;
		stderr = null;
	}
	
	/**
	 * Creates the Reader on stdin.
	 */
	@Override
	public void initialize() {
		stdin = new BufferedReader(new InputStreamReader(input));
		stdout = new BufferedWriter(new OutputStreamWriter(output));
		stderr = new BufferedWriter(new OutputStreamWriter(error));
	}
	
	/**
	 * Closes the stdin Reader.
	 */
	@Override
	public void destroy() {
		try {
			stdin.close();
			stdout.close();
			stderr.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
        
        /**
         * Shows the PlayerName Screen.
         */
        @Override
        public String switchToPlayerNameScreen() {
            screen = Screen.PLAYERNAME;
            println("Please Enter Your Name:");
            return "Override Corrections";
        }
	/**
         * Shows the PlayerName Screen.
         * 
         * @param soundMusic Whether music plays or not.
         * @param soundEffects Whether effects play or not.
         */
        @Override
        public String switchToPlayerNameScreen(boolean soundMusic, boolean soundEffects) {
            screen = Screen.PLAYERNAME;
            println("Please Enter Your Name:");
            return "Override Corrections";
        }
	/**
	 * Shows the intro message and displays the main menu.
	 */
	@Override
	public String switchToIntroScreen() {
		screen = Screen.INTRO;
		println("+-------------------------------------+");
		println("|                Yuuki                |");
		println("|                                     |");
		println("|  By Python'); DROP TABLE Teams;--   |");
		println("+-------------------------------------+");
		println();
		pause();
                return "Override corrections";
	}
	/**
	 * Shows the intro message and displays the main menu.
         * 
         * @param soundMusic Whether music plays or not.
         * @param soundEffects Whether effects play or not.
	 */
	@Override
	public String switchToIntroScreen(boolean soundEffects, boolean soundMusic) {
		screen = Screen.INTRO;
		println("+-------------------------------------+");
		println("|                Yuuki                |");
		println("|                                     |");
		println("|  By Python'); DROP TABLE Teams;--   |");
		println("+-------------------------------------+");
		println();
		pause();
                return "Override Corrections";
	}
	/**
	 * Shows the options screen. The user is prompted to change options
	 * until he quits out of this screen.
	 */
	@Override
	public String switchToOptionsScreen() {
		screen = Screen.OPTIONS;
		boolean inOptions = true;
		while (inOptions) {
			String[] choices = {"Back to main menu"};
			int opt = getChoice("Enter option", choices);
			if (opt == 0) {
				inOptions = false;
			}
		}
                return "Override Corrections";
	}
        /**
	 * Shows the options screen. The user is prompted to change options
	 * until he quits out of this screen.
         * 
         * @param soundMusic Whether music plays or not.
         * @param soundEffects Whether effects play or not.
	 */
	@Override
        public String switchToOptionsScreen(boolean soundMusic, boolean soundEffects) {
            screen = Screen.OPTIONS;
            boolean inOptions = true;
            while (inOptions) {
                String[] choices = {"Back to main menu"};
                int opt = getChoice("Enter option", choices);
                if (opt == 0) {
                    inOptions = false;
                }
            }
            return "Override Corrections";
        }
	/**
	 * Displays the battle scene.
	 *
	 * @param fighters The characters who are fighting.
	 */
	@Override
	public void switchToBattleScreen(Character[][] fighters, boolean soundMusic, boolean soundEffects) {
		screen = Screen.BATTLE;
		showBattleIntro(fighters);
		showTeams(fighters);
	}
	
	/**
	 * Displays the overworld screen.
	 */
	@Override
	public void switchToOverworldScreen() {
		screen = Screen.OVERWORLD;
		println("Here, you would see the overworld.");
		println("Right now, there is nothing.");
		pause();
	}
	
	/**
	 * Displays the pause screen.
	 */
	@Override
	public void switchToPauseScreen() {
		screen = Screen.PAUSE;
		println("Paused the game.");
		pause();
		println("Unpaused the game.");
	}
	
	/**
	 * Displays the ending screen.
	 */
	@Override
	public void switchToEndingScreen() {
		screen = Screen.ENDING;
		println("The end.");
		println();
		println("Thanks for playing!");
		pause();
	}
	
	/**
	 * Displays the stats of a character during a battle.
	 *
	 * @param fighter The Character to update.
	 */
	@Override
	public void showStatUpdate(Character fighter) {
		if (screen != Screen.BATTLE) {
			return;
		}
		showStats(fighter);
	}
	
	/**
	 * Shows that damage occured to a character.
	 *
	 * @param fighter The affected character.
	 * @param stat The affected stat
	 * @param damage The amount of damage
	 */
	@Override
	public void showDamage(Character fighter, Stat stat, int damage) {
		if (screen != Screen.BATTLE) {
			return;
		}
		String msg = fighter.getName() + " took " + damage;
		msg += " damage to " + stat.getName();
		println(msg);
		pause();
	}
	
	/**
	 * Shows that damage occurred to a character.
	 *
	 * @param fighter The affected character.
	 * @param stat The affected stat
	 * @param damage The amount of damage
	 */
	@Override
	public void showDamage(Character fighter, Stat stat, double damage) {
		if (screen != Screen.BATTLE) {
			return;
		}
		String msg = fighter.getName() + " took " + damage;
		msg += " damage to " + stat.getName();
		println(msg);
		pause();
	}
	
	/**
	 * Shows that recovery occurred to a character.
	 *
	 * @param fighter The affected character.
	 * @param stat The affected stat.
	 * @param amount The amount of recovery.
	 */
	@Override
	public void showRecovery(Character fighter, Stat stat, double amount) {
		if (screen != Screen.BATTLE) {
			return;
		}
		String msg = fighter.getName() + " recovered " + amount;
		msg += " " + stat.getName();
		println(msg);
		pause();
	}
	
	/**
	 * Shows that recovery occured to a character.
	 *
	 * @param fighter The affected character.
	 * @param stat The affected stat.
	 * @param amount The amount of recovery.
	 */
	@Override
	public void showRecovery(Character fighter, Stat stat, int amount) {
		if (screen != Screen.BATTLE) {
			return;
		}
		String msg = fighter.getName() + " recovered " + amount;
		msg += " " + stat.getName();
		println(msg);
		pause();
	}
	
	/**
	 * Shows that a character is preparing to use an attack.
	 *
	 * @param action The move used.
	 */
	@Override
	public void showActionPreperation(Action action) {
		if (screen != Screen.BATTLE) {
			return;
		}
		String name = action.getOrigin().getName();
		ArrayList<Character> targets = action.getTargets();
		print(name + " is getting ready to use " + action.getName());
		if (targets.size() > 0) {
			print(" on");
			for (int i = 0; i < targets.size(); i++) {
				print(" " + targets.get(i).getName());
				if (i + 1 < targets.size()) {
					if (targets.size() > 2) {
						print(",");
					}
					if (i + 1 + 1 == targets.size()) {
						print(" and");
					}
				}
			}
		}
		println();
		pause();
	}
	
	/**
	 * Shows a character using an attack successfully.
	 *
	 * @param action The move used.
	 */
	public void showActionUse(Action action) {
		if (screen != Screen.BATTLE) {
			return;
		}
		String name = action.getOrigin().getName();
		println(name + " did it!");
		pause();
	}
	
	/**
	 * Shows a character fail at an attack.
	 *
	 * @param action The move used.
	 */
	public void showActionFailure(Action action) {
		if (screen != Screen.BATTLE) {
			return;
		}
		String name = action.getOrigin().getName();
		println(name + " couldn't pull it off.");
		pause();
	}
	
	/**
	 * Shows a buff activating.
	 *
	 * @param buff The buff to show.
	 */
	public void showBuffActivation(Buff buff) {
		if (screen != Screen.BATTLE) {
			return;
		}
		Character t = buff.getTarget();
		println(t.getName() + " is now " + buff.getName());
		pause();
	}
	
	/**
	 * Shows a buff being applied.
	 *
	 * @param buff The buff to show.
	 */
	public void showBuffApplication(Buff buff) {
		if (screen != Screen.BATTLE) {
			return;
		}
		Character t = buff.getTarget();
		String bName = buff.getName();
		println(t.getName() + " is feeling the effects of the " + bName);
		pause();
	}
	
	/**
	 * Shows a buff being deactivated.
	 *
	 * @param buff The buff to show.
	 */
	public void showBuffDeactivation(Buff buff) {
		if (screen != Screen.BATTLE) {
			return;
		}
		Character t = buff.getTarget();
		println(t.getName() + " is no longer " + buff.getName());
		pause();
	}
	
	/**
	 * Shows a character being removed from battle.
	 *
	 * @param fighter The character to show.
	 */
	public void showCharacterRemoval(Character fighter) {
		if (screen != Screen.BATTLE) {
			return;
		}
		println(fighter.getName() + " is no longer in the battle.");
		pause();
	}
	
	/**
	 * Shows that the specified characters are victorious.
	 *
	 * @param fighters The characters to show as victorious.
	 */
	public void showCharacterVictory(Character[] fighters) {
		if (screen != Screen.BATTLE) {
			return;
		}
		print("VICTORY!");
		for (int i = 0; i < fighters.length; i++) {
			print(" " + fighters[i]);
			if (i + 1 < fighters.length) {
				if (fighters.length > 2) {
					print(",");
				}
				if (i + 1 + 1 == fighters.length) {
					print(" and");
				}
			}
		}
		println(" are victorious!");
		pause();
	}

	
	/**
	 * Displays the message that a Character says.
	 *
	 * @param speaker The speaker of the line. Null for no speaker.
	 * @param message The message to display.
	 */
	@Override
	public void display(Character speaker, String message) {
		String s = "";
		String q = "";
		if (speaker != null) {
			s = speaker.getName().toUpperCase() + ": ";
			q = "\"";
		}
		String line = s + q + message + q;
		println(line);
	}
	
	/**
	 * Gets a String from the user.
	 *
	 * @param prompt The prompt to show the user.
	 *
	 * @return The entered String.
	 */
	@Override
	public String getString(String prompt) {
		String input = null;
		while (input == null) {
			print(prompt + ": ");
			flush();
			try {
				input = stdin.readLine();
			} catch(IOException e) {
				e.printStackTrace();
			}
			if (input.equals("")) {
				input = null;
			}
		}
		return input;
	}
	
	/**
	 * Gets a String from the user.
	 *
	 * @return The entered String.
	 */
	@Override
	public String getString() {
		return getString("");
	}
	
	/**
	 * Gets an int from the user.
	 *
	 * @param prompt The prompt to show the user.
	 *
	 * @return The entered int.
	 */
	@Override
	public int getInt(String prompt) {
		String input = null;
		int intInput = 0;
		boolean inputIsGood = false;
		while (!inputIsGood) {
			input = getString(prompt);
			inputIsGood = true;
			try {
				intInput = Integer.parseInt(input);
			} catch(NumberFormatException e) {
				warn("You must enter an integer!");
				inputIsGood = false;
			}
		}
		return intInput;
	}
	
	/**
	 * Gets an int from the user.
	 *
	 * @return The entered int.
	 */
	@Override
	public int getInt() {
		return getInt("");
	}
	
	/**
	 * Gets an int in a range from the user.
	 *
	 * @param prompt The prompt to show the user.
	 * @param min The minimum that the input can be.
	 * @param max The maximum that the input can be.
	 *
	 * @return A number in the given range.
	 */
	@Override
	public int getInt(String prompt, int min, int max) {
		int input = 0;
		boolean inputIsGood = false;
		while (!inputIsGood) {
			input = getInt(prompt);
			inputIsGood = (input >= min && input <= max);
			if (!inputIsGood) {
				warn("Number must be between " + min + " and " + max + "!");
			}
		}
		return input;
	}
	
	/**
	 * Gets an int in a range from the user.
	 *
	 * @param min The minimum that the input can be.
	 * @param max The maximum that the input can be.
	 *
	 * @return A number in the given range.
	 */
	@Override
	public int getInt(int min, int max) {
		return getInt("", min, max);
	}
	
	/**
	 * Gets a double from the user.
	 *
	 * @param prompt The prompt to show the user.
	 *
	 * @return The entered double.
	 */
	@Override
	public double getDouble(String prompt) {
		String input = null;
		double doubleInput = 0.0;
		boolean inputIsGood = false;
		while (!inputIsGood) {
			input = getString(prompt);
			inputIsGood = true;
			try {
				doubleInput = Double.parseDouble(input);
			} catch(NumberFormatException e) {
				warn("You must enter a real number!");
				inputIsGood = false;
			}
		}
		return doubleInput;
	}
	
	/**
	 * Gets a double from the user.
	 *
	 * @return The entered double.
	 */
	@Override
	public double getDouble() {
		return getDouble("");
	}
	
	/**
	 * Gets a double in a range from the user
	 *
	 * @param prompt The prompt to show the user.
	 * @param min The minimum that the input can be.
	 * @param max The maximum that the input can be.
	 *
	 * @return A number in the given range.
	 */
	@Override
	public double getDouble(String prompt, double min, double max) {
		double input = 0.0;
		boolean inputIsGood = false;
		while (!inputIsGood) {
			input = getDouble(prompt);
			inputIsGood = (input >= min && input <= max);
			if (!inputIsGood) {
				warn("Number must be between " + min + " and " + max + "!");
			}
		}
		return input;
	}
	
	/**
	 * Gets a double in a range from the user
	 *
	 * @param min The minimum that the input can be.
	 * @param max The maximum that the input can be.
	 *
	 * @return A number in the given range.
	 */
	@Override
	public double getDouble(double min, double max) {
		return getDouble("", min, max);
	}
	
	/**
	 * Gets a choice from the user. The choice may be one of the given Strings
	 * in the array.
	 *
	 * @param prompt The prompt to show the user.
	 * @param options The Strings from which the user must choose.
	 *
	 * @return The index of the user's choice.
	 */
	@Override
	public int getChoice(String prompt, String[] options) {
		int choice = 0;
		String builtPrompt = "OPTION:\n";
		for (int i = 0; i < options.length; i++) {
			builtPrompt += (i + 1);
			builtPrompt += " - ";
			builtPrompt += options[i];
			builtPrompt += '\n';
		}
		builtPrompt += prompt;
		choice = getInt(builtPrompt, 1, options.length) - 1;
		return choice;
	}
	
	/**
	 * Gets a choice from the user. The choice may be one of the given Strings
	 * in the array.
	 *
	 * @param options The Strings from which the user must choose.
	 *
	 * @return The index of the user's choice.
	 */
	@Override
	public int getChoice(String[] options) {
		return getChoice("", options);
	}
	
	/**
	 * Gets a confirmation from the user.
	 *
	 * @param prompt The prompt to show the user.
	 * @param yes The text for the true answer.
	 * @param no The text for the false answer.
	 *
	 * @return True if the user chose the yes text; otherwise false.
	 */
	public boolean confirm(String prompt, String yes, String no) {
		String[] options = {yes, no};
		return (getChoice(prompt, options) == 0);
	}
	
	/**
	 * Gets the action that a player wishes to do.
	 *
	 * @param moves The moves from which the player may select.
	 *
	 * @return The index of the selected Action.
	 */
	public int selectAction(Action[] moves) {
		String[] moveNames = new String[moves.length];
		for (int i = 0; i < moves.length; i++) {
			moveNames[i] = moves[i].getName();
		}
		return getChoice("Select a move", moveNames);
	}
	
	/**
	 * Gets the target of a move.
	 *
	 * @param fighters The characters to select from.
	 *
	 * @return The selected target.
	 */
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
	
	/**
	 * Displays the stats of a Character.
	 *
	 * @param f The character to show.
	 */
	private void showStats(Character f) {
		print(f.getName() + " (");
		print("Team " + f.getTeamId());
		println(", Fighter " + f.getFighterId() + ")");
		println("---------------");
		VariableStat hp = f.getHPStat();
		VariableStat mp = f.getMPStat();
		Stat str = f.getStrengthStat();
		Stat def = f.getDefenseStat();
		Stat agt = f.getAgilityStat();
		Stat acc = f.getAccuracyStat();
		Stat mag = f.getMagicStat();
		Stat luk = f.getLuckStat();
		println("HP: " + hp.getCurrent() + "/" + hp.getMax(f.getLevel()));
		println("MP: " + mp.getCurrent() + "/" + mp.getMax(f.getLevel()));
		println("---------------");
		println("STR: " + str.getEffective(f.getLevel()));
		println("DEF: " + def.getEffective(f.getLevel()));
		println("AGT: " + agt.getEffective(f.getLevel()));
		println("ACC: " + acc.getEffective(f.getLevel()));
		println("MAG: " + mag.getEffective(f.getLevel()));
		println("LUK: " + luk.getEffective(f.getLevel()));
		println();
		if (f.getBuffs().size() > 0) {
			showBuffs(f);
		}
		pause();
	}
	
	/**
	 * Shows the buffs currently on a character.
	 * 
	 * @param f The Character to show buffs for.
	 */
	private void showBuffs(Character f) {
		ArrayList<Buff> buffs = f.getBuffs();
		print("Buffs: ");
		for (int i = 0; i < buffs.size(); i++) {
			print(buffs.get(i).getName());
			if (i + 1 < buffs.size()) {
				print(", ");
			}
		}
		println();
	}
	
	/**
	 * Shows the battle introduction.
	 *
	 * @param fighters The fighting characters.
	 */
	private void showBattleIntro(Character[][] fighters) {
		String vsMsg = "";
		for (int i = 0; i < fighters.length; i++) {
			vsMsg += "Team " + fighters[i][0].getName();
			if (i + 1 < fighters.length) {
				vsMsg += " vs. ";
			}
		}
		vsMsg += "!";
		println("A battle started!");
		println(vsMsg);
		println();
		pause();
	}
	
	/**
	 * Shows what each team is made up of.
	 *
	 * @param fighters The fighters.
	 */
	private void showTeams(Character[][] fighters) {
		for (int i = 0; i < fighters.length; i++) {
			print("Team " + i + ":");
			for (int j = 0; j < fighters[i].length; j++) {
				print(" " + fighters[i][j].getName());
				if (j + 1 < fighters[i].length) {
					print(",");
				}
			}
			println();
		}
		println();
		pause();
	}
	
	/**
	 * Prints a message to stdout.
	 *
	 * @param message The message to print.
	 */
	private void print(String message) {
		try {
			stdout.write(message);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Flushes stdout.
	 */
	private void flush() {
		try {
			stdout.flush();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Prints a message to stdout and ends the line.
	 *
	 * @param message The message to print.
	 */
	private void println(String message) {
		try {
			stdout.write(message);
			stdout.newLine();
			stdout.flush();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Prints a line end to stdout.
	 */
	private void println() {
		println("");
	}
	
	/**
	 * Pauses the action until the user presses the enter key.
	 */
	private void pause() {
		println("(press enter to continue)");
//		try {
//			stdin.readLine();
//		} catch(IOException e) {
//			e.printStackTrace();
//		}
	}
	
	/**
	 * Prints an error message.
	 *
	 * @param message The message to print.
	 */
	private void warn(String message) {
		try {
			stderr.write(message);
			stderr.newLine();
			stderr.flush();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
