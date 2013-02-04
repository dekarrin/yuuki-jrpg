package yuuki.sound;

/**
 * An AudioEngine specifically intended for playing sound effects one time
 * each. Several sounds at once are allowed.
 */
public class EffectEngine extends AudioEngine {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void spawnPlayerThread(String soundFile) {
		byte[] data = sounds.get(soundFile);
		SoundPlayerThread player;
		player = new SoundPlayerThread(data, getVolume(), false);
		(new Thread(player, "SFXPlayer")).start();
	}
	
}
