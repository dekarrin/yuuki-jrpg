package yuuki.ui.screen;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class OverworldScreen extends Screen implements MouseListener {

	private ArrayList<OverworldScreenListener> listeners;
	
	private JButton startButton;

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
		listeners = new ArrayList<OverworldScreenListener>();
		startButton = new JButton("Start");
		startButton.addMouseListener(this);
		startButton.addKeyListener(enterListener);
		add(new JLabel("Welcome to the overworld!"));
		add(new JLabel("There's nothing here yet."));
		add(new JLabel("Hit the button to start a battle -->"));
		add(startButton);
	}
	
	public void setInitialFocus() {
		startButton.requestFocus();
	}
	
	public void addListener(OverworldScreenListener l) {
		listeners.add(l);
	}
	
	public void mouseClicked(MouseEvent e) {
		Component c = e.getComponent();
		if (c == startButton) {
			fireStartClicked();
		}
	}
	
	public void mouseReleased(MouseEvent e) {}
	
	public void mousePressed(MouseEvent e) {}
	
	public void mouseExited(MouseEvent e) {}
	
	public void mouseEntered(MouseEvent e) {}
	
	private void fireStartClicked() {
		for (OverworldScreenListener l: listeners) {
			l.startBattleClicked();
		}
	}
}
