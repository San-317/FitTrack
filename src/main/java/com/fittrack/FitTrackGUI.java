package com.fittrack;

import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.*;

public class FitTrackGUI extends JFrame {

    // ===== DARK THEME COLORS =====
    private static final Color BG_DARK      = new Color(18, 18, 18);
    private static final Color BG_PANEL     = new Color(28, 28, 30);
    private static final Color BG_CARD      = new Color(38, 38, 40);
    private static final Color ACCENT_GREEN = new Color(50, 215, 75);
    private static final Color ACCENT_BLUE  = new Color(10, 132, 255);
    private static final Color ACCENT_RED   = new Color(255, 69, 58);
    private static final Color ACCENT_ORANGE= new Color(255, 159, 10);
    private static final Color TEXT_PRIMARY = new Color(255, 255, 255);
    private static final Color TEXT_SECONDARY = new Color(170, 170, 175);
    private static final Font  FONT_TITLE   = new Font("Arial", Font.BOLD, 26);
    private static final Font  FONT_HEADING = new Font("Arial", Font.BOLD, 16);
    private static final Font  FONT_BODY    = new Font("Arial", Font.BOLD, 16);
    private static final Font  FONT_SMALL   = new Font("Arial", Font.BOLD, 14);

    private DailyReport report;
    private JTextArea reportArea;
    private ReportHistory reportHistory = new ReportHistory();
    private DefaultListModel<String> historyListModel = new DefaultListModel<>();

    public FitTrackGUI() {
        setTitle("FitTrack — Daily Health Tracker");
        setSize(900, 680);
        setMinimumSize(new Dimension(700, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        showSetupScreen();
        setVisible(true);
    }

    // ===== STYLED BUTTON =====
    private JButton styledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(TEXT_PRIMARY);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return btn;
    }

    // ===== STYLED TEXT FIELD =====
    private JTextField styledField(int cols) {
        JTextField field = new JTextField(cols);
        field.setBackground(BG_CARD);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TEXT_PRIMARY);
        field.setFont(FONT_BODY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 65), 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return field;
    }

