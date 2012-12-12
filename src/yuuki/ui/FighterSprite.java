package yuuki.ui;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.CharacterIterator;

import yuuki.entity.Character;
import yuuki.entity.Stat;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The graphic used to display the character on the battle screen.
 * 
 * @author TF Nelson
 */
@SuppressWarnings("serial")
public class FighterSprite extends JPanel {
	
	public static final int IMAGE_WIDTH = 80;
	
	public static final int IMAGE_HEIGHT = 120;
	
	public static final int BUFF_HEIGHT = 30;
	
	public static final int BAR_HEIGHT = 10;
	
	public static final int N_SIZE = 10;
	
	private StatBar healthBar;
	
	private StatBar manaBar;
	
	private JComponent image;
	
	private JLabel nameText;
	
	private JComponent buffs;

	/**
	 * Creates a new FighterSprite from a Character.
	 * 
	 * @param fighter The Character to make the sprite for.
	 */
	public FighterSprite(Character fighter) {
		int actualHeight = IMAGE_HEIGHT + N_SIZE;
		actualHeight += 2*BAR_HEIGHT + BUFF_HEIGHT;
		setPreferredSize(new Dimension(IMAGE_WIDTH, actualHeight));
		nameText = new JLabel(fighter.getName());
		healthBar = new StatBar(IMAGE_WIDTH, BAR_HEIGHT, Color.RED);
		manaBar = new StatBar(IMAGE_WIDTH, BAR_HEIGHT, Color.BLUE);
		image = new JPanel();
		image.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
		image.setBackground(Color.GREEN);
		image.setOpaque(true);
		buffs = new JPanel();
		buffs.setPreferredSize(new Dimension(IMAGE_WIDTH, BUFF_HEIGHT));
		buffs.setOpaque(true);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(buffs);
		add(healthBar);
		add(manaBar);
		add(nameText);
		add(buffs);
	}
	
	/**
	 * Adds the stat mod text to this Sprite.
	 * @param s
	 */
	public void addStatMod(Stat s) {
		// TODO Auto-generated method stub
		System.out.println(getLabel() + " has a " + s.getModifier() + " on "
				+ s.getName());
	}
	
	public void showDamage(Stat stat, int damage) {
		// TODO graphics update
		System.out.println(getLabel() + " took " + damage + " to "
				+ stat.getName());
	}
	
	public void showDamage(Stat stat, double damage) {
		// TODO graphics update
		System.out.println(getLabel() + " took " + damage + " to "
				+ stat.getName());
	}
	
	public void removeStatMod(Stat s) {
		// TODO Auto-generated method stub
	}
	
}
