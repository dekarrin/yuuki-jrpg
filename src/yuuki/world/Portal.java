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
	private String land;
	
	/**
	 * The point that this Portal links to.
	 */
	private Point link;
	
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
	 * @param land The name of the Land to link to.
	 * @param link The location in the land that this portal links to.
	 */
	public Portal(String name, String land, Point link) {
		this.name = name;
		this.land = land;
		this.link = link;
		this.open = true;
	}
	
	@Override
	public String getBattleImage() {
		return null;
	}
	
	@Override
	public Displayable getDisplayable() {
		return this;
	}
	
	@Override
	public char getDisplayChar() {
		return 'P';
	}
	
	/**
	 * Gets the location of this Portal's link.
	 * 
	 * @return The link location.
	 */
	public Point getLink() {
		return link;
	}
	
	/**
	 * Gets the name of the Land that this Portal links to.
	 * 
	 * @return The name of the Land that this Portal links to.
	 */
	public String getLinkedLand() {
		return land;
	}
	
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
	
	@Override
	public String getOverworldImage() {
		return null;
	}
	
	/**
	 * Checks whether this Portal may be entered.
	 * 
	 * @return True if this Portal may be entered; otherwise, false.
	 */
	public boolean isOpen() {
		return open;
	}
	
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
	
}
