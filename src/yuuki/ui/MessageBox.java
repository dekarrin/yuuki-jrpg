package yuuki.ui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import yuuki.entity.Character;

@SuppressWarnings("serial")
public class MessageBox extends JPanel implements MouseListener {
	
	private Thread clearThread;

	private JTextArea textBox;
	
	private JTextField input;
	
	private JButton enterButton;
	
	private MessageBoxInputListener listener;
	
	public MessageBox() {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		textBox = new JTextArea("", 5, 70);
		input = new JTextField(30);
		enterButton = new JButton("Enter");
		add(textBox);
	}
	
	public void getString(String prompt, MessageBoxInputListener l) {
		removeAll();
		add(new JLabel(prompt));
		add(input);
		add(enterButton);
		this.listener = l;
	}
	
	public Object getChoice(String prompt, Object[] options) {
		Object selected = null;
		while (selected == null) {
			selected = JOptionPane.showInputDialog(this, prompt,
					"Choice Selection", JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
		}
		return selected;
	}
	
	public void display(Character speaker, String message) {
		class Pauser implements Runnable {
			public String message;
			public void run() {
				textBox.setText(message);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {}
				textBox.setText("");
			}
		}
		Pauser p = new Pauser();
		p.message = message;
		if (clearThread != null) {
			clearThread.interrupt();
		}
		clearThread = new Thread(p);
		clearThread.start();
	}
	
	private void fireEnterClicked() {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getComponent() == enterButton) {
			fireEnterClicked();
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
