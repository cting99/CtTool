package com.ct.tool.fragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import com.ct.tool.base.CtSimpleFragment;

import java.util.List;

/**
 * Created by Cting on 2016/8/30.
 */
public class SensorFragment extends CtSimpleFragment implements SensorEventListener {

    SensorManager mSensorMgr;
    private float mMaxRange = 0;

    @Override
    protected void createDataMap() {

        List<Sensor> sensorList = mSensorMgr.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : sensorList) {
            addMap(parseSensorInfo(sensor));
        }
    }

    public String parseSensorInfo(Sensor sensor) {
        String info = sensor.toString();
        info = info.replace("{", "").replace("}", "").replace(",", "\n");
        return info;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensorMgr = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorMgr = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor pSensor = mSensorMgr.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        Sensor alsSensor = mSensorMgr.getDefaultSensor(Sensor.TYPE_LIGHT);
        Sensor gSensor = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMaxRange = pSensor.getMaximumRange();
        mSensorMgr.registerListener(this, pSensor, SensorManager.SENSOR_DELAY_UI);
        Log.i(TAG, "register,mMaxRange=" + mMaxRange + "-----");

    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorMgr.unregisterListener(this);
        Log.i(TAG, "unRegister-----");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        final float distance = event.values[0];
        boolean positive = distance >= 0.0f && distance < mMaxRange;

        Log.d(TAG, "onSensorChanged,positive=" + positive + ",distance=" + distance);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

        Log.d(TAG, "onAccuracyChanged," + accuracy);
    }
}
