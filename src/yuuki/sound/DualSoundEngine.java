package yuuki.sound;

import yuuki.util.InvalidIndexException;

/**
 * Combines the functionality of a MusicEngine and an EffectEngine into one
 * class for easy access.
 */
public class DualSoundEngine {
	
	/**
	 * Handles sound effects.
	 */
	private EffectEngine effectEngine;
	
	/**
	 * Handles background music.
	 */
	private MusicEngine musicEngine;
	
	/**
	 * Creates a new DualSoundEngine.
	 */
	public DualSoundEngine() {
		effectEngine = new EffectEngine();
		musicEngine = new MusicEngine();
	}
	
	/**
	 * Creates a new DualSoundEngine from the given engines.
	 * 
	 * @param effects The effect engine.
	 * @param music The music engine.
	 */
	public DualSoundEngine(EffectEngine effects, MusicEngine music) {
		effectEngine = effects;
		musicEngine = music;
	}
	
	/**
	 * Gets the sound effect volume.
	 * 
	 * @return The current volume of sound effects.
	 */
	public int getEffectVolume() {
		return effectEngine.getVolume();
	}
	
	/**
	 * Gets the music volume.
	 * 
	 * @return The current volume of music.
	 */
	public int getMusicVolume() {
		return musicEngine.getVolume();
	}
	
	/**
	 * Checks whether this DualSoundEngine is ready to have sounds played.
	 * 
	 * @return True if this sound engine has had both its music data and effect
	 * data set.
	 */
	public boolean isReady() {
		return (effectEngine.isReady() && musicEngine.isReady());
	}
	
	/**
	 * Plays the sound effect file associated with a music index. The file is
	 * loaded if it hasn't yet been loaded, and then it is played.
	 * 
	 * @param effectIndex The index of the sound effect.
	 */
	public void playEffect(String effectIndex) {
		try {
			effectEngine.playSound(effectIndex);
		} catch (InvalidIndexException e) {
			// silently fail
		}
	}
	
	/**
	 * Plays the music file associated with a music index. The file is loaded
	 * if it hasn't yet been loaded, and then it is played. If a music file is
	 * already playing when this method is called, it is stopped.
	 * 
	 * @param musicIndex The index of the music file.
	 */
	public void playMusic(String musicIndex) {
		playMusic(musicIndex, true);
	}
	
	/**
	 * Plays the music file associated with a music index. The file is loaded
	 * if it hasn't yet been loaded, and then it is played. If a music file is
	 * already playing when this method is called, it is stopped.
	 * 
	 * @param musicIndex The index of the music file.
	 * @param restart Whether to restart the music track if the music is
	 * already playing.
	 */
	public void playMusic(String musicIndex, boolean restart) {
		try {
			musicEngine.playSound(musicIndex, restart);
		} catch (InvalidIndexException e) {
			// silently fail
		}
	}
	
	/**
	 * Plays the music file associated with a music index. If a music file is
	 * already playing when this method is called, it is stopped.
	 * 
	 * This method causes the current thread to block until the music has
	 * started to play.
	 * 
	 * @param musicIndex The index of the music file.
	 * 
	 * @throws InterruptedException If the current thread is interrupted while
	 * waiting for the music to start.
	 */
	public void playMusicAndWait(String musicIndex) throws
	InterruptedException {
		try {
			musicEngine.playSoundAndWait(musicIndex);
		} catch (InvalidIndexException e) {
			// silently fail
		}
	}
	
	/**
	 * Sets the sound effect volume.
	 * 
	 * @param volume The new volume for sound effects.
	 */
	public void setEffectVolume(int volume) {
		effectEngine.setVolume(volume);
	}
	
	/**
	 * Sets the music volume.
	 * 
	 * @param volume The new volume for music.
	 */
	public void setMusicVolume(int volume) {
		musicEngine.setVolume(volume);
	}
	
	/**
	 * Stops the currently playing music.
	 */
	public void stopMusic() {
		musicEngine.stopSound();
	}
	
}
