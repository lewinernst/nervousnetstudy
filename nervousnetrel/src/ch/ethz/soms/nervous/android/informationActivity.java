package ch.ethz.soms.nervous.android;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.ethz.soms.nervous.android.info.*;
import ch.ethz.soms.nervous.android.info.graphics.*;
import ch.ethz.soms.nervousnet.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class informationActivity extends Activity implements Displaying, OnItemSelectedListener{
	
	public static final SensorType[] sensors = { SensorType.ACCELEROMETER, SensorType.BATTERY, SensorType.BLEBEACON, 
												 SensorType.CONNECTIVITY, SensorType.GYROSCOPE, SensorType.HUMIDITY, 
												 SensorType.LIGHT, SensorType.MAGNETIC, SensorType.NOISE, 
												 SensorType.PRESSURE, SensorType.PROXIMITY, SensorType.TEMPERATURE};
	
	private Button bStartDate;
	private Button bEndDate;
	private TextView tvStartDate;
	private TextView tvEndDate;
	private TextView tvStartTime;
	private TextView tvEndTime;
	private TextView tvEntropy;
	private Spinner periodSpinner;
	private Spinner frequencySpinner;
	private Spinner sensorSpinner;
	private Button bSubmit;
	
	private Intermedier intermedier;
	private Backend backend;
	private ListView resultList;
	
	private ArrayList<String> periodStrings;
	private ArrayList<String> frequencyStrings;
	private ArrayList<String> sensorStrings;
	
	private SensorType currentSensortype;
	private String period;
	private String frequency;
	private CustomAdapter adapter;
	
	private ArrayList<Record> populationList;
	private HashMap<SensorType, ArrayList<Record>> results;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("%%%%%%%%%%%%%%", "******************");
        setContentView(R.layout.activity_information);
        
        currentSensortype = SensorType.ACCELEROMETER;
        
        populationList = new ArrayList<Record>();
        
        initPopulationList();
        testPopulate();
        
        prep();
        init();       
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
    
    
    private void prep() {
    	this.results = new HashMap<SensorType, ArrayList<Record>>();
    	for(SensorType sensor : sensors) {
        	this.results.put(sensor, new ArrayList<Record>());
        }
    	
    	this.resultList = (ListView) findViewById(R.id.result_list);
    	
    	adapter = new CustomAdapter(getApplicationContext(), populationList);
    	this.resultList.setAdapter(adapter);
    }
    
    private void init() {
    	this.backend = new BackendImplementation(this);
    	this.intermedier = new Middleware(this, backend);
    	this.tvStartDate = (TextView) findViewById(R.id.beginningDate_tv);
    	this.tvEndDate = (TextView) findViewById(R.id.endingDate_tv);
    	this.tvStartTime = (TextView) findViewById(R.id.beginningTime_tv);
    	this.tvEndTime = (TextView) findViewById(R.id.endingTime_tv);
    	this.tvEntropy = (TextView) findViewById(R.id.entropy_tv);
    	this.bStartDate = (Button) findViewById(R.id.beginningDate_b);
    	this.bEndDate = (Button) findViewById(R.id.endingDate_button); 
    	this.periodSpinner = (Spinner) findViewById(R.id.periodSpinner);
    	this.frequencySpinner = (Spinner) findViewById(R.id.frequencySpinner_1);
    	this.sensorSpinner = (Spinner) findViewById(R.id.sensorSpinner);
    	this.bSubmit = (Button) findViewById(R.id.submit_b);
    	
    	this.bStartDate.setOnClickListener(new OnClickListenerUpdated(intermedier, DateType.START_DATE));
    	this.bEndDate.setOnClickListener(new OnClickListenerUpdated(intermedier, DateType.END_DATE));
    	this.bSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				backend.requestRealTimeEntropy(period, frequency);
			}
		});
    	
    	
    	this.periodStrings = new ArrayList<String>();
    	this.frequencyStrings = new ArrayList<String>();
    	this.sensorStrings = new ArrayList<String>();
    	initSpinners();
    	
    	
    }
    
    public void showStartingDateDialog(View view) {
    	DatePickerFragment dpf = new DatePickerFragment(this.intermedier, DateType.START_DATE);
    	dpf.show(getFragmentManager(), "startDatePicker");
    }
    
    public void showEndingDateDialog(View view) {
    	DatePickerFragment dpf = new DatePickerFragment(this.intermedier, DateType.END_DATE);
    	dpf.show(getFragmentManager(), "endDatePicker");
    }

	@Override
	public void displayBeginningDate(String startDate) {
		this.tvStartDate.setText(startDate);
	}

	@Override
	public void dispayEndingDate(String endDate) {
		this.tvEndDate.setText(endDate);
	}

	@Override
	public void displayEntropy(String entropy) {
		this.tvEntropy.setText(entropy);		
	}

	@Override
	public void displayBeginningTime(String startTime) {
		this.tvStartTime.setText(startTime);
	}

	@Override
	public void displayEndingTime(String endTime) {
		this.tvEndTime.setText(endTime);
	}
	
	public class OnClickListenerUpdated implements OnClickListener {
		
		private Intermedier intermedier;
		private DateType dateType;
		
		public OnClickListenerUpdated(Intermedier im, DateType dateType) {
			this.intermedier = im;
			this.dateType = dateType;
		}

		@Override
		public void onClick(View v) {
			DatePickerFragment dpf = new DatePickerFragment(this.intermedier, dateType);
	    	dpf.show(getFragmentManager(), "startDatePicker");
		}		
	}
	
	public class onClickListenerRealtime implements OnClickListener {
		
		private Backend backend;
		
		public onClickListenerRealtime(Backend backend) {
			this.backend = backend;
		}

		@Override
		public void onClick(View v) {
			backend.requestRealTimeEntropy(period, frequency);
		}
		
	}
	
	private void initSpinners() {
		this.periodStrings.add("30 sec");
		this.periodStrings.add("1 min");
		this.periodStrings.add("2 min");
		this.periodStrings.add("3 min");
		this.periodStrings.add("5 min");
		this.periodStrings.add("10 min");
		this.periodStrings.add("15 min");
		this.periodStrings.add("20 min");
		this.periodStrings.add("30 min");
		this.periodStrings.add("45 min");
		this.periodStrings.add("1 h");
		this.periodStrings.add("2 h");
		this.periodStrings.add("10 h");
		this.periodStrings.add("12 h");
		this.periodStrings.add("1 d");
		this.periodStrings.add("2 d");
		this.periodStrings.add("5 d");
		this.periodStrings.add("1 w");
		this.periodStrings.add("2 w");
		this.periodStrings.add("1 m");
		
		this.frequencyStrings.add("30 sec");
		this.frequencyStrings.add("1 min");
		this.frequencyStrings.add("2 min");
		this.frequencyStrings.add("3 min");
		this.frequencyStrings.add("5 min");
		this.frequencyStrings.add("10 min");
		this.frequencyStrings.add("15 min");
		this.frequencyStrings.add("20 min");
		this.frequencyStrings.add("30 min");
		this.frequencyStrings.add("45 min");
		this.frequencyStrings.add("1 h");
		this.frequencyStrings.add("2 h");
		this.frequencyStrings.add("10 h");
		this.frequencyStrings.add("12 h");
		this.frequencyStrings.add("1 d");
		this.frequencyStrings.add("2 d");
		this.frequencyStrings.add("5 d");
		this.frequencyStrings.add("1 w");
		this.frequencyStrings.add("2 w");
		this.frequencyStrings.add("1 m");
		
		this.sensorStrings.add("Accelerometer");
		this.sensorStrings.add("Battery");
		this.sensorStrings.add("BLEBeacon");
		this.sensorStrings.add("Connectivity");
		this.sensorStrings.add("Gyroscope");
		this.sensorStrings.add("Humidity");
		this.sensorStrings.add("Light");
		this.sensorStrings.add("Magnetic");
		this.sensorStrings.add("Noise");
		this.sensorStrings.add("Pressure");
		this.sensorStrings.add("Proximity");
		this.sensorStrings.add("Temperature");	
		
		
		ArrayAdapter<String> periodAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, periodStrings);
	    periodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    periodSpinner.setAdapter(periodAdapter);
	    periodSpinner.setOnItemSelectedListener(this);
	    
	    ArrayAdapter<String> frequencyAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, frequencyStrings);
	    frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    frequencySpinner.setAdapter(frequencyAdapter);	
	    frequencySpinner.setOnItemSelectedListener(this);
	    
	    ArrayAdapter<String> sensorAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, sensorStrings);
	    sensorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    sensorSpinner.setAdapter(sensorAdapter);
	    sensorSpinner.setOnItemSelectedListener(this);
	    
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		String item = parent.getItemAtPosition(position).toString();
		Log.e("ITEM SELECTED LISTENER", "ITEM IS SELECTED!!!");
		this.showToast(this, "Item selected: " + item);
		
		if(parent.getId() == R.id.sensorSpinner) {
			this.currentSensortype = string2sensor(item);
			switchSensors(currentSensortype);
		} else if(parent.getId() == R.id.periodSpinner) {
			this.period = item;
		} else if(parent.getId() == R.id.frequencySpinner_1) {
			this.frequency = item;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	private void showToast(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	@Override
	public void displayResults(SensorType sensor, long time, double entropy, double frequency, double log, double share) {
		Record record = new Record();
		record.time = time + "";
		record.entropy = entropy + "";
		record.frequency = frequency + "";
		record.log = log + "";
		record.share = share + "";
		
		ArrayList<Record> currList = this.results.get(sensor);
		currList.add(record);
		
		if(currentSensortype.equals(sensor)) {
			this.populationList.add(record);
			adapter.notifyDataSetChanged();
		}	
	}
	
	private SensorType string2sensor(String sensor) {		
		if("Accelerometer".equals(sensor)) {
			return SensorType.ACCELEROMETER;
		}
		if("Battery".equals(sensor)) {
			return SensorType.BATTERY;
		}
		if("BLEBeacon".equals(sensor)) {
			return SensorType.BLEBEACON;
		}
		if("Connectivity".equals(sensor)) {
			return SensorType.CONNECTIVITY;
		}
		if("Gyroscope".equals(sensor)) {
			return SensorType.GYROSCOPE;
		}
		if("Humidity".equals(sensor)) {
			return SensorType.HUMIDITY;
		}
		if("Light".equals(sensor)) {
			return SensorType.LIGHT;
		}
		if("Magnetic".equals(sensor)) {
			return SensorType.MAGNETIC;
		}
		if("Noise".equals(sensor)) {
			return SensorType.NOISE;
		}
		if("Pressure".equals(sensor)) {
			return SensorType.PRESSURE;
		}
		if("Proximity".equals(sensor)) {
			return SensorType.PROXIMITY;
		}
		if("Temperature".equals(sensor)) {
			return SensorType.TEMPERATURE;
		}
		return null;
	}
	
	private void initPopulationList() {
		this.populationList.clear();
		Record rec = new Record();
		rec.time = "TIME";
		rec.entropy = "ENTROPY";
		rec.frequency = "FREQUENCY";
		rec.log = "LOG";
		rec.share = "SHARE";
		this.populationList.add(rec);
	}
	
	private void testPopulate() {
		for(int i = 0; i < 20; i++) {
			Record rec = new Record();
			rec.time = i + "";
			rec.entropy = i + "";
			rec.frequency = i + "";
			rec.log = i + "";
			rec.share = i + "";
			this.populationList.add(rec);
		}
	}
	
	private void switchSensors(SensorType sensor) {
		this.currentSensortype = sensor;
		initPopulationList();
		ArrayList<Record> sensorList = this.results.get(currentSensortype);
		this.populationList.addAll(sensorList);
		adapter.notifyDataSetChanged();
	}

	
}
