package com.rizki.model.Anggaran;

import com.rizki.model.keuangan.Pengeluaran;

public class Anggaran {
    private double batasMaksimal;
    private double totalTerpakai;
    private Kategori kategori;
    private PeriodeAnggaran periode;

    public Anggaran(double limit, Kategori kategori, PeriodeAnggaran periode) {
        this.batasMaksimal = limit;
        this.totalTerpakai = 0.0;
        this.kategori = kategori;
        this.periode = periode;
    }

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

    public double hitungSisaLimit(Pengeluaran[] daftarPengeluaran) {
        this.totalTerpakai = 0.0;
        if (daftarPengeluaran != null) {
            for (Pengeluaran p : daftarPengeluaran) {
                if (p != null && p.getKategori() != null && this.kategori != null) {
                    if (p.getKategori().getNamaKategori().equalsIgnoreCase(this.kategori.getNamaKategori())) {
                        this.totalTerpakai += p.getJumlah();
                    }
                }
            }
        }
        return batasMaksimal - totalTerpakai;
    }
}
