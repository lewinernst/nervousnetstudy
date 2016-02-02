package ch.ethz.soms.nervous.android.sensors;

import java.util.ArrayList;
import java.util.List;

import ch.ethz.soms.nervous.android.sensorQueries.SensorQueriesAccelerometer;
import ch.ethz.soms.nervous.nervousproto.SensorUploadProtos.SensorUpload.SensorData;

import java.math.*;

public class SensorDescInformation extends SensorDescVectorValue {
	
	
	public static final long SENSOR_ID = 0x000000000000000ff;
	public static final long earliest = Long.MIN_VALUE;
	public static final long latest = Long.MAX_VALUE;
	public static final long targetSENSOR_ID = 0x0000000000000000L;
	public static final int entropyAccuracy = 1000;
	
	private float entropyX;
	private float entropyY;
	private float entropyZ;
	private float frequency;
	private final boolean isLogging;
	private final boolean isSharing;

	public SensorDescInformation(final long timestamp, final float entropyX, final float entropyY, final float entropyZ, 
			final float frequency, final boolean isLogging, final boolean isSharing) {
		super(timestamp);
		this.entropyX = entropyX;
		this.entropyY = entropyY;
		this.entropyZ = entropyZ;
		
		this.frequency = frequency;
		
		this.isLogging = isLogging;
		this.isSharing = isSharing;
	}
	
	public SensorDescInformation(SensorData sensorData) {
		super(sensorData);
		this.entropyX = sensorData.getValueFloat(1);
		this.entropyY = sensorData.getValueFloat(2);
		this.entropyZ = sensorData.getValueFloat(3);
		this.frequency = sensorData.getValueFloat(4);
		
		this.isLogging = sensorData.getValueBool(0);
		this.isSharing = sensorData.getValueBool(1);
	}

	public float getEntropyX(){
		return entropyX;	
	}
	
	public float getEntropyY(){
		return entropyY;
	}
	
	public float getEntropyZ(){
		return entropyZ;
	}
	
	public float getFrequency(){
		return frequency;
	}
	
	public boolean getIsLogging(){
		return isLogging;
	}
	
	public boolean getIsSharing(){
		return isSharing;
	}


	

	@Override
	public SensorData toProtoSensor() {
		SensorData.Builder sdb = SensorData.newBuilder();
		sdb.setRecordTime(getTimestamp());
		sdb.addValueFloat(getEntropyX());
		sdb.addValueFloat(getEntropyY());
		sdb.addValueFloat(getEntropyZ());
		sdb.addValueFloat(getFrequency());
		sdb.addValueBool(getIsLogging());
		sdb.addValueBool(getIsSharing());
		return sdb.build();
	}

	@Override
	public long getSensorId() {
		return SENSOR_ID;
	}

	@Override
	public ArrayList<Float> getValue() {
		// TODO Auto-generated method stub
		ArrayList<Float> arrayList = new ArrayList<Float>();
		arrayList.add(entropyX);
		arrayList.add(entropyY);
		arrayList.add(entropyZ);
		arrayList.add(frequency);
		return arrayList; // 4 values returned
	}

}

