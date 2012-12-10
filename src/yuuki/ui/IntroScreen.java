package yuuki.ui;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

public class IntroScreen extends JPanel implements MouseListener {
	
	private ArrayList<IntroScreenListener> listeners;
	
	private JButton newGameButton;
	
	private JButton loadGameButton;
	
	private JButton optionsButton;
	
	private JButton exitButton;

	public IntroScreen() {
		listeners = new ArrayList<IntroScreenListener>();
		newGameButton = new JButton("New Game");
		loadGameButton = new JButton("Load Game");
		optionsButton = new JButton("Options");
		exitButton = new JButton("Exit");
		setButtonListeners();
		build();
	}
	
	public void addListener(IntroScreenListener l) {
		listeners.add(l);
	}
	
	public void mouseClicked(MouseEvent e) {
		Component c = e.getComponent();
		if (c == newGameButton) {
			fireNewGameClicked();
		} else if (c == loadGameButton) {
			fireLoadGameClicked();
		} else if (c == optionsButton) {
			fireOptionsClicked();
		} else if (c == exitButton) {
			fireExitClicked();
		}
	}
	
	public void mouseReleased(MouseEvent e) {}
	
	public void mousePressed(MouseEvent e) {}
	
	public void mouseExited(MouseEvent e) {}
	
	public void mouseEntered(MouseEvent e) {}
	
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
