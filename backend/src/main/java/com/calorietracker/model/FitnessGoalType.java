package com.calorietracker.model;

public enum FitnessGoalType {
    WEIGHT_LOSS("Weight Loss"),
    MAINTENANCE("Maintenance"),
    MUSCLE_GAIN("Muscle Gain");

    private final String displayName;

    FitnessGoalType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
