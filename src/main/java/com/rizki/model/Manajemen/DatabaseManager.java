package com.rizki.model.Manajemen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.rizki.model.Anggaran.Anggaran;
import com.rizki.model.Anggaran.Kategori;
import com.rizki.model.Anggaran.PeriodeAnggaran;
import com.rizki.model.Database.DatabaseHelper;
import com.rizki.model.Pengguna.Dompet;
import com.rizki.model.Pengguna.Profile;
import com.rizki.model.Pengguna.User;
import com.rizki.model.keuangan.Pemasukan;
import com.rizki.model.keuangan.Pengeluaran;
import com.rizki.model.keuangan.Transaksi;

/**
 * Class DatabaseManager bertanggung jawab untuk menangani operasi CRUD ke database MySQL.
 * Class ini mengimplementasikan interface PenyimpananData untuk memenuhi prinsip Abstraksi OOP.
 */
public class DatabaseManager implements PenyimpananData {
    private String filePath; // Atribut cadangan jika ingin menyimpan data ke file (tidak digunakan langsung karena beralih ke MySQL)

    /**
     * Constructor DatabaseManager.
     * Menginisialisasi koneksi database dan membuat tabel secara otomatis jika belum ada.
     */
    public DatabaseManager(String filePath) {
        this.filePath = filePath;
        initDatabase();
    }

    /**
     * Method initDatabase() berjalan otomatis saat instansiasi.
     * Membuat tabel 'users', 'transactions', dan 'budgets' jika belum terbentuk di MySQL.
     */
    private void initDatabase() {
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Buat tabel users jika belum ada
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "nama VARCHAR(100)," +
                    "nim VARCHAR(20) UNIQUE," +
                    "email VARCHAR(100) UNIQUE," +
                    "username VARCHAR(50) UNIQUE," +
                    "password VARCHAR(255)," +
                    "saldo_awal DOUBLE" +
                    ")");

            // Buat tabel transactions jika belum ada
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS transactions (" +
                    "id VARCHAR(50) PRIMARY KEY," +
                    "username VARCHAR(50)," +
                    "jumlah DOUBLE," +
                    "tanggal VARCHAR(20)," +
                    "catatan TEXT," +
                    "tipe VARCHAR(20)," +
                    "detail_source VARCHAR(100)" +
                    ")");
            
