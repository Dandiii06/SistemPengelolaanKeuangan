package com.rizki.view;

import com.rizki.model.Manajemen.DatabaseManager;
import com.rizki.model.Manajemen.Validator;
import com.rizki.model.Pengguna.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Class LoginView merepresentasikan halaman masuk (login) ke dalam aplikasi.
 * Halaman ini didesain menggunakan JavaFX layouts (HBox, VBox) secara programmatikal (code-based UI).
 */
public class LoginView {
    private HBox root;                    // Layout utama horizontal (kiri: banner, kanan: form)
    private TextField txtUsername;         // Input text untuk username
    private PasswordField txtPassword;     // Input text tersembunyi untuk password
    private Button btnLogin;               // Tombol aksi login
    private Button btnToRegister;          // Tombol link untuk pindah ke halaman register
    private Label lblError;                // Label untuk menampilkan pesan error jika login gagal

    /**
     * Constructor LoginView untuk membuat tampilan.
     */
    public LoginView() {
        createView();
    }

    /**
     * Membuat seluruh komponen UI dan layouting halaman login secara detail.
     */
    private void createView() {
        root = new HBox();
        root.setPrefSize(1000, 650);

        // --- LEFT PANEL (Brand Presentation / Banner samping kiri) ---
        VBox leftPanel = new VBox(20);
        leftPanel.setPrefWidth(400);
        leftPanel.setMinWidth(350);
        // Menggunakan styling gradasi warna ungu modern dengan CSS inline JavaFX
        leftPanel.setStyle("-fx-background-color: linear-gradient(to bottom right, #6366f1, #8b5cf6);");
        leftPanel.setPadding(new Insets(45));
        leftPanel.setAlignment(Pos.CENTER_LEFT);

        Label lblEmoji = new Label("💰");
        lblEmoji.setStyle("-fx-font-size: 64px;");

        Label lblBrandName = new Label("Sistem\nPengelolaan\nKeuangan");
        lblBrandName.setTextFill(Color.WHITE);
        lblBrandName.setFont(Font.font("Segoe UI", FontWeight.BOLD, 36));
        lblBrandName.setWrapText(true);

        Label lblBrandSlogan = new Label("Kelola pengeluaran, pantau anggaran, dan raih kebebasan finansial Anda bersama kami.");
        lblBrandSlogan.setTextFill(Color.web("#cbd5e1"));
        lblBrandSlogan.setFont(Font.font("Segoe UI", 15));
        lblBrandSlogan.setWrapText(true);

        leftPanel.getChildren().addAll(lblEmoji, lblBrandName, lblBrandSlogan);

        // --- RIGHT PANEL (Login Form / Area kanan) ---
        VBox rightPanel = new VBox();
        rightPanel.setPrefWidth(600);
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setPadding(new Insets(40));
        rightPanel.setStyle("-fx-background-color: #0f172a;"); // Latar belakang gelap (dark mode)

        // Form Card (Kotak form login)
        VBox formCard = new VBox(15);
        formCard.getStyleClass().add("card");
        formCard.setPrefWidth(440);
        formCard.setMaxWidth(440);
        formCard.setAlignment(Pos.CENTER_LEFT);

        Label lblTitle = new Label("Masuk Akun");
        lblTitle.getStyleClass().add("label-title");

        Label lblSubtitle = new Label("Masukkan kredensial untuk mengakses dashboard");
        lblSubtitle.getStyleClass().add("label-subtitle");
        VBox.setMargin(lblSubtitle, new Insets(0, 0, 15, 0));

        // Form Input Fields
        Label lblUsername = new Label("Username");
        lblUsername.getStyleClass().add("form-label");
        txtUsername = new TextField();
        txtUsername.setPromptText("Masukkan username Anda");

        Label lblPassword = new Label("Password");
        lblPassword.getStyleClass().add("form-label");
        txtPassword = new PasswordField();
        txtPassword.setPromptText("Masukkan password Anda");

        lblError = new Label("");
        lblError.getStyleClass().add("error-label");
        lblError.setWrapText(true);
        lblError.setVisible(false);

        btnLogin = new Button("Masuk Sekarang");
        btnLogin.getStyleClass().add("button-primary");
        btnLogin.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(btnLogin, new Insets(10, 0, 5, 0));

        // Tautan ke pendaftaran akun baru
        HBox registerContainer = new HBox(5);
        registerContainer.setAlignment(Pos.CENTER);
        Label lblNoAccount = new Label("Belum punya akun?");
        lblNoAccount.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 14px;");
        btnToRegister = new Button("Daftar di sini");
        btnToRegister.getStyleClass().add("link-button");
        registerContainer.getChildren().addAll(lblNoAccount, btnToRegister);

        // Menambahkan seluruh komponen ke dalam kartu form login
        formCard.getChildren().addAll(
            lblTitle, lblSubtitle,
            lblUsername, txtUsername,
            lblPassword, txtPassword,
            lblError,
            btnLogin, registerContainer
        );

        rightPanel.getChildren().add(formCard);

        HBox.setHgrow(leftPanel, Priority.ALWAYS);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);
        root.getChildren().addAll(leftPanel, rightPanel);

        // --- CONTROLLER EVENTS (Aksi Event Handler Tombol) ---
        btnLogin.setOnAction(e -> {
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();
            
            Validator validator = new Validator();
            // 1. Validasi input apakah ada field kosong
            if (!validator.cekValidasiLogin(username, password)) {
                lblError.setText("Username dan Password tidak boleh kosong!");
                lblError.setVisible(true);
            } else {
                DatabaseManager dbManager = new DatabaseManager("");
                // 2. Load data user berdasarkan username dari database MySQL
                User user = dbManager.loadUser(username);
                
                // 3. Autentikasi kecocokan password dengan BCrypt
                if (user != null && user.autentikasi(username, password)) {
                    lblError.setVisible(false);
                    // Jika sukses login, navigasikan stage ke halaman Home/Dashboard
                    ViewManager.showHomeView(username);
                } else {
                    // Tampilkan pesan error jika salah username/password
                    lblError.setText("Username atau Password salah!");
                    lblError.setVisible(true);
                }
            }
        });

        // Event handler tombol registrasi: navigasikan ke RegisterView
        btnToRegister.setOnAction(e -> ViewManager.showRegisterView());
    }

    /**
     * Mendapatkan root panel tampilan login untuk dipasang pada Scene.
     */
    public Parent getView() {
        return root;
    }
}
