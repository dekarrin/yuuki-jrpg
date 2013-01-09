package yuuki.sound;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
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
	 * The audio stream for playing the audio data.
	 */
	private AudioInputStream stream;
	
	/**
	 * The audio data that is to be played.
	 */
	private byte[] data;
	
	/**
	 * Allocates a new SoundPlayer with given data. The data is copied from
	 * the given array to negate access issues.
	 * 
	 * @param soundData The sound data that is to be played.
	 */
	public SoundPlayer(byte[] soundData) {
		data = Arrays.copyOf(soundData, soundData.length);
	}
	
	/**
	 * Plays the audio data.
	 */
	public void run() {
		openAudioStream();
		openAudioClip();
		try {
			clip.open(stream);
			clip.start();
			while (!clip.isRunning()) {
				Thread.sleep(AUDIO_THREAD_SLEEP_TIME);
			}
			while (clip.isRunning()) {
				Thread.sleep(AUDIO_THREAD_SLEEP_TIME);
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
