package com.rizki.model.Anggaran;

/**
 * Class Kategori digunakan untuk membedakan jenis pengeluaran (misal: Konsumsi, Transportasi).
 * Ini membantu pengelompokan transaksi untuk pembuatan anggaran dan analisis laporan.
 */
public class Kategori {
    private String namaKategori; // Nama kategori (misal: "Makanan", "Hiburan")

    /**
     * Constructor Kategori untuk instansiasi objek Kategori.
     */
    public Kategori(String nama) {
        this.namaKategori = nama;
    }

    public String getNamaKategori() {
        return namaKategori;
    }
}
