package yuuki.sound;

/**
 * Listens for events fired from a SoundPlayerThread.
 */
interface SoundPlayerThreadListener {
	
	/**
	 * Fired when the sound player begins playing its clip.
	 */
	public void playbackStarted();
	
	/**
	 * Fired when the sound player finishes playing its clip.
	 */
	public void playbackFinished();
	
}
