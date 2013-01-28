package yuuki.ui.screen;

import java.awt.Component;
import java.awt.Dimension;
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
	 * The button that moves the character north.
	 */
	private JButton moveNorthButton;
	
	/**
	 * The button that moves the character west.
	 */
	private JButton moveWestButton;
	
	/**
	 * The button that moves the character east.
	 */
	private JButton moveEastButton;
	
	/**
	 * The button that moves the character south.
	 */
	private JButton moveSouthButton;
	
	/**
	 * The button that moves the character north-east.
	 */
	private JButton moveNorthEastButton;
	
	/**
	 * The button that moves the character north-west.
	 */
	private JButton moveNorthWestButton;
	
	/**
	 * The button that moves the character south-east.
	 */
	private JButton moveSouthEastButton;
	
	/**
	 * The button that moves the character south-west.
	 */
	private JButton moveSouthWestButton;
	
	/**
	 * The button that makes the character pass one turn.
	 */
	private JButton moveNullButton;
	
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
		worldViewer = new WorldViewer(VIEWER_WIDTH, VIEWER_HEIGHT);
		startButton = new JButton("Start");
		startButton.addMouseListener(this);
		startButton.addKeyListener(enterListener);
		createMovementButtons();
		addElements();
	}
	
	/**
	 * Constructs the movement buttons.
	 */
	private void createMovementButtons() {
		moveNorthButton = new JButton("\u2191"); // arrow char
		moveWestButton = new JButton("\u2190"); // arrow char
		moveEastButton = new JButton("\u2192"); // arrow char
		moveSouthButton = new JButton("\u2193"); // arrow char
		moveNorthEastButton = new JButton("\u2197"); // arrow char
		moveNorthWestButton = new JButton("\u2196"); // arrow char
		moveSouthEastButton = new JButton("\u2198"); // arrow char
		moveSouthWestButton = new JButton("\u2199"); // arrow char
		moveNullButton = new JButton("\u25CF"); // dot char
	}
	
	/**
	 * Creates the box containing the movement buttons.
	 * 
	 * @return The box containing the movement buttons.
	 */
	private Box createMovementBox() {
		Dimension d = new Dimension(170, 100);
		Box moveBox = Box.createVerticalBox();
		moveBox.setPreferredSize(d);
		moveBox.setMaximumSize(d);
		Box row1 = Box.createHorizontalBox();
		Box row2 = Box.createHorizontalBox();
		Box row3 = Box.createHorizontalBox();
		row1.add(moveNorthWestButton);
		row1.add(Box.createHorizontalGlue());
		row1.add(moveNorthButton);
		row1.add(Box.createHorizontalGlue());
		row1.add(moveNorthEastButton);
		row2.add(moveWestButton);
		row2.add(Box.createHorizontalGlue());
		row2.add(moveNullButton);
		row2.add(Box.createHorizontalGlue());
		row2.add(moveEastButton);
		row3.add(moveSouthWestButton);
		row3.add(Box.createHorizontalGlue());
		row3.add(moveSouthButton);
		row3.add(Box.createHorizontalGlue());
		row3.add(moveSouthEastButton);
		moveBox.add(row1);
		moveBox.add(Box.createVerticalGlue());
		moveBox.add(row2);
		moveBox.add(Box.createVerticalGlue());
		moveBox.add(row3);
		return moveBox;
	}
	
	/**
	 * Creates element containers and adds them to this Screen.
	 */
	private void addElements() {
		Box moveBox = createMovementBox();
		Box vertBox = Box.createVerticalBox();
		Box fieldBox = Box.createHorizontalBox();
		fieldBox.add(new JLabel("The overworld!"));
		fieldBox.add(Box.createHorizontalStrut(2));
		fieldBox.add(new JLabel("Hit the button to start a battle ==>"));
		fieldBox.add(Box.createHorizontalStrut(2));
		fieldBox.add(startButton);
		vertBox.add(worldViewer);
		vertBox.add(fieldBox);
		vertBox.add(Box.createVerticalStrut(10));
		vertBox.add(moveBox);
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
