package yuuki;

/**
 * Contains game options.
 */
public class Options {
	
	/**
	 * The volume of in-game background music.
	 */
	public int bgmVolume = 100;
	
	/**
	 * The volume of in-game sound effects.
	 */
	public int sfxVolume = 100;
	
	/**
	 * Checks whether background music is enabled.
	 * 
	 * @return True if the background music volume property is greater than 0;
	 * otherwise false.
	 */
	public boolean bgmEnabled() {
		return (bgmVolume > 0);
	}
	
	/**
	 * Checks whether sound effects are enabled.
	 * 
	 * @return True if the sound effects volume property is greater than 0;
	 * otherwise false.
	 */
	public boolean sfxEnabled() {
		return (sfxVolume > 0);
	}
	
}
