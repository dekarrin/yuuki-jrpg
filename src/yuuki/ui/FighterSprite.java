package yuuki.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import yuuki.action.Action;
import yuuki.buff.Buff;
import yuuki.entity.Character;
import yuuki.entity.Stat;
import yuuki.entity.VariableStat;

/**
 * The graphic used to display characters on the battle screen.
 */
@SuppressWarnings("serial")
public class FighterSprite extends JPanel {
	
	/**
	 * The height, in pixels, of each stat bar.
	 */
	public static final int BAR_HEIGHT = 10;
	
	/**
	 * The height, in pixels, of the buff icon panel.
	 */
	public static final int BUFF_HEIGHT = 30;
	
	/**
	 * The height, in pixels, of the fighter image.
	 */
	public static final int IMAGE_HEIGHT = 120;
	
	/**
	 * The size, in points, of the font used for the fighter name.
	 */
	public static final int N_SIZE = 10;
	
	/**
	 * The width, in pixels, of the entire sprite.
	 */
	public static final int SPRITE_WIDTH = 80;
	
	/**
	 * The area where active buff icons are displayed.
	 */
	private JComponent buffPanel;
	
	/**
	 * The bar showing the fighter's current and total hit points.
	 */
	private StatBar healthBar;
	
	/**
	 * The area where the fighter's image is displayed.
	 */
	private JComponent imagePanel;
	
	/**
	 * The bar showing the fighter's current and total mana points.
	 */
	private StatBar manaBar;
	
	/**
	 * The label displaying the fighter's name.
	 */
	private JLabel nameLabel;
	
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
	 * Returns this FighterSprite's maximum size. This will be the same as its
	 * preferred size.
	 * 
	 * @return A Dimension with this FighterSprite's size.
	 */
	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
	
	/**
	 * Returns this FighterSprite's preferred size.
	 * 
	 * @return A Dimension with this FighterSprite's size.
	 */
	@Override
	public Dimension getPreferredSize() {
		int actualHeight = IMAGE_HEIGHT + N_SIZE;
		actualHeight += 2*BAR_HEIGHT + BUFF_HEIGHT;
		return new Dimension(SPRITE_WIDTH, actualHeight);
	}
	
	/**
	 * Shows this sprite failing at an action.
	 * 
	 * @param action The action to show.
	 */
	public void showActionFailure(Action action) {
		// TODO: actually do something
	}
	
	/**
	 * Shows this sprite preparing to do an action.
	 * 
	 * @param action The action to show.
	 */
	public void showActionPreparation(Action action) {
		// TODO: actually do something
	}
	
	/**
	 * Shows this sprite performing an action.
	 * 
	 * @param action The action to show.
	 */
	public void showActionUse(Action action) {
		// TODO: actually do something
	}
	
	/**
	 * Shows a buff being activated on this sprite.
	 * 
	 * @param buff The buff to show being activated.
	 */
	public void showBuffActivation(Buff buff) {
		// TODO: actually do something
	}
	
	/**
	 * Shows a buff being applied on this sprite.
	 * 
	 * @param buff The buff to show being applied.
	 */
	public void showBuffApplication(Buff buff) {
		// TODO: actually do something
	}
	
	/**
	 * Shows a buff being deactivated on this sprite.
	 * 
	 * @param buff The buff to show being deactivated.
	 */
	public void showBuffDeactivation(Buff buff) {
		// TODO: actually do something
	}
	
	/**
	 * Shows the animation for character victory.
	 */
	public void showCharacterVictory() {
		// TODO: actually do something
	}
	
	/**
	 * Updates this FighterSprite to show that a stat took damage.
	 * 
	 * @param stat The stat that took damage.
	 * @param damage The amount of damage taken.
	 */
	public void showDamage(Stat stat, double damage) {
		updateStat(stat);
	}
	
	/**
	 * Updates this FighterSprite to show that a stat took damage.
	 * 
	 * @param stat The stat that took damage.
	 * @param damage The amount of damage taken.
	 */
	public void showDamage(Stat stat, int damage) {
		updateStat(stat);
	}
	
	/**
	 * Updates this FighterSprite to show that a stat recovered.
	 * 
	 * @param stat The stat that recovered.
	 * @param amount The amount of recovery.
	 */
	public void showRecovery(Stat stat, double amount) {
		updateStat(stat);
	}
	
	/**
	 * Updates this FighterSprite to show that a stat recovered.
	 * 
	 * @param stat The stat that recovered.
	 * @param amount The amount of recovery.
	 */
	public void showRecovery(Stat stat, int amount) {
		updateStat(stat);
	}
	
	/**
	 * Adds this FighterSprite's components to its content pane.
	 */
	private void addComponents() {
		add(buffPanel);
		add(healthBar);
		add(manaBar);
		add(nameLabel);
		add(imagePanel);
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
	 * Initializes the buff panel of this FighterSprite.
	 */
	private void initializeBuffPanel() {
		buffPanel.setPreferredSize(new Dimension(SPRITE_WIDTH, BUFF_HEIGHT));
		buffPanel.setBackground(Color.RED);
		buffPanel.setOpaque(true);
		buffPanel.setVisible(true);
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
	 * Initializes the health bar component of this FighterSprite.
	 * 
	 * @param hpStat The HP Stat of this FighterSprite's fighter.
	 * @param level The level of this FighterSprite's fighter.
	 */
	private void initializeHealthBar(VariableStat hpStat, int level) {
		healthBar.setStat(hpStat);
		healthBar.setLevel(level);
		healthBar.setVisible(true);
	}
	
	/**
	 * Initializes the image component of this FighterSprite.
	 */
	private void initializeImagePanel() {
		imagePanel.setPreferredSize(new Dimension(SPRITE_WIDTH, IMAGE_HEIGHT));
		imagePanel.setBackground(Color.GREEN);
		imagePanel.setOpaque(true);
		imagePanel.setVisible(true);
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
		manaBar.setVisible(true);
	}
	
	/**
	 * Initializes the name label of this FighterSprite.
	 * 
	 * @param name The name of the fighter to initialize the label for.
	 */
	private void initializeNameLabel(String name) {
		nameLabel.setText(name);
		nameLabel.setVisible(true);
	}
	
	/**
	 * Updates this FighterSprite with new stat values.
	 * 
	 * @param stat The stat to update.
	 */
	private void updateStat(Stat stat) {
		if (healthBar.isWatching(stat)) {
			healthBar.update();
		} else if (manaBar.isWatching(stat)) {
			manaBar.update();
		}
		revalidate();
		repaint();
	}
	
}
