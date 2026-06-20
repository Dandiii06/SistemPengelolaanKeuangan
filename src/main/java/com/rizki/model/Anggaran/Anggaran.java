package com.rizki.model.Anggaran;

import com.rizki.model.keuangan.Pengeluaran;

/**
 * Class Anggaran digunakan untuk membatasi pengeluaran bulanan/mingguan per kategori belanja.
 * Class ini menghitung sisa limit belanja berdasarkan daftar pengeluaran riil pengguna.
 */
public class Anggaran {
    private double batasMaksimal;      // Batas limit uang maksimal yang boleh dibelanjakan
    private double totalTerpakai;       // Total uang yang sudah digunakan dari limit ini
    private Kategori kategori;         // Kategori pengeluaran terkait (asosiasi dengan Kategori)
    private PeriodeAnggaran periode;   // Periode anggaran terkait (asosiasi dengan PeriodeAnggaran)

    /**
     * Constructor Anggaran untuk menginisialisasi budget baru dengan limit, kategori, dan periode.
     */
    public Anggaran(double limit, Kategori kategori, PeriodeAnggaran periode) {
        this.batasMaksimal = limit;
        this.totalTerpakai = 0.0;
        this.kategori = kategori;
        this.periode = periode;
    }

    // --- GETTER DAN SETTER ---

    public double getBatasMaksimal() {
        return batasMaksimal;
    }

    public double getTotalTerpakai() {
        return totalTerpakai;
    }

    public void setTotalTerpakai(double totalTerpakai) {
        this.totalTerpakai = totalTerpakai;
    }

    public Kategori getKategori() {
        return kategori;
    }

    public PeriodeAnggaran getPeriode() {
        return periode;
    }

    /**
     * Menghitung total terpakai dari limit anggaran dengan menyaring daftar transaksi pengeluaran
     * yang memiliki kategori sama dengan kategori anggaran ini.
     * Mengembalikan sisa limit anggaran (batasMaksimal - totalTerpakai).
     */
    public double hitungSisaLimit(Pengeluaran[] daftarPengeluaran) {
        this.totalTerpakai = 0.0;
        if (daftarPengeluaran != null) {
            for (Pengeluaran p : daftarPengeluaran) {
                if (p != null && p.getKategori() != null && this.kategori != null) {
                    // Membandingkan kategori transaksi dengan kategori anggaran ini (case insensitive)
                    if (p.getKategori().getNamaKategori().equalsIgnoreCase(this.kategori.getNamaKategori())) {
                        this.totalTerpakai += p.getJumlah();
                    }
                }
            }
        }
        return batasMaksimal - totalTerpakai;
    }
}
