package com.rizki.model.Laporan;

import com.rizki.model.Pengguna.Dompet;

/**
 * Class LaporanBulanan mewarisi (extends) Class Laporan (Inheritance).
 * Digunakan untuk menyajikan ringkasan laporan keuangan dalam periode bulanan.
 */
public class LaporanBulanan extends Laporan {
    private String bulan; // Menyimpan nama/identifikasi bulan (misal: "Januari", "Juni")

    /**
     * Constructor LaporanBulanan untuk membuat objek laporan bulanan.
     */
    public LaporanBulanan(String tglCetak, String bulan) {
        super(tglCetak);
        this.bulan = bulan;
    }

    public String getBulan() {
        return bulan;
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
        return "LaporanBulanan{tglCetak='" + tglCetak + "', bulan='" + bulan + "', totalNominal=" + totalNominal + "}";
    }
}
