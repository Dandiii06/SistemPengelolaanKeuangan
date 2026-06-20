package com.rizki.model.Anggaran;

/**
 * Class PeriodeAnggaran merepresentasikan jangka waktu berlakunya suatu batas anggaran.
 * Misalnya "Mingguan" atau "Bulanan".
 */
public class PeriodeAnggaran {
    private String jenisPeriode; // Menyimpan rentang waktu (misal: "Mingguan", "Bulanan")

    /**
     * Constructor PeriodeAnggaran untuk instansiasi objek PeriodeAnggaran.
     */
    public PeriodeAnggaran(String jenis) {
        this.jenisPeriode = jenis;
    }

    public String getRentangWaktu() {
        return jenisPeriode;
    }
}
