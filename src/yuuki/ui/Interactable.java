package yuuki.ui;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import yuuki.action.Action;
import yuuki.buff.Buff;
import yuuki.entity.Character;
import yuuki.entity.PlayerCharacter.Orientation;
import yuuki.entity.Stat;
import yuuki.graphic.ImageFactory;
import yuuki.item.InventoryPouch;
import yuuki.item.Item;
import yuuki.sound.DualSoundEngine;
import yuuki.util.Grid;
import yuuki.world.Movable;
import yuuki.world.Portal;
import yuuki.world.Tile;
import yuuki.world.WalkGraph;

/**
 * Shows program input and output.
 */
public interface Interactable {
	
	/**
	 * Clears the items from a certain spot on the overworld screen.
	 * 
	 * @param items The items to remove.
	 * @param count The number of items to remove.
	 */
	public void removeWorldItems(Item[] items, int count);
	
	/**
	 * Sets the inventory screen to show the contents of a pouch.
	 * 
	 * @param pouch The pouch to show.
	 */
	public void updateInventory(InventoryPouch pouch);
	
	/**
	 * Adds mods to the options screen.
	 * 
	 * @param names The names of the mods.
	 * @param ids The IDs of the mods.
	 */
	public void addMods(String[] names, String[] ids);
	
	/**
	 * Adds Characters to the world viewer.
	 * 
	 * @param characters The Characters to add.
	 */
	public void addWorldEntities(ArrayList<Movable> characters);
	
	/**
	 * Adds Items to the world viewer.
	 * 
	 * @param items The Items to add.
	 */
	public void addWorldItems(List<Item> items);
	
	/**
	 * Adds portals to the world viewer.
	 * 
	 * @param portals The portals to add.
	 */
	public void addWorldPortals(ArrayList<Portal> portals);
	
	/**
	 * Applies applicable options to this Interactable.
	 * 
	 * @param options The options.
	 */
	public void applyOptions(yuuki.Options options);
	
	/**
	 * Clears the Locatables from the world viewer.
	 */
	public void clearWorldLocatables();
	
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
	 * Takes down the interface. This is the last method that should be called,
	 * and implementers can use it to destroy any necessary supporting
	 * instances.
	 */
	public void destroy();
	
	/**
	 * Displays a message to the user.
	 *
	 * @param speaker The person doing the talking. Null for none.
	 * @param message The message to display.
	 * @param animated Whether the message should come up one letter at a time.
	 */
	public void display(Character speaker, String message, boolean animated);
	
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
	 * Gets a double from the user.
	 *
	 * @return The entered double.
	 */
	public double getDouble();
	
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
	 * Gets a double from the user.
	 *
	 * @param prompt The prompt to show the user.
	 *
	 * @return The entered double.
	 */
	public double getDouble(String prompt);
	
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
	 * Gets an int from the user.
	 *
	 * @return The entered int.
	 */
	public int getInt();
	
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
	 * Gets an int from the user.
	 *
	 * @param prompt The prompt to show the user.
	 *
	 * @return The entered int.
	 */
	public int getInt(String prompt);
	
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
	 * Gets a String from the user.
	 *
	 * @return The entered String.
	 */
	public String getString();
	
	/**
	 * Gets a String from the user.
	 *
	 * @param prompt The prompt to show the user.
	 *
	 * @return The entered String.
	 */
	public String getString(String prompt);
	
	/**
	 * Sets up the interface. This is the first method that should be called,
	 * and implementers can use it to create any necessary supporting classes
	 * for the interface.
	 */
	public void initialize();
	
	/**
	 * Initializes the image system.
	 * 
	 * @param factory The ImageFactory to use for the image system.
	 */
	public void initializeImages(ImageFactory factory);
	
	/**
	 * Initializes the sound system.
	 * 
	 * @param soundEngine The sound engine to use as the sound system.
	 */
	public void initializeSounds(DualSoundEngine soundEngine);
	
	/**
	 * Plays background music.
	 * 
	 * @param musicIndex The index of the music.
	 */
	public void playMusic(String musicIndex);
	
