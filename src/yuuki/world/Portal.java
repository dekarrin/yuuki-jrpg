package yuuki.world;

import java.awt.Point;

import yuuki.ui.Displayable;

/**
 * A location in a Land that links to another Land.
 */
public class Portal implements Locatable, Displayable {
	
	/**
	 * The Land that this Portal links to.
	 */
	private Land land;
	
	/**
	 * The name of this Portal.
	 */
	private String name;
	
	/**
	 * Whether this Portal may be entered.
	 */
	private boolean open;
	
	/**
	 * This Portal's position relative to the Land that it is in.
	 */
	private Point position;
	
	/**
	 * Creates a new Portal.
	 * 
	 * @param name The name of this Portal.
	 * @param land The Land to link to.
	 */
	public Portal(String name, Land land) {
		this.name = name;
		this.land = land;
	}
	
	/**
	 * Gets the name of the Land that this Portal links to.
	 * 
	 * @return The name of the Land that this Portal links to.
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
	 * Gets the name of this Portal.
	 * 
	 * @return The name of this Portal.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Checks whether this Portal may be entered.
	 * 
	 * @return True if this Portal may be entered; otherwise, false.
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
	 * Sets whether this Portal may be entered.
	 * 
	 * @param open Whether this Portal may be entered.
	 */
	public void setOpen(boolean open) {
		this.open = open;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public char getDisplayChar() {
		return 'P';
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getOverworldImage() {
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBattleImage() {
		return null;
	}
	
}
