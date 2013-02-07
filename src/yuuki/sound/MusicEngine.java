package yuuki.sound;

/**
 * An AudioEngine intended for playing long tracks that loop. Only one track
 * at a time is allowed.
 */
public class MusicEngine extends AudioEngine {
	
	/**
	 * The currently playing music.
	 */
	private SoundRunner musicPlayer;
	
	/**
	 * The name of the currently playing track.
	 */
	private String track = null;
	
	/**
	 * Creates a new MusicEngine.
	 */
	public MusicEngine() {
		super("MusicPlayer");
	}
	
	/**
	 * Spawns a player thread.
	 * 
	 * @param index The index of the sound to play.
	 * @param restart Whether to restart the current player if the requested
	 * sound file is already playing.
	 */
	public void playSound(String index, boolean restart) {
		if (track == null || !track.equals(index) || restart) {
			playSound(index);
		}
	}
	
	/**
	 * Changes the volume of the currently playing music.
	 * 
	 * @param volume The new volume.
	 */
	@Override
	public void setVolume(int volume) {
		super.setVolume(volume);
		if (musicPlayer != null) {
			musicPlayer.setVolume(volume);
		}
	}
	
	/**
	 * Stops the current sound immediately.
	 */
	public void stopSound() {
		if (musicPlayer != null) {
			musicPlayer.stop();
		}
	}
	
	@Override
	protected SoundRunner createPlayer(String index) {
		track = index;
		byte[] data = getAudioData(index);
		stopSound();
		musicPlayer = new SoundRunner(data, getVolume(), true);
		return musicPlayer;
	}
	
}
