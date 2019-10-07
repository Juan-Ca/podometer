package com.example.podometer.data;

import org.json.JSONException;

public class UserGoals {

    private static UserGoals ourInstance;

    private static final double averageWalkingSpeed = 1.4; // m/s

    private static final double averageJoggingSpeed = 2.24; // m/s

    private static final double averageCaloriesConsumedWaling = 0.08; // cal/s

    private static final double averageCaloriesConsumedJogging = 0.16; // cal/s

    private static final double averageStepsWhenWalking = 1.45; // steps/s

    private static final double averageStepsWhenJogging = 2.77; // steps/s

    private static long timeOfLastCall;

    public static final int complete = 10000;

    public static UserGoals getInstance() { return ourInstance; }

    public static UserGoals getInstance(GoalsUpdateListener listener) {
        if(ourInstance == null) {
            ourInstance = new UserGoals(listener);
            numberOfSteps = 0;
            caloriesConsumed = 0;
            distanceTravelled = 0;
        }
        return ourInstance;
    }

    private static GoalsUpdateListener listener;

    private static UserTargets targets;

    private static int numberOfSteps;

    private static double caloriesConsumed;

    private static double distanceTravelled;

    private UserGoals(GoalsUpdateListener listener) {
        this.listener = listener;
    }

    public void setTargetGoals(UserTargets targets ) {
        this.targets = targets;
    }

    public UserTargets getTargets() {
        return targets;
    }

    public synchronized void increaseNumberOfSteps(int numberSteps) {
        if(targets != null) {
            boolean firstTime = (numberOfSteps == 0);

            numberOfSteps += numberSteps;
            listener.updateStepProgress(getProgress(targets.getStepsToBeMade(), numberOfSteps));

            if(firstTime) {
                long msSecond = System.currentTimeMillis() - timeOfLastCall;
                timeOfLastCall = System.currentTimeMillis();
                double stepSpeed = (numberSteps * 1000) / msSecond;
                if (stepSpeed >= averageStepsWhenJogging) {
                    computeNumberOfCalories((int) msSecond, true);
                    computeDistanceTravelled((int) msSecond, true);
                } else if (stepSpeed >= averageStepsWhenWalking) {
                    computeNumberOfCalories((int) msSecond, false);
                    computeDistanceTravelled((int) msSecond, false);
                }

                listener.updateCaloriesProgress(getProgress(targets.getCaloriesToConsumed(), (int) caloriesConsumed));

                listener.updateDistanceProcess(getProgress(targets.getDistanceToTravel(), (int) distanceTravelled));
            } else {
                timeOfLastCall = System.currentTimeMillis();
            }
        }
    }

    private void computeNumberOfCalories(int msSecond, boolean jogging) {
        if(jogging) {
            caloriesConsumed += (averageCaloriesConsumedJogging * msSecond)/1000;
        } else {
            caloriesConsumed += (averageCaloriesConsumedWaling * msSecond)/1000;
        }
    }

    private void computeDistanceTravelled(int msSecond, boolean jogging) {
        if(jogging) {
            distanceTravelled += (averageWalkingSpeed * msSecond)/1000;
        } else {
            distanceTravelled += (averageJoggingSpeed * msSecond)/1000;
        }
    }

    private int getProgress(double expected, double current) {
        return (int)(complete * (current/expected));
    }

    private int getProgress(int expected, int current) {
        return (complete * current)/expected;
    }

    public String saveGoals() throws JSONException {
        if(targets == null) {
            throw new NullPointerException("No goals were specified");
        }
        return targets.saveTargets().toString();
    }


}
