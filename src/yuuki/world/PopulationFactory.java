package yuuki.world;

import java.awt.Point;

import yuuki.entity.EntityFactory;
import yuuki.entity.NonPlayerCharacter;
import yuuki.item.Item;
import yuuki.item.ItemFactory;
import yuuki.util.InvalidIndexException;

/**
 * Creates instances of classes used to populate a Land. PopulationFactory
 * combines the functionality of an EntityFactory, a TileFactory,
 * an ItemFactory, and a PortalFactory to allow the instantiation of any class
 * used in Land population. PopulationFactory does not provide mutation
 * functionality of its component factories.
 */
public class PopulationFactory {
	
	/**
	 * Creates Character instances.
	 */
	private final EntityFactory entities;
	
	/**
	 * Creates Item instances.
	 */
	private final ItemFactory items;
	
	/**
	 * Creates Portal instances.
	 */
	private final PortalFactory portals;
	
	/**
	 * Creates Tile instances.
	 */
	private final TileFactory tiles;
	
	/**
	 * Creates a new PopulationFactory with the specified sub-factories.
	 * 
	 * @param tiles The TileFactory to use.
	 * @param entities The EntityFactory to use.
	 * @param portals The PortalFactory to use.
	 * @param items, The ItemFactory to use.
	 */
	public PopulationFactory(TileFactory tiles, EntityFactory entities,
			PortalFactory portals, ItemFactory items) {
		this.tiles = tiles;
		this.entities = entities;
		this.portals = portals;
		this.items = items;
	}
	
	/**
	 * Creates an Item instance from an ID.
	 * 
	 * @param id The ID of the Item to create an instance for.
	 * @return The created Item.
	 * @throws InvalidIndexException If the given ID is not an Item.
	 */
	public Item createItem(long id) throws InvalidIndexException {
		return items.createItem(id);
	}
	
	/**
	 * @throws InvalidIndexException If the given name does not exist.
	 * @see EntityFactory#createNpc(String, int)
	 */
	public NonPlayerCharacter createNpc(String name, int level) throws
	InvalidIndexException {
		return entities.createNpc(name, level);
	}
	
	/**
	 * @throws InvalidIndexException If the given name does not refer to an
	 * existing portal.
	 * @see PortalFactory#createPortal(String, String, java.awt.Point)
	 */
	public Portal createPortal(String name, String land, Point link) throws
	InvalidIndexException {
		return portals.createPortal(name, land, link);
	}
	
	/**
	 * @throws InvalidIndexException If the given id does not refer to an
	 * existing tile.
	 * @see TileFactory#createTile(int)
	 */
	public Tile createTile(int id) throws InvalidIndexException {
		return tiles.createTile(id);
	}
	
	/**
	 * Creates a void Tile instance. Equivalent to calling
	 * createTile(VOID_TILE).
	 * 
	 * @return An instance of a void Tile.
	 */
	public Tile createVoidTile() {
		Tile v = null;
		try {
			v = createTile(TileFactory.VOID_CHAR);
		} catch (InvalidIndexException e) {
			// should never happen
			throw new Error("VOID_CHAR is invalid", e);
		}
		return v;
	}
	
}
