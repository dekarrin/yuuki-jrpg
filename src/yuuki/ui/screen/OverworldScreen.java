package yuuki.ui.screen;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * The screen displayed when at the overworld.
 */
@SuppressWarnings("serial")
public class OverworldScreen extends Screen<OverworldScreenListener> implements
MouseListener {
	
	/**
	 * The button that advances to the battle screen.
	 */
	private JButton startButton;
	
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
		startButton = new JButton("Start");
		startButton.addMouseListener(this);
		startButton.addKeyListener(enterListener);
		add(new JLabel("Welcome to the overworld!"));
		add(new JLabel("There's nothing here yet."));
		add(new JLabel("Hit the button to start a battle -->"));
		add(startButton);
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
	public void setInitialFocus() {
		startButton.requestFocus();
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
