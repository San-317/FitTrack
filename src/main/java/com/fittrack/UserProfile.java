package com.fittrack;

public class UserProfile {
    private String name;
    private int age;
    private double weightLbs;
    private String goal; // "bulk", "cut", "maintain"
    private double hoursOfSleep;

    public UserProfile(String name, int age, double weightLbs, String goal, double hoursOfSleep) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (age <= 0 || age > 80) {
            throw new IllegalArgumentException("Age must be between 1 and 80");
        }
        if (weightLbs < 50 || weightLbs > 300) {
            throw new IllegalArgumentException("Weight must be between 50 and 300 lbs");

        }
        if (!goal.toLowerCase().contains("bulk") &&
            !goal.toLowerCase().contains("cut") &&
            !goal.toLowerCase().contains("maintain")) {
            throw new IllegalArgumentException("Goal must be bulk, cut, or maintain");
        }
        if (hoursOfSleep < 0 || hoursOfSleep > 24) {
            throw new IllegalArgumentException("Sleep hours must be between 0 and 24");
        }

        this.name = name;
        this.age = age;
        this.weightLbs = weightLbs;
        this.hoursOfSleep = hoursOfSleep;

        if (goal.toLowerCase().contains("bulk")) this.goal = "bulk";
        else if (goal.toLowerCase().contains("cut")) this.goal = "cut";
        else this.goal = "maintain";
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public double getWeightLbs() { return weightLbs; }
    public String getGoal() { return goal; }
    public double getHoursOfSleep() { return hoursOfSleep; }

    public void setWeightLbs(double weightLbs) {
        if (weightLbs <= 0) throw new IllegalArgumentException("Weight must be positive");
        this.weightLbs = weightLbs;
    }

    public void setGoal(String goal) {
        if (!goal.toLowerCase().contains("bulk") &&
            !goal.toLowerCase().contains("cut") &&
            !goal.toLowerCase().contains("maintain")) {
            throw new IllegalArgumentException("Goal must be bulk, cut, or maintain");
        }
        if (goal.toLowerCase().contains("bulk")) this.goal = "bulk";
        else if (goal.toLowerCase().contains("cut")) this.goal = "cut";
        else this.goal = "maintain";
    }

    public void setHoursOfSleep(double hoursOfSleep) {
        if (hoursOfSleep < 0 || hoursOfSleep > 24) {
            throw new IllegalArgumentException("Sleep hours must be between 0 and 24");
        }
        this.hoursOfSleep = hoursOfSleep;
    }

    public String toString() {
        return "Name: " + name + " | Age: " + age +
               " | Weight: " + weightLbs + " lbs | Goal: " + goal +
               " | Sleep: " + hoursOfSleep + " hrs";
    }
}

