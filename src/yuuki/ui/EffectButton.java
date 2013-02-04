package yuuki.ui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

import yuuki.sound.DualSoundEngine;

/**
 * A JButton that does special effects for mouse events.
 */
@SuppressWarnings("serial")
public class EffectButton extends JButton implements MouseListener {
	
	/**
	 * The color that the text changes to when this button is hovered over.
	 */
	private Color hoverColor;
	
	/**
	 * The normal color of this button's text.
	 */
	private Color normalColor;
	
	/**
	 * The sound effect engine for the game.
	 */
	private DualSoundEngine soundEngine;
	
	/**
	 * Creates a Button with the specified text.
	 * 
	 * @param text The text to show on the button.
	 * @param hoverColor The color to change the text to when hovering.
	 * @param soundEngine The sound engine to play sound effects through.
	 */
	public EffectButton(String text, Color hoverColor,
			DualSoundEngine soundEngine) {
		super(text);
		this.hoverColor = hoverColor;
		this.soundEngine = soundEngine;
		normalColor = getForeground();
		addMouseListener(this);
	}
	
	/**
	 * Clears this EffectButton of mouse hover effects.
	 */
	public void clearHoverEffects() {
		setForeground(normalColor);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		soundEngine.playEffect("BUTTON_HIT");
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		soundEngine.playEffect("BUTTON_HOVER");
		setForeground(hoverColor);
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		clearHoverEffects();
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}
	
	@Override
	public void mouseReleased(MouseEvent e) {}
	
}
