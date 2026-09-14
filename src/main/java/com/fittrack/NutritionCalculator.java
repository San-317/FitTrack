package com.fittrack;

public class NutritionCalculator {

    // Calculate daily calories based on weight and goal
    public static double calculateCalories(UserProfile profile) {
        double weight = profile.getWeightLbs();
        String goal = profile.getGoal();

        // Base calories using body weight (standard fitness formula)
        double baseCalories = weight * 15;

        switch (goal) {
            case "bulk":     return baseCalories + 500;
            case "cut":      return baseCalories - 500;
            case "maintain": return baseCalories;
            default:         return baseCalories;
        }
    }

    // Calculate daily protein grams (1g per lb of bodyweight minimum)
    public static double calculateProtein(UserProfile profile) {
        String goal = profile.getGoal();
        double weight = profile.getWeightLbs();

        switch (goal) {
            case "bulk":     return weight * 1.2;
            case "cut":      return weight * 1.0;
            case "maintain": return weight * 1.0;
            default:         return weight * 1.0;
        }
    }

    // Calculate daily carbs in grams
    public static double calculateCarbs(UserProfile profile) {
        double calories = calculateCalories(profile);
        double proteinCalories = calculateProtein(profile) * 4;
        double fatCalories = calculateFats(profile) * 9;
        double carbCalories = calories - proteinCalories - fatCalories;
        return Math.max(0, carbCalories / 4);
    }

    // Calculate daily fats in grams
    public static double calculateFats(UserProfile profile) {
        double weight = profile.getWeightLbs();
        return weight * 0.4;
    }

    // Give a sleep recommendation based on hours slept
    public static String sleepRecommendation(UserProfile profile) {
        double sleep = profile.getHoursOfSleep();

        if (sleep < 6) {
            return "Suggestion: Less than 6 hours hurts muscle recovery. Aim for 7-9 hours.";
        } else if (sleep >= 6 && sleep < 7) {
            return "Fair: You could benefit from a bit more sleep for optimal gains.";
        } else if (sleep >= 7 && sleep <= 9) {
            return "Great: You are in the optimal sleep range for recovery and performance!";
        } else {
            return "Note: Over 9 hours may indicate fatigue. Monitor your energy levels.";
        }
    }
}