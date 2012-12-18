package yuuki.ui;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import yuuki.entity.Character;

@SuppressWarnings("serial")
public class MessageBox extends JPanel {

	public MessageBox() {
		
	}
	
	public String getString(String prompt) {
		String s = JOptionPane.showInputDialog(prompt);
		return s;
	}
	
	public int getChoice(String prompt, String[] options) {
		String selected = null;
		while (selected == null) {
			selected = (String) JOptionPane.showInputDialog(this, prompt,
					"Choice Selection", JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
		}
		int index = -1;
		for (int i = 0; i < options.length; i++) {
			if (options.equals(selected)) {
				index = i;
				break;
			}
		}
		return index;
		
	}
	
	public void display(Character speaker, String message) {
		
	}
	
}
