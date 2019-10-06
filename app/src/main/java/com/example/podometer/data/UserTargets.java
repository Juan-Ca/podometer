package com.example.podometer.data;

import org.json.JSONException;
import org.json.JSONObject;

public class UserTargets {

    private int caloriesToConsumed;

    private int stepsToBeMade;

    private double distanceToTravel;

    public UserTargets(int caloriesConsumed, int stepsToBeMade, double distanceTravel) {
        this.caloriesToConsumed = caloriesConsumed;
        this.stepsToBeMade = stepsToBeMade;
        this.distanceToTravel = distanceTravel;
    }

    public UserTargets(JSONObject saved) throws JSONException {
        caloriesToConsumed = ( saved.has("calories") ) ? saved.getInt("calories") : 0;
        stepsToBeMade = (saved.has("steps") ) ? saved.getInt("steps") : 0;
        distanceToTravel = (saved.has("distance") ) ? saved.getDouble("distance") : 0.0;
    }

    public int getCaloriesToConsumed() {
        return caloriesToConsumed;
    }

    public double getDistanceToTravel() {
        return distanceToTravel;
    }

    public int getStepsToBeMade() {
        return stepsToBeMade;
    }

    public boolean updateCalories(String caloriesConsumed) {
        int value = caloriesConsumed == null ? caloriesToConsumed : Integer.parseInt(caloriesConsumed);
        if(value == caloriesToConsumed) {
            return false;
        }
        caloriesToConsumed = value;
        return true;
    }

    public boolean updateDistance(String distanceTravel) {
        double value = distanceTravel == null ? distanceToTravel : Double.parseDouble(distanceTravel);
        if(value == distanceToTravel) {
            return false;
        }
        this.distanceToTravel = Double.parseDouble(distanceTravel);
        return true;
    }

    public boolean updateSteps(String stepsToBeMade) {
        int value = stepsToBeMade == null ? this.stepsToBeMade : Integer.parseInt(stepsToBeMade);
        if(value == this.stepsToBeMade) {
            return false;
        }
        this.stepsToBeMade = value;
        return true;
    }

    JSONObject saveTargets() throws JSONException {
        JSONObject saving = new JSONObject();
        saving.put("calories", caloriesToConsumed);
        saving.put("steps", stepsToBeMade);
        saving.put("distance", distanceToTravel);
        return saving;
    }
}
