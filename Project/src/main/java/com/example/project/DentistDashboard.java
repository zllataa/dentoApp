package com.example.project;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DentistDashboard extends Application {

    private final String dentistUsername;
    private final DatabaseHandler databaseHandler;

    private TableView<Patient> patientTableView;

    public DentistDashboard(String dentistUsername, DatabaseHandler databaseHandler) {
        this.dentistUsername = dentistUsername;
        this.databaseHandler = databaseHandler;
    }

    public DentistDashboard() {
        this("defaultUsername", new DatabaseHandler());
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("DenTo - Персональний кабінет");

        HBox dentistInfoBox = createDentistInfoBox();

        VBox addPatientForm = createAddPatientForm();

        patientTableView = createPatientTableView();
        patientTableView.setPlaceholder(new Label("Немає доступних пацієнтів"));
        patientTableView.setMaxHeight(Double.MAX_VALUE);

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 20, 20, 20));

        TextField searchField = new TextField();
        Button searchButton = new Button("Пошук");
        searchButton.setOnAction(e -> handleSearchButtonClick(searchField.getText()));

        Button resetButton = new Button("Скинути");
        resetButton.setOnAction(e -> resetPatientTable());

        TextField deleteCodeField = new TextField();
        Button deleteButton = new Button("Видалення");
        deleteButton.setOnAction(e -> handleDeleteButtonClick(deleteCodeField.getText()));

        gridPane.add(dentistInfoBox, 0, 0, 3, 1);
        gridPane.add(addPatientForm, 0, 1);
        gridPane.add(patientTableView, 2, 1);
        gridPane.add(createSearchAndDeleteForm(searchField, searchButton, deleteCodeField, deleteButton, resetButton), 3, 1);

        Scene scene = new Scene(gridPane, 1200, 900);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private TableView<Patient> createPatientTableView() {
        TableView<Patient> tableView = new TableView<>();
        tableView.setPrefWidth(500);

        List<Patient> patients = databaseHandler.getPatientsForDentist(dentistUsername);

        TableColumn<Patient, Integer> codeCol = new TableColumn<>("Код");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));

        TableColumn<Patient, String> firstNameCol = new TableColumn<>("Ім'я");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Patient, String> lastNameCol = new TableColumn<>("Прізвище");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Patient, String> addressCol = new TableColumn<>("Адреса");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Patient, String> phoneNumberCol = new TableColumn<>("Номер телефону");
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        tableView.getColumns().addAll(codeCol, firstNameCol, lastNameCol, addressCol, phoneNumberCol);

        ObservableList<Patient> data = FXCollections.observableArrayList(patients);
        tableView.setItems(data);

        return tableView;
    }

    private HBox createDentistInfoBox() {
        HBox dentistInfoBox = new HBox(20);
        dentistInfoBox.setAlignment(Pos.CENTER_LEFT);
        dentistInfoBox.setPadding(new Insets(20));
        dentistInfoBox.setStyle("-fx-background-color: #f2f2f2;");

        Text title = new Text("DenTo");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Text nameLabel = new Text(getDentistName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Text usernameLabel = new Text(dentistUsername);
        usernameLabel.setFont(Font.font("Arial", 14));

        Text phoneNumberLabel = new Text(getDentistPhoneNumber());
        phoneNumberLabel.setFont(Font.font("Arial", 14));

        dentistInfoBox.getChildren().addAll(title, nameLabel, usernameLabel, phoneNumberLabel);
        dentistInfoBox.setMinWidth(300);

        return dentistInfoBox;
    }

    private VBox createAddPatientForm() {
        VBox addPatientForm = new VBox(10);
        addPatientForm.setAlignment(Pos.CENTER_LEFT);
        addPatientForm.setPadding(new Insets(20));
        addPatientForm.setStyle("-fx-background-color: #f2f2f2;");

        Text formTitle = new Text("Додавання пацієнта");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TextField codeField = new TextField();
        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField addressField = new TextField();
        TextField phoneNumberField = new TextField();

        ComboBox<String> genderComboBox = new ComboBox<>();
        genderComboBox.getItems().addAll("Чоловіча", "Жіноча");
        genderComboBox.setPromptText("Оберіть стать");

        DatePicker dateOfBirthPicker = new DatePicker();
        DatePicker appointmentDatePicker = new DatePicker();
        TextField appointmentTimeField = new TextField();
        TextField problemDescriptionField = new TextField();

        Button addPatientBtn = new Button("Додати пацієнта");
        addPatientBtn.setOnAction(e -> handleAddPatientButtonClick(
                Integer.valueOf(codeField.getText()),
                firstNameField.getText(),
                lastNameField.getText(),
                addressField.getText(),
                phoneNumberField.getText(),
                genderComboBox.getValue(),
                dateOfBirthPicker.getValue(),
                appointmentDatePicker.getValue(),
                LocalTime.parse(appointmentTimeField.getText()),
                problemDescriptionField.getText()
        ));

        addPatientForm.getChildren().addAll(
                formTitle,
                new Label("Код пацієнта: "), codeField,
                new Label("Ім'я: "), firstNameField,
                new Label("Прізвище: "), lastNameField,
                new Label("Адреса: "), addressField,
                new Label("Номер телефону: "), phoneNumberField,
                new Label("Стать: "), genderComboBox,
                new Label("Дата народження: "), dateOfBirthPicker,
                new Label("Дата прийому: "), appointmentDatePicker,
                new Label("Час прийому: "), appointmentTimeField,
                new Label("Опис проблеми: "), problemDescriptionField,
                addPatientBtn
        );

        codeField.setMaxWidth(Double.MAX_VALUE);
        firstNameField.setMaxWidth(Double.MAX_VALUE);
        lastNameField.setMaxWidth(Double.MAX_VALUE);
        addressField.setMaxWidth(Double.MAX_VALUE);
        phoneNumberField.setMaxWidth(Double.MAX_VALUE);
        genderComboBox.setMaxWidth(Double.MAX_VALUE);
        dateOfBirthPicker.setMaxWidth(Double.MAX_VALUE);
        appointmentDatePicker.setMaxWidth(Double.MAX_VALUE);
        appointmentTimeField.setMaxWidth(Double.MAX_VALUE);
        problemDescriptionField.setMaxWidth(Double.MAX_VALUE);
        addPatientBtn.setMaxWidth(Double.MAX_VALUE);

        return addPatientForm;
    }

    private VBox createSearchAndDeleteForm(TextField searchField, Button searchButton, TextField deleteCodeField, Button deleteButton, Button resetButton) {
        VBox searchAndDeleteForm = new VBox(10);
        searchAndDeleteForm.setAlignment(Pos.CENTER_LEFT);
        searchAndDeleteForm.setPadding(new Insets(20));
        searchAndDeleteForm.setStyle("-fx-background-color: #f2f2f2;");

        Text formTitle = new Text("Пошук та видалення пацієнта");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        searchField.setMaxWidth(Double.MAX_VALUE);
        deleteCodeField.setMaxWidth(Double.MAX_VALUE);
        searchButton.setMaxWidth(Double.MAX_VALUE);
        deleteButton.setMaxWidth(Double.MAX_VALUE);
        resetButton.setMaxWidth(Double.MAX_VALUE);

        searchAndDeleteForm.getChildren().addAll(
                formTitle,
                new Label("Пошук пацієнта: "), searchField,
                searchButton,
                new Label("Видалення за кодом: "), deleteCodeField,
                deleteButton,
                resetButton
        );

        return searchAndDeleteForm;
    }

    private void resetPatientTable() {
        // Оновлення таблиці і показ всіх пацієнтів лікаря
        List<Patient> patients = databaseHandler.getPatientsForDentist(dentistUsername);
        ObservableList<Patient> data = FXCollections.observableArrayList(patients);
        patientTableView.setItems(data);
    }


    private void handleAddPatientButtonClick(int code, String firstName, String lastName, String address, String phoneNumber, String gender, LocalDate dateOfBirth, LocalDate appointmentDate, LocalTime appointmentTime, String problemDescription) {
        // Логіка додавання пацієнта до бази даних
        boolean success = databaseHandler.addPatient(
                dentistUsername,
                code, firstName, lastName, address, phoneNumber, gender,
                dateOfBirth, appointmentDate, appointmentTime, problemDescription
        );

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Успіх", "Пацієнта додано до бази даних.");
            refreshPatientTable();
        } else {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Не вдалося додати пацієнта.");
        }
    }




    private void handleSearchButtonClick(String searchTerm) {
        List<Patient> searchResults = databaseHandler.searchPatients(dentistUsername, searchTerm);
        ObservableList<Patient> data = FXCollections.observableArrayList(searchResults);
        patientTableView.setItems(data);
    }

    private void handleDeleteButtonClick(String deleteCode) {
        try {
            int code = Integer.valueOf(deleteCode);
            if (databaseHandler.deletePatient(dentistUsername, code)) {
                boolean success = databaseHandler.deletePatient(dentistUsername, code);
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Успіх", "Пацієнта видалено з бази даних.");
                    refreshPatientTable();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Помилка", "Не вдалося видалити пацієнта.");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Попередження", "Пацієнта з таким кодом не знайдено.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Неправильний формат коду пацієнта.");
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void refreshPatientTable() {
        List<Patient> patients = databaseHandler.getPatientsForDentist(dentistUsername);
        ObservableList<Patient> data = FXCollections.observableArrayList(patients);
        patientTableView.setItems(data);
    }

    private String getDentistName() {
        return databaseHandler.getDentistName(dentistUsername);
    }

    private String getDentistPhoneNumber() {
        return databaseHandler.getDentistPhoneNumber(dentistUsername);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
