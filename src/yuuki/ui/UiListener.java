package yuuki.ui;

/**
 * Allows a class to observe changes in the UI.
 * 
 * @author TF Nelson
 */

public interface UiListener {
	public void onNewGameRequested();
	public void onLoadGameRequested();
	public void onOptionsScreenRequested();
	public void onQuitRequested();
	public void onCreateCharacter(String name, int level);
}
