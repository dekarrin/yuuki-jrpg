package yuuki.ui.screen;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

@SuppressWarnings("serial")
public class IntroScreen extends Screen implements MouseListener {
	
	private KeyListener enterListener = new KeyListener() {

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				fireButtonClicked(e.getComponent());
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {}

		@Override
		public void keyTyped(KeyEvent e) {}
	};
	
	private ArrayList<IntroScreenListener> listeners;
	
	private JButton newGameButton;
	
	private JButton loadGameButton;
	
	private JButton optionsButton;
	
	private JButton exitButton;

	public IntroScreen(int width, int height) {
		super(width, height);
		listeners = new ArrayList<IntroScreenListener>();
		newGameButton = new JButton("New Game");
		loadGameButton = new JButton("Load Game");
		optionsButton = new JButton("Options");
		exitButton = new JButton("Exit");
		setButtonListeners();
		build();
	}
	
	public void setInitialFocus() {
		newGameButton.requestFocus();
	}
	
	public void addListener(IntroScreenListener l) {
		listeners.add(l);
	}
	
	public void mouseClicked(MouseEvent e) {
		Component c = e.getComponent();
		fireButtonClicked(c);
	}
	
	public void mouseReleased(MouseEvent e) {}
	
	public void mousePressed(MouseEvent e) {}
	
	public void mouseExited(MouseEvent e) {}
	
	public void mouseEntered(MouseEvent e) {}
	
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
	
	private void fireNewGameClicked() {
		for (IntroScreenListener l: listeners) {
			l.newGameClicked();
		}
	}
	
	private void fireLoadGameClicked() {
		for (IntroScreenListener l: listeners) {
			l.loadGameClicked();
		}
	}
	
	private void fireOptionsClicked() {
		for (IntroScreenListener l: listeners) {
			l.optionsClicked();
		}
	}
	
	private void fireExitClicked() {
		for (IntroScreenListener l: listeners) {
			l.exitClicked();
		}
	}
	
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
}
