package yuuki.world;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Holds a series of Tile instances at a specific set of coordinates. Every
 * TileGrid is rectangular.
 */
public class TileGrid implements Grid<Tile> {
	
	/**
	 * The height of this TileGrid, in number of Tile instances.
	 */
	private int height;
	
	/**
	 * The Tile instances in this TileGrid.
	 */
	private Tile[][] tiles;
	
	/**
	 * The width of this TileGrid, in number of Tile instances.
	 */
	private int width;
	
	/**
	 * Creates a new TileGrid and initializes new Tiles with the given names.
	 * 
	 * @param w The width of the new TileGrid.
	 * @param h The height of the new TileGrid.
	 * @param names The names of the Tile instances.
	 */
	public TileGrid(int w, int h, String[] names) {
		this.width = w;
		this.height = h;
		tiles = new Tile[width][height];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				tiles[j][i] = new Tile(names[(width * i) + j], true);
			}
		}
	}
	
	/**
	 * Creates a new TileGrid from an existing Tile array.
	 * 
	 * @param w The width of the new TileGrid.
	 * @param h The height of the new TileGrid.
	 * @param tiles The existing Tile array to create the TileGrid from.
	 */
	public TileGrid(int w, int h, Tile[] tiles) {
		this.width = w;
		this.height = h;
		this.tiles = new Tile[w][h];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				this.tiles[j][i] = tiles[(width * i) + j];
			}
		}
	}
	
	/**
	 * Creates a new TileGrid an initializes its Tile array from an existing
	 * Tile array. Each Tile reference in the existing array is copied; the
	 * array itself is not copied.
	 * 
	 * @param tiles The existing tiles to use for this TileGrid's array.
	 */
	public TileGrid(Tile[][] tiles) {
		this.width = tiles.length;
		this.height = tiles[0].length;
		this.tiles = new Tile[width][height];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				this.tiles[j][i] = tiles[j][i];
			}
		}
	}
	
	/**
	 * Creates a new TileGrid without setting any properties.
	 */
	protected TileGrid() {}
	
	@Override
	public boolean contains(Point point) {
		Rectangle box = new Rectangle(getSize());
		return box.contains(point);
	}
	
	@Override
	public Dimension getSize() {
		return new Dimension(width, height);
	}
	
	@Override
	public Grid<Tile> getSubGrid(Rectangle boundingBox) {
		return new TileSubGrid(this, boundingBox);
	}
	
	@Override
	public Tile itemAt(Point point) {
		return tiles[point.x][point.y];
	}
	
}
