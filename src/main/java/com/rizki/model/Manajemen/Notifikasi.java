package com.rizki.model.Manajemen;

import com.rizki.model.Anggaran.Anggaran;

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
                tampilkanPesan("Peringatan: Budget Anda telah mencapai batas maksimal!");
            } else if (sisaBudget < anggaran.getBatasMaksimal() * 0.2) {
                tampilkanPesan("Peringatan: Sisa budget Anda kurang dari 20% dari batas maksimal!");
            }
        }
    }

    public String getPesanPeringatan() {
        return pesanPeringatan;
    }
}
