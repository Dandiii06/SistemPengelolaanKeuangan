package com.rizki.model.Manajemen;

/**
 * Class Validator bertugas untuk melakukan validasi input data dari pengguna
 * agar data yang diinput aman dan sesuai dengan format sebelum diproses ke database/model.
 */
public class Validator {
    // Menyimpan status hasil validasi terakhir (true jika valid, false jika tidak)
    private boolean statusValidasi;

    /**
     * Constructor untuk inisialisasi awal status validasi diset ke false.
     */
    public Validator() {
        this.statusValidasi = false;
    }

    /**
     * Memvalidasi apakah nominal saldo/transaksi yang dimasukkan bernilai positif (> 0).
     */
    public boolean cekInputSaldo(double nominal) {
        if (nominal <= 0) {
            statusValidasi = false;
            return false;
        }
        statusValidasi = true;
        return true;
    }

    /**
     * Memvalidasi apakah input username dan password pada halaman login tidak kosong.
     */
    public boolean cekValidasiLogin(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            statusValidasi = false;
            return false;
        }
        statusValidasi = true;
        return true;
    }

    /**
     * Mendapatkan status hasil validasi terakhir.
     */
    public boolean getStatusValidasi() {
        return statusValidasi;
    }
}
