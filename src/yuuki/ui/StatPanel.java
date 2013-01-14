package yuuki.ui;

import java.awt.Component;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A panel that all stats are displayed on. This can be used to easily display
 * all current stats of the user.
 */
@SuppressWarnings("serial")
public class StatPanel extends JPanel {
	
	/**
	 * The size of the font used by this panel's labels.
	 */
	public static final float FONT_SIZE = 10.0f;
	
	/**
	 * The current accuracy.
	 */
	private JLabel accuracyDisplay;
	
	/**
	 * The current agility.
	 */
	private JLabel agilityDisplay;
	
	/**
	 * The current defense.
	 */
	private JLabel defenseDisplay;
	
	/**
	 * The current health.
	 */
	private JLabel hpDisplay;
	
	/**
	 * The current luck.
	 */
	private JLabel luckDisplay;
	
	/**
	 * The current magic.
	 */
	private JLabel magicDisplay;
	
	/**
	 * The max health.
	 */
	private JLabel maxHpDisplay;
	
	/**
	 * The max mana.
	 */
	private JLabel maxMpDisplay;
	
	/**
	 * The current mana.
	 */
	private JLabel mpDisplay;
	
	/**
	 * The current strength.
	 */
	private JLabel strengthDisplay;
	
	/**
	 * Allocates a new StatPanel. The child components are created and added to
	 * this panel. All stat values are initialized to 0.
	 */
	public StatPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		createComponents();
		addComponents();
	}
	
	/**
	 * Sets the accuracy stat.
	 * 
	 * @param stat The new stat.
	 */
	public void setAccuracy(int stat) {
		accuracyDisplay.setText("" + stat);
	}
	
	/**
	 * Sets the agility stat.
	 * 
	 * @param stat The new stat.
	 */
	public void setAgility(int stat) {
		agilityDisplay.setText("" + stat);
	}
	
	/**
	 * Sets the defense stat.
	 * 
	 * @param stat The new stat.
	 */
	public void setDefense(int stat) {
		defenseDisplay.setText("" + stat);
	}
	
	/**
	 * Sets the HP stat.
	 * 
	 * @param stat The new stat.
	 */
	public void setHp(int stat) {
		hpDisplay.setText("" + stat);
	}
	
	/**
	 * Sets the HP max stat.
	 * 
	 * @param stat The new stat.
	 */
	public void setHpMax(int stat) {
		maxHpDisplay.setText("" + stat);
	}
	
	/**
	 * Sets the luck stat.
	 * 
	 * @param stat The new stat.
	 */
	public void setLuck(int stat) {
		luckDisplay.setText("" + stat);
	}
	
	/**
	 * Sets the magic stat.
	 * 
	 * @param stat The new stat.
	 */
	public void setMagic(int stat) {
		magicDisplay.setText("" + stat);
	}
	
	/**
	 * Sets the mana stat.
	 * 
	 * @param stat The new stat.
	 */
	public void setMp(int stat) {
		mpDisplay.setText("" + stat);
	}
	
	/**
	 * Sets the mana max stat.
	 * 
	 * @param stat The new stat.
	 */
	public void setMpMax(int stat) {
		maxMpDisplay.setText("" + stat);
	}
	
	/**
	 * Sets the strength stat.
	 * 
	 * @param stat The new stat.
	 */
	public void setStrength(int stat) {
		strengthDisplay.setText("" + stat);
	}
	
	/**
	 * Adds the child components. Any additional components needed for display
	 * of the primary child components are created and added as well.
	 */
	private void addComponents() {
		addVariableDisplay("HP:", hpDisplay, maxHpDisplay);
		addVariableDisplay("MP:", mpDisplay, maxMpDisplay);
		addDisplay("STR:", strengthDisplay);
		addDisplay("DEF:", defenseDisplay);
		addDisplay("AGL:", agilityDisplay);
		addDisplay("ACC:", accuracyDisplay);
		addDisplay("MAG:", magicDisplay);
		addDisplay("LUK:", luckDisplay);
	}
	
	/**
	 * Adds a stat display for a stat with only one possible value.
	 * 
	 * @param text The text to show before the number.
	 * @param current The JLabel that shows the stat's value.
	 */
	private void addDisplay(String text, JLabel current) {
		JLabel textLabel = new JLabel(text + " ");
		Font sizedFont = textLabel.getFont().deriveFont(FONT_SIZE);
		textLabel.setFont(sizedFont);
		current.setFont(sizedFont);
		JPanel display = constructDisplayPanel();
		display.add(textLabel);
		display.add(current);
		add(display);
	}
	
	/**
	 * Adds a stat display for a stat with both a current and maximum value.
	 * 
	 * @param text The text to show before the numbers.
	 * @param current The JLabel that shows the stat's current value.
	 * @param max The JLabel that shows the stat's maximum value.
	 */
	private void addVariableDisplay(String text, JLabel current, JLabel max) {
		JLabel textLabel = new JLabel(text + " ");
		JLabel slashLabel = new JLabel("/");
		Font sizedFont = textLabel.getFont().deriveFont(FONT_SIZE);
		textLabel.setFont(sizedFont);
		current.setFont(sizedFont);
		slashLabel.setFont(sizedFont);
		max.setFont(sizedFont);
		JPanel display = constructDisplayPanel();
		display.add(textLabel);
		display.add(current);
		display.add(slashLabel);
		display.add(max);
		add(display);
	}
	
	/**
	 * Creates a panel to hold the components of a stat display.
	 * 
	 * @return A panel to hold the components.
	 */
	private JPanel constructDisplayPanel() {
		JPanel display = new JPanel();
		display.setLayout(new BoxLayout(display, BoxLayout.X_AXIS));
		display.setAlignmentX(Component.LEFT_ALIGNMENT);
		display.setOpaque(false);
		return display;
	}
	
	/**
	 * Creates the primary components of this StatPanel. This does not include
	 * helper components such as labels for the stats; these must be created
	 * when the primary components are added, as there is no way to keep track
	 * of them after they are created.
	 */
	private void createComponents() {
		hpDisplay		= new JLabel("0");
		maxHpDisplay	= new JLabel("0");
		mpDisplay		= new JLabel("0");
		maxMpDisplay	= new JLabel("0");
		strengthDisplay	= new JLabel("0");
		defenseDisplay	= new JLabel("0");
		agilityDisplay	= new JLabel("0");
		accuracyDisplay	= new JLabel("0");
		magicDisplay	= new JLabel("0");
		luckDisplay		= new JLabel("0");
	}
	
}
