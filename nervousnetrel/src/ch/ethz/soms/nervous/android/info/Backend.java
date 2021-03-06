package ch.ethz.soms.nervous.android.info;

public interface Backend {
	
	public void calculateEntropy(int startYear,	int startMonth,	int startDay,	int startHour,	int startMinute,
								 int endYear,	int endMonth,	int endDay,		int endHour,	int endMinute);
	
	public void requestRealTimeEntropy(String period, String frequency);

}
