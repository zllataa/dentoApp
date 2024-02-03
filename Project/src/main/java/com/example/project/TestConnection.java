package com.example.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String userName = "postgres";
        String pass = "2264";
        return DriverManager.getConnection(url, userName, pass);
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try (Connection con = getConnection()) {
            System.out.println("Connection created");

            // Ваш код роботи з базою даних тут

            System.out.println("Connection closed");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
