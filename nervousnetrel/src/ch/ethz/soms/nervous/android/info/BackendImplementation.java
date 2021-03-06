package ch.ethz.soms.nervous.android.info;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import ch.ethz.soms.nervous.android.sensorQueries.*;
import ch.ethz.soms.nervous.android.sensors.SensorDescInformation;
import ch.ethz.soms.nervous.nervousproto.SensorUploadProtos.SensorUpload.SensorData;
import ch.ethz.soms.nervous.utils.NervousStatics;
import android.util.Log;
import android.widget.Toast;

public class BackendImplementation implements Backend {
	
	private Displaying display;
	private File filesDir;
	
	public BackendImplementation(Displaying display) {
		this.display = display;
	}

	@Override
	public void calculateEntropy(int startYear, int startMonth, int startDay,
			int startHour, int startMinute, int endYear, int endMonth,
			int endDay, int endHour, int endMinute) {
		
		Log.d("lewin", "gotcalled");
//		String entropy = startDay + "." + 
//						(startMonth+1) + "." + 
//						startYear + ". " + 
//						startHour + ":" +
//						startMinute + "___" +
//						endDay + "." + 
//						(endMonth+1) + "." + 
//						endYear + ". " +
//						endHour + ":" +
//						endMinute;
		
		//String entropy = "89";
		
		
		int entropyAccuracy = SensorDescInformation.entropyAccuracy;
		SensorQueriesAccelerometer accQuery = new SensorQueriesAccelerometer(SensorDescInformation.earliest, SensorDescInformation.latest, filesDir);
		SensorQueriesBattery batQuery = new SensorQueriesBattery(SensorDescInformation.earliest, SensorDescInformation.latest, filesDir);
		System.out.println(batQuery.containsReadings());
		
		if (accQuery.containsReadings()){
			int count = accQuery.getCount();
			float min = accQuery.getMinValue().getAccX();
			float max = accQuery.getMaxValue().getAccX();
			float interval = max-min;
			List<SensorData> data = accQuery.list;
			int [][] counter = new int [entropyAccuracy][3];
			for (SensorData temp : data) {
				
				int joX = (int) ((temp.getValueFloat(0)-min)*entropyAccuracy/interval);
				int joY = (int) ((temp.getValueFloat(1)-min)*entropyAccuracy/interval);
				int joZ = (int) ((temp.getValueFloat(2)-min)*entropyAccuracy/interval);
				counter [joX][0]++;
				counter [joY][1]++;
				counter [joZ][2]++;
			}
			float entroX = 0, entroY = 0, entroZ = 0;
			for (int i = 0; i < entropyAccuracy; i++ ){
				entroX += (counter[i][0]/count)*Math.log10(counter[i][0]/count);
				entroY += (counter[i][1]/count)*Math.log10(counter[i][1]/count);
				entroZ += (counter[i][2]/count)*Math.log10(counter[i][2]/count);
			}
		
			String entroppy = Float.toString(entroX);
			Log.d("lewin", entroppy);

			this.display.displayEntropy(entroppy);	
		}
		
		else this.display.displayEntropy("no values");
	}

	@Override
	public void requestRealTimeEntropy(String period, String frequency) {
		//do some calculations
		display.displayResults(SensorType.ACCELEROMETER, 0, 1, 2, 3, 4);
		display.displayResults(SensorType.BATTERY, 1, 1, 2, 3, 4);
		display.displayResults(SensorType.BLEBEACON, 2, 1, 2, 3, 4);
		display.displayResults(SensorType.CONNECTIVITY, 3, 1, 2, 3, 4);
		display.displayResults(SensorType.GYROSCOPE, 4, 1, 2, 3, 4);
		display.displayResults(SensorType.HUMIDITY, 5, 1, 2, 3, 4);
		display.displayResults(SensorType.LIGHT, 6, 1, 2, 3, 4);
		display.displayResults(SensorType.MAGNETIC, 7, 1, 2, 3, 4);
		display.displayResults(SensorType.NOISE, 8, 1, 2, 3, 4);
		display.displayResults(SensorType.PRESSURE, 9, 1, 2, 3, 4);
		display.displayResults(SensorType.PROXIMITY, 10, 1, 2, 3, 4);
		display.displayResults(SensorType.TEMPERATURE, 11, 1, 2, 3, 4);
	}
	

}
