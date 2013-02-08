package yuuki.animation.engine;

import java.util.EventObject;

import yuuki.animation.Animation;

/**
 * Contains information on an animation event.
 */
public class AnimationEvent extends EventObject {
	
	/**
	 * Ensures serialization fails with incompatible versions.
	 */
	private static final long serialVersionUID = 8505559520525948621L;
	
	/**
	 * The object that caused the event to be fired.
	 */
	private Object cause;
	
	/**
	 * Creates a new AnimationEvent.
	 * 
	 * @param source The animatable that fired the event.
	 * @param cause The object that caused the event to be fired.
	 */
	public AnimationEvent(Animation source, Object cause) {
		super(source);
		this.cause = cause;
	}
	
	/**
	 * Gets the object that caused the event to be fired.
	 * 
	 * @return The cause.
	 */
	public Object getCause() {
		return cause;
	}
	
}
