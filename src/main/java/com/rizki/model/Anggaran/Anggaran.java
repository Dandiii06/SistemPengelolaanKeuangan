package com.rizki.model.Anggaran;

import java.time.DayOfWeek;
import java.time.LocalDate;

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
     * Filter juga berdasarkan periode:
     *   - "Mingguan" : hanya hitung pengeluaran dalam minggu ini (Senin s/d Minggu)
     *   - "Bulanan"  : hanya hitung pengeluaran dalam bulan ini (1 s/d akhir bulan)
     * Mengembalikan sisa limit anggaran (batasMaksimal - totalTerpakai).
     */
    public double hitungSisaLimit(Pengeluaran[] daftarPengeluaran) {
        this.totalTerpakai = 0.0;

        // Tentukan rentang tanggal berdasarkan jenis periode anggaran ini
        LocalDate today = LocalDate.now();
        LocalDate startDate;
        LocalDate endDate;

        String jenisPeriode = (this.periode != null) ? this.periode.getRentangWaktu() : "Bulanan";

        if ("Mingguan".equalsIgnoreCase(jenisPeriode)) {
            // Minggu ini: Senin (awal minggu) s/d Minggu (akhir minggu)
            startDate = today.with(DayOfWeek.MONDAY);
            endDate   = today.with(DayOfWeek.SUNDAY);
        } else {
            // Bulan ini: tanggal 1 s/d akhir bulan
            startDate = today.withDayOfMonth(1);
            endDate   = today.withDayOfMonth(today.lengthOfMonth());
        }

        if (daftarPengeluaran != null) {
            for (Pengeluaran p : daftarPengeluaran) {
                if (p != null && p.getKategori() != null && this.kategori != null) {
                    // Membandingkan kategori transaksi dengan kategori anggaran ini (case insensitive)
                    if (p.getKategori().getNamaKategori().equalsIgnoreCase(this.kategori.getNamaKategori())) {
                        // Filter berdasarkan rentang tanggal periode anggaran
                        try {
                            LocalDate tglTransaksi = LocalDate.parse(p.getTanggal());
                            if (!tglTransaksi.isBefore(startDate) && !tglTransaksi.isAfter(endDate)) {
                                this.totalTerpakai += p.getJumlah();
                            }
                        } catch (Exception ex) {
                            // Jika format tanggal tidak bisa di-parse, tetap hitung sebagai fallback aman
                            this.totalTerpakai += p.getJumlah();
                        }
                    }
                }
            }
        }
        return batasMaksimal - totalTerpakai;
    }
}
