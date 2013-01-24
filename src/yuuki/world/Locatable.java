package yuuki.world;

import java.awt.Point;

/**
 * An object that exists on a Land.
 */
public interface Locatable {
	
	/**
	 * Gets the current location of this Locatable.
	 * 
	 * @return This Locatable's current location.
	 */
	public Point getLocation();
	
	/**
	 * Sets the current location of this Locatable.
	 * 
	 * @param l The location to set this Locatable to.
	 */
	public void setLocation(Point l);
	
}
