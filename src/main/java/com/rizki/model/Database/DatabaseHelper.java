package com.rizki.model.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class DatabaseHelper bertanggung jawab untuk menyediakan koneksi ke database MySQL.
 * Ini menggunakan JDBC (Java Database Connectivity) untuk berkomunikasi dengan database.
 */
public class DatabaseHelper {
    // URL Database MySQL lokal dengan nama database 'sistem_keuangan'
    private static final String URL = "jdbc:mysql://localhost:3306/sistem_keuangan?useSSL=false&serverTimezone=UTC";
    // Username default untuk XAMPP MySQL
    private static final String USER = "root"; 
    // Password default untuk XAMPP MySQL (biasanya kosong)
    private static final String PASSWORD = ""; 

    /**
     * Method getConnection() digunakan untuk meminta objek Connection dari DriverManager.
     * Connection ini digunakan untuk mengeksekusi query SQL ke MySQL.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Memastikan driver JDBC MySQL (com.mysql.cj.jdbc.Driver) dimuat ke memori
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL tidak ditemukan!");
            e.printStackTrace();
        }
        // Mengembalikan koneksi yang berhasil dibuat
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}