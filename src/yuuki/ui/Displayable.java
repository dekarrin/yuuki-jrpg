package yuuki.ui;

/**
 * An object that has a graphical representation.
 */
public interface Displayable {
	
	/**
	 * Gets the path of the image used for displaying this object in the battle
	 * screen.
	 * 
	 * @return The path to the battle image.
	 */
	public String getBattleImage();
	
	/**
	 * Gets the character used to display this object on a text-console.
	 * 
	 * @return The character.
	 */
	public char getDisplayChar();
	
	/**
	 * Gets the path to the Image used for displaying this object in the
	 * overworld.
	 * 
	 * @return The path to the overworld image.
	 */
	public String getOverworldImage();
	
}
