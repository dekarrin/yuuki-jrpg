package yuuki.sprite;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import yuuki.action.Action;
import yuuki.anim.Animator;
import yuuki.buff.Buff;
import yuuki.entity.Character;
import yuuki.entity.Stat;
import yuuki.entity.VariableStat;
import yuuki.sprite.Sprite;
import yuuki.ui.*;

/**
 * The graphic used to display characters on the battle screen.
 */
@SuppressWarnings("serial")
public class FighterSprite extends Sprite {
	
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
	 * The height, in pixels, of the fighter name area.
	 */
	public static final int N_SIZE = 14;
	
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
	 * The area where both stats and the image are displayed.
	 */
	private JLayeredPane imageStatHolder;
	
	/**
	 * The bar showing the fighter's current and total mana points.
	 */
	private StatBar manaBar;
	
	/**
	 * The label displaying the fighter's name.
	 */
	private JLabel nameLabel;
	
	/**
	 * The area where the fighter's stats are displayed.
	 */
	private StatPanel statPanel;
	
	/**
	 * Creates a new FighterSprite from a Character.
	 * 
	 * @param fighter The Character to make the sprite for.
	 * @param animator The Animator that will drive this Sprite's animation.
	 */
	public FighterSprite(Character fighter, Animator animator) {
		super(SPRITE_WIDTH,
				BUFF_HEIGHT + 2*BAR_HEIGHT + N_SIZE + IMAGE_HEIGHT,
				animator);
		createComponents();
		initializeComponents(fighter);
		addComponents();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void advance(int fps) {}
	
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
	 * Updates the the stat panel to reflect the current stats.
	 * 
	 * @param fighter The fighter to show the stats for.
	 */
	public void showStatUpdate(Character fighter) {
		statPanel.setHpMax(fighter.getMaxHP());
		statPanel.setHp(fighter.getHP());
		statPanel.setMpMax(fighter.getMaxMP());
		statPanel.setMp(fighter.getMP());
		statPanel.setStrength(fighter.getStrength());
		statPanel.setDefense(fighter.getDefense());
		statPanel.setAgility(fighter.getAgility());
		statPanel.setAccuracy(fighter.getAccuracy());
		statPanel.setMagic(fighter.getMagic());
		statPanel.setLuck(fighter.getLuck());
	}
	
	/**
	 * Adds this FighterSprite's components to its content pane.
	 */
	private void addComponents() {
		imageStatHolder.add(imagePanel, new Integer(0));
		imageStatHolder.add(statPanel, new Integer(1));
		add(buffPanel);
		add(healthBar);
		add(manaBar);
		add(nameLabel);
		add(imageStatHolder);
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
		statPanel = new StatPanel();
		imageStatHolder = new JLayeredPane();
	}
	
	/**
	 * Initializes the buff panel of this FighterSprite.
	 */
	private void initializeBuffPanel() {
		buffPanel.setBounds(0, 0, SPRITE_WIDTH, BUFF_HEIGHT);
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
		initializeImageStatHolder();
		initializeStatPanel(fighter);
	}
	
	/**
	 * Initializes the health bar component of this FighterSprite.
	 * 
	 * @param hpStat The HP Stat of this FighterSprite's fighter.
	 * @param level The level of this FighterSprite's fighter.
	 */
	private void initializeHealthBar(VariableStat hpStat, int level) {
		healthBar.setBounds(0, BUFF_HEIGHT, SPRITE_WIDTH, BAR_HEIGHT);
		healthBar.setStat(hpStat);
		healthBar.setLevel(level);
		healthBar.setVisible(true);
	}
	
	/**
	 * Initializes the image component of this FighterSprite.
	 */
	private void initializeImagePanel() {
		imagePanel.setBounds(0, 0, SPRITE_WIDTH, IMAGE_HEIGHT);
		imagePanel.setBackground(Color.GREEN);
		imagePanel.setOpaque(true);
		imagePanel.setVisible(true);
	}
	
	/**
	 * Initializes the stat and image component of this FighterSprite.
	 */
	private void initializeImageStatHolder() {
		Dimension size = new Dimension(SPRITE_WIDTH, IMAGE_HEIGHT);
		imageStatHolder.setPreferredSize(size);
		int y = BUFF_HEIGHT + 2*BAR_HEIGHT + N_SIZE;
		imageStatHolder.setBounds(0, y, SPRITE_WIDTH, IMAGE_HEIGHT);
	}
	
	/**
	 * Initializes the mana bar component of this FighterSprite.
	 * 
	 * @param mpStat The MP Stat of this FighterSprite's fighter.
	 * @param level The level of this FighterSprite's fighter.
	 */
	private void initializeManaBar(VariableStat mpStat, int level) {
		int y = BUFF_HEIGHT + BAR_HEIGHT;
		manaBar.setBounds(0, y, SPRITE_WIDTH, BAR_HEIGHT);
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
		int y = BUFF_HEIGHT + 2*BAR_HEIGHT;
		nameLabel.setBounds(0, y, SPRITE_WIDTH, N_SIZE);
		nameLabel.setText(name);
		nameLabel.setVisible(true);
	}
	
	/**
	 * Initializes the stat panel of this FighterSprite.
	 * 
	 * @param fighter The fighter to make the stat panel show the stats of.
	 */
	private void initializeStatPanel(Character fighter) {
		statPanel.setPreferredSize(new Dimension(SPRITE_WIDTH, IMAGE_HEIGHT));
		statPanel.setBounds(0, 0, SPRITE_WIDTH, IMAGE_HEIGHT);
		statPanel.setOpaque(false);
		showStatUpdate(fighter);
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
