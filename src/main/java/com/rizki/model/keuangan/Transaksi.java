package com.rizki.model.keuangan;

public abstract class Transaksi {
    private String idTransaksi;
    private double jumlahSpend;
    private String tanggal;
    private String catatan;

    public Transaksi(String idTransaksi, double jumlahSpend, String tanggal, String catatan) {
        this.idTransaksi = idTransaksi;
        this.jumlahSpend = jumlahSpend;
        this.tanggal = tanggal;
        this.catatan = catatan;
    }

    // Getters
    public String getIdTransaksi() {
        return idTransaksi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getCatatan() {
        return catatan;
    }

    // Abstract method to be implemented by subclasses
    public abstract String getDetail();

    public double getJumlahSpend() {
        return jumlahSpend;
    }
}
