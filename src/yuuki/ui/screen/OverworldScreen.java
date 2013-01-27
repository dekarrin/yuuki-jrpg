package yuuki.ui.screen;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;

import yuuki.ui.WorldViewer;
import yuuki.world.TileGrid;

/**
 * The screen displayed when at the overworld.
 */
@SuppressWarnings("serial")
public class OverworldScreen extends Screen<OverworldScreenListener> implements
MouseListener {
	
	/**
	 * The height of the world viewer, in tiles.
	 */
	public static final int VIEWER_HEIGHT = 10;
	
	/**
	 * The width of the world viewer, in tiles.
	 */
	public static final int VIEWER_WIDTH = 10;
	
	/**
	 * The button that advances to the battle screen.
	 */
	private JButton startButton;
	
	/**
	 * Displays the world.
	 */
	private WorldViewer worldViewer;
	
	/**
	 * Creates a new OverworldScreen. The child components are created and
	 * added to the screen.
	 * 
	 * @param width The width of the screen.
	 * @param height The height of the screen.
	 */
	public OverworldScreen(int width, int height) {
		super(width, height);
		KeyListener enterListener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					fireStartClicked();
				}
			}
		};
		setLayout(new FlowLayout());
		Box vertBox = Box.createVerticalBox();
		Box fieldBox = Box.createHorizontalBox();
		worldViewer = new WorldViewer(VIEWER_WIDTH, VIEWER_HEIGHT);
		startButton = new JButton("Start");
		startButton.addMouseListener(this);
		startButton.addKeyListener(enterListener);
		fieldBox.add(new JLabel("The overworld!"));
		fieldBox.add(Box.createHorizontalStrut(2));
		fieldBox.add(new JLabel("Hit the button to start a battle ==>"));
		fieldBox.add(Box.createHorizontalStrut(2));
		fieldBox.add(startButton);
		vertBox.add(worldViewer);
		vertBox.add(fieldBox);
		add(vertBox);
	}
	
	/**
	 * Fires the start clicked event on all registered listeners. This method
	 * is called when the start button is clicked.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		Component c = e.getComponent();
		if (c == startButton) {
			fireStartClicked();
		}
	}
	
	/**
	 * Not used.
	 */
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	/**
	 * Not used.
	 */
	@Override
	public void mouseExited(MouseEvent e) {}
	
	/**
	 * Not used.
	 */
	@Override
	public void mousePressed(MouseEvent e) {}
	
	/**
	 * Not used.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {}
	
	/**
	 * Sets the initial focus of this screen to the start button.
	 */
	@Override
	public void setInitialProperties() {
		startButton.requestFocus();
	}
	
	/**
	 * Changes the world viewer's view of the world.
	 * 
	 * @param view The TileGrid with the view to show.
	 */
	public void setWorldView(TileGrid view) {
		worldViewer.setView(view);
	}
	
	/**
	 * Updates the world view to show a new center.
	 * 
	 * @param center The coordinates of the new center to show.
	 */
	public void updateWorldView(Point center) {
		worldViewer.updateDisplay(center);
	}
	
	/**
	 * Calls the startBattleClicked() method on all listeners.
	 */
	private void fireStartClicked() {
		for (OverworldScreenListener l: getElementListeners()) {
			l.startBattleClicked();
		}
	}
	
}
