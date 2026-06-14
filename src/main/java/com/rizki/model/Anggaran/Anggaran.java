package com.rizki.model.Anggaran;

import com.rizki.model.keuangan.Pengeluaran;

public class Anggaran {
    private double batasMaksimal;
    private double totalTerpakai;

    public Anggaran(double limit) {
        this.batasMaksimal = limit;
        this.totalTerpakai = 0.0;
    }

    public double getBatasMaksimal() {
        return batasMaksimal;
    }

    public double getTotalTerpakai() {
        return totalTerpakai;
    }

    public double hitungSisaLimit(Pengeluaran pengeluaran) {
        if (pengeluaran != null) {
            totalTerpakai = pengeluaran.getTotalPengeluaran();
        }
        return batasMaksimal - totalTerpakai;
    }
}