    // ===== STYLED COMBO BOX =====
    private JComboBox<String> styledCombo(String[] options) {
        JComboBox<String> box = new JComboBox<>(options);
        box.setBackground(BG_CARD);
        box.setForeground(TEXT_PRIMARY);
        box.setFont(FONT_BODY);
        box.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 65), 1));
        return box;
    }

    // ===== STYLED LABEL =====
    private JLabel styledLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    // ===== STYLED PROGRESS BAR =====
    private JProgressBar styledProgress(Color color) {
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setStringPainted(true);
        bar.setForeground(color);
        bar.setBackground(new Color(50, 50, 55));
        bar.setFont(FONT_SMALL);
        bar.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        bar.setPreferredSize(new Dimension(400, 28));
        return bar;
    }

    // ===== SETUP SCREEN =====
    private void showSetupScreen() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        JPanel wrapper = new JPanel(new GridBagLayout()) {
        @Override
        protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(255, 255, 255, 60));        g2d.setStroke(new BasicStroke(1.2f));

        // Top left corner geometric lines
        for (int i = 0; i < 6; i++) {
            g2d.drawLine(0, i * 40, i * 40, 0);
        }

        // Bottom right corner geometric lines
        int w = getWidth();
        int h = getHeight();
        for (int i = 0; i < 6; i++) {
            g2d.drawLine(w, h - i * 40, w - i * 40, h);
        }

        // Top right circle accent
        g2d.setColor(new Color(50, 215, 75, 60));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawOval(w - 180, -60, 220, 220);
        g2d.drawOval(w - 140, -40, 160, 160);

        // Bottom left circle accent
        g2d.drawOval(-60, h - 160, 200, 200);
        g2d.drawOval(-40, h - 120, 140, 140);
    }
        };
        wrapper.setBackground(BG_DARK);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(BG_PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 65), 1),
            BorderFactory.createEmptyBorder(40, 50, 40, 50)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = styledLabel("🏋️ FitTrack", FONT_TITLE, ACCENT_GREEN);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(title, gbc);

        JLabel subtitle = styledLabel("Build your best self, one day at a time.", FONT_BODY, TEXT_SECONDARY);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        card.add(subtitle, gbc);

        // Separator
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(60, 60, 65));
        gbc.gridy = 2;
        card.add(sep, gbc);

        gbc.gridwidth = 1;

        // Fields
        String[] labels = {"Your Name:", "Age:", "Weight (lbs):", "Hours of Sleep:"};
        JTextField[] fields = new JTextField[4];
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = 3 + i;
            card.add(styledLabel(labels[i], FONT_BODY, TEXT_SECONDARY), gbc);
            fields[i] = styledField(20);
            gbc.gridx = 1;
            card.add(fields[i], gbc);
        }

        // Goal
        gbc.gridx = 0; gbc.gridy = 7;
        card.add(styledLabel("Goal:", FONT_BODY, TEXT_SECONDARY), gbc);
        JComboBox<String> goalBox = styledCombo(new String[]{"maintain - Keep Current Muscle", "bulk - Build Muscle & Size", "cut - Lose Fat & Get Lean"});        gbc.gridx = 1;
        card.add(goalBox, gbc);

        // Start button
        JButton startBtn = styledButton("Start Tracking →", ACCENT_GREEN);
        startBtn.setForeground(Color.BLACK);
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 0, 10);
        card.add(startBtn, gbc);

        startBtn.addActionListener(e -> {
            try {
                String name = fields[0].getText().trim();
                int age = Integer.parseInt(fields[1].getText().trim());
                double weight = Double.parseDouble(fields[2].getText().trim());
                String goal = (String) goalBox.getSelectedItem();
                double sleep = Double.parseDouble(fields[3].getText().trim());

                UserProfile profile = new UserProfile(name, age, weight, goal, sleep);
                report = new DailyReport(profile);
                showMainScreen();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid NUMBERS for age, weight, and sleep.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        wrapper.add(card);
        add(wrapper, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // ===== MAIN SCREEN =====
    private void showMainScreen() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // Header bar
        JPanel header = new JPanel(new BorderLayout()) {
        @Override
        protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(50, 215, 75, 25));
        g2d.setStroke(new BasicStroke(1f));
        // Small diagonal lines on right side of header
        int w = getWidth();
        int h = getHeight();
        for (int i = 0; i < 5; i++) {
            g2d.drawLine(w - 60 + i * 15, 0, w - 60 + i * 15 + h, h);
        }
    }
        };
        header.setBackground(BG_PANEL);
        header.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel appName = styledLabel("🏋️ FitTrack", new Font("Arial", Font.BOLD, 20), ACCENT_GREEN);
        JLabel userName = styledLabel("Welcome, " + report.getProfile().getName() + "  |  Goal: "
                + report.getProfile().getGoal().toUpperCase(), FONT_BODY, TEXT_SECONDARY);

        header.add(appName, BorderLayout.WEST);
        header.add(userName, BorderLayout.EAST);

        // Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(BG_DARK);
        tabs.setForeground(TEXT_PRIMARY);
        tabs.setFont(new Font("Arial", Font.BOLD, 13));
        UIManager.put("TabbedPane.selected", BG_PANEL);
        UIManager.put("TabbedPane.selectedForeground", ACCENT_GREEN);
        UIManager.put("TabbedPane.foreground", TEXT_SECONDARY);
        tabs.updateUI();

        tabs.addTab("🍗  Log Meal", buildMealPanel());
        tabs.addTab("💪  Log Workout", buildWorkoutPanel());
        tabs.addTab("😴  Log Sleep", buildSleepPanel());
        tabs.addTab("📊  Daily Report", buildReportPanel());
        tabs.addTab("📅  History", buildHistoryPanel());
        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // ===== MEAL TAB =====
    private JPanel buildMealPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10)) {
        @Override
        protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g, getWidth(), getHeight());
        }
        };
        panel.setBackground(BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleBar.setBackground(BG_PANEL);
        titleBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 65), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        titleBar.add(styledLabel("🍗  Log a Meal", FONT_HEADING, TEXT_PRIMARY));
        panel.add(titleBar, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG_PANEL);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 65), 1),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Meal Name:", "Protein (g):", "Carbs (g):", "Fat (g):"};
        JTextField[] fields = new JTextField[4];
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            form.add(styledLabel(labels[i], FONT_BODY, TEXT_SECONDARY), gbc);
            fields[i] = styledField(20);
            gbc.gridx = 1;
            form.add(fields[i], gbc);
        }

        JButton addBtn = styledButton("+ Add Meal", ACCENT_GREEN);
        addBtn.setForeground(Color.BLACK);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        form.add(addBtn, gbc);

        JTextArea mealList = new JTextArea(6, 40);
        mealList.setEditable(false);
        mealList.setBackground(BG_CARD);
        mealList.setForeground(TEXT_PRIMARY);
        mealList.setFont(new Font("Monospaced", Font.PLAIN, 12));
        mealList.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JScrollPane scroll = new JScrollPane(mealList);
        scroll.setMinimumSize(new Dimension(400, 100));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 65), 1));

        addBtn.addActionListener(e -> {
            try {
                String name = fields[0].getText().trim();
                double protein = Double.parseDouble(fields[1].getText().trim());
                double carbs = Double.parseDouble(fields[2].getText().trim());
                double fat = Double.parseDouble(fields[3].getText().trim());
                MealLog meal = new MealLog(name, protein, carbs, fat);
                report.addMeal(meal);
                mealList.append("✅ " + meal.toString() + "\n");
                for (JTextField f : fields) f.setText("");
                JOptionPane.showMessageDialog(this, "Meal added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid NUMBERS for protein, carbs, and fat.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(form, BorderLayout.CENTER);
        panel.add(scroll, BorderLayout.SOUTH);
        return panel;
    }

    // ===== WORKOUT TAB =====
    private JPanel buildWorkoutPanel() {
       JPanel panel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBackground(g, getWidth(), getHeight());
            }
        };
        panel.setBackground(BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleBar.setBackground(BG_PANEL);
        titleBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 65), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
    titleBar.add(styledLabel("💪  Log a Workout", FONT_HEADING, TEXT_PRIMARY));
    panel.add(titleBar, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG_PANEL);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 65), 1),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(styledLabel("Workout Type:", FONT_BODY, TEXT_SECONDARY), gbc);
        JComboBox<String> typeBox = styledCombo(new String[]{"weights", "cardio", "both"});
        gbc.gridx = 1;
        form.add(typeBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(styledLabel("Duration (minutes):", FONT_BODY, TEXT_SECONDARY), gbc);
        JTextField durationField = styledField(20);
        gbc.gridx = 1;
        form.add(durationField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        form.add(styledLabel("Notes (optional):", FONT_BODY, TEXT_SECONDARY), gbc);
        JTextField notesField = styledField(20);
        gbc.gridx = 1;
        form.add(notesField, gbc);

        JButton addBtn = styledButton("+ Log Workout", ACCENT_BLUE);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        form.add(addBtn, gbc);

        JTextArea workoutList = new JTextArea(6, 40);
        workoutList.setEditable(false);
        workoutList.setBackground(BG_CARD);
        workoutList.setForeground(TEXT_PRIMARY);
        workoutList.setFont(new Font("Monospaced", Font.PLAIN, 12));
        workoutList.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JScrollPane scroll = new JScrollPane(workoutList);
        scroll.setMinimumSize(new Dimension(400, 100));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 65), 1));

        addBtn.addActionListener(e -> {
            try {
                String type = (String) typeBox.getSelectedItem();
                int duration = Integer.parseInt(durationField.getText().trim());
                String notes = notesField.getText().trim();
                WorkoutLog workout = new WorkoutLog(type, duration, notes);
                report.addWorkout(workout);
                workoutList.append("✅ " + workout.toString() + "\n");
                durationField.setText("");
                notesField.setText("");
                JOptionPane.showMessageDialog(this, "Workout logged successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid NUMBER for duration.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(form, BorderLayout.CENTER);
        panel.add(scroll, BorderLayout.SOUTH);
        return panel;
    }

    // ===== SLEEP TAB =====
    private JPanel buildSleepPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBackground(g, getWidth(), getHeight());
            }
        };
        panel.setBackground(BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleBar.setBackground(BG_PANEL);
        titleBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 65), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        titleBar.add(styledLabel("😴  Log Your Sleep", FONT_HEADING, TEXT_PRIMARY));
        panel.add(titleBar, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG_PANEL);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 65), 1),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(styledLabel("Hours Slept:", FONT_BODY, TEXT_SECONDARY), gbc);
        JTextField hoursField = styledField(20);
        gbc.gridx = 1;
        form.add(hoursField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(styledLabel("Sleep Quality:", FONT_BODY, TEXT_SECONDARY), gbc);
        JComboBox<String> qualityBox = styledCombo(new String[]{"good", "okay", "poor"});
        gbc.gridx = 1;
        form.add(qualityBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        form.add(styledLabel("Notes (optional):", FONT_BODY, TEXT_SECONDARY), gbc);
        JTextField notesField = styledField(20);
        gbc.gridx = 1;
        form.add(notesField, gbc);

        JButton logBtn = styledButton("+ Log Sleep", ACCENT_ORANGE);
        logBtn.setForeground(Color.BLACK);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        form.add(logBtn, gbc);

        JTextArea sleepFeedback = new JTextArea(5, 40);
        sleepFeedback.setEditable(false);
        sleepFeedback.setLineWrap(true);
        sleepFeedback.setWrapStyleWord(true);
        sleepFeedback.setBackground(BG_CARD);
        sleepFeedback.setForeground(TEXT_PRIMARY);
        sleepFeedback.setFont(FONT_BODY);
        sleepFeedback.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scroll = new JScrollPane(sleepFeedback);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 65), 1));

        logBtn.addActionListener(e -> {
            try {
                double hours = Double.parseDouble(hoursField.getText().trim());
                String quality = (String) qualityBox.getSelectedItem();
                String notes = notesField.getText().trim();
                SleepLog sleepLog = new SleepLog(hours, quality, notes);
                report.setSleepLog(sleepLog);
                sleepFeedback.setText("✅ Sleep logged!\n\n" + sleepLog.getFeedback());
                hoursField.setText("");
                notesField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid NUMBER for hours slept.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(form, BorderLayout.CENTER);
        panel.add(scroll, BorderLayout.SOUTH);
        return panel;
    }

    // ===== REPORT TAB =====
    private JPanel buildReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBackground(g, getWidth(), getHeight());
            }
        };
        panel.setBackground(BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleBar.setBackground(BG_PANEL);
        titleBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 65), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        titleBar.add(styledLabel("📊  Your Daily Report", FONT_HEADING, TEXT_PRIMARY));
        panel.add(titleBar, BorderLayout.NORTH);

        // Progress bars
        JPanel barsPanel = new JPanel(new GridLayout(4, 1, 5, 8));
        barsPanel.setBackground(BG_PANEL);
        barsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 65), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JProgressBar calorieProgress = styledProgress(ACCENT_BLUE);
        calorieProgress.setString("Calories: Log meals to see progress");
        JProgressBar proteinProgress = styledProgress(ACCENT_GREEN);
        proteinProgress.setString("Protein: Log meals to see progress");
        JProgressBar carbProgress = styledProgress(ACCENT_ORANGE);
        carbProgress.setString("Carbs: Log meals to see progress");
        JProgressBar fatProgress = styledProgress(ACCENT_RED);
        fatProgress.setString("Fats: Log meals to see progress");

        barsPanel.add(calorieProgress);
        barsPanel.add(proteinProgress);
        barsPanel.add(carbProgress);
        barsPanel.add(fatProgress);

        // Report text area
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setBackground(BG_CARD);
        reportArea.setForeground(TEXT_PRIMARY);
        reportArea.setFont(new Font("Arial", Font.BOLD, 14));
        reportArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scroll = new JScrollPane(reportArea);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 65), 1));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, barsPanel, scroll);
        splitPane.setDividerLocation(160);
        splitPane.setEnabled(false);
        splitPane.setBackground(BG_DARK);
        panel.add(splitPane, BorderLayout.CENTER);

        JPanel bottomBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        bottomBtns.setBackground(BG_DARK);
        JButton refreshBtn = styledButton("🔄  Refresh Report", ACCENT_BLUE);
        JButton saveBtn = styledButton("💾  Save Report", ACCENT_GREEN);
        saveBtn.setForeground(Color.BLACK);
        bottomBtns.add(refreshBtn);
        bottomBtns.add(saveBtn);
        panel.add(bottomBtns, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> {
            String content = reportArea.getText().trim();
            if (content.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please refresh the report first before saving!",
                        "Nothing to Save", JOptionPane.WARNING_MESSAGE);
                return;
            }
            reportHistory.saveReport(content);
            refreshHistoryList();
            JOptionPane.showMessageDialog(this,
            "Report saved to history successfully!",
            "Saved!", JOptionPane.INFORMATION_MESSAGE);
        });

        refreshBtn.addActionListener(e -> {
            reportArea.setText(report.getSummary() + "\n" + report.getCoachingMessage());

            double targetCalories = NutritionCalculator.calculateCalories(report.getProfile());
            double targetProtein  = NutritionCalculator.calculateProtein(report.getProfile());
            double targetCarbs    = NutritionCalculator.calculateCarbs(report.getProfile());
            double targetFats     = NutritionCalculator.calculateFats(report.getProfile());

            int calPct  = (int) Math.min(100, (report.getTotalCaloriesConsumed() / targetCalories) * 100);
            int protPct = (int) Math.min(100, (report.getTotalProtein() / targetProtein) * 100);
            int carbPct = (int) Math.min(100, (report.getTotalCarbs() / targetCarbs) * 100);
            int fatPct  = (int) Math.min(100, (report.getTotalFats() / targetFats) * 100);

            calorieProgress.setValue(calPct);
            calorieProgress.setString(String.format("🔥 Calories: %.0f / %.0f kcal (%d%%)",
                    report.getTotalCaloriesConsumed(), targetCalories, calPct));
            calorieProgress.setForeground(calPct >= 90 ? ACCENT_GREEN : ACCENT_BLUE);

            proteinProgress.setValue(protPct);
            proteinProgress.setString(String.format("🥩 Protein: %.1f / %.1f g (%d%%)",
                    report.getTotalProtein(), targetProtein, protPct));
            proteinProgress.setForeground(protPct >= 90 ? ACCENT_GREEN : ACCENT_RED);

            carbProgress.setValue(carbPct);
            carbProgress.setString(String.format("🍚 Carbs: %.1f / %.1f g (%d%%)",
                    report.getTotalCarbs(), targetCarbs, carbPct));
            carbProgress.setForeground(carbPct >= 90 ? ACCENT_GREEN : ACCENT_ORANGE);

            fatProgress.setValue(fatPct);
            fatProgress.setString(String.format("🥑 Fats: %.1f / %.1f g (%d%%)",
                    report.getTotalFats(), targetFats, fatPct));
            fatProgress.setForeground(fatPct >= 90 ? ACCENT_GREEN : ACCENT_RED);
        });

        return panel;
    }

    // Refresh the history list display
