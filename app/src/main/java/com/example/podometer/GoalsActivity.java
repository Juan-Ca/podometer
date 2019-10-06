package com.example.podometer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.podometer.data.UserGoals;
import com.example.podometer.data.UserTargets;

public class GoalsActivity extends AppCompatActivity {

    private UserTargets targetsInputted;

    private UserGoals goals;

    private String messageOutput;

    private EditText numberOfSteps, amountOfCalories, distanceDesired;

    private CharSequence caloriesSequence, stepsSequence, distanceSequence;

    private TextView messageDisplay;

    private Button saveGoalsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        numberOfSteps = findViewById(R.id.numberOfSteps);
        amountOfCalories = findViewById(R.id.numberOfCalories);
        distanceDesired = findViewById(R.id.distanceToTravel);
        messageDisplay = findViewById(R.id.goalsMessageDisplay);
        saveGoalsButton = findViewById(R.id.saveButton);

        goals = UserGoals.getInstance();
        targetsInputted = goals.getTargets();
        if(targetsInputted == null) {
            targetsInputted = new UserTargets(0,0,0);
            messageOutput = "Displaying default goals";
        } else {
            messageOutput = "Displaying stored goals";
        }

        messageDisplay.setText(messageOutput);

        caloriesSequence = "" + targetsInputted.getCaloriesToConsumed();
        stepsSequence = "" + targetsInputted.getStepsToBeMade();
        distanceSequence = "" + targetsInputted.getDistanceToTravel();

        numberOfSteps.setText(stepsSequence);
        amountOfCalories.setText(caloriesSequence);
        distanceDesired.setText(distanceSequence);

        saveGoalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageOutput += "\nStoring your goals";
                if(numberOfSteps.getText().toString().equals("")) {
                    numberOfSteps.setText(stepsSequence);
                }
                if(amountOfCalories.getText().toString().equals("")) {
                    amountOfCalories.setText(caloriesSequence);
                }
                if(distanceDesired.getText().toString().equals("")) {
                    distanceDesired.setText(distanceSequence);
                }
                messageDisplay.setText(messageOutput);
                saveGoals();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void saveGoals() {
        String value = amountOfCalories.getText().toString();
        System.out.println("Found this :"+value);
        if(targetsInputted.updateCalories(value)) {
            messageOutput += "\nUpdated calories";
        }
        value = numberOfSteps.getText().toString();
        System.out.println(value);
        if(targetsInputted.updateSteps(value)) {
            messageOutput += "\nUpdated number of steps";
        }
        value = distanceDesired.getText().toString();
        System.out.println(value);
        if(targetsInputted.updateDistance(value)) {
            messageOutput += "\nUpdated distance to cover";
        }
        messageDisplay.setText(messageOutput);
        goals.setTargetGoals(targetsInputted);
    }

    public void returnToSteps(View view) {
        saveGoals();
        finish();
    }
}
