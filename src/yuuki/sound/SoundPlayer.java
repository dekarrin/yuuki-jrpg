package yuuki.sound;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Plays sound data in a thread.
 */
class SoundPlayer implements Runnable {
	
	/**
	 * The amount of time in milliseconds to for the player thread to wait
	 * before checking to see if the audio is done playing.
	 */
	private static final int AUDIO_THREAD_SLEEP_TIME = 10;
	
	/**
	 * The Clip that plays the audio data.
	 */
	private Clip clip;
	
	/**
	 * The control for changing the clip volume.
	 */
	private FloatControl volumeControl;
	
	/**
	 * The audio stream for playing the audio data.
	 */
	private AudioInputStream stream;
	
	/**
	 * The audio data that is to be played.
	 */
	private byte[] data;
	
	/**
	 * The volume to play the data at.
	 */
	private int volume;
	
	/**
	 * The thread playing the audio clip.
	 */
	private Thread playerThread;
	
	/**
	 * Allocates a new SoundPlayer with given data. The data is copied from
	 * the given array to negate access issues.
	 * 
	 * @param soundData The sound data that is to be played.
	 * @param volume The volume to play the sound data at.
	 */
	public SoundPlayer(byte[] soundData, int volume) {
		data = Arrays.copyOf(soundData, soundData.length);
		this.volume = volume;
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
	 * Plays the audio data.
	 */
	public void run() {
		playerThread = Thread.currentThread();
		openAudioStream();
		openAudioClip();
		try {
			clip.open(stream);
			FloatControl.Type controlType = FloatControl.Type.MASTER_GAIN;
			volumeControl = (FloatControl) clip.getControl(controlType);
			adjustClipVolume();
			clip.start();
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
			// do nothing; interruption is expected
		} finally {
			clip.close();	
		}
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
		float max = volumeControl.getMaximum();
		float min = volumeControl.getMinimum();
		float range = max - min;
		float desiredOffset = ((float) volume / 100) * range;
		float desired = min + desiredOffset;
		if (volumeControl.getValue() != desired) {
			volumeControl.setValue(desired);
		}
	}
	
	/**
	 * Opens the audio stream on the audio data.
	 */
	private void openAudioStream() {
		ByteArrayInputStream input = new ByteArrayInputStream(data);
		try {
			stream = AudioSystem.getAudioInputStream(input);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
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
	
}