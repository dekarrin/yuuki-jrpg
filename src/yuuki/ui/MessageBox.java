package yuuki.ui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import yuuki.entity.Character;

@SuppressWarnings("serial")
public class MessageBox extends JPanel {

	public MessageBox() {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
		
	}
	
}
