package com.rizki.model.Manajemen;

public class Validator {
    private boolean statusValidasi;

    public Validator() {
        this.statusValidasi = false;
    }

    public boolean cekInputSaldo(double nominal) {
        if (nominal <= 0) {
            statusValidasi = false;
            return false;
        }
        statusValidasi = true;
        return true;
    }

    public boolean cekValidasiLogin(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            statusValidasi = false;
            return false;
        }
        statusValidasi = true;
        return true;
    }

    public boolean getStatusValidasi() {
        return statusValidasi;
    }
}
