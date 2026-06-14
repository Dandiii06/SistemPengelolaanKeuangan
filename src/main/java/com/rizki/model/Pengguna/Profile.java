package com.rizki.model.Pengguna;

public class Profile {
    private String nama;
    private String nim;
    private String email;
    private String Password;

    public Profile(String nama,String Password, String nim, String email) {
        this.nama = nama;
        this.Password = Password;
        this.nim = nim;
        this.email = email;
    }

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
        return Password;
    }

    public void updateProfile(String nama, String email) {
        this.nama = nama;
        this.email = email;
    }
}