            // Buat tabel budgets jika belum ada
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS budgets (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(50)," +
                    "kategori VARCHAR(100)," +
                    "batas_maksimal DOUBLE," +
                    "periode VARCHAR(50)," +
                    "total_terpakai DOUBLE" +
                    ")");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method saveToStorage() menyimpan berbagai jenis objek (User, Transaksi, Anggaran)
     * ke database secara dinamis dengan menggunakan pemeriksaan keyword 'instanceof'.
     */
    @Override
    public boolean saveToStorage(Object data) {
        if (data == null) {
            return false;
        }

        // --- Kasus 1: Menyimpan / Mengupdate Data USER ---
        if (data instanceof User) {
            User user = (User) data;
            Profile profile = user.getProfil();
            Dompet dompet = user.getDompet();
            
            if (profile == null || dompet == null) return false;

            // Cek apakah user sudah ada di database (berdasarkan username)
            boolean exists = false;
            String checkQuery = "SELECT COUNT(*) FROM users WHERE username = ?";
            try (Connection conn = DatabaseHelper.getConnection();
                 PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, user.getUsername());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        exists = true;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

            if (exists) {
                // Update data user yang sudah terdaftar
                String updateQuery = "UPDATE users SET nama = ?, nim = ?, email = ?, password = ?, saldo_awal = ? WHERE username = ?";
                try (Connection conn = DatabaseHelper.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                    stmt.setString(1, profile.getNama());
                    stmt.setString(2, profile.getNim());
                    stmt.setString(3, profile.getEmail());
                    stmt.setString(4, user.getPassword());
                    stmt.setDouble(5, dompet.getSaldo());
                    stmt.setString(6, user.getUsername());
                    stmt.executeUpdate();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                // Insert data user baru
                String insertQuery = "INSERT INTO users (nama, nim, email, username, password, saldo_awal) VALUES (?, ?, ?, ?, ?, ?)";
                try (Connection conn = DatabaseHelper.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                    stmt.setString(1, profile.getNama());
                    stmt.setString(2, profile.getNim());
                    stmt.setString(3, profile.getEmail());
                    stmt.setString(4, user.getUsername());
                    stmt.setString(5, user.getPassword());
                    stmt.setDouble(6, dompet.getSaldo());
                    stmt.executeUpdate();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        } 
        // --- Kasus 2: Menyimpan Data TRANSAKSI (Pemasukan / Pengeluaran) ---
        else if (data instanceof Transaksi) {
            Transaksi tx = (Transaksi) data;
            String tipe = "";
            String detailSource = "";
            
            // Mengidentifikasi tipe transaksi (Pemasukan vs Pengeluaran)
            if (tx instanceof Pemasukan) {
                tipe = "Pemasukan";
                detailSource = ((Pemasukan) tx).getSumber();
            } else if (tx instanceof Pengeluaran) {
                tipe = "Pengeluaran";
                Kategori cat = ((Pengeluaran) tx).getKategori();
                detailSource = (cat != null) ? cat.getNamaKategori() : "Lainnya";
            }

            // Mendapatkan username aktif dari ViewManager
            String currentUsername = com.rizki.view.ViewManager.getCurrentUsername();

            // Memasukkan transaksi baru (atau update jika ID sudah ada)
            String insertTxQuery = "INSERT INTO transactions (id, username, jumlah, tanggal, catatan, tipe, detail_source) VALUES (?, ?, ?, ?, ?, ?, ?)"
                    + " ON DUPLICATE KEY UPDATE jumlah=?, tanggal=?, catatan=?, tipe=?, detail_source=?";
            try (Connection conn = DatabaseHelper.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(insertTxQuery)) {
                stmt.setString(1, tx.getIdTransaksi());
                stmt.setString(2, currentUsername);
                stmt.setDouble(3, tx.getJumlah());
                stmt.setString(4, tx.getTanggal());
                stmt.setString(5, tx.getCatatan());
                stmt.setString(6, tipe);
                stmt.setString(7, detailSource);
                // Parameter untuk ON DUPLICATE KEY
                stmt.setDouble(8, tx.getJumlah());
                stmt.setString(9, tx.getTanggal());
                stmt.setString(10, tx.getCatatan());
                stmt.setString(11, tipe);
                stmt.setString(12, detailSource);
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } 
        // --- Kasus 3: Menyimpan Data ANGGARAN (Batas Anggaran) ---
        else if (data instanceof Anggaran) {
            Anggaran budget = (Anggaran) data;
            String currentUsername = com.rizki.view.ViewManager.getCurrentUsername();
            String kat = (budget.getKategori() != null) ? budget.getKategori().getNamaKategori() : "Lainnya";
            String per = (budget.getPeriode() != null) ? budget.getPeriode().getRentangWaktu() : "Bulanan";

            // Menghapus data batas anggaran lama untuk kategori yang sama, lalu memasukkan yang baru
            String deleteQuery = "DELETE FROM budgets WHERE username=? AND kategori=?";
            String insertQuery = "INSERT INTO budgets (username, kategori, batas_maksimal, periode, total_terpakai) VALUES (?, ?, ?, ?, ?)";
            
            try (Connection conn = DatabaseHelper.getConnection()) {
                try (PreparedStatement delStmt = conn.prepareStatement(deleteQuery)) {
                    delStmt.setString(1, currentUsername);
                    delStmt.setString(2, kat);
                    delStmt.executeUpdate();
                }
                try (PreparedStatement insStmt = conn.prepareStatement(insertQuery)) {
                    insStmt.setString(1, currentUsername);
                    insStmt.setString(2, kat);
                    insStmt.setDouble(3, budget.getBatasMaksimal());
                    insStmt.setString(4, per);
                    insStmt.setDouble(5, budget.getTotalTerpakai());
                    insStmt.executeUpdate();
                }
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        return false;
    }

    @Override
    public Object loadFromStorage() {
        // Method default interface, tidak digunakan secara langsung
        return null;
    }

    /**
     * Memuat objek User lengkap dari database berdasarkan username,
     * termasuk data profil personal dan data dompet serta semua transaksi sejarahnya.
     */
    public User loadUser(String username) {
        String userQuery = "SELECT nama, nim, email, password, saldo_awal FROM users WHERE username = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(userQuery)) {
            
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nama = rs.getString("nama");
                    String nim = rs.getString("nim");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    double saldoAwal = rs.getDouble("saldo_awal");
                    
                    // Rekonstruksi Profile dan Dompet menggunakan constructor (OOP Composition)
                    Profile profile = new Profile(nama, password, nim, email);
                    Dompet dompet = new Dompet(saldoAwal);
                    
                    // Memuat seluruh transaksi sejarah milik user ini dari MySQL
                    List<Transaksi> txList = new ArrayList<>();
                    String txQuery = "SELECT id, jumlah, tanggal, catatan, tipe, detail_source FROM transactions WHERE username = ?";
                    try (PreparedStatement txStmt = conn.prepareStatement(txQuery)) {
                        txStmt.setString(1, username);
                        try (ResultSet txRs = txStmt.executeQuery()) {
                            while (txRs.next()) {
                                String id = txRs.getString("id");
                                double jumlah = txRs.getDouble("jumlah");
                                String tanggal = txRs.getString("tanggal");
                                String catatan = txRs.getString("catatan");
                                String tipe = txRs.getString("tipe");
                                String detailSource = txRs.getString("detail_source");
                                
                                // Rekonstruksi polimorfik Pemasukan / Pengeluaran berdasarkan kolom 'tipe'
                                if ("Pemasukan".equalsIgnoreCase(tipe)) {
                                    Pemasukan p = new Pemasukan(id, jumlah, tanggal, catatan, detailSource);
                                    dompet.tambahTransaksi(p);
                                } else {
                                    Kategori katObj = new Kategori(detailSource);
                                    Pengeluaran p = new Pengeluaran(id, jumlah, tanggal, catatan, katObj);
                                    dompet.tambahTransaksi(p);
                                }
                            }
                        }
                    }
                    
                    // Rekonstruksi objek User utuh
                    User user = new User(username, password, profile, dompet);
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Memuat daftar anggaran aktif milik pengguna dari database,
     * dan menghitung ulang sisa limit belanja berdasarkan riwayat pengeluaran rill.
     */
    public List<Anggaran> loadBudgets(String username, Pengeluaran[] listPengeluaran) {
        List<Anggaran> list = new ArrayList<>();
        String query = "SELECT kategori, batas_maksimal, periode FROM budgets WHERE username = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String kat = rs.getString("kategori");
                    double limit = rs.getDouble("batas_maksimal");
                    String per = rs.getString("periode");
                    
                    Kategori kategoriObj = new Kategori(kat);
                    PeriodeAnggaran periodeObj = new PeriodeAnggaran(per);
                    
                    Anggaran anggaran = new Anggaran(limit, kategoriObj, periodeObj);
                    // Hitung jumlah pengeluaran rill untuk kategori anggaran ini
                    anggaran.hitungSisaLimit(listPengeluaran);
                    list.add(anggaran);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
