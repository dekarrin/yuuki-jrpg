package yuuki.ui;

import java.awt.*;

import yuuki.entity.Character;
import yuuki.entity.Stat;
import yuuki.entity.VariableStat;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The graphic used to display the character on the battle screen.
 * 
 * @author TF Nelson
 */
public class FighterSprite extends JPanel {
	
	private static final long serialVersionUID = 5004775256726541372L;

	public static final int SPRITE_WIDTH = 80;
	
	public static final int IMAGE_HEIGHT = 120;
	
	public static final int BUFF_HEIGHT = 30;
	
	public static final int BAR_HEIGHT = 10;
	
	public static final int N_SIZE = 10;
	
	private StatBar healthBar;
	
	private StatBar manaBar;
	
	private JComponent imagePanel;
	
	private JLabel nameLabel;
	
	private JComponent buffPanel;

	/**
	 * Creates a new FighterSprite from a Character.
	 * 
	 * @param fighter The Character to make the sprite for.
	 */
	public FighterSprite(Character fighter) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		createComponents();
		initializeComponents(fighter);
		addComponents();
	}
	
	/**
	 * Returns this FighterSprite's preferred size.
	 * 
	 * @return A Dimension with this FighterSprite's size.
	 */
	public Dimension getPreferredSize() {
		int actualHeight = IMAGE_HEIGHT + N_SIZE;
		actualHeight += 2*BAR_HEIGHT + BUFF_HEIGHT;
		return new Dimension(SPRITE_WIDTH, actualHeight);
	}
	
	public void showDamage(Stat stat, int damage) {
		if (healthBar.isWatching(stat)) {
			healthBar.update();
		} else if (manaBar.isWatching(stat)) {
			manaBar.update();
		}
	}
	
	public void showDamage(Stat stat, double damage) {
		showDamage(stat, 0); // actual damage value doesn't matter
	}
	
	/**
	 * Creates the components of this FighterSprite.
	 */
	private void createComponents() {
		buffPanel = new JPanel();
		healthBar = new StatBar(SPRITE_WIDTH, BAR_HEIGHT, Color.RED);
		manaBar = new StatBar(SPRITE_WIDTH, BAR_HEIGHT, Color.BLUE);
		nameLabel = new JLabel();
		imagePanel = new JPanel();
	}
	
	/**
	 * Sets the properties of this FighterSprite's components to their proper
	 * values from the given Character.
	 * 
	 * @param Character fighter The fighter to set the properties from.
	 */
	private void initializeComponents(Character fighter) {
		initializeBuffPanel();
		initializeHealthBar(fighter.getHPStat(), fighter.getLevel());
		initializeManaBar(fighter.getMPStat(), fighter.getLevel());
		initializeNameLabel(fighter.getName());
		initializeImagePanel();
	}
	
	/**
	 * Initializes the name label of this FighterSprite.
	 * 
	 * @param name The name of the fighter to initialize the label for.
	 */
	private void initializeNameLabel(String name) {
		nameLabel.setText(name);
	}
	
	/**
	 * Initializes the image component of this FighterSprite.
	 */
	private void initializeImagePanel() {
		imagePanel.setPreferredSize(new Dimension(SPRITE_WIDTH, IMAGE_HEIGHT));
		imagePanel.setBackground(Color.GREEN);
		imagePanel.setOpaque(true);
	}
	
	/**
	 * Initializes the health bar component of this FighterSprite.
	 * 
	 * @param hpStat The HP Stat of this FighterSprite's fighter.
	 * @param level The level of this FighterSprite's fighter.
	 */
	private void initializeHealthBar(VariableStat hpStat, int level) {
		healthBar.setStat(hpStat);
		healthBar.setLevel(level);
	}
	
	/**
	 * Initializes the mana bar component of this FighterSprite.
	 * 
	 * @param mpStat The MP Stat of this FighterSprite's fighter.
	 * @param level The level of this FighterSprite's fighter.
	 */
	private void initializeManaBar(VariableStat mpStat, int level) {
		manaBar.setStat(mpStat);
		manaBar.setLevel(level);
	}
	
	/**
	 * Initializes the buff panel of this FighterSprite.
	 */
	private void initializeBuffPanel() {
		buffPanel.setPreferredSize(new Dimension(SPRITE_WIDTH, BUFF_HEIGHT));
		buffPanel.setOpaque(true);
	}
	
	/**
	 * Adds this FighterSprite's components to its content pane.
	 */
	private void addComponents() {
		add(buffPanel);
		add(healthBar);
		add(manaBar);
		add(nameLabel);
		add(buffPanel);
	}
}
