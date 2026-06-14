package com.rizki.model.Laporan;

import com.rizki.model.Pengguna.Dompet;

public class LaporanBulanan extends Laporan {
    private String bulan;

    public LaporanBulanan(String tglCetak, String bulan) {
        super(tglCetak);
        this.bulan = bulan;
    }

    public String getBulan() {
        return bulan;
    }

    @Override
    public void generateStatistik(Dompet d) {
        if (d != null) {
            totalNominal = d.getSaldo();
        }
    }

    @Override
    public String toString() {
        return "LaporanBulanan{tglCetak='" + tglCetak + "', bulan='" + bulan + "', totalNominal=" + totalNominal + "}";
    }
}
