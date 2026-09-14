package com.fittrack;

import java.util.ArrayList;
import java.util.List;

public class DailyReport {
    private UserProfile profile;
    private List<MealLog> meals;
    private List<WorkoutLog> workouts;
    private SleepLog sleepLog;

    public DailyReport(UserProfile profile) {
        if (profile == null) {
            throw new IllegalArgumentException("Profile cannot be null");
        }
        this.profile = profile;
        this.meals = new ArrayList<>();
        this.workouts = new ArrayList<>();
        this.sleepLog = null;
    }

    public void addMeal(MealLog meal) {
        if (meal == null) throw new IllegalArgumentException("Meal cannot be null");
        meals.add(meal);
    }

    public void addWorkout(WorkoutLog workout) {
        if (workout == null) throw new IllegalArgumentException("Workout cannot be null");
        workouts.add(workout);
    }

    public void setSleepLog(SleepLog sleepLog) {
        if (sleepLog == null) throw new IllegalArgumentException("Sleep log cannot be null");
        this.sleepLog = sleepLog;
    }

    // Total protein consumed today
    public double getTotalProtein() {
        double total = 0;
        for (MealLog meal : meals) {
            total += meal.getProteinGrams();
        }
        return total;
    }

    // Total carbs consumed today
    public double getTotalCarbs() {
        double total = 0;
        for (MealLog meal : meals) {
            total += meal.getCarbGrams();
        }
        return total;
    }

    // Total fats consumed today
    public double getTotalFats() {
        double total = 0;
        for (MealLog meal : meals) {
            total += meal.getFatGrams();
        }
        return total;
    }

    // Total calories consumed today
    public double getTotalCaloriesConsumed() {
        double total = 0;
        for (MealLog meal : meals) {
            total += meal.getCalories();
        }
        return total;
    }

    // Total calories burned today
    public double getTotalCaloriesBurned() {
        double total = 0;
        for (WorkoutLog workout : workouts) {
            total += workout.estimateCaloriesBurned();
        }
        return total;
    }

    // Total workout minutes today
    public int getTotalWorkoutMinutes() {
        int total = 0;
        for (WorkoutLog workout : workouts) {
            total += workout.getDurationMinutes();
        }
        return total;
    }

    public List<MealLog> getMeals() { return meals; }
    public List<WorkoutLog> getWorkouts() { return workouts; }
    public SleepLog getSleepLog() { return sleepLog; }
    public UserProfile getProfile() { return profile; }

    // Full summary of the day
    public String getSummary() {
        double targetCalories = NutritionCalculator.calculateCalories(profile);
        double targetProtein = NutritionCalculator.calculateProtein(profile);
        double targetCarbs = NutritionCalculator.calculateCarbs(profile);
        double targetFats = NutritionCalculator.calculateFats(profile);

        StringBuilder sb = new StringBuilder();
        sb.append("===== DAILY REPORT =====\n");
        sb.append("Profile: ").append(profile.toString()).append("\n\n");

        sb.append("--- NUTRITION ---\n");
        sb.append(String.format("Calories:  %.0f / %.0f kcal\n",
                getTotalCaloriesConsumed(), targetCalories));
        sb.append(String.format("Protein:   %.1f / %.1f g\n",
                getTotalProtein(), targetProtein));
        sb.append(String.format("Carbs:     %.1f / %.1f g\n",
                getTotalCarbs(), targetCarbs));
        sb.append(String.format("Fats:      %.1f / %.1f g\n",
                getTotalFats(), targetFats));

        sb.append("\n--- WORKOUTS ---\n");
        if (workouts.isEmpty()) {
            sb.append("There was no workouts logged today.\n");
        } else {
            for (WorkoutLog w : workouts) {
                sb.append(w.toString()).append("\n");
            }
            sb.append(String.format("Total workout time: %d mins | Calories burned: ~%.0f\n",
                    getTotalWorkoutMinutes(), getTotalCaloriesBurned()));
        }

        sb.append("\n--- SLEEP ---\n");
        if (sleepLog == null) {
            sb.append("There was No sleep logged yet.\n");
        } else {
            sb.append(sleepLog.toString()).append("\n");
        }

        return sb.toString();
    }

