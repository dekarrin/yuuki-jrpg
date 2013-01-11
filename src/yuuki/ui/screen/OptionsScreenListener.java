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
	 * Fired when the sound effects volume is changed.
	 * 
	 * @param volume The new percent volume of the sound effects.
	 */
	public void sfxVolumeChanged(int volume);
	
	/**
	 * Fired when the OK button is pushed on the options screen.
	 */
	public void optionsSubmitted();
	
	/**
	 * Fired when the test button is pushed on the options screen.
	 */
	public void sfxTestClicked();
	
}
