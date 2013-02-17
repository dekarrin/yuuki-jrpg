package yuuki.file;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import yuuki.util.Progressable;

/**
 * Loads all sounds in a resource file.
 */
public class SoundLoader extends CsvResourceLoader {
	
	/**
	 * The path to the directory containing the sound files that are referenced
	 * by the resource files.
	 */
	private ByteArrayLoader soundLoader;
	
	/**
	 * Creates a new SoundLoader for resource files at the specified location.
	 * 
	 * @param location The path to the directory containing the resource files
	 * to be loaded, relative to the package structure.
	 * @param soundRoot The path to the directory containing the sound files
	 * that are referenced by the resource files.
	 */
	public SoundLoader(String location, String soundRoot) {
		super(location);
		soundLoader = new ByteArrayLoader(soundRoot);
	}
	
	/**
	 * Loads a resource containing a list of sounds and indexes as well as all
	 * the sound files it refers to.
	 * 
	 * @param resource The location of the file to load, relative to the
	 * resource root.
	 * 
	 * @return A map with the loaded sound files.
	 * 
	 * @throws ResourceNotFoundException If the resource file or a file
	 * referenced by the resource file does not exist.
	 * @throws IOException If an IOException occurs.
	 */
	public Map<String, byte[]> load(String resource) throws
	ResourceNotFoundException, IOException {
		Map<String, byte[]> sounds = new HashMap<String, byte[]>();
		String[][] records = loadRecords(resource);
		for (String[] r : records) {
			String index = r[0];
			String file = r[1];
			Progressable m = getProgressSubSection(1.0 / records.length);
			soundLoader.setProgressMonitor(m);
			try {
				byte[] data = soundLoader.load(file);
				if (m != null) {
					m.finishProgress();
				}
				sounds.put(index, data);
			} catch (ResourceNotFoundException e) {
				System.err.println("Could not load sound: " + e.getMessage());
			}
		}
		return sounds;
	}
	
}
