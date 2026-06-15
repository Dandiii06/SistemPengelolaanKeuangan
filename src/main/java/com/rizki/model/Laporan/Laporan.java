package com.rizki.model.Laporan;

import com.rizki.model.Pengguna.Dompet;
import com.rizki.model.keuangan.Transaksi;

public abstract class Laporan {
    protected String tglCetak;
    protected double totalNominal;

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

    public Transaksi[] filterDataByDate(Transaksi[] data, String start, String end) {
        if (data == null) {
            return new Transaksi[0];
        }

        java.util.List<Transaksi> result = new java.util.ArrayList<>();
        for (Transaksi t : data) {
            if (t != null && t.getTanggal() != null) {
                String tanggal = t.getTanggal();
                if (tanggal.compareTo(start) >= 0 && tanggal.compareTo(end) <= 0) {
                    result.add(t);
                }
            }
        }

        return result.toArray(new Transaksi[0]);
    }

    public abstract void generateStatistik(Dompet d);
}
