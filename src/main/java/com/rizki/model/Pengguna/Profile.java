package com.rizki.model.Pengguna;

public class Profile {
    private String nama;
    private String nim;
    private String email;

    public Profile(String nama, String nim, String email) {
        this.nama = nama;
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

    public void updateProfile(String nama, String email) {
        this.nama = nama;
        this.email = email;
    }
}
