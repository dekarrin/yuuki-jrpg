package yuuki.ui;

/**
 * Informed when a mod is enabled or disabled.
 */
public interface ModControlListener {
	
	/**
	 * Called when a mod is disabled.
	 * 
	 * @param id The identifier for the mod that was disabled.
	 */
	public void modDisabled(String id);
	
	/**
	 * Called when a mod is enabled.
	 * 
	 * @param id The identifier for the mod that was enabled.
	 */
	public void modEnabled(String id);
	
}
