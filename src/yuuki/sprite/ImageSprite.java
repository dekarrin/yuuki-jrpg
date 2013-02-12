package yuuki.sprite;

import java.awt.Image;

import javax.swing.JComponent;

import yuuki.animation.engine.AnimationManager;
import yuuki.graphic.ImageComponent;

/**
 * A Sprite that consists solely of a background image.
 */
public class ImageSprite extends Sprite {

	/**
	 * Creates a new ImageSprite.
	 * 
	 * @param animator The AnimationManager to use for this Sprite.
	 * @param width The width of the new ImageSprite.
	 * @param height The height of the new ImageSprite.
	 */
	public ImageSprite(AnimationManager animator, int width, int height) {
		super(animator, width, height);
	}
	
	@Override
	protected JComponent createComponent() {
		return new ImageComponent();
	}
	
	/**
	 * Sets the displayed image of this ImageSprite.
	 * 
	 * @param image The image data to display.
	 */
	public void setImage(Image image) {
		((ImageComponent) component).setBackgroundImage(image);
	}
	
}
