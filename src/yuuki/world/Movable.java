package yuuki.world;

import java.awt.Point;

/**
 * An object that moves around on a Land.
 */
public interface Movable extends Locatable {
	
	/**
	 * Gets the next move of this Locatable. This must not actually move this
	 * Locatable, only get where it wishes to move to next.
	 * 
	 * @param land The land that this Locatable is to move on.
	 * 
	 * @return This Locatable's next move.
	 */
	public Point getNextMove(Land land);
	
}
