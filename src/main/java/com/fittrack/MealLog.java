package com.fittrack;

import java.time.LocalDate;

public class MealLog {
    private LocalDate date;
    private String mealName;
    private double proteinGrams;
    private double carbGrams;
    private double fatGrams;
    private double calories;

    public MealLog(String mealName, double proteinGrams, double carbGrams, double fatGrams) {
        if (mealName == null || mealName.trim().isEmpty()) {
            throw new IllegalArgumentException("The meal name cannot be empty");
        }
        if (proteinGrams < 0) {
            throw new IllegalArgumentException("Protein amount cannot be negative");
        }
        if (carbGrams < 0) {
            throw new IllegalArgumentException("Carbs cannot be negative");
        }
        if (fatGrams < 0) {
            throw new IllegalArgumentException("Fat amount cannot be negative");
        }

        this.date = LocalDate.now();
        this.mealName = mealName;
        this.proteinGrams = proteinGrams;
        this.carbGrams = carbGrams;
        this.fatGrams = fatGrams;
        this.calories = (proteinGrams * 4) + (carbGrams * 4) + (fatGrams * 9);
    }

    public LocalDate getDate() { return date; }
    public String getMealName() { return mealName; }
    public double getProteinGrams() { return proteinGrams; }
    public double getCarbGrams() { return carbGrams; }
    public double getFatGrams() { return fatGrams; }
    public double getCalories() { return calories; }

    public String toString() {
        return date + " | Meal: " + mealName +
               " | Protein: " + proteinGrams + "g" +
               " | Carbs: " + carbGrams + "g" +
               " | Fat: " + fatGrams + "g" +
               " | Calories: " + String.format("%.0f", calories);
    }
}
