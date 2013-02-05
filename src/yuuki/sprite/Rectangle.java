package yuuki.sprite;

import java.awt.Color;
import java.awt.Graphics;

import yuuki.animation.engine.Animator;

/**
 * A sprite in the shape of a rectangle.
 */
public class Rectangle extends Shape {
	
	/**
	 * The custom JPanel for this Sprite.
	 */
	@SuppressWarnings("serial")
	protected static class RectangleComponent extends ShapeComponent {
		
		@Override
		protected void drawBorder(Graphics g, Color color) {
			if (color != null) {
				g.setColor(color);
				g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
			}
		}
		
		@Override
		protected void drawFill(Graphics g, Color color) {
			if (color != null) {
				g.setColor(color);
				g.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
			}
		}
		
	}
	
	/**
	 * Allocates a new Rectangle.
	 * 
	 * @param animator The Animator that handles this sprite's animation.
	 * @param width The width of the rectangle.
	 * @param height The height of the rectangle.
	 */
	public Rectangle(Animator animator, int width, int height) {
		super(animator, width, height);
	}
	
	/**
	 * Creates the shape for this Sprite.
	 * 
	 * @return The ShapeComponent.
	 */
	@Override
	protected ShapeComponent createShapeComponent() {
		return new RectangleComponent();
	}
	
}
