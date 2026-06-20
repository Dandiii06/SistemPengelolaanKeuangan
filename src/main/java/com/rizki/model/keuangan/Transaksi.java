package com.rizki.model.keuangan;

/**
 * Class Transaksi merupakan class abstrak yang merepresentasikan model transaksi keuangan umum.
 * Class ini berfungsi sebagai superclass (induk) dari Pemasukan dan Pengeluaran,
 * menerapkan konsep Inheritance (Pewarisan) dan Polymorphism (Polimorfisme) melalui method abstrak.
 */
public abstract class Transaksi {
    private String idTransaksi; // ID Unik Transaksi
    private double jumlah;      // Nominal uang transaksi
    private String tanggal;     // Tanggal transaksi (format YYYY-MM-DD)
    private String catatan;     // Catatan/keterangan tambahan transaksi

    /**
     * Constructor Transaksi untuk menginisialisasi atribut dasar transaksi.
     */
    public Transaksi(String idTransaksi, double jumlah, String tanggal, String catatan) {
        this.idTransaksi = idTransaksi;
        this.jumlah = jumlah;
        this.tanggal = tanggal;
        this.catatan = catatan;
    }

    // --- GETTER (Enkapsulasi) ---

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getCatatan() {
        return catatan;
    }

    /**
     * Method abstrak getDetail() yang harus diimplementasikan oleh subclass (Pemasukan & Pengeluaran)
     * untuk memberikan detail representasi teks dari masing-masing tipe transaksi.
     */
    public abstract String getDetail();

    public double getJumlah() {
        return jumlah;
    }
}
