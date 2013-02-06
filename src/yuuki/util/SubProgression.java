package yuuki.util;

/**
 * Monitors progress through a part of another ProgressMonitor.
 */
public class SubProgression implements Progressable {
	
	/**
	 * The ProgressMonitor that this SubMonitor is running on a portion of.
	 */
	private final Progressable monitor;
	
	/**
	 * The percent that the parent monitor will be at when this SubMonitor is
	 * complete.
	 */
	private final double end;
	
	/**
	 * The percent that the parent monitor was at when this SubMonitor was
	 * started on it.
	 */
	private final double start;
	
	/**
	 * Creates a new SubMonitor that runs from the current position of the
	 * given monitor through the given length.
	 * 
	 * @param monitor The monitor that this SubMonitor is running on a portion
	 * of.
	 * @param length The percentage of the given ProgressMonitor that this
	 * SubMonitor is representative.
	 */
	public SubProgression(Progressable monitor, double length) {
		this.monitor = monitor;
		this.start = monitor.getProgress();
		this.end = this.start + length;
	}
	
	@Override
	public void advanceProgress(double percent) {
		if ((getProgress() + percent) >= 1.0) {
			finishProgress();
		} else {
			percent = Math.min(1 - getProgress(), percent);
			monitor.advanceProgress(percent * getLength());
		}
	}
	
	@Override
	public double getProgress() {
		double absoluteProgress = monitor.getProgress() - start;
		double percentProgress = absoluteProgress / getLength();
		return percentProgress;
	}
	
	@Override
	public void setProgress(double percent) {
		double absoluteProgress = percent * getLength();
		double actualPercent = absoluteProgress + start;
		monitor.setProgress(actualPercent);
	}
	
	@Override
	public void finishProgress() {
		monitor.setProgress(end);
	}
	
	@Override
	public Progressable getProgressSubSection(double length) {
		Progressable subMonitor = new SubProgression(this, length);
		return subMonitor;
	}
	
	/**
	 * Gets the length of this SubMonitor in terms of percentage of its parent
	 * monitor.
	 * 
	 * @return The percent of the parent monitor that this SubMonitor covers.
	 */
	private double getLength() {
		return (end - start);
	}
	
}
