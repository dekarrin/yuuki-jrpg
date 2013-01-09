package yuuki.sound;

import javax.sound.sampled.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Loads and plays audio files.
 */
public class AudioEngine implements ThreadCounter {
	
	/**
	 * The number of AudioEngine instances. Needed for thread naming.
	 */
	private static int instances = 0;
	
	/**
	 * This particular instance ID.
	 */
	private int instanceId;
	
	/**
	 * The number of spawned threads for SoundPlayers. Needed for thread
	 * naming.
	 */
	private int threads = 0;
	
	/**
	 * The location of sound resource files.
	 */
	private static final String RESOURCE_LOCATION = "/yuuki/resource/";
	
	/**
	 * Audio data loaded from disk.
	 */
	private HashMap<String, byte[]> sounds;
	
	/**
	 * Decreases the number of threads by one.
	 */
	public synchronized void decreaseThreadCount() {
		threads--;
	}
	
	/**
	 * Increases the number of threads by one.
	 */
	public synchronized void increaseThreadCount() {
		threads++;
	}
	
	/**
	 * Creates a new AudioEngine.
	 */
	public AudioEngine() {
		sounds = new HashMap<String, byte[]>();
		instanceId = AudioEngine.instances;
		AudioEngine.instances++;
	}
	
	/**
	 * Caches a sound file's data immediately if and only if it has not already
	 * been cached.
	 * 
	 * @param soundFile The name of the sound resource, relative to the
	 * resource package.
	 */
	public void preload(String soundFile) {
		if (!isLoaded(soundFile)) {
			try {
				loadSound(soundFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Plays a sound file. If it hasn't yet been loaded, its data will be read
	 * from disk and cached.
	 * 
	 * @param soundFile The name of the sound resource, relative to the
	 * resource package.
	 */
	public void playSound(String soundFile) {
		preload(soundFile);
		spawnPlayerThread(soundFile);
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
		String actualLocation = RESOURCE_LOCATION + soundFile;
		InputStream s = getClass().getResourceAsStream(actualLocation);
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
	protected void spawnPlayerThread(String soundFile) {
		byte[] data = sounds.get(soundFile);
		SoundPlayer player = new SoundPlayer(data);
		increaseThreadCount();
		String threadName = "SoundPlayer-" + instanceId + "-" + threads;
		(new Thread(player, threadName)).start();
	}
	
}
