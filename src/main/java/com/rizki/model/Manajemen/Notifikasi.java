package com.rizki.model.Manajemen;

import com.rizki.model.Anggaran.Anggaran;
import com.rizki.model.Pengguna.Dompet;

/**
 * Class Notifikasi bertanggung jawab untuk memantau kondisi saldo dan batas anggaran pengguna,
 * lalu memberikan pesan peringatan jika saldo kritis atau anggaran hampir/sudah habis.
 */
public class Notifikasi {
    // Menyimpan string pesan peringatan terakhir yang dihasilkan
    private String pesanPeringatan;

    /**
     * Constructor inisialisasi pesan peringatan kosong.
     */
    public Notifikasi() {
        this.pesanPeringatan = "";
    }

    /**
     * Menyimpan pesan ke dalam atribut dan menampilkannya di console (untuk log debugging).
     */
    public void tampilkanPesan(String pesan) {
        if (pesan != null && !pesan.isEmpty()) {
            this.pesanPeringatan = pesan;
            System.out.println("[NOTIFIKASI] " + pesan);
        }
    }

    /**
     * Memeriksa sisa anggaran dari suatu kategori.
     * Jika limit anggaran sudah habis atau tersisa kurang dari 20%, berikan pesan peringatan.
     */
    public void cekAmbangBatas(Anggaran anggaran) {
        if (anggaran != null) {
            double sisaBudget = anggaran.getBatasMaksimal() - anggaran.getTotalTerpakai();
            if (sisaBudget <= 0) {
                tampilkanPesan("Peringatan: Budget Anda untuk kategori " + (anggaran.getKategori() != null ? anggaran.getKategori().getNamaKategori() : "") + " telah mencapai batas maksimal!");
            } else if (sisaBudget < anggaran.getBatasMaksimal() * 0.2) {
                tampilkanPesan("Peringatan: Sisa budget Anda untuk kategori " + (anggaran.getKategori() != null ? anggaran.getKategori().getNamaKategori() : "") + " kurang dari 20%!");
            }
        }
    }

    /**
     * Memeriksa total saldo di dompet pengguna.
     * Jika saldo habis (<= 0) atau kritis (< Rp 50.000), berikan pesan peringatan.
     */
    public void cekSaldoKritis(Dompet dompet) {
        if (dompet != null) {
            double saldo = dompet.getSaldo();
            if (saldo <= 0) {
                tampilkanPesan("Peringatan: Saldo dompet Anda telah habis!");
            } else if (saldo < 50000) {
                tampilkanPesan("Peringatan: Saldo dompet Anda kritis (kurang dari Rp 50.000)!");
            }
        }
    }

    /**
     * Mengambil isi pesan peringatan terakhir.
     */
    public String getPesanPeringatan() {
        return pesanPeringatan;
    }
}
