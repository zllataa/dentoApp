package com.example.project;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class PatientsManagement {

    private final DatabaseHandler databaseHandler;
    private final List<Patient> patients;
    private final String dentistUsername;

    public PatientsManagement(DatabaseHandler databaseHandler, List<Patient> patients, String dentistUsername) {
        this.databaseHandler = databaseHandler;
        this.patients = patients;
        this.dentistUsername = dentistUsername;
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Patients Management");

        VBox layout = new VBox(10);
        Scene scene = new Scene(layout, 400, 300);

        Label titleLabel = new Label("Patients List");
        layout.getChildren().add(titleLabel);

        for (Patient patient : patients) {
            // Додайте елементи інтерфейсу для кожного пацієнта, наприклад, інформація про ім'я, прізвище, тощо.
            Label patientLabel = new Label(patient.getFirstName() + " " + patient.getLastName());
            layout.getChildren().add(patientLabel);
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
