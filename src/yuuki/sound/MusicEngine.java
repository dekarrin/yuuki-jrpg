package yuuki.sound;

/**
 * An AudioEngine intended for playing long tracks that loop. Only one track
 * at a time is allowed.
 */
public class MusicEngine extends AudioEngine {
	
	/**
	 * The currently playing music.
	 */
	private SoundPlayer musicPlayer;
	
	/**
	 * The name of the currently playing track.
	 */
	private String track = null;
	
	/**
	 * Spawns a player thread.
	 * 
	 * @param soundFile The name of the file to play.
	 * @param restart Whether to restart the current player if the requested
	 * sound file is already playing.
	 */
	public void playSound(String soundFile, boolean restart) {
		if (track == null || !track.equals(soundFile) || restart) {
			playSound(soundFile);
		}
	}
	
	/**
	 * Change the volume of the currently playing music.
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void spawnPlayerThread(String soundFile) {
		track = soundFile;
		byte[] data = sounds.get(soundFile);
		stopSound();
		musicPlayer = new SoundPlayer(data, getVolume(), true);
		(new Thread(musicPlayer, "MusicPlayer")).start();
	}
	
}
