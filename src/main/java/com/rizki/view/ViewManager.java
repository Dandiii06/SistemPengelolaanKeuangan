package com.rizki.view;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewManager {
    private static Stage stage;
    private static String currentUsername = "Pengguna";

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static void setStage(Stage mainStage) {
        stage = mainStage;
    }

    public static void showLoginView() {
        LoginView loginView = new LoginView();
        Scene scene = new Scene(loginView.getView(), 1000, 650);
        loadStylesheet(scene);
        stage.setScene(scene);
        stage.setTitle("Sistem Pengelolaan Keuangan - Login");
        stage.centerOnScreen();
    }

    public static void showRegisterView() {
        RegisterView registerView = new RegisterView();
        Scene scene = new Scene(registerView.getView(), 1000, 650);
        loadStylesheet(scene);
        stage.setScene(scene);
        stage.setTitle("Sistem Pengelolaan Keuangan - Registrasi");
        stage.centerOnScreen();
    }

    public static void showHomeView(String username) {
        currentUsername = username;
        HomeView homeView = new HomeView(username);
        Scene scene = new Scene(homeView.getView(), 1200, 750);
        loadStylesheet(scene);
        stage.setScene(scene);
        stage.setTitle("Sistem Pengelolaan Keuangan - Dashboard");
        stage.centerOnScreen();
    }

    private static void loadStylesheet(Scene scene) {
        try {
            String cssPath = ViewManager.class.getResource("/styles.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
        } catch (Exception e) {
            System.err.println("Gagal memuat styles.css: " + e.getMessage());
        }
    }
}
