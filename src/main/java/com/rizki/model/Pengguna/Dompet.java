package com.rizki.model.Pengguna;

import com.rizki.model.keuangan.Transaksi;

public class Dompet {
    private double saldoUtama;
    private Transaksi[] daftarTransaksi;

    public Dompet(double saldoAwal) {
        this.saldoUtama = saldoAwal;
        this.daftarTransaksi = new Transaksi[0];
    }

    public void setSaldoManual(double jumlah) {
        this.saldoUtama = jumlah;
    }

    public double getSaldo() {
        return saldoUtama;
    }

    public void updateSaldoPemasukan(double nominal) {
        this.saldoUtama += nominal;
    }

    public void updateSaldoPengeluaran(double nominal) {
        this.saldoUtama -= nominal;
    }

    public Transaksi[] getDaftarTransaksi() {
        return daftarTransaksi;
    }
}
