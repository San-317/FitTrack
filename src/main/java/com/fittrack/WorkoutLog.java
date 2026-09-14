package com.fittrack;

import java.time.LocalDate;

public class WorkoutLog {
    private LocalDate date;
    private String workoutType; // "cardio", "weights", "both"
    private int durationMinutes;
    private String notes;

    public WorkoutLog(String workoutType, int durationMinutes, String notes) {
        if (!workoutType.equalsIgnoreCase("cardio") &&
            !workoutType.equalsIgnoreCase("weights") &&
            !workoutType.equalsIgnoreCase("both")) {
            throw new IllegalArgumentException("The Workout type must be cardio, weights, or both");
        }
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0");
        }

        this.date = LocalDate.now();
        this.workoutType = workoutType.toLowerCase();
        this.durationMinutes = durationMinutes;
        this.notes = (notes == null) ? "" : notes;
    }

    public LocalDate getDate() { return date; }
    public String getWorkoutType() { return workoutType; }
    public int getDurationMinutes() { return durationMinutes; }
    public String getNotes() { return notes; }

    // Estimate calories burned based on type and duration
    public double estimateCaloriesBurned() {
        switch (workoutType) {
            case "cardio":  return durationMinutes * 8.5;
            case "weights": return durationMinutes * 6.0;
            case "both":    return durationMinutes * 7.5;
            default:        return 0;
        }
    }

    public String toString() {
        return date + " | Type: " + workoutType +
               " | Duration: " + durationMinutes + " mins" +
               " | Calories Burned: ~" + String.format("%.0f", estimateCaloriesBurned()) +
               (notes.isEmpty() ? "" : " | Notes: " + notes);
    }
}
