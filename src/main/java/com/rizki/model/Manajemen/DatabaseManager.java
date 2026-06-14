package com.rizki.model.Manajemen;

public class DatabaseManager implements PenyimpananData {
    private String filePath;

    public DatabaseManager(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean saveToStorage(Object data) {
        if (data == null || filePath == null || filePath.isEmpty()) {
            return false;
        }
        // TODO: Implementasikan penyimpanan data ke file
        return true;
    }

    @Override
    public Object loadFromStorage() {
        if (filePath == null || filePath.isEmpty()) {
            return null;
        }
        // TODO: Implementasikan pembacaan data dari file
        return null;
    }
}
