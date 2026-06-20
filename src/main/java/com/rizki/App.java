package com.rizki;

import com.rizki.view.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Class App merupakan entry point (titik awal) dari seluruh aplikasi Sistem Pengelolaan Keuangan.
 * Class ini mewarisi (extends) class javafx.application.Application untuk menjalankan UI JavaFX.
 */
public class App extends Application {

    /**
     * Method start() adalah lifecycle method dari JavaFX yang otomatis dipanggil setelah program dijalankan.
     * Method ini menerima parameter 'stage' yang merupakan window utama dari aplikasi JavaFX.
     */
    @Override
    public void start(Stage stage) {
        // Hubungkan Stage utama ke ViewManager agar pergantian halaman bisa dikontrol secara terpusat
        ViewManager.setStage(stage);
        
        // Tampilkan halaman login pertama kali saat aplikasi dibuka
        ViewManager.showLoginView();
        
        // Menampilkan window aplikasi ke layar pengguna
        stage.show();
    }

    /**
     * Method main() adalah method utama yang dipanggil oleh JVM saat program pertama kali dieksekusi.
     * Method ini hanya bertugas memanggil launch() untuk memulai aplikasi JavaFX.
     */
    public static void main(String[] args) {
        launch();
    }
}