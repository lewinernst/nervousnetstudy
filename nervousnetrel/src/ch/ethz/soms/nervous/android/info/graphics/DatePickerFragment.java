package ch.ethz.soms.nervous.android.info.graphics;

import java.util.Calendar;

import ch.ethz.soms.nervous.android.info.DateType;
import ch.ethz.soms.nervous.android.info.Intermedier;
import ch.ethz.soms.nervous.android.info.TimeType;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	
	private int setYear;
	private int setMonth;
	private int setDay;
	private Intermedier intermedier;
	private DateType whichDate;
	
	
	public DatePickerFragment(Intermedier im, DateType dt) {
		this.intermedier = im;
		this.whichDate = dt;
	}
	
	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		setYear = year;
		setMonth = month;
		setDay = day;
		
		if(whichDate.equals(DateType.START_DATE)) {
			this.intermedier.startingDateChanged(year, month, day);
			createTimePicker();
		} else if(whichDate.equals(DateType.END_DATE)){
			this.intermedier.endingDateChanged(year, month, day);
			createTimePicker();
		} else {
			//
		}		
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstance) {
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}
	
	private void createTimePicker() {
		TimeType tt = null;
		if(whichDate.equals(DateType.START_DATE)) {
			tt = TimeType.START_TIME;
		} else {
			tt = TimeType.END_TIME;
		}
		TimePickerFragment tpf = new TimePickerFragment(intermedier, tt);
		tpf.show(getFragmentManager(), "timePicker");
	}
	
	

}
