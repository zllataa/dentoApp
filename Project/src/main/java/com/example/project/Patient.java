package com.example.project;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Patient {
    private final IntegerProperty patientID;
    private final IntegerProperty code;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty address;
    private final StringProperty phoneNumber;
    private final StringProperty gender;
    private final ObjectProperty<LocalDate> dateOfBirth;

    // Конструктор з параметрами
    public Patient(int patientID, int code, String firstName, String lastName, String address,
                   String phoneNumber, String gender, LocalDate dateOfBirth) {
        this.patientID = new SimpleIntegerProperty(patientID);
        this.code = new SimpleIntegerProperty(code);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.address = new SimpleStringProperty(address);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.gender = new SimpleStringProperty(gender);
        this.dateOfBirth = new SimpleObjectProperty<>(dateOfBirth);
    }


    // Гетери для властивостей
    public int getPatientID() {
        return patientID.get();
    }

    public IntegerProperty patientIDProperty() {
        return patientID;
    }

    public int getCode() {
        return code.get();
    }

    public IntegerProperty codeProperty() {
        return code;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public String getGender() {
        return gender.get();
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth.get();
    }

    public ObjectProperty<LocalDate> dateOfBirthProperty() {
        return dateOfBirth;
    }
}
