package yuuki.sound;

import java.util.Map;

import yuuki.ui.DialogHandler;
import yuuki.util.InvalidIndexException;

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
	
	@Override
	public void merge(Map<String, byte[]> content) {
		super.merge(content);
		if (track != null) {
			for (String index : content.keySet()) {
				if (track.equals(index)) {
					restartSound();
					break;
				}
			}
		}
	}
	
	/**
	 * Spawns a player thread.
	 * 
	 * @param index The index of the sound to play.
	 * @param restart Whether to restart the current player if the requested
	 * sound file is already playing.
	 * @throws InvalidIndexException
	 */
	public void playSound(String index, boolean restart) throws
	InvalidIndexException {
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
		track = null;
		if (musicPlayer != null) {
			musicPlayer.stop();
		}
	}
	
	@Override
	public void subtract(Map<String, byte[]> content) {
		super.subtract(content);
		if (track != null) {
			for (String index : content.keySet()) {
				if (track.equals(index)) {
					restartSound();
					break;
				}
			}
		}
	}
	
	/**
	 * Restarts the sound currently playing. If no sound is currently playing,
	 * this method has no effect.
	 */
	private void restartSound() {
		if (track != null) {
			try {
				playSound(track, true);
			} catch (InvalidIndexException e) {
				// should never happen.
				DialogHandler.showError(e);
			}
		}
	}
	
	@Override
	protected SoundRunner createPlayer(String index) throws
	InvalidIndexException {
		stopSound();
		track = index;
		byte[] data = getAudioData(index);
		musicPlayer = new SoundRunner(data, getVolume(), true);
		return musicPlayer;
	}
	
}
