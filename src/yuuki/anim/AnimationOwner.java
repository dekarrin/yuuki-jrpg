package yuuki.anim;

/**
 * Owns Animatable instances.
 */
public interface AnimationOwner {
	
	/**
	 * Adds an Animatable to the list of animated objects.
	 * 
	 * @param a The animatable to add.
	 * 
	 * @throws IllegalArgumentException If the Animatable already has an owner.
	 */
	public void addAnim(Animatable a);
	
	/**
	 * Removes an Animatable from the list of animated objects.
	 * 
	 * @param a The animatable to remove.
	 */
	public void removeAnim(Animatable a);
	
}
