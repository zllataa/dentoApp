package com.example.project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.geometry.VPos;
import javafx.geometry.HPos;


public class DentistRegistrationForm extends Application {

    private final DatabaseHandler databaseHandler;

    public DentistRegistrationForm() {
        // Ініціалізуємо об'єкт DatabaseHandler у конструкторі за замовчуванням
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
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        // Додавання заголовку
        Label titleLabel = new Label("Реєстрація в персональному кабінеті DenTo");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        grid.add(titleLabel, 0, 0, 2, 1);
        GridPane.setHalignment(titleLabel, javafx.geometry.HPos.CENTER);
        GridPane.setMargin(titleLabel, new Insets(0, 0, 50, 0)); // Встановлення відступу

        // Додавання елементів на форму
        Label firstNameLbl = new Label("Ім'я:");
        TextField firstNameInput = new TextField();
        grid.add(firstNameLbl, 0, 1);
        grid.add(firstNameInput, 1, 1);

        Label lastNameLbl = new Label("Прізвище:");
        TextField lastNameInput = new TextField();
        grid.add(lastNameLbl, 0, 2);
        grid.add(lastNameInput, 1, 2);

        Label phoneNumberLbl = new Label("Номер телефону:");
        TextField phoneNumberInput = new TextField();
        grid.add(phoneNumberLbl, 0, 3);
        grid.add(phoneNumberInput, 1, 3);

        Label usernameLbl = new Label("Ім'я користувача:");
        TextField usernameInput = new TextField();
        grid.add(usernameLbl, 0, 4);
        grid.add(usernameInput, 1, 4);

        Label passwordLbl = new Label("Пароль:");
        PasswordField passwordInput = new PasswordField();
        grid.add(passwordLbl, 0, 5);
        grid.add(passwordInput, 1, 5);

        Button registerBtn = new Button("Реєстрація");
        grid.add(registerBtn, 1, 6);
        grid.setConstraints(registerBtn, 1, 6, 1, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setMargin(registerBtn, new Insets(20, 0, 0, 20)); // Встановлення відступу

        Button returnBtn = new Button("Повернутися до авторизації");
        grid.add(returnBtn, 0, 6);
        grid.setConstraints(returnBtn, 0, 6, 1, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setMargin(returnBtn, new Insets(20, 0, 0, 20)); // Встановлення відступу

        // Логіка кнопки "Повернутися"
        returnBtn.setOnAction(e -> {
            DentistLoginForm dentistLoginForm = new DentistLoginForm();
            dentistLoginForm.start(primaryStage);
        });

        // Логіка кнопки "Реєстрація"
        registerBtn.setOnAction(e -> {
            // Отримайте дані для реєстрації
            String firstName = firstNameInput.getText();
            String lastName = lastNameInput.getText();
            String phoneNumber = phoneNumberInput.getText();
            String username = usernameInput.getText();
            String password = passwordInput.getText();

            // Валідація введених даних
            if (firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty() || username.isEmpty() || password.isEmpty()) {
                showAlert("Будь ласка, заповніть всі поля.");
                return;
            }

            // Викликайте метод реєстрації лікаря в DatabaseHandler
            boolean registrationSuccess = databaseHandler.registerDentists(firstName, lastName, phoneNumber, username, password);

            if (registrationSuccess) {
                // Якщо реєстрація пройшла успішно, виведіть повідомлення та закрийте вікно
                showAlert("Реєстрація успішна");
                primaryStage.close();
            } else {
                // Якщо реєстрація не вдалася, виведіть повідомлення про помилку
                showAlert("Помилка реєстрації. Будь ласка, спробуйте ще раз.");
            }
        });

        Scene scene = new Scene(grid, 900, 800);
        primaryStage.setScene(scene);

        // Центрування вікна на екрані
        primaryStage.centerOnScreen();

        primaryStage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Інформація");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
