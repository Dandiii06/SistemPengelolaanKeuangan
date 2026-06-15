package com.rizki.model.Manajemen;

import com.rizki.model.Anggaran.Anggaran;
import com.rizki.model.Pengguna.Dompet;

public class Notifikasi {
    private String pesanPeringatan;

    public Notifikasi() {
        this.pesanPeringatan = "";
    }

    public void tampilkanPesan(String pesan) {
        if (pesan != null && !pesan.isEmpty()) {
            this.pesanPeringatan = pesan;
            System.out.println("[NOTIFIKASI] " + pesan);
        }
    }

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

    public String getPesanPeringatan() {
        return pesanPeringatan;
    }
}
