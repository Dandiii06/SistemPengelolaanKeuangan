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
     * Method saveToStorage() bertugas mendeteksi jenis kelas objek yang dikirim (User, Transaksi, atau Anggaran),
     * lalu memproses penyimpanan atau pembaruan datanya ke database MySQL.
     * Ini menerapkan Polimorfisme Parameter (Object data) agar satu fungsi bisa menerima berbagai jenis model data.
     */
    @Override
    public boolean saveToStorage(Object data) {
        // Cek jika data yang dikirim kosong (null), return false (proses batal/gagal)
        if (data == null) {
            return false;
        }

        // =========================================================================
        // KASUS 1: JIKA OBJEK DATA YANG INGIN DISIMPAN ADALAH INSTANCE DARI CLASS USER
        // =========================================================================
        if (data instanceof User) {
            // Casting tipe data umum Object menjadi tipe spesifik User
            User user = (User) data;
            // Mengambil relasi objek Profile dari objek User
            Profile profile = user.getProfil();
            // Mengambil relasi objek Dompet dari objek User
            Dompet dompet = user.getDompet();
            
            // Validasi: jika profil atau dompet bernilai null, hentikan proses dan return false
            if (profile == null || dompet == null) return false;

            // Inisialisasi status: menandakan apakah user tersebut sudah ada di tabel users
            boolean exists = false;
            // Query SQL untuk memeriksa keberadaan username di tabel users
            String checkQuery = "SELECT COUNT(*) FROM users WHERE username = ?";
            
            // Membuka koneksi database dan menyiapkan PreparedStatement (mencegah SQL Injection)
            try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                // Mengisi parameter tanda tanya pertama (?) dengan nilai username dari user
                checkStmt.setString(1, user.getUsername());
                // Mengeksekusi SELECT query dan menyimpan hasilnya di ResultSet rs
                try (ResultSet rs = checkStmt.executeQuery()) {
                    // Jika data ditemukan dan kolom pertama (COUNT) nilainya lebih besar dari 0
                    if (rs.next() && rs.getInt(1) > 0) {
                        exists = true; // Tandai bahwa user sudah ada (perlu UPDATE, bukan INSERT)
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Tulis error stacktrace di console jika gagal
                return false;
            }

            // A. JIKA PENGGUNA SUDAH TERDAFTAR (exists == true), LAKUKAN UPDATE DATA
            if (exists) {
                // SQL query untuk memperbarui informasi nama, nim, email, password, dan saldo awal berdasarkan username
                String updateQuery = "UPDATE users SET nama = ?, nim = ?, email = ?, password = ?, saldo_awal = ? WHERE username = ?";
                try (Connection conn = DatabaseHelper.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                    // Mengisi tiap parameter tanda tanya (?) sesuai urutan kolom pada query UPDATE
                    stmt.setString(1, profile.getNama());        // ? ke-1: Nama Lengkap
                    stmt.setString(2, profile.getNim());         // ? ke-2: NIM
                    stmt.setString(3, profile.getEmail());       // ? ke-3: Email Student
                    stmt.setString(4, user.getPassword());       // ? ke-4: Password Ter-hash
                    stmt.setDouble(5, dompet.getSaldo());        // ? ke-5: Saldo Terkini Dompet
                    stmt.setString(6, user.getUsername());       // ? ke-6: Username (kunci pencarian data)
                    
                    // Mengeksekusi instruksi update ke database MySQL
                    stmt.executeUpdate();
                    return true; // Berhasil memperbarui data user
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            } 
            // B. JIKA PENGGUNA BELUM TERDAFTAR (exists == false), LAKUKAN INSERT DATA BARU
            else {
                // SQL query untuk memasukkan baris data user baru ke tabel users
                String insertQuery = "INSERT INTO users (nama, nim, email, username, password, saldo_awal) VALUES (?, ?, ?, ?, ?, ?)";
                try (Connection conn = DatabaseHelper.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                    // Mengisi tiap parameter tanda tanya (?) sesuai urutan kolom pada query INSERT
                    stmt.setString(1, profile.getNama());        // ? ke-1: Nama Lengkap
                    stmt.setString(2, profile.getNim());         // ? ke-2: NIM
                    stmt.setString(3, profile.getEmail());       // ? ke-3: Email Student
                    stmt.setString(4, user.getUsername());       // ? ke-4: Username Unik
                    stmt.setString(5, user.getPassword());       // ? ke-5: Password Ter-hash
                    stmt.setDouble(6, dompet.getSaldo());        // ? ke-6: Saldo Awal Dompet
                    
                    // Mengeksekusi instruksi insert/penulisan ke database MySQL
                    stmt.executeUpdate();
                    return true; // Berhasil menyimpan data user baru
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        } 
        // =========================================================================
        // KASUS 2: JIKA OBJEK DATA YANG INGIN DISIMPAN ADALAH INSTANCE DARI TRANSAKSI
        // =========================================================================
        else if (data instanceof Transaksi) {
            // Casting objek umum menjadi tipe spesifik Transaksi
            Transaksi tx = (Transaksi) data;
            String tipe = "";          // Menyimpan string tipe transaksi ("Pemasukan" atau "Pengeluaran")
            String detailSource = "";   // Menyimpan string kategori pengeluaran atau sumber pemasukan
            
            // Cek polimorfik: Jika transaksi adalah Pemasukan
            if (tx instanceof Pemasukan) {
                tipe = "Pemasukan";
                detailSource = ((Pemasukan) tx).getSumber(); // Ambil string nama sumber (misal: "Gaji")
            } 
            // Cek polimorfik: Jika transaksi adalah Pengeluaran
            else if (tx instanceof Pengeluaran) {
                tipe = "Pengeluaran";
                Kategori cat = ((Pengeluaran) tx).getKategori();
                detailSource = (cat != null) ? cat.getNamaKategori() : "Lainnya"; // Ambil nama kategori belanja
            }

            // Mendapatkan data username pengguna aktif yang sedang login dari ViewManager
            String currentUsername = com.rizki.view.ViewManager.getCurrentUsername();

            // SQL Query INSERT yang cerdas: jika ID transaksi sudah ada, lakukan UPDATE (ON DUPLICATE KEY UPDATE)
            String insertTxQuery = "INSERT INTO transactions (id, username, jumlah, tanggal, catatan, tipe, detail_source) VALUES (?, ?, ?, ?, ?, ?, ?)"
                    + " ON DUPLICATE KEY UPDATE jumlah=?, tanggal=?, catatan=?, tipe=?, detail_source=?";
            
            try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(insertTxQuery)) {
                // --- Set parameter untuk INSERT pertama kali ---
                stmt.setString(1, tx.getIdTransaksi());      // ? ke-1: ID Transaksi Unik
                stmt.setString(2, currentUsername);          // ? ke-2: Username Pemilik Transaksi
                stmt.setDouble(3, tx.getJumlah());            // ? ke-3: Nominal Uang
                stmt.setString(4, tx.getTanggal());           // ? ke-4: Tanggal Transaksi
                stmt.setString(5, tx.getCatatan());           // ? ke-5: Catatan Deskripsi
                stmt.setString(6, tipe);                     // ? ke-6: Tipe ("Pemasukan"/"Pengeluaran")
                stmt.setString(7, detailSource);             // ? ke-7: Kategori/Sumber
                
                // --- Set parameter jika ID sudah ada (ON DUPLICATE KEY UPDATE) ---
                stmt.setDouble(8, tx.getJumlah());            // ? ke-8: Update Nominal
                stmt.setString(9, tx.getTanggal());           // ? ke-9: Update Tanggal
                stmt.setString(10, tx.getCatatan());          // ? ke-10: Update Catatan
                stmt.setString(11, tipe);                    // ? ke-11: Update Tipe
                stmt.setString(12, detailSource);            // ? ke-12: Update Kategori/Sumber
                
                // Jalankan query SQL di MySQL
                stmt.executeUpdate();
                return true; // Berhasil menyimpan transaksi
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } 
        // =========================================================================
        // KASUS 3: JIKA OBJEK DATA YANG INGIN DISIMPAN ADALAH INSTANCE DARI ANGGARAN
        // =========================================================================
        else if (data instanceof Anggaran) {
            // Casting objek umum menjadi tipe spesifik Anggaran
            Anggaran budget = (Anggaran) data;
            String currentUsername = com.rizki.view.ViewManager.getCurrentUsername();
            // Ambil nama kategori dan tipe periode
            String kat = (budget.getKategori() != null) ? budget.getKategori().getNamaKategori() : "Lainnya";
            String per = (budget.getPeriode() != null) ? budget.getPeriode().getRentangWaktu() : "Bulanan";

            // Untuk menghindari data ganda, kita hapus budget dengan kategori DAN periode yang sama baru kita input yang baru.
            // Filter juga berdasarkan periode agar anggaran mingguan dan bulanan di kategori yang sama tidak saling menimpa.
            String deleteQuery = "DELETE FROM budgets WHERE username=? AND kategori=? AND periode=?";
            String insertQuery = "INSERT INTO budgets (username, kategori, batas_maksimal, periode, total_terpakai) VALUES (?, ?, ?, ?, ?)";
            
            try (Connection conn = DatabaseHelper.getConnection()) {
                // 1. Eksekusi DELETE budget dengan kategori dan periode yang sama (lebih presisi)
                try (PreparedStatement delStmt = conn.prepareStatement(deleteQuery)) {
                    delStmt.setString(1, currentUsername); // ? ke-1: Username aktif
                    delStmt.setString(2, kat);             // ? ke-2: Kategori anggaran
                    delStmt.setString(3, per);             // ? ke-3: Periode anggaran (Mingguan/Bulanan)
                    delStmt.executeUpdate();
                }
                
                // 2. Eksekusi INSERT budget kategori yang baru
                try (PreparedStatement insStmt = conn.prepareStatement(insertQuery)) {
                    insStmt.setString(1, currentUsername);               // ? ke-1: Username aktif
                    insStmt.setString(2, kat);                           // ? ke-2: Kategori anggaran
                    insStmt.setDouble(3, budget.getBatasMaksimal());     // ? ke-3: Limit maksimal anggaran
                    insStmt.setString(4, per);                           // ? ke-4: Periode (Mingguan/Bulanan)
                    insStmt.setDouble(5, budget.getTotalTerpakai());     // ? ke-5: Total uang terpakai saat ini
                    insStmt.executeUpdate();
                }
                return true; // Berhasil menyimpan budget
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        // Return false jika objek data yang masuk tidak cocok dengan jenis data manapun
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
