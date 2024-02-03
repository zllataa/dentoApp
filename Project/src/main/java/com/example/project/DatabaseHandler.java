package com.example.project;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler {

    private final Connection connection;

    // Конструктор, який приймає з'єднання
    public DatabaseHandler(Connection connection) {
        this.connection = connection;
    }

    // Конструктор за замовчуванням, що ініціалізує з'єднання з базою даних
    public DatabaseHandler() {
        this.connection = initConnection();
    }

    private Connection initConnection() {
        try {
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String userName = "postgres";
            String pass = "2264";
            return DriverManager.getConnection(url, userName, pass);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to initialize DatabaseHandler", e);
        }
    }

    public boolean registerDentists(String firstName, String lastName, String phoneNumber, String username, String plainTextPassword) {
        String insert = "INSERT INTO Dentists (FirstName, LastName, PhoneNumber, UserName, Password) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insert)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, phoneNumber);
            preparedStatement.setString(4, username);

            String hashedPassword = hashPassword(plainTextPassword);
            preparedStatement.setString(5, hashedPassword);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

            return rowsAffected > 0;
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.err.println("Exception details: " + e.getMessage());
            return false;
        }
    }

    public boolean loginDentists(String username, String password) {
        String query = "SELECT * FROM Dentists WHERE UserName = ? AND Password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashPassword(password));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // Повертає true, якщо знайдено користувача з таким логіном та паролем
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Patient> getDentistPatients(int dentistID) {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM Patients WHERE DentistID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, dentistID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int patientID = resultSet.getInt("PatientID");
                    String firstName = resultSet.getString("FirstName");
                    String lastName = resultSet.getString("LastName");
                    String address = resultSet.getString("Address");
                    String phoneNumber = resultSet.getString("PhoneNumber");
                    String gender = resultSet.getString("Gender");
                    LocalDate dateOfBirth = resultSet.getDate("DateOfBirth").toLocalDate();

                    Patient patient = new Patient(
                            resultSet.getInt("PatientID"),
                            resultSet.getInt("Code"),
                            resultSet.getString("FirstName"),
                            resultSet.getString("LastName"),
                            resultSet.getString("Address"),
                            resultSet.getString("PhoneNumber"),
                            resultSet.getString("Gender"),
                            resultSet.getDate("DateOfBirth").toLocalDate()
                    );

                    patients.add(patient);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patients;
    }

    public List<Schedule> getDentistSchedule(int dentistID) {
        List<Schedule> scheduleList = new ArrayList<>();
        String query = "SELECT * FROM Schedule WHERE DentistID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, dentistID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int scheduleID = resultSet.getInt("ScheduleID");
                    LocalDate date = resultSet.getDate("Date").toLocalDate();
                    LocalTime time = resultSet.getTime("Time").toLocalTime();
                    String problemDescription = resultSet.getString("ProblemDescription");
                    int patientID = resultSet.getInt("PatientID");

                    Schedule schedule = new Schedule(scheduleID, date, time, problemDescription, patientID, dentistID);
                    scheduleList.add(schedule);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return scheduleList;
    }

    public List<MedicalProcedure> getDentistProcedures(int dentistID) {
        List<MedicalProcedure> procedures = new ArrayList<>();
        String query = "SELECT * FROM MedicalProcedures WHERE DentistID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, dentistID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int procedureID = resultSet.getInt("MedicalProceduresID");
                    LocalDate date = resultSet.getDate("Date").toLocalDate();
                    String note = resultSet.getString("Note");
                    int patientID = resultSet.getInt("PatientID");
                    int serviceID = resultSet.getInt("ServiceID");

                    MedicalProcedure procedure = new MedicalProcedure(procedureID, date, note, patientID, serviceID);
                    procedures.add(procedure);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return procedures;
    }

    public List<Patient> getPatientsForDentist(String dentistUsername) {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM Patients WHERE DentistID = (SELECT DentistID FROM Dentists WHERE UserName = ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, dentistUsername);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Patient patient = new Patient(
                            resultSet.getInt("PatientID"),
                            resultSet.getInt("Code"),
                            resultSet.getString("FirstName"),
                            resultSet.getString("LastName"),
                            resultSet.getString("Address"),
                            resultSet.getString("PhoneNumber"),
                            resultSet.getString("Gender"),
                            resultSet.getDate("DateOfBirth").toLocalDate()
                    );
                    patients.add(patient);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }


    // Додамо метод для отримання графіку для лікаря
    public List<Schedule> getScheduleForDentist(String dentistUsername) {
        List<Schedule> schedule = new ArrayList<>();
        String query = "SELECT * FROM Schedule WHERE DentistID = (SELECT DentistID FROM Dentists WHERE UserName = ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, dentistUsername);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Schedule entry = new Schedule(
                            resultSet.getInt("ScheduleID"),
                            resultSet.getDate("Date").toLocalDate(),
                            resultSet.getTime("Time").toLocalTime(),
                            resultSet.getString("ProblemDescription"),
                            resultSet.getInt("PatientID"),
                            resultSet.getInt("DentistID")
                    );
                    schedule.add(entry);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedule;
    }

    public String getDentistName(String username) {
        String query = "SELECT FirstName, LastName FROM Dentists WHERE UserName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("FirstName") + " " + resultSet.getString("LastName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Ім'я Прізвище";
    }
    // Отримати ім'я користувача за його ID
    public String getDentistUsername(int dentistID) {
        String query = "SELECT UserName FROM Dentists WHERE DentistID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, dentistID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("UserName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // або можна повертати певне значення за замовчуванням, якщо не знайдено
    }

    // Отримати номер телефону лікаря за його ID
    public String getDentistPhoneNumber(String username) {
        String query = "SELECT PhoneNumber FROM Dentists WHERE UserName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("PhoneNumber");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "123-456-7890";
    }


    public boolean addPatient(String dentistUsername, Integer code, String firstName, String lastName, String address, String phoneNumber, String gender, LocalDate dateOfBirth, LocalDate appointmentDate, LocalTime appointmentTime, String problemDescription) {
        String insertPatient = "INSERT INTO Patients (Code, FirstName, LastName, Address, PhoneNumber, Gender, DateOfBirth, DentistID) VALUES (?, ?, ?, ?, ?, ?, ?, (SELECT DentistID FROM Dentists WHERE UserName = ?))";
        String insertSchedule = "INSERT INTO Schedule (Date, Time, ProblemDescription, PatientID, DentistID) VALUES (?, ?, ?, (SELECT MAX(PatientID) FROM Patients), (SELECT DentistID FROM Dentists WHERE UserName = ?))";

        try (PreparedStatement preparedStatementPatient = connection.prepareStatement(insertPatient);
             PreparedStatement preparedStatementSchedule = connection.prepareStatement(insertSchedule)) {

            connection.setAutoCommit(false);

            preparedStatementPatient.setInt(1, code);
            preparedStatementPatient.setString(2, firstName);
            preparedStatementPatient.setString(3, lastName);
            preparedStatementPatient.setString(4, address);
            preparedStatementPatient.setString(5, phoneNumber);
            preparedStatementPatient.setString(6, gender);
            preparedStatementPatient.setDate(7, Date.valueOf(dateOfBirth));
            preparedStatementPatient.setString(8, dentistUsername);

            int rowsAffectedPatient = preparedStatementPatient.executeUpdate();

            if (rowsAffectedPatient > 0) {
                preparedStatementSchedule.setDate(1, Date.valueOf(appointmentDate));
                preparedStatementSchedule.setTime(2, Time.valueOf(appointmentTime));
                preparedStatementSchedule.setString(3, problemDescription);
                preparedStatementSchedule.setString(4, dentistUsername);

                int rowsAffectedSchedule = preparedStatementSchedule.executeUpdate();

                if (rowsAffectedSchedule > 0) {
                    connection.commit();
                    connection.setAutoCommit(true);
                    return true;
                }
            }

            connection.rollback();
            connection.setAutoCommit(true);
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //видалити пацієнта
    public boolean deletePatient(String dentistUsername, int patientCode) {
        String deletePatientQuery = "DELETE FROM patients WHERE dentist_username = ? AND code = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deletePatientQuery)) {
            preparedStatement.setString(1, dentistUsername);
            preparedStatement.setInt(2, patientCode);

            int affectedRows = preparedStatement.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Patient> searchPatients(String dentistUsername, String searchTerm) {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM Patients WHERE DentistID = (SELECT DentistID FROM Dentists WHERE UserName = ?) AND (FirstName ILIKE ? OR LastName ILIKE ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, dentistUsername);
            preparedStatement.setString(2, "%" + searchTerm + "%");
            preparedStatement.setString(3, "%" + searchTerm + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Patient patient = new Patient(
                            resultSet.getInt("PatientID"),
                            resultSet.getInt("Code"),
                            resultSet.getString("FirstName"),
                            resultSet.getString("LastName"),
                            resultSet.getString("Address"),
                            resultSet.getString("PhoneNumber"),
                            resultSet.getString("Gender"),
                            resultSet.getDate("DateOfBirth").toLocalDate()
                    );
                    patients.add(patient);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }


    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes());

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
