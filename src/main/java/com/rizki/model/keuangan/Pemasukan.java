package com.rizki.model.keuangan;

import com.rizki.model.Pengguna.Dompet;

/**
 * Class Pemasukan merupakan subclass dari Transaksi (Inheritance).
 * Class ini secara khusus merepresentasikan transaksi bertipe pemasukan uang.
 */
public class Pemasukan extends Transaksi{
    private String sumber;          // Sumber pemasukan (misal: Gaji, Uang Saku, dll)
    private double totalPemasukan;  // Total nominal pemasukan

    /**
     * Constructor Pemasukan untuk membuat objek transaksi pemasukan baru.
     * Menggunakan super() untuk memanggil constructor superclass (Transaksi).
     */
    public Pemasukan(String idTransaksi, double jumlah, String tanggal, String catatan, String sumber) {
        super(idTransaksi, jumlah, tanggal, catatan);
        this.sumber = sumber;
        this.totalPemasukan = jumlah;
    }

    /**
     * Menambahkan nominal pemasukan ini ke saldo utama objek Dompet.
     */
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

    /**
     * Implementasi method getDetail() (Polimorfisme Overriding) dari class abstrak Transaksi.
     */
    @Override
    public String getDetail(){
        return "Pemasukan dari " + sumber + ": " + getJumlah()  + " pada " + getTanggal() + ". Catatan: " + getCatatan();
    }
}
