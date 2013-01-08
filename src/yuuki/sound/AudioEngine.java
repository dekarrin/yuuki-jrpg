package yuuki.sound;

import javax.sound.sampled.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Loads and plays audio files.
 */
public class AudioEngine {
	
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
	private synchronized void decreaseThreadCount() {
		threads--;
	}
	
	/**
	 * Increases the number of threads by one.
	 */
	private synchronized void increaseThreadCount() {
		threads++;
	}
	
	/**
	 * Plays sound data in a thread.
	 */
	private class SoundPlayer implements Runnable {
		
		/**
		 * The amount of time in milliseconds to for the player thread to wait
		 * before checking to see if the audio is done playing.
		 */
		private static final int AUDIO_THREAD_SLEEP_TIME = 10;
		
		/**
		 * The Clip that plays the audio data.
		 */
		private Clip clip;
		
		/**
		 * The audio stream for playing the audio data.
		 */
		private AudioInputStream stream;
		
		/**
		 * The audio data that is to be played.
		 */
		private byte[] data;
		
		/**
		 * Allocates a new SoundPlayer with given data. The data is copied from
		 * the given array to negate access issues.
		 * 
		 * @param soundData The sound data that is to be played.
		 */
		public SoundPlayer(byte[] soundData) {
			data = Arrays.copyOf(soundData, soundData.length);
		}
		
		/**
		 * Plays the audio data.
		 */
		public void run() {
			openAudioStream();
			openAudioClip();
			try {
				clip.open(stream);
				clip.start();
				while (!clip.isRunning()) {
					Thread.sleep(AUDIO_THREAD_SLEEP_TIME);
				}
				while (clip.isRunning()) {
					Thread.sleep(AUDIO_THREAD_SLEEP_TIME);
				}
				clip.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			decreaseThreadCount();
		}
		
		/**
		 * Opens the audio stream on the audio data.
		 */
		private void openAudioStream() {
			ByteArrayInputStream input = new ByteArrayInputStream(data);
			try {
				stream = AudioSystem.getAudioInputStream(input);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Opens the Clip for playing the audio data with the audio stream.
		 */
		private void openAudioClip() {
			try {
				AudioFormat format = stream.getFormat();
				DataLine.Info info = new DataLine.Info(Clip.class, format);
				clip = (Clip) AudioSystem.getLine(info);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		}
		
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
	private void spawnPlayerThread(String soundFile) {
		byte[] data = sounds.get(soundFile);
		SoundPlayer player = new SoundPlayer(data);
		increaseThreadCount();
		String threadName = "SoundPlayer-" + instanceId + "-" + threads;
		(new Thread(player, threadName)).start();
	}
	
}
