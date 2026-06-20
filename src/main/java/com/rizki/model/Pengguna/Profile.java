package com.rizki.model.Pengguna;

/**
 * Class Profile menyimpan informasi detail mengenai profil personal pengguna aplikasi,
 * seperti nama lengkap, NIM (Nomor Induk Mahasiswa), email student, dan password.
 */
public class Profile {
    private String nama;
    private String nim;
    private String email;
    private String password;

    /**
     * Constructor Profile untuk menginisialisasi atribut profil secara lengkap.
     */
    public Profile(String nama, String password, String nim, String email) {
        this.nama = nama;
        this.password = password;
        this.nim = nim;
        this.email = email;
    }

    // --- GETTER DAN SETTER ---

    public String getNama() {
        return nama;
    }

    public String getNim() {
        return nim;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Mengupdate data profil pengguna (hanya nama lengkap dan email saja, NIM bersifat permanen unik).
     */
    public void updateProfile(String nama, String email) {
        this.nama = nama;
        this.email = email;
    }
}
