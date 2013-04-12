package yuuki.ui.screen;

import java.awt.Point;

import yuuki.entity.PlayerCharacter.Orientation;

/**
 * Listens for events fired by pressing the movement buttons.
 */
public interface OverworldMovementListener {
	
	/**
	 * Fired when a movement button is pressed.
	 * 
	 * @param moveLocation The location that the button press indicates.
	 */
	public void movementButtonClicked(Point moveLocation);

	/**
	 * Fired when a movement button is pressed and the character turns that
	 * way.
	 * 
	 * @param orientation The character's new orientation.
	 */
	public void turnButtonClicked(Orientation orientation);
	
}
