package com.rizki.model.Laporan;

import com.rizki.model.Pengguna.Dompet;
import com.rizki.model.keuangan.Transaksi;

/**
 * Class Laporan merupakan class abstrak (super class) untuk semua bentuk laporan keuangan.
 * Mendefinisikan atribut dasar seperti tanggal cetak dan total nominal, serta menyediakan
 * fungsi helper untuk memfilter data transaksi berdasarkan rentang tanggal tertentu.
 */
public abstract class Laporan {
    protected String tglCetak;       // Tanggal saat laporan dicetak/dibuat
    protected double totalNominal;   // Akumulasi nominal uang dalam laporan

    /**
     * Constructor Laporan untuk menginisialisasi tanggal cetak.
     */
    public Laporan(String tglCetak) {
        this.tglCetak = tglCetak;
        this.totalNominal = 0.0;
    }

    public String getTglCetak() {
        return tglCetak;
    }

    public double getTotalNominal() {
        return totalNominal;
    }

    /**
     * Memfilter array transaksi untuk mendapatkan transaksi yang berada dalam rentang tanggal start hingga end.
     */
    public Transaksi[] filterDataByDate(Transaksi[] data, String start, String end) {
        if (data == null) {
            return new Transaksi[0];
        }

        java.util.List<Transaksi> result = new java.util.ArrayList<>();
        for (Transaksi t : data) {
            if (t != null && t.getTanggal() != null) {
                String tanggal = t.getTanggal();
                // Membandingkan string tanggal secara leksikografis (cocok untuk format YYYY-MM-DD)
                if (tanggal.compareTo(start) >= 0 && tanggal.compareTo(end) <= 0) {
                    result.add(t);
                }
            }
        }

        return result.toArray(new Transaksi[0]);
    }

    /**
     * Method abstrak yang harus diimplementasikan oleh subclass untuk menghasilkan
     * analisis statistik keuangan spesifik dari objek Dompet.
     */
    public abstract void generateStatistik(Dompet d);
}