    // Smart coaching message combining sleep + nutrition + workout
    public String getCoachingMessage() {
    double targetProtein = NutritionCalculator.calculateProtein(profile);
    double targetCalories = NutritionCalculator.calculateCalories(profile);
    double totalProtein = getTotalProtein();
    double totalCalories = getTotalCaloriesConsumed();
    int workoutMins = getTotalWorkoutMinutes();
    double sleepHours = (sleepLog != null) ? sleepLog.getHoursSlept() : 0;

    StringBuilder msg = new StringBuilder();
    msg.append("===== SMART COACHING =====\n");

    // Sleep + Workout recovery warning
    if (sleepHours > 0 && sleepHours < 7 && workoutMins >= 60) {
        msg.append("⚠️  RECOVERY ALERT: You trained hard (")
           .append(workoutMins).append(" mins) but only slept ")
           .append(sleepHours).append(" hrs. Your muscles recover during sleep — ")
           .append("prioritize 7-9 hours tonight or your gains will suffer.\n\n");
    }

    // Protein feedback
    if (totalProtein >= targetProtein) {
        msg.append("✅ Protein GOAL MET! Great job hitting ")
           .append(String.format("%.1f", totalProtein)).append("g today.\n");
    } else {
        double remaining = targetProtein - totalProtein;
        msg.append("❌ Protein: You still need ")
           .append(String.format("%.1f", remaining))
           .append("g more protein today. Try adding chicken, eggs, or Greek yogurt.\n");
    }

    // Calorie feedback
    if (totalCalories >= targetCalories * 0.9 && totalCalories <= targetCalories * 1.1) {
        msg.append("✅ Calories on track! You are within your daily target.\n");
    } else if (totalCalories < targetCalories * 0.9) {
        msg.append("⚠️  Calories: You are under your target. Eat more to fuel your goal.\n");
    } else {
        msg.append("⚠️  Calories: You are over your target today. Consider lighter meals tomorrow.\n");
    }

    // Workout feedback
    if (workoutMins == 0) {
        msg.append("❌ No workout logged today. Even a 30 min walk counts!\n");
    } else if (workoutMins < 30) {
        msg.append("⚠️  Short workout today. Try to hit at least 30-45 mins for best results.\n");
    } else {
        msg.append("✅ Workout DONE! Great work putting in ").append(workoutMins).append(" mins.\n");
    }

    // Sleep feedback
    if (sleepHours == 0) {
        msg.append("⚠️  No sleep logged yet.\n");
    } else if (sleepHours < 7) {
        msg.append("⚠️  Sleep: Under 7 hours hurts recovery and performance. Rest up tonight!\n");
    } else {
        msg.append("✅ Sleep looks good! Keep that consistent sleep schedule going.\n");
    }

    // Overall message
    msg.append("\n--- OVERALL ---\n");
    boolean proteinMet = totalProtein >= targetProtein;
    boolean caloriesMet = totalCalories >= targetCalories * 0.9;
    boolean workoutDone = workoutMins >= 30;
    boolean sleepGood = sleepHours >= 7;

    int score = (proteinMet ? 1 : 0) + (caloriesMet ? 1 : 0) +
                (workoutDone ? 1 : 0) + (sleepGood ? 1 : 0);

    if (score == 4) {
        msg.append("🏆 PERFECT DAY! You nailed nutrition, workout, and sleep. Keep it up!\n");
    } else if (score == 3) {
        msg.append("💪 Strong day! You hit 3 out of 4 goals. Almost there!\n");
    } else if (score == 2) {
        msg.append("📈 Decent day. 2 out of 4 goals hit. Focus on the gaps tomorrow.\n");
    } else {
        msg.append("💡 Tough day. Don't give up — small steps every day add up!\n");
    }

    return msg.toString();
}
}
