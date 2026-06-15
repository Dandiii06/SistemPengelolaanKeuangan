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

    public void tambahTransaksi(Transaksi t) {
        if (t == null) return;
        Transaksi[] baru = new Transaksi[this.daftarTransaksi.length + 1];
        System.arraycopy(this.daftarTransaksi, 0, baru, 0, this.daftarTransaksi.length);
        baru[baru.length - 1] = t;
        this.daftarTransaksi = baru;
    }
}
