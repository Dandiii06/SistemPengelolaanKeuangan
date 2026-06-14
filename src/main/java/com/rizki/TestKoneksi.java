package com.rizki;

import java.sql.Connection;
import java.sql.SQLException;

import com.rizki.model.Database.DatabaseHelper;

public class TestKoneksi {
    public static void main(String[] args) {
        System.out.println("Mencoba menghubungkan ke database...");
        try (Connection conn = DatabaseHelper.getConnection()) {
            if (conn != null) {
                System.out.println("Koneksi BERHASIL! Aplikasi Anda terhubung ke database.");
            }
        } catch (SQLException e) {
            System.err.println("Koneksi GAGAL! Periksa kembali XAMPP/MySQL Anda.");
            e.printStackTrace();
        }
    }
}