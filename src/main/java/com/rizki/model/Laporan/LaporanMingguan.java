package com.rizki.model.Laporan;

import com.rizki.model.Pengguna.Dompet;

/**
 * Class LaporanMingguan mewarisi (extends) Class Laporan (Inheritance).
 * Digunakan untuk menyajikan ringkasan laporan keuangan dalam periode mingguan.
 */
public class LaporanMingguan extends Laporan {
    private int mingguKe; // Menyimpan informasi minggu keberapa dalam bulan (misal: 1, 2, 3, 4)

    /**
     * Constructor LaporanMingguan untuk membuat objek laporan mingguan.
     */
    public LaporanMingguan(String tglCetak, int mingguKe) {
        super(tglCetak);
        this.mingguKe = mingguKe;
    }

    public int getMingguKe() {
        return mingguKe;
    }

    /**
     * Implementasi method generateStatistik() dari superclass.
     * Mengisi totalNominal dengan total saldo dompet terkini.
     */
    @Override
    public void generateStatistik(Dompet d) {
        if (d != null) {
            totalNominal = d.getSaldo();
        }
    }

    @Override
    public String toString() {
        return "LaporanMingguan{tglCetak='" + tglCetak + "', mingguKe=" + mingguKe + ", totalNominal=" + totalNominal + "}";
    }
}