private void refreshHistoryList() {
    historyListModel.clear();
    java.util.List<String> reports = reportHistory.getSavedReports();
    for (int i = 0; i < reports.size(); i++) {
        // Show just the date line as the list item
        String report = reports.get(i);
        String firstLine = report.split("\n")[0];
        historyListModel.addElement("Report " + (i + 1) + " — " + firstLine.replace("SAVED ON: ", ""));
    }
}

// ===== HISTORY TAB =====
private JPanel buildHistoryPanel() {
    JPanel panel = new JPanel(new BorderLayout(10, 10)) {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawBackground(g, getWidth(), getHeight());
        }
    };
    panel.setBackground(BG_DARK);
    panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

    // Title bar
    JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
    titleBar.setBackground(BG_PANEL);
    titleBar.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(60, 60, 65), 1),
        BorderFactory.createEmptyBorder(8, 15, 8, 15)
    ));
    titleBar.add(styledLabel("📅  Report History", FONT_HEADING, TEXT_PRIMARY));
    panel.add(titleBar, BorderLayout.NORTH);

    // Split — left is list, right is detail view
    JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
    leftPanel.setBackground(BG_PANEL);
    leftPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(60, 60, 65), 1),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)
    ));

    leftPanel.add(styledLabel("Saved Reports", new Font("Arial", Font.BOLD, 13),
            ACCENT_GREEN), BorderLayout.NORTH);

    refreshHistoryList();
    JList<String> historyList = new JList<>(historyListModel);
    historyList.setBackground(BG_CARD);
    historyList.setForeground(TEXT_PRIMARY);
    historyList.setFont(FONT_BODY);
    historyList.setSelectionBackground(ACCENT_BLUE);
    historyList.setSelectionForeground(Color.WHITE);
    historyList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    JScrollPane listScroll = new JScrollPane(historyList);
    listScroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 65), 1));
    leftPanel.add(listScroll, BorderLayout.CENTER);

    // Right panel — detail view
    JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
    rightPanel.setBackground(BG_PANEL);
    rightPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(60, 60, 65), 1),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)
    ));

    rightPanel.add(styledLabel("Report Detail", new Font("Arial", Font.BOLD, 13),
            ACCENT_GREEN), BorderLayout.NORTH);

    JTextArea detailArea = new JTextArea();
    detailArea.setEditable(false);
    detailArea.setBackground(BG_CARD);
    detailArea.setForeground(TEXT_PRIMARY);
    detailArea.setFont(new Font("Arial", Font.PLAIN, 13));
    detailArea.setLineWrap(true);
    detailArea.setWrapStyleWord(true);
    detailArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    JScrollPane detailScroll = new JScrollPane(detailArea);
    detailScroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 65), 1));
    rightPanel.add(detailScroll, BorderLayout.CENTER);

    // When user clicks a report in the list, show its detail
    historyList.addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
            int idx = historyList.getSelectedIndex();
            if (idx >= 0 && idx < reportHistory.getSavedReports().size()) {
                detailArea.setText(reportHistory.getSavedReports().get(idx));
                detailArea.setCaretPosition(0);
            }
        }
    });

    // Split pane
    JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
    split.setDividerLocation(250);
    split.setBackground(BG_DARK);
    panel.add(split, BorderLayout.CENTER);

    // Bottom buttons
    JPanel bottomBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
    bottomBtns.setBackground(BG_DARK);

    JButton deleteBtn = styledButton("🗑️  Delete Selected", ACCENT_RED);
    JButton deleteAllBtn = styledButton("❌  Delete All", new Color(100, 30, 30));

    bottomBtns.add(deleteBtn);
    bottomBtns.add(deleteAllBtn);
    panel.add(bottomBtns, BorderLayout.SOUTH);

    deleteBtn.addActionListener(e -> {
        int idx = historyList.getSelectedIndex();
        if (idx == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a report to delete first!",
                    "Nothing Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this report?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            reportHistory.deleteReport(idx);
            refreshHistoryList();
            detailArea.setText("");
        }
    });

    deleteAllBtn.addActionListener(e -> {
        if (reportHistory.getCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No reports to delete!",
                    "Nothing to Delete", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete ALL reports? This cannot be undone!",
                "Confirm Delete All", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            reportHistory.deleteAllReports();
            refreshHistoryList();
            detailArea.setText("");
        }
    });

    return panel;
}

    // ===== SHARED BACKGROUND PAINTER =====
