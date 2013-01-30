package yuuki.util;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import java.util.ArrayList;

/**
 * Holds a series of object instances at a specific set of coordinates. Every
 * ElementGrid is rectangular.
 * 
 * @param <E> The types of the elements in the ElementGrid.
 */
public class ElementGrid<E> implements Grid<E> {
	
	/**
	 * The size of this ElementGrid, in number of element instances.
	 */
	private Dimension size;
	
	/**
	 * The element instances in this ElementGrid.
	 */
	private ArrayList<ArrayList<E>> items;
	
	/**
	 * Creates a new ElementGrid from an existing element array.
	 * 
	 * @param size The size of the element grid.
	 * @param items The existing element array to create the ElementGrid from.
	 */
	public ElementGrid(Dimension size, E[] items) {
		this.size = size;
		this.items = new ArrayList<ArrayList<E>>();
		for (int i = 0; i < size.height; i++) {
			for (int j = 0; j < size.width; j++) {
				if (i == 0) {
					this.items.add(new ArrayList<E>());
				}
				this.items.get(j).add(items[(size.width * i) + j]);
			}
		}
	}
	
	@Override
	public boolean contains(Point point) {
		Rectangle box = new Rectangle(getSize());
		return box.contains(point);
	}
	
	@Override
	public Dimension getSize() {
		return size;
	}
	
	@Override
	public Grid<E> getSubGrid(Rectangle boundingBox) {
		return new SubGrid<E>(this, boundingBox);
	}
	
	@Override
	public E itemAt(Point point) {
		return items.get(point.x).get(point.y);
	}
	
}
