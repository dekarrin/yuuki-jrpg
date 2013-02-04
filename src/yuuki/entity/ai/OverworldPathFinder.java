package yuuki.entity.ai;

import java.awt.Point;

import yuuki.world.WalkGraph;

/**
 * Overworld movement AI.
 */
public abstract class OverworldPathFinder {
	
	/**
	 * Gets the next point to go to.
	 * 
	 * @param graph The WalkGraph to use for calculating the next move.
	 * 
	 * @return The next point.
	 */
	public abstract Point getNextMove(WalkGraph graph);
	
}
