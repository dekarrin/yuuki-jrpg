package yuuki.sound;

import yuuki.util.InvalidIndexException;

/**
 * An AudioEngine specifically intended for playing sound effects one time
 * each. Several sounds at once are allowed.
 */
public class EffectEngine extends AudioEngine {
	
	/**
	 * Creates a new EffectEngine.
	 */
	public EffectEngine() {
		super("SFXPlayer");
	}
	
	@Override
	protected SoundRunner createPlayer(String index) throws
	InvalidIndexException {
		byte[] data = getAudioData(index);
		return new SoundRunner(data, getVolume(), false);
	}
	
}
