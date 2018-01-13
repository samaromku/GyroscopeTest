package ru.savchenko.andrey.gyroscopetest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    public static final String TAG = "MainActivity";
    private boolean isTop = false;
    private boolean isBottom = false;
    private int angleCount;
    private int totalCount;
    private TextView tvTotalCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTotalCount = findViewById(R.id.tvTotalCount);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(mSensor==null){
            Toast.makeText(this, "null fuck!!!", Toast.LENGTH_SHORT).show();
        }
        Log.i(TAG, "onCreate: " + mSensor);
        mSensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                Log.i(TAG, "onSensorChanged: x " + x + " y: " + y + " z: " + z);
                if (z > 10) {
//                    Log.i(TAG, "onSensorChanged: телефон экраном вверх");
                    isTop = true;
                    if(isBottom){
                        angleCount++;
                        Log.i(TAG, "onSensorChanged: телефон повернулся на 180");
                        isBottom = false;
                    }
                } else if (z < -10) {
//                    Log.i(TAG, "onSensorChanged: телефон экраном вниз");
                    isBottom = true;
                    if(isTop){
                        angleCount++;
                        Log.i(TAG, "onSensorChanged: телефон повернулся на 180");
                        isTop = false;
                    }
                }
                if(angleCount!=0 && angleCount%2==0){
                    Toast.makeText(MainActivity.this, "телефон повернулся на 360", Toast.LENGTH_SHORT).show();
                    angleCount = 0;
                    totalCount++;
                    tvTotalCount.setText("Общее количество поворотов " + totalCount);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                Log.i(TAG, "onAccuracyChanged: " + sensor + " " + accuracy);
            }
        }, mSensor, 1);
    }
}
