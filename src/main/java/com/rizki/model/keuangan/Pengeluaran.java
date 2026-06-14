package com.rizki.model.keuangan;

import com.rizki.model.Pengguna.Dompet;

public class Pengeluaran extends Transaksi{
    private String kategori;
    private double totalPengeluaran;

    public Pengeluaran(String idTransaksi, double jumlahSpend,  String tanggal, String catatan, String kategori, double pengeluaran) {
        super(idTransaksi, jumlahSpend, tanggal, catatan);
        this.kategori = kategori;
        this.totalPengeluaran += jumlahSpend;
        
    }

    public void kurangiSaldoDompet(Dompet d) {
        if (d != null) {
            d.updateSaldoPengeluaran(getJumlahSpend());
        }
    }

    @Override
    public String getDetail(){
        return "Pengeluaran untuk " + kategori + ": " + getJumlahSpend() + " pada " + getTanggal() + ". Catatan: " + getCatatan();
    }

    public double getTotalPengeluaran() {
        return totalPengeluaran;
    }
}
