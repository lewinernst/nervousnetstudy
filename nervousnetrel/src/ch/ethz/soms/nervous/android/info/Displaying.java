package ch.ethz.soms.nervous.android.info;

public interface Displaying {
	
	public void displayBeginningDate(String startDate);
	
	public void displayBeginningTime(String startTime);
	
	public void dispayEndingDate(String endDate);
	
	public void displayEndingTime(String endTime);
	
	public void displayEntropy(String entropy);
	
	public void displayResults(SensorType sensor, long time, double entropy, double frequency, double log, double share);

}
