package ch.ethz.soms.nervous.android.info.graphics;

import java.util.Calendar;

import ch.ethz.soms.nervous.android.info.Intermedier;
import ch.ethz.soms.nervous.android.info.TimeType;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
	
	private Intermedier intermedier;
	private int hour;
	private int minute;
	private TimeType whichTime;
	
	public TimePickerFragment(Intermedier im, TimeType tt) {
		this.intermedier = im;
		this.whichTime = tt;
	}

	@Override
	public void onTimeSet(TimePicker view, int hour, int minute) {
		this.hour = hour;
		this.minute = minute;
		
		if(this.whichTime.equals(TimeType.START_TIME)) {
			this.intermedier.startingTimeChanged(hour, minute);
		} else if(this.whichTime.equals(TimeType.END_TIME)) {
			this.intermedier.endingTimeChanged(hour, minute);
		}		
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

}
