package yuuki.sound;

import java.util.Map;

/**
 * Plays audio data.
 */
abstract class AudioEngine {
	
	/**
	 * Audio data loaded from disk.
	 */
	private Map<String, byte[]> sounds;
	
	/**
	 * The volume percentage of the sounds.
	 */
	private int volume;
	
	/**
	 * Creates a new AudioEngine.
	 * 
	 * @param soundData A map of string indexes to byte arrays containing sound
	 * data. Such a map can be easily obtained using a SoundLoader object.
	 */
	public AudioEngine(Map<String, byte[]> soundData) {
		sounds = soundData;
		volume = 50;
	}
	
	/**
	 * Gets the volume.
	 * 
	 * @return The volume.
	 */
	public int getVolume() {
		return volume;
	}
	
	/**
	 * Plays a sound file. If it hasn't yet been loaded, its data will be read
	 * from disk and cached.
	 * 
	 * @param index The index of the sound resource.
	 */
	public void playSound(String index) {
		spawnPlayerThread(index);
	}
	
	/**
	 * Sets the volume.
	 * 
	 * @param volume The new volume.
	 */
	public void setVolume(int volume) {
		this.volume = volume;
	}
	
	/**
	 * Gets a byte array of audio data for a sound index.
	 * 
	 * @param index The index to get the byte array for.
	 * 
	 * @return The byte array at the given index.
	 */
	protected byte[] getAudioData(String index) {
		return sounds.get(index);
	}
	
	/**
	 * Starts a player thread for a sound file that begins playing it
	 * immediately.
	 * 
	 * @param soundFile the file to make the player thread for.
	 */
	protected abstract void spawnPlayerThread(String soundFile);
	
}
