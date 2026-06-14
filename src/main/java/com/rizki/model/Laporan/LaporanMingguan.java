package com.rizki.model.Laporan;

import com.rizki.model.Pengguna.Dompet;

public class LaporanMingguan extends Laporan {
    private int mingguKe;

    public LaporanMingguan(String tglCetak, int mingguKe) {
        super(tglCetak);
        this.mingguKe = mingguKe;
    }

    public int getMingguKe() {
        return mingguKe;
    }

    @Override
    public void generateStatistik(Dompet d) {
        if (d != null) {
            totalNominal = d.getSaldo();
        }
    }

    @Override
    public String toString() {
        return "LaporanMingguan{tglCetak='" + tglCetak + "', mingguKe=" + mingguKe + ", totalNominal=" + totalNominal + "}";
    }
}
