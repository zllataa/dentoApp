package com.example.project;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        DentistLoginForm dentistLoginForm = new DentistLoginForm();
        dentistLoginForm.start(primaryStage);
    }
}

