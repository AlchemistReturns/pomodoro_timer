package com.abrar.pomodoro_timer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.awt.Toolkit;

public class HelloController {
    @FXML private Label timerLabel;
    @FXML private ProgressBar progressBar;
    @FXML private Button startPauseBtn;
    @FXML private ComboBox<Integer> workInput, breakInput, longBreakInput;
    @FXML private ToggleButton themeToggle;

    private Timeline timeline;
    private boolean isRunning = false;
    private boolean isWorkSession = true;
    private int sessionCounter = 0;

    private int workDuration = 25 * 60;
    private int breakDuration = 5 * 60;
    private int longBreakDuration = 20 * 60;
    private int timeLeft = workDuration;

    @FXML
    public void initialize() {
        for (int i = 1; i <= 60; i++) {
            workInput.getItems().add(i);
            breakInput.getItems().add(i);
            longBreakInput.getItems().add(i);
        }

        workInput.setValue(25);
        breakInput.setValue(5);
        longBreakInput.setValue(20);

        workInput.setOnAction(e -> {
            workDuration = workInput.getValue() * 60;
            if (isWorkSession) resetSession();
        });
        breakInput.setOnAction(e -> breakDuration = breakInput.getValue() * 60);
        longBreakInput.setOnAction(e -> longBreakDuration = longBreakInput.getValue() * 60);

        updateTimerLabel();
        updateProgressBar();
    }

    @FXML
    private void onStartPause() {
        if (isRunning) {
            timeline.stop();
            startPauseBtn.setText("Start");
        } else {
            startCountdown();
            startPauseBtn.setText("Pause");
        }
        isRunning = !isRunning;
    }

    @FXML
    private void onReset() {
        if (timeline != null) timeline.stop();
        isRunning = false;
        isWorkSession = true;
        sessionCounter = 0;
        timeLeft = workDuration;
        startPauseBtn.setText("Start");
        updateTimerLabel();
        updateProgressBar();
    }

    @FXML
    private void onThemeToggle() {
        boolean dark = themeToggle.isSelected();
        Scene scene = timerLabel.getScene();
        scene.getStylesheets().clear();

        String css = dark ? "style-dark.css" : "style-light.css";
        scene.getStylesheets().add(getClass().getResource(css).toExternalForm());

        themeToggle.setText(dark ? "Light Mode" : "Dark Mode");
    }

    private void resetSession() {
        timeLeft = isWorkSession ? workDuration : breakDuration;
        updateTimerLabel();
        updateProgressBar();
    }

    private void startCountdown() {
        int totalDuration = isWorkSession ? workDuration : (sessionCounter == 4 ? longBreakDuration : breakDuration);
        if (timeLeft == 0) timeLeft = totalDuration;

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeLeft--;
            updateTimerLabel();
            updateProgressBar(totalDuration);

            if (timeLeft <= 0) {
                Toolkit.getDefaultToolkit().beep();

                if (isWorkSession) sessionCounter++;
                if (sessionCounter == 4 && !isWorkSession) sessionCounter = 0;

                isWorkSession = !isWorkSession;
                timeLeft = isWorkSession ? workDuration : (sessionCounter == 4 ? longBreakDuration : breakDuration);
                updateTimerLabel();
                updateProgressBar();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateTimerLabel() {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void updateProgressBar(int sessionDuration) {
        progressBar.setProgress((double) (sessionDuration - timeLeft) / sessionDuration);
    }

    private void updateProgressBar() {
        int duration = isWorkSession ? workDuration : (sessionCounter == 4 ? longBreakDuration : breakDuration);
        updateProgressBar(duration);
    }
}
