package com.rizki;

import java.sql.Connection;
import java.sql.SQLException;

import com.rizki.model.Database.DatabaseHelper;

/**
 * Class TestKoneksi digunakan untuk menguji koneksi dari aplikasi Java ke database MySQL.
 * Digunakan sebagai alat bantu pengecekan sebelum aplikasi dijalankan secara penuh.
 */
public class TestKoneksi {
    public static void main(String[] args) {
        System.out.println("Mencoba menghubungkan ke database...");
        
        // Membuka koneksi menggunakan try-with-resources agar koneksi otomatis ditutup setelah selesai digunakan
        try (Connection conn = DatabaseHelper.getConnection()) {
            if (conn != null) {
                System.out.println("Koneksi BERHASIL! Aplikasi Anda terhubung ke database.");
            }
        } catch (SQLException e) {
            // Menangkap error jika koneksi gagal (misalnya MySQL di XAMPP mati)
            System.err.println("Koneksi GAGAL! Periksa kembali XAMPP/MySQL Anda.");
            e.printStackTrace();
        }
    }
}