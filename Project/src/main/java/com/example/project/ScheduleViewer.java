package com.example.project;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class ScheduleViewer {

    private final DatabaseHandler databaseHandler;
    private final List<Schedule> schedule;
    private final String dentistUsername;

    public ScheduleViewer(DatabaseHandler databaseHandler, List<Schedule> schedule, String dentistUsername) {
        this.databaseHandler = databaseHandler;
        this.schedule = schedule;
        this.dentistUsername = dentistUsername;
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Schedule Viewer");

        VBox layout = new VBox(10);
        Scene scene = new Scene(layout, 400, 300);

        Label titleLabel = new Label("Schedule for Dentist: " + dentistUsername);
        layout.getChildren().add(titleLabel);

        for (Schedule appointment : schedule) {
            // Додайте елементи інтерфейсу для кожного призначення, наприклад, інформація про дату, час, проблему тощо.
            Label appointmentLabel = new Label("Date: " + appointment.getDate() + ", Time: " + appointment.getTime() + ", Problem: " + appointment.getProblemDescription());
            layout.getChildren().add(appointmentLabel);
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
