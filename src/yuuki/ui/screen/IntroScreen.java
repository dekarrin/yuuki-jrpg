package yuuki.ui.screen;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

/**
 * The screen shown at the introduction. This is the starting screen.
 */
@SuppressWarnings("serial")
public class IntroScreen extends Screen<IntroScreenListener> implements
MouseListener {
	
	/**
	 * The listener assigned to each of this screen's buttons to see if the
	 * enter key is pressed while the button has focus.
	 */
	private KeyListener enterListener = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				fireButtonClicked(e.getComponent());
			}
		}
	};
	
	/**
	 * The button to quit the game with.
	 */
	private JButton exitButton;
	
	/**
	 * The button to load a new game with.
	 */
	private JButton loadGameButton;
	
	/**
	 * The button to start a new game with.
	 */
	private JButton newGameButton;
	
	/**
	 * The button to go into the options screen with.
	 */
	private JButton optionsButton;
	
	/**
	 * Creates a new IntroScreen. The child components are created and added to
	 * this screen.
	 * 
	 * @param width The width of the screen.
	 * @param height The height of the screen.
	 */
	public IntroScreen(int width, int height) {
		super(width, height);
		newGameButton = new JButton("New Game");
		loadGameButton = new JButton("Load Game");
		optionsButton = new JButton("Options");
		exitButton = new JButton("Exit");
		setButtonListeners();
		build();
	}
	
	/**
	 * Called when the mouse is clicked on a button.
	 * 
	 * @param e The event that triggered the event firing.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		Component c = e.getComponent();
		fireButtonClicked(c);
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
	 * Sets the initial focus of this screen to the new game button.
	 */
	@Override
	public void setInitialFocus() {
		newGameButton.requestFocus();
	}
	
	/**
	 * Adds the child components of this screen to itself.
	 */
	private void build() {
		setLayout(new OverlayLayout(this));
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
		buttonPanel.setOpaque(false);
		buttonPanel.add(new JLabel("Yuuki"));
		buttonPanel.add(newGameButton);
		buttonPanel.add(loadGameButton);
		buttonPanel.add(optionsButton);
		buttonPanel.add(exitButton);
		add(buttonPanel);
	}
	
	/**
	 * Fires the appropriate event depending on which button the user clicked.
	 * 
	 * @param button The button that the user clicked.
	 */
	private void fireButtonClicked(Component button) {
		if (button == newGameButton) {
			fireNewGameClicked();
		} else if (button == loadGameButton) {
			fireLoadGameClicked();
		} else if (button == optionsButton) {
			fireOptionsClicked();
		} else if (button == exitButton) {
			fireExitClicked();
		}
	}
	
	/**
	 * Calls the exitClicked() method on all listeners.
	 */
	private void fireExitClicked() {
		for (IntroScreenListener l: getElementListeners()) {
			l.exitClicked();
		}
	}
	
	/**
	 * Calls the loadGameClicked() method on all listeners.
	 */
	private void fireLoadGameClicked() {
		for (IntroScreenListener l: getElementListeners()) {
			l.loadGameClicked();
		}
	}
	
	/**
	 * Calls the newGameClicked() method on all listeners.
	 */
	private void fireNewGameClicked() {
		for (IntroScreenListener l: getElementListeners()) {
			l.newGameClicked();
		}
	}
	
	/**
	 * Calls the optionsClicked() method on all listeners.
	 */
	private void fireOptionsClicked() {
		for (IntroScreenListener l: getElementListeners()) {
			l.optionsClicked();
		}
	}
	
	/**
	 * Adds the appropriate listeners to child UI components.
	 */
	private void setButtonListeners() {
		newGameButton.addMouseListener(this);
		loadGameButton.addMouseListener(this);
		optionsButton.addMouseListener(this);
		exitButton.addMouseListener(this);
		newGameButton.addKeyListener(enterListener);
		loadGameButton.addKeyListener(enterListener);
		optionsButton.addKeyListener(enterListener);
		exitButton.addKeyListener(enterListener);
	}
	
}
