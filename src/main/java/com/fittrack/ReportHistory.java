package com.fittrack;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportHistory {
    private static final String HISTORY_FILE = "report_history.txt";
    private List<String> savedReports;

    public ReportHistory() {
        savedReports = new ArrayList<>();
        loadFromFile();
    }

    // Save a report entry
    public void saveReport(String reportText) {
        if (reportText == null || reportText.trim().isEmpty()) {
            throw new IllegalArgumentException("Report cannot be empty");
        }
        String entry = "SAVED ON: " + LocalDate.now() + "\n"
                + reportText + "\n";
        savedReports.add(entry);
        writeToFile();
    }

    // Delete a report by index
    public void deleteReport(int index) {
        if (index < 0 || index >= savedReports.size()) {
            throw new IllegalArgumentException("Invalid report index");
        }
        savedReports.remove(index);
        writeToFile();
    }

    // Delete all reports
    public void deleteAllReports() {
        savedReports.clear();
        writeToFile();
    }

    // Get all saved reports
    public List<String> getSavedReports() {
        return savedReports;
    }

    // Get count
    public int getCount() {
        return savedReports.size();
    }

    // Write all reports to file
    private void writeToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(HISTORY_FILE, false))) {
            for (String report : savedReports) {
                // Use a delimiter to separate reports in the file
                writer.println("---REPORT_START---");
                writer.print(report);
                writer.println("---REPORT_END---");
            }
        } catch (Exception e) {
            System.out.println("Error saving report history: " + e.getMessage());
        }
    }

    // Load reports from file
    private void loadFromFile() {
        File file = new File(HISTORY_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder current = new StringBuilder();
            String line;
            boolean reading = false;

            while ((line = reader.readLine()) != null) {
                if (line.equals("---REPORT_START---")) {
                    reading = true;
                    current = new StringBuilder();
                } else if (line.equals("---REPORT_END---")) {
                    if (reading) {
                        savedReports.add(current.toString());
                        reading = false;
                    }
                } else if (reading) {
                    current.append(line).append("\n");
                }
            }
        } catch (Exception e) {
            System.out.println("There is no previous report history found.");
        }
    }
}
