package com.rizki.model.Pengguna;

public class User {
    private String username;
    private String password;
    private Profile profil;
    private Dompet dompet;

    public User(String username, String password, Profile profil, Dompet dompet) {
        this.username = username;
        this.password = password;
        this.profil = profil;
        this.dompet = dompet;
    }

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

    public boolean autentikasi(String username, String password) {
        if (this.username.equals(username)) {
            try {
                return org.mindrot.jbcrypt.BCrypt.checkpw(password, this.password);
            } catch (Exception e) {
                // Fallback jika password di DB belum ter-hash (plaintext)
                return this.password.equals(password);
            }
        }
        return false;
    }
}
