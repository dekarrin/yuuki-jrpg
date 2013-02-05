package yuuki.world;

import yuuki.ui.Displayable;

/**
 * A single tile in a land.
 */
public class Tile implements Displayable {
	
	/**
	 * The ID of this Tile, used for getting the representative character.
	 */
	private int id;
	
	/**
	 * The name of this tile. This may be used for identifying different types
	 * of tiles for graphical representation.
	 */
	private String name;
	
	/**
	 * Whether this tile currently has occupants.
	 */
	private boolean occupied;
	
	/**
	 * Whether this tile can be walked on.
	 */
	private boolean walkable;
	
	/**
	 * Creates a new Tile.
	 * 
	 * @param name The name of the new Tile.
	 * @param walkable Whether this Tile may be walked on.
	 */
	public Tile(String name, boolean walkable) {
		this.name = name;
		this.walkable = walkable;
		occupied = false;
	}
	
	@Override
	public String getBattleImage() {
		return null;
	}
	
	@Override
	public char getDisplayChar() {
		return ((char) id);
	}
	
	/**
	 * Gets the name of this Tile, normalized to lower-case.
	 * 
	 * @return This Tile's name.
	 */
	public String getName() {
		return name.toLowerCase();
	}
	
	@Override
	public String getOverworldImage() {
		return null;
	}
	
	/**
	 * Checks whether this Tile is occupied.
	 * 
	 * @return True if there is some occupant on this tile; otherwise, false.
	 */
	public boolean isOccupied() {
		return occupied;
	}
	
	/**
	 * Checks whether this Tile is walkable.
	 * 
	 * @return True if this Tile can be walked on; false otherwise.
	 */
	public boolean isWalkable() {
		return walkable;
	}
	
	/**
	 * Sets the ID of this Tile.
	 * 
	 * @param id The ID to set it to.
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Sets whether this Tile is occupied.
	 * 
	 * @param occupied What to set this Tile's occupied status to.
	 */
	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
	
	/**
	 * Sets whether this Tile is walkable.
	 * 
	 * @param walkable What to set this Tile's walkable status to.
	 */
	public void setWalkable(boolean walkable) {
		this.walkable = walkable;
	}
	
	@Override
	public String toString() {
		return "" + getDisplayChar();
	}
	
}
