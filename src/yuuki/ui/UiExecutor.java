package yuuki.ui;

import yuuki.entity.Character;
import yuuki.entity.PlayerCharacter.Orientation;

/**
 * The class that performs the game logic as requested by the user interface.
 * It is not guaranteed that a worker thread will be spawned to call any of
 * these methods, so if thread safety is desired, the implementation must
 * handle it.
 */
public interface UiExecutor {
	
	/**
	 * Requests that the inventory screen be opened.
	 */
	public void requestInventoryOpen();
	
	/**
	 * Requests that the inventory screen be closed.
	 */
	public void requestInventoryClose();
	
	/**
	 * Requests that a battle be initialized and its intro shown if main. The
	 * main battle is shown on screen, but there can only be one at a time.
	 * Non-main battles are run through as fast as possible. Battles that
	 * include the player character as a participant cannot be non-main.
	 * 
	 * @param isMain Whether the battle should be considered the main battle.
	 * @param t1 The characters on the bottom team.
	 * @param t2 The characters on the top team.
	 */
	public void requestBattle(boolean isMain, Character[] t1, Character[] t2);
	
	/**
	 * Requests that the battle be finished and the game return to the
	 * overworld.
	 */
	public void requestBattleEnd();
	
	/**
	 * Requests that the battle immediately be terminated without resolving it.
	 */
	public void requestBattleKill();
	
	/**
	 * Requests that the battle be paused.
	 */
	public void requestBattlePause();
	
	/**
	 * Requests that the battle be resumed.
	 */
	public void requestBattleResume();
	
	/**
	 * Requests that the main battle be run through.
	 */
	public void requestBattleStart();
	
	/**
	 * Requests that a player character is created.
	 * 
	 * @param name The name of the player character.
	 * @param level The level of the player character.
	 */
	public void requestCharacterCreation(String name, int level);
	
	/**
	 * Requests that the current game be closed and that the user be returned
	 * to the main menu.
	 */
	public void requestCloseGame();
	
	/**
	 * Requests that an old game be loaded.
	 */
	public void requestLoadGame();
	
	/**
	 * Requests that a mod be disabled.
	 * 
	 * @param id The ID of the mod to disable.
	 */
	public void requestModDisable(String id);
	
	/**
	 * Requests that a mod be enabled.
	 * 
	 * @param id The ID of the mod to enable.
	 */
	public void requestModEnable(String id);
	
	/**
	 * Requests that a new game be started.
	 */
	public void requestNewGame();
	
	/**
	 * Requests that all options be applied.
	 */
	public void requestOptionApplication();
	
	/**
	 * Requests that the options screen be shown.
	 */
	public void requestOptionsScreen();
	
	/**
	 * Called when the options have been submitted.
	 */
	public void requestOptionsSubmission();
	
	/**
	 * Requests that the game be exited.
	 */
	public void requestQuit();
	
	/**
	 * Requests that the game be saved.
	 */
	public void requestSaveGame();
	
	/**
	 * Requests that the player be turned to face a different direction.
	 * 
	 * @param orientation The direction that the player is to face.
	 */
	public void requestPlayerTurn(Orientation orientation);

	/**
	 * Requests that the player pick up an item in front of him.
	 */
	public void requestGetItem();
	
}
