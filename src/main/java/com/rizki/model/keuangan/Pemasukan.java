package com.rizki.model.keuangan;

import com.rizki.model.Pengguna.Dompet;

public class Pemasukan extends Transaksi{
    private String sumber;
    private double totalPemasukan;

    public Pemasukan(String idTransaksi, double jumlah, String tanggal, String catatan, String sumber) {
        super(idTransaksi, jumlah, tanggal, catatan);
        this.sumber = sumber;
        this.totalPemasukan = jumlah;
    }

    public void tambahSaldoDompet(Dompet d) {
        if (d != null) {
            d.updateSaldoPemasukan(getJumlah());
        }
    }

    public double getTotalPemasukan() {
        return totalPemasukan;
    }

    public String getSumber() {
        return sumber;
    }

    @Override
    public String getDetail(){
        return "Pemasukan dari " + sumber + ": " + getJumlah()  + " pada " + getTanggal() + ". Catatan: " + getCatatan();
    }
}
