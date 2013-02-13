package yuuki.ui;

import java.awt.Image;

/**
 * An object that has a graphical representation.
 */
public interface Displayable {
	
	/**
	 * Gets the image used for displaying this object in the battle screen.
	 * 
	 * @return The battle image.
	 */
	public Image getBattleImage();
	
	/**
	 * Gets the character used to display this object on a text-console.
	 * 
	 * @return The character.
	 */
	public char getDisplayChar();
	
	/**
	 * Gets the Image used for displaying this object in the overworld.
	 * 
	 * @return The overworld image.
	 */
	public Image getOverworldImage();
	
}
