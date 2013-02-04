package yuuki.sound;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Plays sound data in a thread.
 */
class SoundPlayerThread implements Runnable {
	
	/**
	 * The amount of time in milliseconds to for the player thread to wait
	 * before checking to see if the audio is done playing.
	 */
	private static final int AUDIO_THREAD_SLEEP_TIME = 10;
	
	/**
	 * The maximum volume in dBs.
	 */
	private static final float MAXIMUM_VOLUME = 0f;
	
	/**
	 * The minimum volume in dBs.
	 */
	private static final float MINIMUM_VOLUME = -40f;
	
	/**
	 * The Clip that plays the audio data.
	 */
	private Clip clip;
	
	/**
	 * The audio data that is to be played.
	 */
	private byte[] data;
	
	/**
	 * Whether the sound clip will loop continuously.
	 */
	private boolean looping;
	
	/**
	 * The control for muting this clip.
	 */
	private BooleanControl muteControl;
	
	/**
	 * The thread playing the audio clip.
	 */
	private Thread playerThread;
	
	/**
	 * The audio stream for playing the audio data.
	 */
	private AudioInputStream stream;
	
	/**
	 * The volume to play the data at.
	 */
	private volatile int volume;
	
	/**
	 * The control for changing the clip volume.
	 */
	private FloatControl volumeControl;
	
	/**
	 * Allocates a new SoundPlayer with given data. The data is copied from
	 * the given array to negate access issues.
	 * 
	 * @param soundData The sound data that is to be played.
	 * @param volume The volume to play the sound data at.
	 * @param loop Whether to loop play back until stopped.
	 */
	public SoundPlayerThread(byte[] soundData, int volume, boolean loop) {
		data = Arrays.copyOf(soundData, soundData.length);
		this.volume = volume;
		this.looping = loop;
	}
	
	/**
	 * Plays the audio data.
	 */
	@Override
	public void run() {
		playerThread = Thread.currentThread();
		openAudioStream();
		openAudioClip();
		try {
			clip.open(stream);
			FloatControl.Type vType = FloatControl.Type.MASTER_GAIN;
			BooleanControl.Type mType = BooleanControl.Type.MUTE;
			volumeControl = (FloatControl) clip.getControl(vType);
			muteControl = (BooleanControl) clip.getControl(mType);
			adjustClipVolume();
			if (looping) {
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			} else {
				clip.start();
			}
			while (!clip.isRunning()) {
				Thread.sleep(AUDIO_THREAD_SLEEP_TIME);
				adjustClipVolume();
			}
			while (clip.isRunning()) {
				Thread.sleep(AUDIO_THREAD_SLEEP_TIME);
				adjustClipVolume();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} finally {
			clip.close();
		}
	}
	
	/**
	 * Sets the volume of the playing clip.
	 * 
	 * @param volume The new volume.
	 */
	public void setVolume(int volume) {
		this.volume = volume;
	}
	
	/**
	 * Stops playing immediately by interrupting the player thread.
	 */
	public void stop() {
		playerThread.interrupt();
	}
	
	/**
	 * Changes the clip volume if the current volume is different from the
	 * desired volume.
	 */
	private void adjustClipVolume() {
		muteControl.setValue(volume == 0);
		if (volume != 0) {
			float gMax = Math.min(volumeControl.getMaximum(), MAXIMUM_VOLUME);
			float gMin = Math.max(volumeControl.getMinimum(), MINIMUM_VOLUME);
			double aMax = Math.pow(10, gMax/20);
			double aMin = Math.pow(10, gMin/20);
			double amplitude = aMin + ((volume - 1) * (aMax - aMin)) / 99;
			float gain = (float) (20 * Math.log10(amplitude));
			if (Math.abs(volumeControl.getValue() - gain) >= 0.0001) {
				volumeControl.setValue(gain);
			}
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
	
	/**
	 * Opens the audio stream on the audio data.
	 */
	private void openAudioStream() {
		ByteArrayInputStream input = new ByteArrayInputStream(data);
		try {
			AudioInputStream raw = AudioSystem.getAudioInputStream(input);
			AudioFormat.Encoding enc = AudioFormat.Encoding.PCM_SIGNED;
			stream = AudioSystem.getAudioInputStream(enc, raw);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}
	
}
