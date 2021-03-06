package ch.ethz.soms.nervous.android.info;

public class Middleware implements Intermedier {
	
	private Displaying display;
	private Backend backend;
	private boolean startingDateAndTimeSet = false;
	private boolean endingDateAndTimeSet = false;
	
	private int startYear;
	private int startMonth;
	private int startDay;
	private int startHour;
	private int startMinute;
	
	private int endYear;
	private int endMonth;
	private int endDay;
	private int endHour;
	private int endMinute;
	
	public Middleware(Displaying display, Backend backend) {
		this.display = display;
		this.backend = backend;
	}

	@Override
	public void startingDateChanged(int year, int month, int day) {
		this.display.displayBeginningDate(this.formatDate(year, month, day));		
		this.startDay = day;
		this.startMonth = month;
		this.startYear = year;
		check();
	}

	@Override
	public void endingDateChanged(int year, int month, int day) {
		this.display.dispayEndingDate(this.formatDate(year, month, day));		
		this.endDay = day;
		this.endMonth = month;
		this.endYear = year;
		check();
	}

	@Override
	public void startingTimeChanged(int hour, int minute) {
		this.display.displayBeginningTime(this.formatTime(hour, minute));
		if(!startingDateAndTimeSet) startingDateAndTimeSet = true;
		this.startHour = hour;
		this.startMinute = minute;
		check();
	}

	@Override
	public void endingTimeChanged(int hour, int minute) {
		this.display.displayEndingTime(this.formatTime(hour, minute));
		if(!endingDateAndTimeSet) endingDateAndTimeSet = true;
		this.endHour = hour;
		this.endMinute = minute;
		check();
	}
	
	private String formatDate(int year, int month, int day) {
		return day + "." + (month+1) + "." + year;
	}
	
	private String formatTime(int hour, int minute) {
		return hour + ":" + minute;
	}
	
	
	private void check() {
		if(this.startingDateAndTimeSet && this.endingDateAndTimeSet) {
			this.backend.calculateEntropy(startYear, startMonth, startDay, startHour, startMinute, endYear, endMonth, endDay, endHour, endMinute);
		}
	}
	
	

}
