package com.rizki.model.Pengguna;

public class Profile {
    private String nama;
    private String nim;
    private String email;
    private String password;

    public Profile(String nama, String password, String nim, String email) {
        this.nama = nama;
        this.password = password;
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
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void updateProfile(String nama, String email) {
        this.nama = nama;
        this.email = email;
    }
}
