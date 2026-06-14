package com.rizki.model.keuangan;

import com.rizki.model.Pengguna.Dompet;

public class Pemasukan extends Transaksi{
    private String sumber;
    private double totalPemasukan;

    public Pemasukan(String idTransaksi, String tanggal, double jumlahSpend, String catatan, String sumber) {
        super(idTransaksi, jumlahSpend, tanggal, catatan);
        this.sumber = sumber;
        this.totalPemasukan += jumlahSpend;
    }

    public void tambahSaldoDompet(Dompet d) {
        if (d != null) {
            d.updateSaldoPemasukan(getJumlahSpend());
        }
    }

    public double getTotalPemasukan() {
        return totalPemasukan;
    }

    @Override
    public String getDetail(){
        return "Pemasukan dari " + sumber + ": " + getJumlahSpend()  + " pada " + getTanggal() + ". Catatan: " + getCatatan();
    }
}
