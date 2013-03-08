package yuuki.ui.screen;

/**
 * A listener for events fired from an OptionsScreen.
 */
public interface OptionsScreenListener extends ScreenListener {
	
	/**
	 * Fired when the background music volume is changed.
	 * 
	 * @param volume The new percent volume of the background music.
	 */
	public void bgmVolumeChanged(int volume);
	
	/**
	 * Fired when the OK button is pushed on the options screen.
	 */
	public void optionsSubmitted();
	
	/**
	 * Fired when the test button is pushed on the options screen.
	 */
	public void sfxTestClicked();
	
	/**
	 * Fired when the sound effects volume is changed.
	 * 
	 * @param volume The new percent volume of the sound effects.
	 */
	public void sfxVolumeChanged(int volume);
	
	/**
	 * Fired when a mod is enabled.
	 * 
	 * @param id The ID of the enabled mod.
	 */
	public void modEnabled(String id);
	
	/**
	 * Fired when a mod is disabled.
	 * 
	 * @param id The ID of the disabled mod.
	 */
	public void modDisabled(String id);
	
}
