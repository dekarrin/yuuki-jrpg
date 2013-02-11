package yuuki.sprite;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;

import yuuki.animation.engine.Animatable;
import yuuki.animation.engine.AnimationListener;
import yuuki.animation.engine.AnimationManager;
import yuuki.animation.engine.AnimationOwner;

/**
 * A graphical object that can be animated. A Sprite may have other Animatable
 * instances contained within it, which are advanced every time the owner
 * Sprite is.
 * 
 * Sprite instances do not fire animation events.
 */
public class Sprite implements Animatable, AnimationOwner {
	
	/**
	 * Whether this Sprite's advancement has started.
	 */
	private boolean advancing = false;
	
	/**
	 * Whether this Sprite has an animation controller.
	 */
	private boolean controlled;
	
	/**
	 * The height of this Sprite.
	 */
	private int height;
	
	/**
	 * Listeners for AnimationEvents.
	 */
	private Set<AnimationListener> listeners;
	
	/**
	 * The Animatable instances that are child components of this Sprite.
	 */
	private ArrayList<Animatable> ownedAnims;
	
	/**
	 * Whether this Sprite has been paused.
	 */
	private boolean paused = false;
	
	/**
	 * The width of this Sprite.
	 */
	private int width;
	
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
	protected AnimationManager animator;
	
	/**
	 * The graphical component that this Sprite controls.
	 */
	protected JComponent component;
	
	/**
	 * Allocates a new Sprite.
	 * 
	 * @param animator The animation engine that will drive this sprite.
	 * @param width The width of the Sprite
	 * @param height The height of the Sprite.
	 */
	public Sprite(AnimationManager animator, int width, int height) {
		x = 0;
		y = 0;
		this.animator = animator;
		this.width = width;
		this.height = height;
		controlled = false;
		ownedAnims = new ArrayList<Animatable>();
		setupComponent();
		updateBounds();
	}
	
	/**
	 * Adds a non-Sprite component to this Sprite. The component has its bounds
	 * set and is then added to this Sprite.
	 * 
	 * @param c The component to add.
	 */
	public Component add(Component c) {
		return add(c, true);
	}
	
	/**
	 * Adds a Sprite to this Sprite. The added Sprite's component is added to
	 * this Sprite's component and the added Sprite itself is added to the list
	 * of anims. The added Sprite is then considered owned by this Sprite.
	 * 
	 * @param s The Sprite to add.
	 */
	public Component add(Sprite s) {
		addAnim(s);
		return add(s.getComponent(), false);
	}
	
	@Override
	public void addAnim(Animatable a) {
		if (a.isControlled()) {
			String error = "Animatable is already controlled!";
			throw new IllegalArgumentException(error);
		}
		a.setControlled(true);
		ownedAnims.add(a);
	}
	
	@Override
	public void addAnimationListener(AnimationListener l) {
		listeners.add(l);
	}
	
	@Override
	public void advanceFrame(int fps) {
		if (advancing && !paused) {
			for (Animatable a : ownedAnims) {
				a.advanceFrame(fps);
			}
			advance(fps);
			component.repaint();
		}
	}
	
	/**
	 * Has no effect, as sprites do not have a final position.
	 */
	@Override
	public void finish() {}
	
	/**
	 * Gets this Sprite's component.
	 * 
	 * @return The component.
	 */
	public JComponent getComponent() {
		return component;
	}
	
	/**
	 * Gets the height of this Sprite.
	 * 
	 * @return The height of this Sprite.
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Gets the width of this Sprite.
	 * 
	 * @return The width of this Sprite.
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Gets the x-coordinate of this Sprite.
	 * 
	 * @return The x-coordinate of the location of this sprite.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Gets the y-coordinate of this Sprite.
	 * 
	 * @return The y-coordinate of the location of this sprite.
	 */
	public int getY() {
		return y;
	}
	
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
	
	@Override
	public void pause() {
		paused = true;
	}
	
	@Override
	public void removeAnim(Animatable a) {
		if (ownedAnims.remove(a)) {
			a.setControlled(false);
		}
	}
	
	@Override
	public void removeAnimationListener(Object l) {
		listeners.remove(l);
	}
	
	/**
	 * Has no effect, as sprites do not have an initial position.
	 */
	@Override
	public void reset() {}
	
	/**
	 * Changes the size of this Sprite by a specific amount.
	 * 
	 * @param dw The amount to resize the width by.
	 * @param dh The amount to resize the height by.
	 */
	public void resize(int dw, int dh) {
		this.width += dw;
		this.height += dh;
		updateBounds();
	}
	
	@Override
	public void resume() {
		paused = false;
	}
	
	/**
	 * Sets the component that this Sprite controls.
	 * 
	 * @param c The new component.
	 */
	public void setComponent(JComponent c) {
		component = c;
		setComponentProperties();
	}
	
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
		this.height = height;
		updateBounds();
	}
	
	/**
	 * Sets the size of this Sprite.
	 * 
	 * @param width The new width of this Sprite.
	 * @param height The new height of this Sprite.
	 */
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		updateBounds();
	}
	
	/**
	 * Sets the width of this sprite.
	 * 
	 * @param width The new width.
	 */
	public void setWidth(int width) {
		this.width = width;
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
	
	@Override
	public void start() {
		advancing = true;
	}
	
	@Override
	public void stop() {
		advancing = false;
	}
	
	/**
	 * Adds a non-Sprite component to this Sprite.
	 * 
	 * @param setBounds Whether the bounds of the component should be set.
	 * @param c The component to add.
	 */
	private Component add(Component c, boolean setBounds) {
		if (setBounds) {
			setDefaultComponentBounds(c);
		}
		return component.add(c);
	}
	
	/**
	 * Sets the properties of the component.
	 */
	private void setComponentProperties() {
		Dimension size = new Dimension(width, height);
		component.setSize(size);
		component.setPreferredSize(size);
		component.setMinimumSize(size);
		component.setMaximumSize(size);
		component.setOpaque(false);
	}
	
	/**
	 * Sets the properties of this Sprite's component.
	 */
	private void setupComponent() {
		component = createComponent();
		setComponentProperties();
		component.setLayout(null);
	}
	
	/**
	 * Advances the animation by one frame.
	 * 
	 * @param fps The speed that animation is occurring at.
	 */
	protected void advance(int fps) {}
	
	/**
	 * Creates the component. This should be overridden by subclasses that wish
	 * to have their component do custom painting.
	 * 
	 * @return The component.
	 */
	protected JComponent createComponent() {
		return new JPanel();
	}
	
	/**
	 * Sets a component dimensions from its preferred dimensions and its
	 * location from its current location.
	 * 
	 * @param c The component to set.
	 */
	protected void setDefaultComponentBounds(Component c) {
		Dimension d = c.getPreferredSize();
		Point p = c.getLocation();
		c.setBounds(p.x, p.y, d.width, d.height);
	}
	
	/**
	 * Updates the bounds of this sprite and repaints.
	 */
	protected void updateBounds() {
		component.setBounds(x, y, width, height);
	}
	
}
