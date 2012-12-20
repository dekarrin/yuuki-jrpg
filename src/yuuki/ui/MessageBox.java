package yuuki.ui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import yuuki.entity.Character;

@SuppressWarnings("serial")
public class MessageBox extends JPanel {
	
	private Thread clearThread;

	private JTextArea textBox;
	
	public MessageBox() {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		textBox = new JTextArea("", 5, 70);
		add(textBox);
	}
	
	public String getString(String prompt) {
		String s = JOptionPane.showInputDialog(prompt);
		return s;
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
	
}
