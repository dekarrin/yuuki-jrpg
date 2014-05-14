package yuuki.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/**
 * Lays out components in a grid whose number of columns and number of rows is
 * automatically determined by the sizes of its elements, the gap between them,
 * and the size of the container that layout is being performed for. The number
 * of rows and number of columns is dynamic. They will grow as needed to fit
 * all child components in the grid.
 * 
 * The grid created by AutoGridLayout is fixed-width. The width of the grid is
 * consistent to the value given at construction; changing the size of the
 * component that layout is being performed for will not change the size of the
 * grid.
 * 
 * Minimum spacing can be set such that all elements must be at least a certain
 * distance from each other. The spacing between each element is stretched so
 * that they are uniformly distributed across the width of the grid.
 * 
 * AutoGridLayout uses the same cell size for every component; this must be set
 * at construction. All cells are square.
 */
public class AutoGridLayout implements LayoutManager {
	
	/**
	 * The length of one side of a cell.
	 */
	private int cellSize;
	
	/**
	 * The minimum amount of spacing between each element.
	 */
	private int minGap;
	
	/**
	 * The width of the grid.
	 */
	private int width;
	
	/**
	 * Creates a new AutoGridLayout with a minimum gap of 0.
	 * 
	 * @param width The width of the grid.
	 * @param cellSize The length of one side of a cell.
	 */
	public AutoGridLayout(int width, int cellSize) {
		this(width, cellSize, 0);
	}
	
	/**
	 * Creates a new AutoGridLayout.
	 * 
	 * @param width The width of the grid.
	 * @param cellSize The length of one side of a cell.
	 * @param minGap The minimum amount of space between each child of the
	 * component that layout is being performed for.
	 */
	public AutoGridLayout(int width, int cellSize, int minGap) {
		this.width = width;
		this.cellSize = cellSize;
		this.minGap = minGap;
	}
	
	/**
	 * Not used by this class.
	 */
	@Override
	public void addLayoutComponent(String name, Component comp) {}
	
	/**
	 * Gets the length of one side of a grid cell.
	 * 
	 * @return The size of a cell.
	 */
	public int getCellSize() {
		return cellSize;
	}
	
	/**
	 * Gets the width of the grid created during layout.
	 * 
	 * @return The width of the grid.
	 */
	public int getGridWidth() {
		return width;
	}
	
	/**
	 * Gets the minimum amount of space between each child component.
	 * 
	 * @return The minimum amount of gap between each child component.
	 */
	public int getMinGap() {
		return minGap;
	}
	
	@Override
	public void layoutContainer(Container parent) {
		synchronized(parent.getTreeLock()) {
			int count = parent.getComponentCount();
			for (int i = 0; i < count; i++) {
				setCellBounds(parent, i);
			}
		}
	}
	
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return preferredLayoutSize(parent);
	}
	
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		int actualCellHeight = cellSize + cellSpacing();
		int height = cellSpacing() + getRowCount(parent) * actualCellHeight;
		return new Dimension(width, height);
	}
	
	/**
	 * Not used by this class.
	 */
	@Override
	public void removeLayoutComponent(Component comp) {}
	
	/**
	 * Sets the length of one side of a grid cell.
	 * 
	 * @param cellSize The size of a cell.
	 */
	public void setCellSize(int cellSize) {
		this.cellSize = cellSize;
	}
	
	/**
	 * Sets the width of the grid created during layout.
	 * 
	 * @param width The width of the grid.
	 */
	public void setGridWidth(int width) {
		this.width = width;
	}
	
	/**
	 * Sets the minimum amount of space between each child component.
	 * 
	 * @param minGap The minimum amount of gap between each child component.
	 */
	public void setMinGap(int minGap) {
		this.minGap = minGap;
	}
	
	/**
	 * Calculates the spacing between each item cell. The spacing will always
	 * be at least the amount specified by minGap, but if there is enough room, spacing will stretch so that the item
	 * cells are uniformly distributed about the width of this container.
	 * 
	 * @return The calculated padding for an item cell.
	 */
	private int cellSpacing() {
		int cols = getColCount();
		int colsWidth = (cellSize + minGap) * cols;
		int usedWidth = colsWidth + minGap;
		int extraWidth = width - usedWidth;
		int extraSpace = extraWidth / (cols + 1);
		int totalSpace = minGap + extraSpace;
		return totalSpace;
	}
	
	/**
	 * Gets the number of columns that fit in a row.
	 * 
	 * @return The number of cells.
	 */
	private int getColCount() {
		int useableWidth = width - minGap;
		int colCount = useableWidth / (cellSize + minGap);
		return colCount;
	}
	
	/**
	 * Gets the number of rows.
	 * 
	 * @param parent The component layout is being performed for.
	 * @return The number of cells.
	 */
	private int getRowCount(Container parent) {
		return (parent.getComponentCount() - 1) / getColCount() + 1;
	}
	
	/**
	 * Sets the bounds of a cell.
	 * 
	 * @param parent The container that layout is being performed for.
	 * @param i The index of the cell within the parent container.
	 */
	private void setCellBounds(Container parent, int i) {
		Component comp = parent.getComponent(i);
		int relX = i % getColCount();
		int relY = i / getColCount();
		int x = cellSpacing() + relX * (cellSize + cellSpacing());
		int y = cellSpacing() + relY * (cellSize + cellSpacing());
		comp.setBounds(x, y, cellSize, cellSize);
	}
	
}
