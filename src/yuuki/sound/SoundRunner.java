package yuuki.sound;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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
class SoundRunner implements Runnable {
	
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
	 * The listeners registered to this SoundPlayerThread.
	 */
	private Set<SoundRunnerListener> listeners;
	
	/**
	 * Whether the sound clip will loop continuously.
	 */
	private boolean looping;
	
	/**
	 * The control for muting this clip.
	 */
	private BooleanControl muteControl;
	
	/**
	 * The thread that plays the sounds.
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
	 * Allocates a new SoundPlayer with given data.
	 * 
	 * @param soundData The sound data that is to be played.
	 * @param volume The volume to play the sound data at.
	 * @param loop Whether to loop play back until stopped.
	 */
	public SoundRunner(byte[] soundData, int volume, boolean loop) {
		data = soundData;
		this.volume = volume;
		this.looping = loop;
		this.listeners = new HashSet<SoundRunnerListener>();
		initializeAudioProperties();
	}
	
	/**
	 * Adds a listener to this SoundPlayerThread.
	 * 
	 * @param l The listener to add.
	 */
	public void addListener(SoundRunnerListener l) {
		listeners.add(l);
	}
	
	/**
	 * Removes a listener from this SoundPlayerThread.
	 * 
	 * @param l The listener to remove.
	 */
	public void removeListener(Object l) {
		listeners.remove(l);
	}
	
	/**
	 * Plays the audio data.
	 */
	@Override
	public void run() {
		playerThread = Thread.currentThread();
		try {
			adjustClipVolume();
			startPlayback();
			blockUntilPlaybackStarts();
			firePlaybackStarted();
			blockUntilPlaybackFinishes();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (RuntimeException e) {
			throw e;
		} finally {
			closeAudioClip();
			firePlaybackFinished();
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
	 * Stops playing immediately.
	 */
	public void stop() {
		if (playerThread != null) {
			playerThread.interrupt();
		}
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
	 * Causes the current thread to block until the clip has finished playing.
	 * 
	 * @throws InterruptedException If the current thread is interrupted while
	 * waiting for the clip to finish playing.
	 */
	private void blockUntilPlaybackFinishes() throws InterruptedException {
		while (clip.isRunning()) {
			Thread.sleep(AUDIO_THREAD_SLEEP_TIME);
			adjustClipVolume();
		}
	}
	
	/**
	 * Causes the current thread to block until the clip has started playing.
	 * 
	 * @throws InterruptedException If the current thread is interrupted while
	 * waiting for the clip to start playing.
	 */
	private void blockUntilPlaybackStarts() throws InterruptedException {
		while (!clip.isRunning()) {
			Thread.sleep(AUDIO_THREAD_SLEEP_TIME);
			adjustClipVolume();
		}
	}
	
	/**
	 * Closes the audio clip.
	 */
	private void closeAudioClip() {
		clip.close();
	}
	
	/**
	 * Calls playbackFinished() on each of the listeners.
	 */
	private void firePlaybackFinished() {
		int size = listeners.size();
		SoundRunnerListener[] ls = new SoundRunnerListener[size];
		listeners.toArray(ls);
		for (SoundRunnerListener l : ls) {
			l.playbackFinished();
		}
	}
	
	/**
	 * Calls playbackStarted() on each of the listeners.
	 */
	private void firePlaybackStarted() {
		int size = listeners.size();
		SoundRunnerListener[] ls = new SoundRunnerListener[size];
		listeners.toArray(ls);
		for (SoundRunnerListener l : ls) {
			l.playbackStarted();
		}
	}
	
	/**
	 * Gets the audio line controls from the open audio clip.
	 */
	private void getClipControls() {
		FloatControl.Type vType = FloatControl.Type.MASTER_GAIN;
		BooleanControl.Type mType = BooleanControl.Type.MUTE;
		volumeControl = (FloatControl) clip.getControl(vType);
		muteControl = (BooleanControl) clip.getControl(mType);
	}
	
	/**
	 * Initializes the audio stream, the audio line, and the audio clip.
	 */
	private void initializeAudioProperties() {
		openAudioStream();
		openAudioLine();
		try {
			openAudioClip();
			getClipControls();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Opens the audio clip on the audio stream.
	 * 
	 * @throws IOException If an IOException occurs.
	 * @throws LineUnavailableException If the audio line is unavailable.
	 */
	private void openAudioClip() throws LineUnavailableException, IOException {
		clip.open(stream);
	}
	
	/**
	 * Opens an audio line for playing the audio data with the audio stream.
	 */
	private void openAudioLine() {
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
	
	/**
	 * Starts playing the data in the open audio clip.
	 */
	private void startPlayback() {
		if (looping) {
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} else {
			clip.start();
		}
	}
	
}
