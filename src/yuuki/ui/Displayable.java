package yuuki.ui;

/**
 * An object that has a graphical representation.
 */
public interface Displayable {
	
	/**
	 * Gets the character used to display this object on a text-console.
	 * 
	 * @return The character.
	 */
	public char getDisplayChar();
	
	/**
	 * Gets the path to the image used for displaying this object in the
	 * overworld. This is relative to the images resource directory in the
	 * package structure.
	 * 
	 * @return The path to the overworld image.
	 */
	public String getOverworldImage();
	
	/**
	 * Gets the path to the image used for displaying this object in the battle
	 * screen. This is relative to the images resource directory in the package
	 * structure.
	 * 
	 * @return The path to the battle image.
	 */
	public String getBattleImage();
	
}
