package com.rizki.model.Manajemen;

/**
 * Interface PenyimpananData mendefinisikan kontrak (kontrak perilaku/abstraksi) 
 * untuk mekanisme penyimpanan data dalam aplikasi, misalnya ke database atau file.
 */
public interface PenyimpananData {
    // Menyimpan objek data ke media penyimpanan
    boolean saveToStorage(Object data);
    // Memuat data dari media penyimpanan
    Object loadFromStorage();
}
