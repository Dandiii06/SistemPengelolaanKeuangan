package com.rizki.model.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {
    // Sesuaikan URL, Username, dan Password dengan database MySQL lokal Anda
    private static final String URL = "jdbc:mysql://localhost:3306/sistem_keuangan?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; 
    private static final String PASSWORD = ""; // Biarkan kosong jika tidak ada password di XAMPP/MySQL Anda

    public static Connection getConnection() throws SQLException {
        try {
            // Memastikan driver MySQL dimuat ke memori
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL tidak ditemukan!");
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}