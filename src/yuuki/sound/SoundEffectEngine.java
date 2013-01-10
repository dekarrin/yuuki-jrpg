package yuuki.sound;

/**
 * An AudioEngine specifically intended for playing sound effects one time
 * each.
 */
public class SoundEffectEngine extends AudioEngine {
	
	/**
	 * @inheritDoc
	 */
	protected void spawnPlayerThread(String soundFile) {
		byte[] data = sounds.get(soundFile);
		SoundPlayer player = new SoundPlayer(data, getVolume());
		(new Thread(player, "SFXPlayer")).start();
	}
}
