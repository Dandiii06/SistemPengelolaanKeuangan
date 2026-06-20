package com.rizki.model.keuangan;

import com.rizki.model.Pengguna.Dompet;
import com.rizki.model.Anggaran.Kategori;

/**
 * Class Pengeluaran merupakan subclass dari Transaksi (Inheritance).
 * Class ini secara khusus merepresentasikan transaksi bertipe pengeluaran uang.
 */
public class Pengeluaran extends Transaksi{
    private Kategori kategori;      // Objek Kategori pengeluaran (misal: Makanan, Transportasi)
    private double totalPengeluaran; // Total nominal pengeluaran

    /**
     * Constructor Pengeluaran untuk membuat objek transaksi pengeluaran baru.
     * Menggunakan super() untuk memanggil constructor superclass (Transaksi).
     */
    public Pengeluaran(String idTransaksi, double jumlah, String tanggal, String catatan, Kategori kategori) {
        super(idTransaksi, jumlah, tanggal, catatan);
        this.kategori = kategori;
        this.totalPengeluaran = jumlah;
    }

    /**
     * Mengurangi nominal pengeluaran ini dari saldo utama objek Dompet.
     */
    public void kurangiSaldoDompet(Dompet d) {
        if (d != null) {
            d.updateSaldoPengeluaran(getJumlah());
        }
    }

    /**
     * Implementasi method getDetail() (Polimorfisme Overriding) dari class abstrak Transaksi.
     */
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
