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
	public Dimension getSize() {
		return new Dimension(width, height);
	}
	
	@Override
	public TileGrid getSubGrid(Rectangle boundingBox) {
		int subX = getBounded(boundingBox.x, 0, width - 1);
		int subY = getBounded(boundingBox.y, 0, height - 1);
		int subWidth = getDependentBounded(boundingBox.width, boundingBox.x,
				subX);
		int subHeight = getDependentBounded(boundingBox.height, boundingBox.y,
				subY);
		Tile[][] subTiles = new Tile[subWidth][subHeight];
		for (int i = 0; i < subWidth; i++) {
			for (int j = 0; j < subHeight; j++) {
				subTiles[i][j] = tiles[subX + i][subY + j];
			}
		}
		TileGrid subGrid = new TileGrid();
		subGrid.tiles = subTiles;
		subGrid.width = subTiles.length;
		subGrid.height = subTiles[0].length;
		return subGrid;
	}
	
	@Override
	public Tile itemAt(Point point) {
		return tiles[point.x][point.y];
	}
	
	/**
	 * Gets the bounded version of a number.
	 * 
	 * @param x The original number.
	 * @param low The lower inclusive bound.
	 * @param high The upper inclusive bound.
	 * 
	 * @return A bounded version of x that is in the range [low, high].
	 */
	private int getBounded(int x, int low, int high) {
		x = Math.max(x, low);
		x = Math.min(x, high);
		return x;
	}
	
	/**
	 * Gets the value of a some integer that changes based on whether or not
	 * bounded some other integer changes its value.
	 * 
	 * @param x The value that depends on the bounded variable.
	 * @param oldX The value of the other property when unbounded.
	 * @param newX The value of the other property when bounded.
	 * 
	 * @return The original x minus the absolute value of the difference
	 * between the bounded and the unbounded property.
	 */
	private int getDependentBounded(int x, int unbounded, int bounded) {
		return x - Math.abs(bounded - unbounded);
	}
	
}
