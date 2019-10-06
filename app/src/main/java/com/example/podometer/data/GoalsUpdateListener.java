package com.example.podometer.data;

public interface GoalsUpdateListener {
    void updateCaloriesProgress(int progress);
    void updateStepProgress(int progress);
    void updateDistanceProcess(int progress);
}
