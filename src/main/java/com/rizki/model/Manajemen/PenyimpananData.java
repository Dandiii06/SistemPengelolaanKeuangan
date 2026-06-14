package com.rizki.model.Manajemen;

public interface PenyimpananData {
    boolean saveToStorage(Object data);
    Object loadFromStorage();
}
