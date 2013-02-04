package yuuki.entity.ai;

import java.awt.Point;

import yuuki.world.WalkGraph;

/**
 * Finds a path to the current location; that is, does not move at all.
 */
public class StandingPathFinder extends OverworldPathFinder {
	
	/**
	 * The current location.
	 */
	private Point location;
	
	/**
	 * Sets the current location.
	 * 
	 * @param location The current location.
	 */
	public void setLocation(Point location) {
		this.location = new Point(location);
	}
	
	@Override
	public Point getNextMove(WalkGraph graph) {
		return new Point(location);
	}
	
}
