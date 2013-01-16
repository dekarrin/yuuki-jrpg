package yuuki.sprite;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;

import yuuki.animation.engine.Animatable;
import yuuki.animation.engine.AnimationOwner;
import yuuki.animation.engine.Animator;

/**
 * A graphical object that can be animated. A Sprite may have other Animatable
 * instances contained within it, which are advanced every time the owner
 * Sprite is.
 */
@SuppressWarnings("serial")
public abstract class Sprite extends JPanel implements Animatable,
AnimationOwner {
	
	/**
	 * Whether this Sprite has an animation controller.
	 */
	private boolean controlled;
	
	/**
	 * The Animatable instances that are child components of this Sprite.
	 */
	private ArrayList<Animatable> ownedAnims;
	
	/**
	 * The X-coordinate of this Sprite.
	 */
	private int x;
	
	/**
	 * The Y-coordinate of this Sprite.
	 */
	private int y;
	
	/**
	 * The animation engine that is driving the animation of this Sprite.
	 */
	protected Animator animator;
	
	/**
	 * Allocates a new Sprite.
	 * 
	 * @param width The width of the Sprite
	 * @param height The height of the Sprite.
	 * @param animator The animation engine that will drive this sprite.
	 */
	public Sprite(int width, int height, Animator animator) {
		x = 0;
		y = 0;
		this.animator = animator;
		setSize(width, height);
		controlled = false;
		ownedAnims = new ArrayList<Animatable>();
		setLayout(null);
		updateBounds();
	}
	
	/**
	 * Also adds the component to the list of anims.
	 */
	public Component add(Sprite c) {
		addAnim(c);
		return super.add(c);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addAnim(Animatable a) {
		if (a.isControlled()) {
			String error = "Animatable is already controlled!";
			throw new IllegalArgumentException(error);
		}
		a.setControlled(true);
		ownedAnims.add(a);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void advanceFrame(int fps) {
		for (Animatable a: ownedAnims) {
			a.advanceFrame(fps);
		}
		advance(fps);
		repaint();
	}
	
	/**
	 * Returns this Sprite's maximum size. This will be the same as its
	 * preferred size.
	 * 
	 * @return A Dimension with this Sprite's size.
	 */
	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
	
	/**
	 * Returns this Sprite's minimum size. This will be the same as its
	 * preferred size.
	 * 
	 * @return A Dimension with this Sprite's size.
	 */
	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}
	
	/**
	 * Returns this Sprite's preferred size.
	 * 
	 * @return A Dimension with this Sprite's size.
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(getWidth(), getHeight());
	}
	
	/**
	 * Gets the x-coordinate of this sprite.
	 * 
	 * @return The x-coordinate of the location of this sprite.
	 */
	@Override
	public int getX() {
		return x;
	}
	
	/**
	 * Gets the y-coordinate of this sprite.
	 * 
	 * @return The y-coordinate of the location of this sprite.
	 */
	@Override
	public int getY() {
		return y;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isControlled() {
		return controlled;
	}
	
	/**
	 * Moves this sprite by a specific amount.
	 * 
	 * @param dx The amount to move it along the x-axis.
	 * @param dy The amount to move it along the y-axis.
	 */
	@Override
	public void move(int dx, int dy) {
		this.x += dx;
		this.y += dy;
		updateBounds();
	}
	
	/**
	 * Moves this sprite to a specific point.
	 * 
	 * @param x The x-coordinate of the point to move it to.
	 * @param y The y-coordinate of the point to move it to.
	 */
	public void moveTo(int x, int y) {
		this.x = x;
		this.y = y;
		updateBounds();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAnim(Animatable a) {
		if (ownedAnims.remove(a)) {
			a.setControlled(false);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setControlled(boolean controlled) {
		this.controlled = controlled;
	}
	
	/**
	 * Sets the height of this sprite.
	 * 
	 * @param height The new height.
	 */
	public void setHeight(int height) {
		setSize(getWidth(), height);
		updateBounds();
	}
	
	/**
	 * Sets the width of this sprite.
	 * 
	 * @param width The new width.
	 */
	public void setWidth(int width) {
		setSize(width, getHeight());
		updateBounds();
	}
	
	/**
	 * Sets the x-coordinate of this sprite.
	 * 
	 * @param x The new x-coordinate.
	 */
	public void setX(int x) {
		this.x = x;
		updateBounds();
	}
	
	/**
	 * Sets the y-coordinate of this sprite.
	 * 
	 * @param y The new y-coordinate.
	 */
	public void setY(int y) {
		this.y = y;
		updateBounds();
	}
	
	/**
	 * Updates the bounds of this sprite and repaints.
	 */
	private void updateBounds() {
		setBounds(x, y, getWidth(), getHeight());
		repaint();
	}
	
	/**
	 * Advances the animation by one frame.
	 * 
	 * @param fps The speed that animation is occurring at.
	 */
	protected abstract void advance(int fps);
	
}
