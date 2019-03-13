package com.nareta.smartwatchtest1;

import android.Manifest;
import android.content.Context;
import android.content.pm.PermissionInfo;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivityWatch extends WearableActivity implements SensorEventListener, ActivityCompat.OnRequestPermissionsResultCallback {


    //obiekty GUI
    private TextView hrOutput;
    private Button button;
    private View background;
    private TextView accOutput;

    //pola
    private boolean isOn;

    private float xAcc;
    private float yAcc;
    private float zAcc;
    private float heartRate;


    // czujniki
    private SensorManager sensorManager;
    Sensor senAccelerometer;
    Sensor senHeartRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_watch);


        // Enables Always-on
        setAmbientEnabled();

        //referencje
        button = (Button) findViewById(R.id.button);
        background = (View) findViewById(R.id.background);
        accOutput = (TextView) findViewById(R.id.accOutput);
        hrOutput = (TextView) findViewById(R.id.hrOutput);

        //wyglad GUI


        //managery czujnikow

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        senAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senHeartRate = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        sensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);


        String[] per = {Manifest.permission.BODY_SENSORS};
        if (ContextCompat.checkSelfPermission(this, per[0]) != 0) {

            ActivityCompat.requestPermissions(this, per, PermissionInfo.PROTECTION_DANGEROUS);
            Log.d("PERMISSIONS:", "requested, " + ContextCompat.checkSelfPermission(this, per[0]));


        }

    }


    public void buttonPressed(View view) {


        if (isOn) {
            isOn = false;
            sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE));

        } else {
            isOn = true;
            hrOutput.setText("Please wait...");

            boolean sensorRegistered = sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE),
                    SensorManager.SENSOR_DELAY_NORMAL);
            Log.d("Sensor Status:", " Sensor registered: " + sensorRegistered);

        }


    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {


        Sensor mySensor = sensorEvent.sensor;


        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {


            if (isOn) {


                xAcc = sensorEvent.values[0];
                yAcc = sensorEvent.values[1];
                zAcc = sensorEvent.values[2];

                String accOutputText = "x=" + xAcc + '\n' + "y=" + yAcc + '\n' + "z=" + zAcc;


                accOutput.setText(accOutputText);


                button.setText("OFF");

            } else {
                button.setText("ON");
                // sensorManager.unregisterListener(this, senAccelerometer);
            }
        }

        if (mySensor.getType() == Sensor.TYPE_HEART_RATE) {

            heartRate = sensorEvent.values[0];

            hrOutput.setText("heart rate: " + heartRate);


        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
