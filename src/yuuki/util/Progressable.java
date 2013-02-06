package yuuki.util;

/**
 * Gives information on the progress of some task.
 */
public interface Progressable {
	
	/**
	 * The precision of progress. If the difference between 1 and the current
	 * progress is less than this number, the current progress is set to 1
	 * immediately.
	 */
	public static final double PROGRESS_PRECISION = 0.0000001;
	
	/**
	 * Advances the progress by a certain percent.
	 * 
	 * @param percent The percent to increase it by, in the range of (0, 1].
	 */
	public void advanceProgress(double percent);
	
	/**
	 * Immediately completes this ProgressMonitor. Its progress is immediately
	 * set to 1.
	 */
	public void finishProgress();
	
	/**
	 * Gets the current percent completion of this ProgressMonitor.
	 * 
	 * @return The current percent completion.
	 */
	public double getProgress();
	
	/**
	 * Gets a progress monitor for a subsection of this one. Updates to it
	 * cause updates in the specified range to occur.
	 * 
	 * @param length The percentage of this monitor that the returned monitor
	 * covers, in the range of (0, 100].
	 * 
	 * @return A ProgressMonitor that updates a subsection of this one.
	 */
	public Progressable getSubProgressable(double length);
	
	/**
	 * Sets the progress directly.
	 * 
	 * @param percent The percent to set it to, in the range of (0, 1].
	 */
	public void setProgress(double percent);
	
}
