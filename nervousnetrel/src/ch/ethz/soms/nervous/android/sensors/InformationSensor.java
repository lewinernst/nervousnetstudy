package ch.ethz.soms.nervous.android.sensors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ch.ethz.soms.nervous.android.sensorQueries.SensorQueriesAccelerometer;
import ch.ethz.soms.nervous.android.sensors.NoiseSensor.NoiseListener;
import ch.ethz.soms.nervous.android.sensors.SensorDescInformation;
import ch.ethz.soms.nervous.nervousproto.SensorUploadProtos.SensorUpload.SensorData;
import ch.ethz.soms.nervous.utils.NervousStatics;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public class InformationSensor {
	private Context context;

	public InformationSensor(Context context) {
		this.context = context;
	}

	private List<InformationListener> listenerList = new ArrayList<InformationListener>();
	private Lock listenerMutex = new ReentrantLock();


	public interface InformationListener {
		public void informationSensorDataReady( long timestamp,  float entropyX,  float entropyY,  float entropyZ, float frequency, float changeRateX, float changeRateY, float changeRateZ,
				 boolean isLogging,  boolean isSharing);
	}

	public void addListener(InformationListener listener) {
		listenerMutex.lock();
		listenerList.add(listener);
		listenerMutex.unlock();
	}
	
	public void removeListener(InformationListener listener) {
		listenerMutex.lock();
		listenerList.remove(listener);
		listenerMutex.unlock();
	}
	
	public void clearListeners() {
		listenerMutex.lock();
		listenerList.clear();
		listenerMutex.unlock();
	}

	public void dataReady( long timestamp,  float entropyX,  float entropyY,  float entropyZ,  float  frequency, float changeRateX, float changeRateY, float changeRateZ,
			 boolean isLogging,  boolean isSharing) {
		listenerMutex.lock();
		for (InformationListener listener : listenerList) {
			listener.informationSensorDataReady(timestamp, entropyX, entropyY, entropyZ, frequency, changeRateX, changeRateY, changeRateZ,
					isLogging, isSharing);
		}
		listenerMutex.unlock();
	}

	public class InformationTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			
			/*TODO: final long timestamp, final float entropyX, final float entropyY, final float entropyZ,
			final boolean isLogging, final boolean isSharing*/
			
			
				
				
			int entropyAccuracy = SensorDescInformation.entropyAccuracy;
			SensorQueriesAccelerometer accQuery = new SensorQueriesAccelerometer(SensorDescInformation.earliest, SensorDescInformation.latest, context.getFilesDir());
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
			
			float changeRateX = 0, changeRateY = 0, changeRateZ = 0;
		
			for (int i = 0; i< data.size(); i++){
				changeRateX += (data.get(i+1).getValueFloat(0) - data.get(i).getValueFloat(0))/(data.get(i+1).getRecordTime()-data.get(i).getRecordTime());
				changeRateY += (data.get(i+1).getValueFloat(1) - data.get(i).getValueFloat(1))/(data.get(i+1).getRecordTime()-data.get(i).getRecordTime());
				changeRateZ += (data.get(i+1).getValueFloat(2) - data.get(i).getValueFloat(2))/(data.get(i+1).getRecordTime()-data.get(i).getRecordTime());
			}
			changeRateX /= data.size();
			changeRateY /= data.size();
			changeRateZ /= data.size();
			
			
			
			final SharedPreferences settings = context.getSharedPreferences(NervousStatics.SENSOR_PREFS, 0);
			
			float freq = (int)context.getSharedPreferences(NervousStatics.SENSOR_FREQ, 0).getInt(Long.toHexString(SensorDescInformation.targetSENSOR_ID) + "_freqValue", 30) * 1000;
			boolean isLo = settings.getBoolean(Long.toHexString(SensorDescInformation.targetSENSOR_ID) + "_doMeasure", true);
			boolean isSh = settings.getBoolean(Long.toHexString(SensorDescInformation.targetSENSOR_ID) + "_doShare", true);
			
			
			dataReady(System.currentTimeMillis(), entroX, entroY, entroZ, freq, changeRateX, changeRateY, changeRateZ, isLo, isSh);
			return null;
		}

	}

	public void start() {
		new InformationTask().execute();
	}

}