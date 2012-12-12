package yuuki.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * A bar for displaying a stat.
 * 
 * @author TF Nelson
 */
public class StatBar extends JPanel {
	
	private static final long serialVersionUID = 8673785141956435286L;

	public static final Color BACKGROUND_COLOR = new Color(79, 76, 0);

	private Color barColor;
	
	private int width;
	
	private int height;
	
	private int minimum;
	
	private int maximum;
	
	private int value;
	
	public StatBar(int width, int height, Color barColor) {
		this.width = width;
		this.height = height;
		this.barColor = barColor;
		this.minimum = 0;
		this.maximum = 100;
		this.value = 0;
	}
	
	public void setMinimum(int min) {
		minimum = min;
	}
	
	public void setMaximum(int max) {
		maximum = max;
	}
	
	public void setValue(int value) {
		if (value != this.value) {
			repaint(1, 1, width - 2, height - 2);
			this.value = value;
			repaint(1, 1, width - 2, height - 2);
		}
	}
	
	public void setPercent(double percent) {
		setValue((int) Math.round(maximum * percent));
	}
	
	public int getMinimum() {
		return minimum;
	}
	
	public int getMaximum() {
		return maximum;
	}
	
	public int getValue() {
		return value;
	}
	
	public double getPercent() {
		return (double) value / maximum;
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Color old = g.getColor();
		paintEdge(g);
		paintBackground(g);
		paintProgress(g);
		g.setColor(old);
	}
	
	private void paintEdge(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width, height);
	}
	
	private void paintBackground(Graphics g) {
		double p = getPercent();
		int progWidth = (int) Math.round((width - 2) * p);
		int rectWidth = (width - 2) - progWidth;
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(progWidth + 2, 1, rectWidth, height - 2);
	}
	
	private void paintProgress(Graphics g) {
		double p = getPercent();
		int progWidth = (int) Math.round((width - 2) * p);
		g.setColor(barColor);
		g.fillRect(1, 1, progWidth, height - 2);
	}
	
}