package yuuki.ui;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

import yuuki.entity.Character;

import javax.swing.JPanel;

/**
 * The graphic used to display the character on the battle screen.
 * 
 * @author TF Nelson
 */
@SuppressWarnings("serial")
public class FighterSprite extends JPanel {
	
	public static final int WIDTH = 80;
	
	public static final int HEIGHT = 120;
	
	public static final int N_STYLE = Font.PLAIN;
	
	public static final int N_SIZE = 10;
	
	public static final String N_FONT = "Verdana";
	
	public static final Color N_COLOR = Color.BLACK;
	
	private final Dimension size;
	
	private final Font nameFont;
	
	private AttributedString nameText;

	/**
	 * Creates a new FighterSprite from a Character.
	 * 
	 * @param fighter The Character to make the sprite for.
	 */
	public FighterSprite(Character fighter) {
		size = new Dimension(WIDTH, HEIGHT);
		nameFont = new Font(N_FONT, N_STYLE, N_SIZE);
		setNameText(fighter.getName());
	}
	
	public Dimension getPreferredSize() {
		return size;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.fill(new Rectangle(size));
		// paint name;
	}
	
	private void setNameText(String text) {
		nameText = new AttributedString(text);
		nameText.addAttribute(TextAttribute.FONT, nameFont);
		nameText.addAttribute(TextAttribute.FOREGROUND, N_COLOR);
	}
	
}
