package com.rizki.model.Pengguna;

import com.rizki.model.keuangan.Transaksi;

/**
 * Class Dompet bertanggung jawab untuk mengelola saldo keuangan utama pengguna
 * serta menampung daftar transaksi (pemasukan & pengeluaran) dalam bentuk array.
 */
public class Dompet {
    // Saldo utama yang dimiliki user saat ini
    private double saldoUtama;
    // Array dinamis/list untuk menyimpan seluruh transaksi yang terhubung ke dompet ini
    private Transaksi[] daftarTransaksi;

    /**
     * Constructor Dompet untuk mengeset saldo awal dan menginisialisasi array transaksi dengan panjang 0.
     */
    public Dompet(double saldoAwal) {
        this.saldoUtama = saldoAwal;
        this.daftarTransaksi = new Transaksi[0];
    }

    /**
     * Mengatur ulang saldo dompet secara manual (tanpa transaksi baru, misal saat load data awal).
     */
    public void setSaldoManual(double jumlah) {
        this.saldoUtama = jumlah;
    }

    /**
     * Mengambil jumlah saldo utama saat ini.
     */
    public double getSaldo() {
        return saldoUtama;
    }

    /**
     * Menambahkan nominal pemasukan ke saldo utama.
     */
    public void updateSaldoPemasukan(double nominal) {
        this.saldoUtama += nominal;
    }

    /**
     * Mengurangi nominal pengeluaran dari saldo utama.
     */
    public void updateSaldoPengeluaran(double nominal) {
        this.saldoUtama -= nominal;
    }

    /**
     * Mengambil daftar transaksi dalam dompet.
     */
    public Transaksi[] getDaftarTransaksi() {
        return daftarTransaksi;
    }

    /**
     * Menambahkan transaksi baru ke dalam array daftarTransaksi.
     * Method ini melakukan copy-and-expand array secara manual untuk mensimulasikan array dinamis.
     */
    public void tambahTransaksi(Transaksi t) {
        if (t == null) return;
        // Membuat array baru dengan panjang N + 1
        Transaksi[] baru = new Transaksi[this.daftarTransaksi.length + 1];
        // Menyalin data dari array lama ke array baru
        System.arraycopy(this.daftarTransaksi, 0, baru, 0, this.daftarTransaksi.length);
        // Memasukkan transaksi baru ke indeks terakhir
        baru[baru.length - 1] = t;
        // Mengganti referensi array lama dengan array baru
        this.daftarTransaksi = baru;
    }

    /**
     * Menghapus transaksi berdasarkan ID transaksi dari daftar transaksi dompet.
     * Mengembalikan efek nominal transaksi terhadap saldo utama (pemasukan mengurangi saldo, pengeluaran menambah saldo).
     */
    public void hapusTransaksi(String idTransaksi) {
        if (idTransaksi == null || this.daftarTransaksi == null) return;
        
        int index = -1;
        for (int i = 0; i < this.daftarTransaksi.length; i++) {
            if (this.daftarTransaksi[i] != null && idTransaksi.equals(this.daftarTransaksi[i].getIdTransaksi())) {
                index = i;
                break;
            }
        }
        
        // Jika transaksi ditemukan
        if (index != -1) {
            Transaksi t = this.daftarTransaksi[index];
            
            // Batalkan efek transaksi terhadap saldo utama
            if (t instanceof com.rizki.model.keuangan.Pemasukan) {
                // Hapus pemasukan = mengurangi saldo utama
                this.saldoUtama -= t.getJumlah();
            } else if (t instanceof com.rizki.model.keuangan.Pengeluaran) {
                // Hapus pengeluaran = mengembalikan dana (menambah saldo utama)
                this.saldoUtama += t.getJumlah();
            }
            
            // Hapus dari array dengan resize
            Transaksi[] baru = new Transaksi[this.daftarTransaksi.length - 1];
            System.arraycopy(this.daftarTransaksi, 0, baru, 0, index);
            System.arraycopy(this.daftarTransaksi, index + 1, baru, index, this.daftarTransaksi.length - index - 1);
            this.daftarTransaksi = baru;
        }
    }
}
