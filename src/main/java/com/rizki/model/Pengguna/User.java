package com.rizki.model.Pengguna;

/**
 * Class User merepresentasikan entitas pengguna dalam aplikasi Sistem Pengelolaan Keuangan.
 * Class ini menerapkan prinsip OOP Komposisi & Agregasi, di mana satu User memiliki
 * objek Profile dan objek Dompet.
 */
public class User {
    private String username;
    private String password; // Menyimpan password (yang ter-hash dengan BCrypt di database)
    private Profile profil;  // Objek Profil pengguna (Nama, NIM, Email)
    private Dompet dompet;   // Objek Dompet pengguna (Mengelola saldo utama dan transaksi)

    /**
     * Constructor User untuk membuat objek user baru.
     */
    public User(String username, String password, Profile profil, Dompet dompet) {
        this.username = username;
        this.password = password;
        this.profil = profil;
        this.dompet = dompet;
    }

    // --- GETTER DAN SETTER (Prinsip Enkapsulasi) ---

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Profile getProfil() {
        return profil;
    }

    public void setProfil(Profile profil) {
        this.profil = profil;
    }

    public Dompet getDompet() {
        return dompet;
    }

    public void setDompet(Dompet dompet) {
        this.dompet = dompet;
    }

    /**
     * Metode autentikasi() memverifikasi apakah input username & password yang dimasukkan
     * sesuai dengan data akun pengguna ini.
     * Menggunakan pustaka BCrypt untuk mencocokkan password yang di-hash (keamanan data).
     * Jika terjadi kegagalan atau password belum ter-hash (plaintext), akan beralih ke perbandingan String biasa (equals).
     */
    public boolean autentikasi(String username, String password) {
        if (this.username.equals(username)) {
            try {
                // Membandingkan plain text input password dengan hashed password di DB
                return org.mindrot.jbcrypt.BCrypt.checkpw(password, this.password);
            } catch (Exception e) {
                // Fallback jika password di DB belum ter-hash (plaintext)
                return this.password.equals(password);
            }
        }
        return false;
    }
}