	/**
	 * Plays background music and then blocks until the music starts playing.
	 * 
	 * @param musicIndex The index of the music.
	 * 
	 * @throws InterruptedException If the current thread is interrupted while
	 * waiting for the music to start.
	 */
	public void playMusicAndWait(String musicIndex) throws
	InterruptedException;
	
	/**
	 * Plays a sound effect.
	 *
	 * @param effectIndex The index of the sound.
	 */
	public void playSound(String effectIndex);
	
	/**
	 * Resets the active prompt.
	 */
	public void resetPrompt();
	
	/**
	 * Gets the action that a player wishes to do.
	 *
	 * @param moves The moves from which the player may select.
	 *
	 * @return The selected Action.
	 */
	public Action selectAction(Action[] moves);
	
	/**
	 * Gets the next move that the human character wishes to make.
	 * 
	 * @param graph The graph of available places to walk to.
	 * @param orientation The current orientation of the player.
	 * 
	 * @return The point that the user wants to move to.
	 * 
	 * @throws InterruptedException If the current thread is interrupted while
	 * waiting for input.
	 */
	public Point selectMove(WalkGraph graph, Orientation orientation) throws
	InterruptedException;
	
	/**
	 * Gets the target of a move.
	 *
	 * @param fighters The characters to select from.
	 *
	 * @return The selected target.
	 */
	public Character selectTarget(ArrayList<ArrayList<Character>> fighters);
	
	/**
	 * Sets whether the loading bar is in indeterminate mode.
	 * 
	 * @param b Whether to put the loading bar in indeterminate mode.
	 */
	public void setLoadingIndeterminate(boolean b);
	
	/**
	 * Sets the world view to the given tile grid.
	 * 
	 * @param view The tiles that make up the world view.
	 * @param name The name of the land being displayed.
	 */
	public void setWorldView(Grid<Tile> view, String name);
	
	/**
	 * Shows a character fail at an attack.
	 *
	 * @param action The move used.
	 */
	public void showActionFailure(Action action);
	
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
	 * Pops up an alert dialog box above all windows.
	 * 
	 * @param msg The message to alert the user with.
	 */
	public void showAlertDialog(String msg);
	
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
	 * Pops up a confirmation dialog box above all windows.
	 * 
	 * @param msg The message to alert the user with.
	 * 
	 * @return True if the user clicks the yes button; false otherwise.
	 */
	public boolean showConfirmDialog(String msg);
	
	/**
	 * Shows that damage occurred to a character.
	 *
	 * @param fighter The affected character.
	 * @param stat The affected stat
	 * @param damage The amount of damage
	 */
	public void showDamage(Character fighter, Stat stat, double damage);
	
	/**
	 * Shows that damage occurred to a character.
	 *
	 * @param fighter The affected character.
	 * @param stat The affected stat
	 * @param damage The amount of damage
	 */
	public void showDamage(Character fighter, Stat stat, int damage);
	
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
	 * Updates the displayed stats of a character during a battle.
	 *
	 * @param fighter The Character to update.
	 */
	public void showStatUpdate(Character fighter);
	
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
	 * Shows the ending screen.
	 */
	public void switchToEndingScreen();
	
	/**
	 * Shows the Player Name screen.
	 */
	public void switchToIntroScreen();
	
	/**
	 * Switches to the inventory screen.
	 */
	public void switchToInvenScreen();
	
	/**
	 * Switches to the previous screen.
	 */
	public void switchToLastScreen();
	
	/**
	 * Shows the loading screen.
	 */
	public void switchToLoadingScreen();
	
	/**
	 * Shows the options screen.
	 */
	public void switchToOptionsScreen();
	
	/**
	 * Shows the overworld.
	 */
	public void switchToOverworldScreen();
	
	/**
	 * Shows the pause screen.
	 */
	public void switchToPauseScreen();
	
	/**
	 * Updates the loading progress display.
	 * 
	 * @param percent The percentage of loading complete.
	 * @param text The text to display.
	 */
	public void updateLoadingProgress(double percent, String text);
	
	/**
	 * Updates the world view to show a certain position.
	 * 
	 * @param center The point to center the view on.
	 */
	public void updateWorldView(Point center);
	
	/**
	 * Waits for the message to stop displaying.
	 */
	public void waitForDisplay();
	
}
