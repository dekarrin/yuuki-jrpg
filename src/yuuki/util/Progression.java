package yuuki.util;

/**
 * Progress through from 0 to 1.
 */
public class Progression implements Progressable {
	
	/**
	 * The current percent of progress.
	 */
	private double progress = 0.0;
	
	@Override
	public void advanceProgress(double percent) {
		setProgress(progress + percent);
	}
	
	@Override
	public void setProgress(double percent) {
		progress = percent;
		approximateProgress();
	}
	
	@Override
	public void finishProgress() {
		progress = 1.0;
	}
	
	@Override
	public double getProgress() {
		return progress;
	}
	
	@Override
	public Progressable getSubProgressable(double length) {
		Progressable subMonitor = new SubProgression(this, length);
		return subMonitor;
	}
	
	/**
	 * Bounds progress such that it is set to 100 if it is sufficiently close
	 * to 100.
	 */
	private void approximateProgress() {
		if (1.0 - progress < Progressable.PROGRESS_PRECISION) {
			finishProgress();
		}
	}
	
}
