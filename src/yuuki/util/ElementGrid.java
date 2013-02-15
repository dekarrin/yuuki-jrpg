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
	 * The element instances in this ElementGrid.
	 */
	private ArrayList<ArrayList<E>> items;
	
	/**
	 * The size of this ElementGrid, in number of element instances.
	 */
	private Dimension size;
	
	/**
	 * Creates a new ElementGrid of a specific size.
	 * 
	 * @param size The size of the new ElementGrid.
	 */
	public ElementGrid(Dimension size) {
		this.size = new Dimension(size.width, size.height);
		items = new ArrayList<ArrayList<E>>(size.width);
		for (int i = 0; i < size.width; i++) {
			ArrayList<E> list = new ArrayList<E>(size.height);
			for (int j = 0; j < size.height; j++) {
				list.add(null);
			}
			items.add(list);
		}
	}
	
	/**
	 * Creates a new ElementGrid from an existing element array.
	 * 
	 * @param size The size of the element grid.
	 * @param items The existing element array to create the ElementGrid from.
	 */
	public ElementGrid(Dimension size, E[] items) {
		this.size = new Dimension(size.width, size.height);
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
	public void clear() {
		Point p = new Point(0, 0);
		for (p.y = 0; p.y < size.height; p.y++) {
			for (p.x = 0; p.x < size.width; p.x++) {
				items.get(p.x).set(p.y, null);
			}
		}
	}
	
	@Override
	public boolean contains(Point point) {
		Rectangle box = new Rectangle(getSize());
		return box.contains(point);
	}
	
	@Override
	public Point getLocation() {
		return new Point(0, 0);
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
	
	@Override
	public void set(Point p, E e) {
		if (contains(p)) {
			items.get(p.x).set(p.y, e);
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Point p = new Point(0, 0);
		for (p.y = 0; p.y < size.height; p.y++) {
			for (p.x = 0; p.x < size.width; p.x++) {
				sb.append(itemAt(p).toString());
			}
			if (p.y < size.height - 1) {
				sb.append('\n');
			}
		}
		return sb.toString();
	}
	
}
