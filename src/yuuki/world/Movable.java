package yuuki.world;

import java.awt.Point;

/**
 * An object that moves around on a Land.
 */
public interface Movable extends Locatable {
	
	/**
	 * Gets the next move of this Locatable. This must not actually move this
	 * Locatable, only get where it wishes to move to next. Returns null for
	 * no movement requested.
	 * 
	 * @param land The land that this Locatable is to move on.
	 * 
	 * @return This Locatable's next move.
	 * 
	 * @throws InterruptedException If the current thread is interrupted while
	 * waiting for the next move.
	 */
	public Point getNextMove(Land land) throws InterruptedException;
	
	/**
	 * Checks whether this Movable will transfer to another world if it walks
	 * on a tile occupied by a Portal.
	 * 
	 * @return Whether stepping on Portals make this Movable transfer.
	 */
	public boolean isTransferrable();
	
}
