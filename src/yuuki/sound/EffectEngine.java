package yuuki.sound;

import java.util.Map;

/**
 * An AudioEngine specifically intended for playing sound effects one time
 * each. Several sounds at once are allowed.
 */
public class EffectEngine extends AudioEngine {
	
	/**
	 * Creates a new EffectEngine.
	 * 
	 * @param soundData A map of string indexes to byte arrays containing sound
	 * data. Such a map can be easily obtained using a SoundLoader object.
	 */
	public EffectEngine(Map<String, byte[]> soundData) {
		super(soundData);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void spawnPlayerThread(String soundFile) {
		byte[] data = getAudioData(soundFile);
		SoundPlayerThread player;
		player = new SoundPlayerThread(data, getVolume(), false);
		(new Thread(player, "SFXPlayer")).start();
	}
	
}