private void drawBackground(Graphics g, int w, int h) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Thick white geometric lines top left
    g2d.setStroke(new BasicStroke(2.5f));
    g2d.setColor(new Color(255, 255, 255, 120));
    for (int i = 0; i < 10; i++) {
        g2d.drawLine(0, i * 55, i * 55, 0);
    }

    // Thick white geometric lines bottom right
    for (int i = 0; i < 10; i++) {
        g2d.drawLine(w, h - i * 55, w - i * 55, h);
    }

    // Bold green circles top right
    g2d.setStroke(new BasicStroke(3f));
    g2d.setColor(new Color(50, 215, 75, 130));
    g2d.drawOval(w - 200, -80, 260, 260);
    g2d.drawOval(w - 155, -55, 190, 190);
    g2d.drawOval(w - 110, -30, 120, 120);

    // Bold green circles bottom left
    g2d.drawOval(-80, h - 200, 260, 260);
    g2d.drawOval(-55, h - 155, 190, 190);
    g2d.drawOval(-30, h - 110, 120, 120);

    // Extra white cross lines center-right for depth
    g2d.setStroke(new BasicStroke(1.5f));
    g2d.setColor(new Color(255, 255, 255, 40));
    g2d.drawLine(w - 100, 0, w - 100, h);
    g2d.drawLine(w - 60, 0, w - 60, h);
    g2d.drawLine(0, h - 80, w, h - 80);
}
}

