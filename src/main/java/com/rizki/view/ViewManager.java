package com.rizki.view;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class ViewManager bertindak sebagai Controller Navigasi (Navigator).
 * Class ini menggunakan pola singleton/static untuk mengontrol perpindahan scene (halaman)
 * pada satu Stage utama yang berjalan di aplikasi.
 */
public class ViewManager {
    // Stage utama aplikasi tempat scene ditampilkan
    private static Stage stage;
    // Menyimpan username pengguna yang sedang login aktif saat ini
    private static String currentUsername = "Pengguna";

    /**
     * Mengambil username pengguna yang saat ini sedang aktif (login).
     */
    public static String getCurrentUsername() {
        return currentUsername;
    }

    /**
     * Mengeset Stage utama aplikasi. Dipanggil sekali di awal pada App.java.
     */
    public static void setStage(Stage mainStage) {
        stage = mainStage;
    }

    /**
     * Membuka halaman Login (LoginView).
     */
    public static void showLoginView() {
        // Membuat instance view login
        LoginView loginView = new LoginView();
        // Mengatur resolusi halaman login menjadi 1000x650 px
        Scene scene = new Scene(loginView.getView(), 1000, 650);
        // Memuat styling CSS eksternal
        loadStylesheet(scene);
        // Mengganti scene di stage utama dengan scene login
        stage.setScene(scene);
        stage.setTitle("Sistem Pengelolaan Keuangan - Login");
        // Memposisikan window di tengah layar monitor
        stage.centerOnScreen();
    }

    /**
     * Membuka halaman Registrasi (RegisterView).
     */
    public static void showRegisterView() {
        // Membuat instance view registrasi
        RegisterView registerView = new RegisterView();
        // Mengatur resolusi halaman registrasi menjadi 1000x650 px
        Scene scene = new Scene(registerView.getView(), 1000, 650);
        loadStylesheet(scene);
        stage.setScene(scene);
        stage.setTitle("Sistem Pengelolaan Keuangan - Registrasi");
        stage.centerOnScreen();
    }

    /**
     * Membuka halaman Dashboard Utama (HomeView) setelah login/register sukses.
     * @param username nama pengguna yang berhasil masuk.
     */
    public static void showHomeView(String username) {
        // Menyimpan status username aktif
        currentUsername = username;
        // Membuat instance dashboard homeview dengan username tersebut
        HomeView homeView = new HomeView(username);
        // Mengatur resolusi halaman dashboard menjadi 1200x750 px
        Scene scene = new Scene(homeView.getView(), 1200, 750);
        loadStylesheet(scene);
        stage.setScene(scene);
        stage.setTitle("Sistem Pengelolaan Keuangan - Dashboard");
        stage.centerOnScreen();
    }

    /**
     * Method helper privat untuk memuat file stylesheet CSS eksternal (styles.css) ke dalam Scene.
     */
    private static void loadStylesheet(Scene scene) {
        try {
            // Mencari file styles.css di folder resources
            String cssPath = ViewManager.class.getResource("/styles.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
        } catch (Exception e) {
            System.err.println("Gagal memuat styles.css: " + e.getMessage());
        }
    }
}
