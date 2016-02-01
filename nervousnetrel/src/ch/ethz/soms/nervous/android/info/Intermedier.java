package ch.ethz.soms.nervous.android.info;

public interface Intermedier {
	
	public void startingDateChanged(int year, int month, int day);
	
	public void endingDateChanged(int year, int month, int day);
	
	public void startingTimeChanged(int hour, int minute);
	
	public void endingTimeChanged(int hour, int minute);

}
