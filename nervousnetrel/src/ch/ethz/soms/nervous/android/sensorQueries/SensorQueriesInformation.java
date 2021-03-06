package ch.ethz.soms.nervous.android.sensorQueries;

import java.io.File;

import ch.ethz.soms.nervous.android.Queries.QueryNumVectorValue;
import ch.ethz.soms.nervous.android.sensors.SensorDescGyroscopeNew;
import ch.ethz.soms.nervous.android.sensors.SensorDescInformation;
import ch.ethz.soms.nervous.nervousproto.SensorUploadProtos.SensorUpload.SensorData;

public class SensorQueriesInformation extends QueryNumVectorValue<SensorDescInformation> {
	@Override
	public long getSensorId() {
		return SensorDescGyroscopeNew.SENSOR_ID;
	}

	public SensorQueriesInformation(long timestamp_from, long timestamp_to,
			File file) {
		super(timestamp_from, timestamp_to, file);
	}


	@Override
	public SensorDescInformation createSensorDescVectorValue(
			SensorData sensorData) {
		// TODO Auto-generated method stub
		return new SensorDescInformation(sensorData);
	}

	@Override
	public SensorDescInformation createDummyObject() {
		// TODO Auto-generated method stub
		return new SensorDescInformation(0, 0, 0, 0, 0, 0, 0, 0, false, false);
	}
	

}
