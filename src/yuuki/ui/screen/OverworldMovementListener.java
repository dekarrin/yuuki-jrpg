package yuuki.ui.screen;

import java.awt.Point;

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
	
}
