package com.rizki.model.Anggaran;

public class PeriodeAnggaran {
    private String jenisPeriode;

    public PeriodeAnggaran(String jenis) {
        this.jenisPeriode = jenis;
    }

    public String getRentangWaktu() {
        return jenisPeriode;
    }
}
