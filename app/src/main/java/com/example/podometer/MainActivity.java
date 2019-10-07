package com.example.podometer;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.podometer.data.GoalsUpdateListener;
import com.example.podometer.data.UserGoals;
import com.example.podometer.data.UserTargets;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private int initial_count = 0;

    private ProgressBar stepProgress, caloriesProgress, distanceProgress;

    private UserGoals goals;

    private GoalsUpdateListener listener = new GoalsUpdateListener() {
        @Override
        public void updateCaloriesProgress(int progress) {
            caloriesProgress.setProgress(progress);
        }

        @Override
        public void updateStepProgress(int progress) {
            stepProgress.setProgress(progress);
        }

        @Override
        public void updateDistanceProcess(int progress) {
            distanceProgress.setProgress(progress);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stepProgress = findViewById(R.id.stepsProgressBar);
        caloriesProgress = findViewById(R.id.caloriesProgressBar);
        distanceProgress = findViewById(R.id.distanceProgressBar);
        stepProgress.setMax(UserGoals.complete);
        caloriesProgress.setMax(UserGoals.complete);
        distanceProgress.setMax(UserGoals.complete);

        goals = UserGoals.getInstance(listener);
        checkForSavedGoals();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            File jsonFile = new File(getCacheDir(), "podometer.json");
            FileOutputStream fileOutputStream = new FileOutputStream(jsonFile);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            bufferedWriter.write(goals.saveGoals());

            bufferedWriter.flush();
            bufferedWriter.close();
            outputStreamWriter.close();
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;

        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            Log.i("Nouveau pas", "Total :" + sensorEvent.values[0]);
            if (initial_count == 0) {
                initial_count = (int) sensorEvent.values[0];
            } else {
                TextView myAwesomeTextView = (TextView)findViewById(R.id.counts);
                int stepCount = (int) sensorEvent.values[0] - initial_count;
                goals.increaseNumberOfSteps(stepCount);
                myAwesomeTextView.setText(new Integer(stepCount).toString());
            }
        }
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    public void openGoals(View view) {
        Intent intent = new Intent(this, GoalsActivity.class);
        startActivity(intent);
    }


    private void checkForSavedGoals() {
        File jsonFile = new File(getCacheDir(), "podometer.json");

        if(jsonFile.exists()) {
            String contents = "";
            try {
                FileInputStream fis = new FileInputStream(jsonFile);
                byte[] data = new byte[(int) jsonFile.length()];
                fis.read(data);
                fis.close();
                contents = new String(data, "UTF-8");

                goals.setTargetGoals( new UserTargets(new JSONObject( contents ) ) );
            } catch(Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT);
            }
        } else {
            Toast.makeText(this, "No goals defined", Toast.LENGTH_SHORT);
        }
    }

}