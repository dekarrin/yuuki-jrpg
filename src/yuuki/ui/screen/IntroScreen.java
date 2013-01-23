package yuuki.ui.screen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;

import yuuki.sound.SoundEngine;
import yuuki.ui.EffectButton;

/**
 * The screen shown at the introduction. This is the starting screen.
 */
@SuppressWarnings("serial")
public class IntroScreen extends Screen<IntroScreenListener> implements
MouseListener {
	
	/**
	 * The amount of vertical space between each button.
	 */
	private static final int BUTTON_V_GAP = 15;
	
	/**
	 * The amount of space above the buttons.
	 */
	private static final int TOP_MARGIN = 75;
	
	/**
	 * The amount of space to the right of the buttons.
	 */
	private static final int RIGHT_MARGIN = 125;
	
	/**
	 * The color that buttons change to when hovered over.
	 */
	public static final Color EFFECT_COLOR = new Color(222, 127, 0);
	
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
	 * @param sound The game sound engine.
	 */
	public IntroScreen(int width, int height, SoundEngine sound) {
		super(width, height);
		newGameButton = new EffectButton("New Game", EFFECT_COLOR, sound);
		loadGameButton = new EffectButton("Load Game", EFFECT_COLOR, sound);
		optionsButton = new EffectButton("Options", EFFECT_COLOR, sound);
		exitButton = new EffectButton("Exit", EFFECT_COLOR, sound);
		newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		loadGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		optionsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
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
		setLayout(new BorderLayout());
		Box buttonsHorz = Box.createHorizontalBox();
		Box buttonsVert = Box.createVerticalBox();
		JLabel nameLabel = new JLabel("Yuuki");
		nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonsVert.add(Box.createVerticalStrut(TOP_MARGIN));
		buttonsVert.add(nameLabel);
		buttonsVert.add(Box.createVerticalStrut(BUTTON_V_GAP));
		buttonsVert.add(newGameButton);
		buttonsVert.add(Box.createVerticalStrut(BUTTON_V_GAP));
		buttonsVert.add(loadGameButton);
		buttonsVert.add(Box.createVerticalStrut(BUTTON_V_GAP));
		buttonsVert.add(optionsButton);
		buttonsVert.add(Box.createVerticalStrut(BUTTON_V_GAP));
		buttonsVert.add(exitButton);
		buttonsVert.setAlignmentY(Component.TOP_ALIGNMENT);
		buttonsHorz.add(buttonsVert);
		buttonsHorz.add(Box.createHorizontalStrut(RIGHT_MARGIN));
		add(buttonsHorz, BorderLayout.EAST);
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
