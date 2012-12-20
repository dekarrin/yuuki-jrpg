/**
 * Shows program input and output.
 */

package yuuki.ui;

import java.util.ArrayList;

import yuuki.entity.Character;
import yuuki.entity.Stat;
import yuuki.buff.Buff;
import yuuki.action.Action;

public interface Interactable {

	/**
	 * Sets up the interface. This is the first method that should be called,
	 * and implementers can use it to create any necessary supporting classes
	 * for the interface.
	 */
	public void initialize();
	
	/**
	 * Takes down the interface. This is the last method that should be called,
	 * and implementers can use it to destroy any necessary supporting
	 * instances.
	 */
	public void destroy();
        
	/**
	 * Shows the Player Name screen.
	 */
	public void switchToIntroScreen();
        
	/**
	 * Shows the options screen.
	 */
	public void switchToOptionsScreen();
        
	/**
	 * Shows the battle screen.
	 *
	 * @param fighters The characters fighting.
	 */
	public void switchToBattleScreen(Character[][] fighters);
	
	/**
	 * Shows the player creation screen.
	 */
	public void switchToCharacterCreationScreen();
	
	/**
	 * Updates the displayed stats of a character during a battle.
	 *
	 * @param fighter The Character to update.
	 */
	public void showStatUpdate(Character fighter);
	
	/**
	 * Shows that damage occurred to a character.
	 *
	 * @param fighter The affected character.
	 * @param stat The affected stat
	 * @param damage The amount of damage
	 */
	public void showDamage(Character fighter, Stat stat, int damage);
	
	/**
	 * Shows that damage occurred to a character.
	 *
	 * @param fighter The affected character.
	 * @param stat The affected stat
	 * @param damage The amount of damage
	 */
	public void showDamage(Character fighter, Stat stat, double damage);
	
	/**
	 * Shows that recovery occurred to a character.
	 *
	 * @param fighter The affected character.
	 * @param stat The affected stat.
	 * @param amount The amount of recovery.
	 */
	public void showRecovery(Character fighter, Stat stat, double amount);
	
	/**
	 * Shows that recovery occurred to a character.
	 *
	 * @param fighter The affected character.
	 * @param stat The affected stat.
	 * @param amount The amount of recovery.
	 */
	public void showRecovery(Character fighter, Stat stat, int amount);
	
	/**
	 * Shows that a character is preparing to use an attack.
	 *
	 * @param action The move used.
	 */
	public void showActionPreperation(Action action);
	
	/**
	 * Shows a character using an attack successfully.
	 *
	 * @param action The move used.
	 */
	public void showActionUse(Action action);
	
	/**
	 * Shows a character fail at an attack.
	 *
	 * @param action The move used.
	 */
	public void showActionFailure(Action action);
	
	/**
	 * Shows a buff activating.
	 *
	 * @param buff The buff to show.
	 */
	public void showBuffActivation(Buff buff);
	
	/**
	 * Shows a buff being applied.
	 *
	 * @param buff The buff to show.
	 */
	public void showBuffApplication(Buff buff);
	
	/**
	 * Shows a buff being deactivated.
	 *
	 * @param buff The buff to show.
	 */
	public void showBuffDeactivation(Buff buff);
	
	/**
	 * Shows a character being removed from battle.
	 *
	 * @param fighter The character to show.
	 */
	public void showCharacterRemoval(Character fighter);
	
	/**
	 * Shows that the specified characters are victorious.
	 *
	 * @param fighters The characters to show as victorious.
	 */
	public void showCharacterVictory(Character[] fighters);
	
	/**
	 * Shows the overworld.
	 */
	public void switchToOverworldScreen();
	
	/**
	 * Shows the pause screen.
	 */
	public void switchToPauseScreen();
	
	/**
	 * Shows the ending screen.
	 */
	public void switchToEndingScreen();
	
	/**
	 * Gets a String from the user.
	 *
	 * @param prompt The prompt to show the user.
	 *
	 * @return The entered String.
	 */
	public String getString(String prompt);
	
	/**
	 * Gets a String from the user.
	 *
	 * @return The entered String.
	 */
	public String getString();
	
	/**
	 * Gets an int from the user.
	 *
	 * @param prompt The prompt to show the user.
	 *
	 * @return The entered int.
	 */
	public int getInt(String prompt);
	
	/**
	 * Gets an int from the user.
	 *
	 * @return The entered int.
	 */
	public int getInt();
	
	/**
	 * Gets an int in a range from the user.
	 *
	 * @param prompt The prompt to show the user.
	 * @param min The minimum that the input can be.
	 * @param max The maximum that the input can be.
	 *
	 * @return A number in the given range.
	 */
	public int getInt(String prompt, int min, int max);
	
	/**
	 * Gets an int in a range from the user.
	 *
	 * @param min The minimum that the input can be.
	 * @param max The maximum that the input can be.
	 *
	 * @return A number in the given range.
	 */
	public int getInt(int min, int max);
	
	/**
	 * Gets a double from the user.
	 *
	 * @param prompt The prompt to show the user.
	 *
	 * @return The entered double.
	 */
	public double getDouble(String prompt);
	
	/**
	 * Gets a double from the user.
	 *
	 * @return The entered double.
	 */
	public double getDouble();
	
	/**
	 * Gets a double in a range from the user
	 *
	 * @param prompt The prompt to show the user.
	 * @param min The minimum that the input can be.
	 * @param max The maximum that the input can be.
	 *
	 * @return A number in the given range.
	 */
	public double getDouble(String prompt, double min, double max);
	
	/**
	 * Gets a double in a range from the user
	 *
	 * @param min The minimum that the input can be.
	 * @param max The maximum that the input can be.
	 *
	 * @return A number in the given range.
	 */
	public double getDouble(double min, double max);
	
	/**
	 * Gets a choice from the user. The choice may be one of the given Objects
	 * in the array. Each of the Objects should have a valid toString() method.
	 *
	 * @param prompt The prompt to show the user.
	 * @param options The Objects from which the user must choose.
	 *
	 * @return The user's choice.
	 */
	public Object getChoice(String prompt, Object[] options);
	
	/**
	 * Gets a choice from the user. The choice may be one of the given Objects
	 * in the array. Each of the Objects should have a valid toString() method.
	 *
	 * @param options The Objects from which the user must choose.
	 *
	 * @return The user's choice.
	 */
	public Object getChoice(Object[] options);
	
	/**
	 * Gets a confirmation from the user.
	 *
	 * @param prompt The prompt to show the user.
	 * @param yes The text for the true answer.
	 * @param no The text for the false answer.
	 *
	 * @return True if the user chose the yes text; otherwise false.
	 */
	public boolean confirm(String prompt, String yes, String no);
	
	/**
	 * Gets the action that a player wishes to do.
	 *
	 * @param moves The moves from which the player may select.
	 *
	 * @return The selected Action.
	 */
	public Action selectAction(Action[] moves);
	
	/**
	 * Gets the target of a move.
	 *
	 * @param fighters The characters to select from.
	 *
	 * @return The selected target.
	 */
	public Character selectTarget(ArrayList<ArrayList<Character>> fighters);
	
	/**
	 * Plays a sound from an audio file.
	 *
	 * @param path The file name of the sound.
	 */
	public void playSound(String path);
	
	/**
	 * Displays a message to the user.
	 *
	 * @param speaker The person doing the talking. Null for none.
	 * @param message The message to display.
	 */
	public void display(Character speaker, String message);
}