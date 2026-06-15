package com.rizki.model.keuangan;

import com.rizki.model.Pengguna.Dompet;
import com.rizki.model.Anggaran.Kategori;

public class Pengeluaran extends Transaksi{
    private Kategori kategori;
    private double totalPengeluaran;

    public Pengeluaran(String idTransaksi, double jumlah, String tanggal, String catatan, Kategori kategori) {
        super(idTransaksi, jumlah, tanggal, catatan);
        this.kategori = kategori;
        this.totalPengeluaran = jumlah;
    }

    public void kurangiSaldoDompet(Dompet d) {
        if (d != null) {
            d.updateSaldoPengeluaran(getJumlah());
        }
    }

    @Override
    public String getDetail(){
        return "Pengeluaran untuk " + (kategori != null ? kategori.getNamaKategori() : "Lainnya") + ": " + getJumlah() + " pada " + getTanggal() + ". Catatan: " + getCatatan();
    }

    public Kategori getKategori() {
        return kategori;
    }

    public double getTotalPengeluaran() {
        return totalPengeluaran;
    }
}
