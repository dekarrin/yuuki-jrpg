package yuuki.sound;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Loads and plays audio files.
 */
abstract class AudioEngine {
	
	/**
	 * The volume percentage of the sounds.
	 */
	private int volume;
	
	/**
	 * Audio data loaded from disk.
	 */
	protected HashMap<String, byte[]> sounds;
	
	/**
	 * Creates a new AudioEngine.
	 */
	public AudioEngine() {
		sounds = new HashMap<String, byte[]>();
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
	 * @param soundFile The name of the sound resource, relative to the
	 * package structure.
	 */
	public void playSound(String soundFile) {
		preload(soundFile);
		if (isLoaded(soundFile)) {
			spawnPlayerThread(soundFile);
		} else {
			System.err.println("Could not play '" + soundFile + "'");
		}
	}
	
	/**
	 * Caches a sound file's data immediately if and only if it has not already
	 * been cached.
	 * 
	 * @param soundFile The name of the sound resource, relative to the
	 * package structure.
	 */
	public void preload(String soundFile) {
		if (!isLoaded(soundFile)) {
			try {
				loadSound(soundFile);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				System.err.println("could not open '" + soundFile + "'");
			}
		}
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
	 * Checks whether a sound file has been loaded.
	 * 
	 * @param soundFile The file to check.
	 * 
	 * @return Whether the file has been loaded.
	 */
	private boolean isLoaded(String soundFile) {
		return sounds.containsKey(soundFile);
	}
	
	/**
	 * Loads a sound file from disk and stores it in the sounds hash.
	 * 
	 * @param soundFile The file to load.
	 * 
	 * @throws IOException If an IOException occurs.
	 */
	private void loadSound(String soundFile) throws IOException {
		InputStream s = getClass().getResourceAsStream(soundFile);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int n = 0;
		while ((n = s.read()) != -1) {
			buffer.write(n);
		}
		buffer.flush();
		byte[] sData = buffer.toByteArray();
		sounds.put(soundFile, sData);
	}
	
	/**
	 * Starts a player thread for a sound file that begins playing it
	 * immediately.
	 * 
	 * @param soundFile the file to make the player thread for.
	 */
	protected abstract void spawnPlayerThread(String soundFile);
	
}
