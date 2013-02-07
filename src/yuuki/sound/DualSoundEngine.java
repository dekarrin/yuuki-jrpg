package yuuki.sound;

import java.util.Map;

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
	 * Checks whether this DualSoundEngine is ready to have sounds played.
	 * 
	 * @return True if this sound engine has had both its music data and effect
	 * data set.
	 */
	public boolean isReady() {
		return (effectEngine.isReady() && musicEngine.isReady());
	}
	
	/**
	 * Sets the data for the music engine.
	 * 
	 * @param musicData A map of string indexes to byte arrays containing sound
	 * data for music. Such a map can be easily obtained using a SoundLoader
	 * object.
	 */
	public void setMusicData(Map<String, byte[]> musicData) {
		musicEngine.setData(musicData);
	}
	
	/**
	 * Sets the data for the effect engine.
	 * 
	 * @param effectData A map of string indexes to byte arrays containing
	 * sound data for music. Such a map can be easily obtained using a
	 * SoundLoader object.
	 */
	public void setEffectData(Map<String, byte[]> effectData) {
		effectEngine.setData(effectData);
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
	 * Plays the sound effect file associated with a music index. The file is
	 * loaded if it hasn't yet been loaded, and then it is played.
	 * 
	 * @param musicIndex The index of the sound effect file.
	 */
	public void playEffect(String effectIndex) {
		effectEngine.playSound(effectIndex);
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
		musicEngine.playSound(musicIndex, restart);
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
		musicEngine.playSoundAndWait(musicIndex);
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
