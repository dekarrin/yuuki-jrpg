package yuuki.world;

import java.awt.Point;

/**
 * A location in a Land that links to another Land.
 */
public class Town implements Locatable {
	
	/**
	 * The Land that this Town links to.
	 */
	private Land land;
	
	/**
	 * The name of this Town.
	 */
	private String name;
	
	/**
	 * Whether this Town may be entered.
	 */
	private boolean open;
	
	/**
	 * This Town's position relative to the Land that it is in.
	 */
	private Point position;
	
	/**
	 * Creates a new Town.
	 * 
	 * @param name The name of this Town.
	 * @param land The Land to link to.
	 */
	public Town(String name, Land land) {
		this.name = name;
		this.land = land;
	}
	
	/**
	 * Gets the name of the Land that this Town links to.
	 * 
	 * @return The name of the Land that this Town links to.
	 */
	public String getLinkedLandName() {
		return land.getName();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point getLocation() {
		return position;
	}
	
	/**
	 * Gets the name of this Town.
	 * 
	 * @return The name of this Town.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point getNextMove(Land land) {
		// Towns do not move
		return getLocation();
	}
	
	/**
	 * Checks whether this Town may be entered.
	 * 
	 * @return True if this Town may be entered; otherwise, false.
	 */
	public boolean isOpen() {
		return open;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLocation(Point l) {
		position = l;
	}
	
	/**
	 * Sets whether this Town may be entered.
	 * 
	 * @param open Whether this Town may be entered.
	 */
	public void setOpen(boolean open) {
		this.open = open;
	}
	
}
