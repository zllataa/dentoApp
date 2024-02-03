package com.example.project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DentistLoginForm extends Application {

    private final DatabaseHandler databaseHandler;

    public DentistLoginForm() {
        try {
            this.databaseHandler = new DatabaseHandler(initConnection());
        } catch (SQLException e) {
            throw new RuntimeException("Unable to initialize DatabaseHandler", e);
        }
    }

    private Connection initConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String userName = "postgres";
        String pass = "2264";
        return DriverManager.getConnection(url, userName, pass);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("DenTo");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);

        // Додавання заголовку
        Label titleLabel = new Label("Персональний кабінет DenTo");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        grid.add(titleLabel, 0, 0, 2, 1);
        GridPane.setHalignment(titleLabel, javafx.geometry.HPos.CENTER);
        GridPane.setMargin(titleLabel, new Insets(0, 0, 50, 0)); // Встановлення відступу

        // Додавання елементів на форму
        Label usernameLbl = new Label("Імʼя користувача:");
        TextField usernameInput = new TextField();
        grid.add(usernameLbl, 0, 1);
        grid.add(usernameInput, 1, 1);

        Label passwordLbl = new Label("Пароль:");
        PasswordField passwordInput = new PasswordField();
        grid.add(passwordLbl, 0, 2);
        grid.add(passwordInput, 1, 2);

        Button loginBtn = new Button("  Ввійти  ");
        Button registerBtn = new Button("Зареєструватися");

        // Додавання елементів та визначення відступів і центрування
        grid.add(loginBtn, 0, 3);
        grid.add(registerBtn, 1, 3);

        GridPane.setConstraints(loginBtn, 0, 3);
        GridPane.setConstraints(registerBtn, 1, 3);

        GridPane.setMargin(loginBtn, new Insets(20, 10, 0, 50)); // Встановлення відступу
        GridPane.setMargin(registerBtn, new Insets(20, 10, 0, 10)); // Встановлення відступу

        // Визначення горизонтального центрування
        GridPane.setHalignment(loginBtn, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(registerBtn, javafx.geometry.HPos.CENTER);

        loginBtn.setOnAction(e -> {
            String username = usernameInput.getText();
            String password = passwordInput.getText();

            boolean loginSuccess = databaseHandler.loginDentists(username, password);

            if (loginSuccess) {
                // Відобразити персональний кабінет
                showDentistDashboard(username); // Припускається, що username є ідентифікатором лікаря
            } else {
                // Повідомлення про невдалий вхід
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid username or password. Please try again.");
                alert.showAndWait();
            }
        });

        registerBtn.setOnAction(e -> {
            DentistRegistrationForm dentistRegistrationForm = new DentistRegistrationForm();
            dentistRegistrationForm.start(primaryStage);
        });

        Scene scene = new Scene(grid, 900, 800);
        primaryStage.setScene(scene);

        // Центрування вікна на екрані
        primaryStage.centerOnScreen();

        primaryStage.show();
    }

    private void showDentistDashboard(String dentistUsername) {
        DentistDashboard dentistDashboard = new DentistDashboard(dentistUsername, databaseHandler);
        dentistDashboard.start(new Stage());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
