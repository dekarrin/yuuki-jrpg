package yuuki.ui;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

/**
 * A client for a JScrollPane for displaying a number of the same-height
 * objects. VerticalScrollPaneClient is only allows scrolling vertically.
 */
@SuppressWarnings("serial")
public class VerticalScrollPaneClient extends JPanel implements Scrollable {
	
	/**
	 * The height of the view.
	 */
	public final int viewerHeight;
	
	/**
	 * The width of the view.
	 */
	public final int viewerWidth;
	
	/**
	 * The height of a single item in this client.
	 */
	private final int unitHeight;
	
	/**
	 * Creates a new scroll pane client.
	 * @param width The width of the view.
	 * @param height The height of the view.
	 * @param unitHeight The height of the one of the items that this client is
	 * intended to display.
	 */
	public VerticalScrollPaneClient(int width, int height, int unitHeight) {
		this.viewerWidth = width;
		this.viewerHeight = height;
		this.unitHeight = unitHeight;
		setOpaque(false);
	}
	
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return new Dimension(viewerWidth, viewerHeight);
	}
	
	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		final int y = visibleRect.y;
		final int h = visibleRect.height;
		if (orientation == SwingConstants.HORIZONTAL) {
			return 1;
		} else {
			if (direction > 0) {
				return getViewRemainder(y, h);
			} else {
				return (h - getInitial(y));
			}
		}
	}
	
	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}
	
	@Override
	public boolean getScrollableTracksViewportWidth() {
		return true;
	}
	
	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		final int y = visibleRect.y;
		final int h = visibleRect.height;
		if (orientation == SwingConstants.HORIZONTAL) {
			return 1;
		} else {
			if (direction > 0) {
				int viewRemainder = getViewRemainder(y, h);
				return unitHeight - (h - viewRemainder);
			} else {
				return unitHeight - getInitial(y);
			}
		}
	}
	
	/**
	 * Gets the number of full rows contained inside the view.
	 * 
	 * @param scroll The amount, in pixels, that the view is scrolled down.
	 * @param height The height, in pixels, of the view.
	 * @return The number of rows that are completely exposed within the
	 * view.
	 */
	private int getFullRows(int scroll, int height) {
		int pixelsAfterInitial = height - getInitial(scroll);
		return pixelsAfterInitial / unitHeight;
	}
	
	/**
	 * Gets the number of pixels exposed of the first unit in the view, if
	 * and only if the first unit is partially exposed. If the first unit
	 * is fully exposed, this method will return 0.
	 * 
	 * @param scroll The amount, in pixels, that the view is scrolled down.
	 * @return The number of exposed pixels of the first unit in the view,
	 * or 0 if the first unit is fully exposed.
	 */
	private int getInitial(int scroll) {
		int remaining = scroll % unitHeight;
		if (remaining != 0) {
			return unitHeight - remaining;
		} else {
			return 0;
		}
	}
	
	/**
	 * Gets the amount of view up to and excluding the last
	 * partially-exposed unit.
	 * 
	 * @param scroll The amount, in pixels, that the view is scrolled down.
	 * @param height The height, in pixels, of the view.
	 * @return The number of pixels leading up to the last
	 * partially-exposed unit. If the last unit is fully exposed, this will
	 * return all of the pixels.
	 */
	private int getViewRemainder(int scroll, int height) {
		int fullRows = getFullRows(scroll, height);
		int fullPixels = fullRows * unitHeight;
		int viewRemainder = getInitial(scroll) + fullPixels;
		return viewRemainder;
	}
	
}