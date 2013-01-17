package yuuki.sprite;

import java.awt.Color;
import java.awt.Graphics;
import yuuki.animation.engine.Animator;

/**
 * A sprite in a specific shape that has a background and a border.
 */
@SuppressWarnings("serial")
public abstract class Shape extends Sprite {

	/**
	 * The color of the border. Null is no border.
	 */
	private Color border;
	
	/**
	 * The color of the fill. Null is no fill.
	 */
	private Color fill;
	
	/**
	 * Creates a new Shape.
	 * 
	 * @param animator The animator of this shape.
	 * @param width The width of the shape's bounding box.
	 * @param height The height of the shape's bounding box.
	 */
	public Shape(Animator animator, int width, int height) {
		super(animator, width, height);
		border = null;
		fill = null;
	}
	
	/**
	 * Gets the fill of this shape.
	 * 
	 * @return The fill color.
	 */
	public Color getFillColor() {
		return fill;
	}
	
	/**
	 * Gets the border color of this shape.
	 * 
	 * @return The border color.
	 */
	public Color getBorderColor() {
		return border;
	}
	
	/**
	 * Sets the border color.
	 * 
	 * @param color The new border color. Set to null for no border.
	 */
	public void setBorderColor(Color color) {
		border = color;
		repaint();
	}
	
	/**
	 * Sets the fill color.
	 * 
	 * @param color The new fill color. Set to null for no fill.
	 */
	public void setFillColor(Color color) {
		fill = color;
		repaint();
	}
	
	/**
	 * Paints this shape on a graphical context.
	 * 
	 * @param g The graphical context to paint this shape on.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Color oldColor = g.getColor();
		drawFill(g, fill);
		drawBorder(g, border);
		g.setColor(oldColor);
	}
	
	/**
	 * Draws the fill of this shape.
	 * 
	 * @param g The context to paint the fill on.
	 * @param color The color to make the fill. Null indicates no fill.
	 */
	protected abstract void drawFill(Graphics g, Color color);
	
	/**
	 * Draws the border of this shape.
	 * 
	 * @param g The context to paint the border on.
	 * @param color The color to make the border. Null indicates no border.
	 */
	protected abstract void drawBorder(Graphics g, Color color);
	
}
