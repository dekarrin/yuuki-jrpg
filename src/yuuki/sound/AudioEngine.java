package yuuki.sound;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Plays audio data.
 */
abstract class AudioEngine {
	
	/**
	 * The data for a blank WAV for initializing the engine.
	 */
	private static final short[] BLANK_DATA = {0x52, 0x49, 0x46, 0x46, 0x00,
		0x00, 0x00, 0x28, 0x57, 0x41, 0x56, 0x45, 0x66, 0x6d, 0x74, 0x20, 0x10,
		0x00, 0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x44, 0xac, 0x00, 0x00, 0x88,
		0x58, 0x01, 0x00, 0x02, 0x00, 0x10, 0x00, 0x64, 0x61, 0x74, 0x61, 0x04,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00};
	
	/**
	 * Audio data loaded from disk.
	 */
	private Map<String, byte[]> sounds;
	
	/**
	 * The name of player threads started by this AudioEngine.
	 */
	private final String threadName;
	
	/**
	 * The volume percentage of the sounds.
	 */
	private int volume;
	
	/**
	 * Creates a new AudioEngine.
	 * 
	 * @param threadName The name of player threads started by this
	 * AudioEngine.
	 */
	public AudioEngine(String threadName) {
		sounds = null;
		volume = 50;
		this.threadName = threadName;
		initializeAudioApi();
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
	 * Checks whether this AudioEngine is ready to play sounds.
	 * 
	 * @return True if this AudioEngine has had its sound data set; otherwise,
	 * false.
	 */
	public boolean isReady() {
		return (sounds != null);
	}
	
	/**
	 * Plays a sound file. If it hasn't yet been loaded, its data will be read
	 * from disk and cached.
	 * 
	 * @param index The index of the sound resource.
	 */
	public void playSound(String index) {
		SoundRunner player = createPlayer(index);
		(new Thread(player, threadName)).start();
	}
	
	/**
	 * Plays a sound file. This method causes the current thread to block until
	 * the sound begins playing.
	 * 
	 * @param index The index of the sound to play.
	 */
	public void playSoundAndWait(String index) {
		SoundRunner player = createPlayer(index);
		class Sleeper implements SoundRunnerListener {
			public boolean started = false;
			@Override
			public void playbackStarted() {
				started = true;
			}
			@Override
			public void playbackFinished() {}			
		}
		Sleeper s = new Sleeper();
	}
	
	/**
	 * Sets the data for this AudioEngine.
	 * 
	 * @param soundData A map of string indexes to byte arrays containing sound
	 * data. Such a map can be easily obtained using a SoundLoader object.
	 */
	public void setData(Map<String, byte[]> soundData) {
		sounds = soundData;
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
	 * Gets the bytes for a blank audio file.
	 */
	private byte[] getBlankData() {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(byteStream);
		try {
			for (short element : BLANK_DATA) {
				dataStream.write(element);
			}
			dataStream.flush();
		} catch (IOException e) {
			System.err.println("Failed to get audio initialization data");
		}
		return byteStream.toByteArray();
	}
	
	/**
	 * Plays a silent track to initialize the audio API.
	 */
	private void initializeAudioApi() {
		spawnEmptyPlayerThread();
	}
	
	/**
	 * Starts a player thread for empty sound data and begins playing it
	 * immediately.
	 */
	private void spawnEmptyPlayerThread() {
		byte[] data = getBlankData();
		SoundRunner player;
		player = new SoundRunner(data, 0, false);
		(new Thread(player, "AudioInit")).start();
	}
	
	/**
	 * Creates a sound player.
	 * 
	 * @param index The index of the sound that the player is to play.
	 * 
	 * @return The newly-created SoundRunner.
	 */
	protected abstract SoundRunner createPlayer(String index);
	
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
	
}
