package com.fittrack;

import java.time.LocalDate;

public class SleepLog {
    private LocalDate date;
    private double hoursSlept;
    private String quality; // "good", "okay", "poor"
    private String notes;

    public SleepLog(double hoursSlept, String quality, String notes) {
        if (hoursSlept < 0 || hoursSlept > 24) {
            throw new IllegalArgumentException("The total hours slept must be between 0 and 24");
        }
        if (!quality.equalsIgnoreCase("good") &&
            !quality.equalsIgnoreCase("okay") &&
            !quality.equalsIgnoreCase("poor")) {
            throw new IllegalArgumentException("Quality must be good, okay, or poor");
        }

        this.date = LocalDate.now();
        this.hoursSlept = hoursSlept;
        this.quality = quality.toLowerCase();
        this.notes = (notes == null) ? "" : notes;
    }

    public LocalDate getDate() { return date; }
    public double getHoursSlept() { return hoursSlept; }
    public String getQuality() { return quality; }
    public String getNotes() { return notes; }

    // Give feedback based on hours and quality
    public String getFeedback() {
        if (hoursSlept < 6) {
            return "Critical: Severely under-slept. Muscle recovery and performance will suffer significantly.";
        } else if (hoursSlept < 7) {
            if (quality.equals("poor")) {
                return "Suggestion: Low sleep hours and poor quality. Prioritize rest tonight.";
            }
            return "Fair: Slightly under optimal. Try to get at least 7 hours for better recovery.";
        } else if (hoursSlept <= 9) {
            if (quality.equals("good")) {
                return "Excellent: Great sleep! Your body is well recovered and ready to perform.";
            } else if (quality.equals("okay")) {
                return "Good: Decent sleep. Consider a consistent bedtime to improve quality.";
            } else {
                return "Note: Enough hours but poor quality. You can try limiting screen time before bed.";
            }
        } else {
            return "Note: Over 9 hours. If you feel tired often, consider checking in with a doctor.";
        }
    }

    public String toString() {
        return date + " | Hours Slept: " + hoursSlept +
               " | Quality: " + quality +
               " | Feedback: " + getFeedback() +
               (notes.isEmpty() ? "" : " | Notes: " + notes);
    }
}
