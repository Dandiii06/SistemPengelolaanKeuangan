package com.rizki.view;

import com.rizki.model.Manajemen.DatabaseManager;
import com.rizki.model.Pengguna.Dompet;
import com.rizki.model.Pengguna.Profile;
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
 * Class RegisterView merepresentasikan halaman pendaftaran akun baru bagi pengguna.
 * UI dibangun secara programmatikal menggunakan layout HBox dan VBox pada JavaFX.
 */
public class RegisterView {
    private HBox root;                    // Layout utama horizontal
    private TextField txtNama;             // Input Nama Lengkap
    private TextField txtNim;              // Input NIM
    private TextField txtEmail;            // Input Email student
    private TextField txtUsername;         // Input Username baru
    private PasswordField txtPassword;     // Input Password baru
    private TextField txtSaldoAwal;        // Input Saldo Awal dompet
    private Button btnRegister;            // Tombol untuk mendaftar
    private Button btnToLogin;             // Tombol tautan kembali ke login
    private Label lblError;                // Label penampil error jika registrasi gagal
    private Label lblSuccess;              // Label penampil sukses jika registrasi berhasil

    /**
     * Constructor RegisterView untuk membuat tampilan halaman registrasi.
     */
    public RegisterView() {
        createView();
    }

    /**
     * Membuat semua kontrol komponen input JavaFX dan layouting form pendaftaran.
     */
    private void createView() {
        root = new HBox();
        root.setPrefSize(1000, 650);

        // --- LEFT PANEL (Brand Presentation / Banner gradasi kiri) ---
        VBox leftPanel = new VBox(20);
        leftPanel.setPrefWidth(400);
        leftPanel.setMinWidth(350);
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

        // --- RIGHT PANEL (Register Form / Area form kanan) ---
        VBox rightPanel = new VBox();
        rightPanel.setPrefWidth(600);
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setPadding(new Insets(30));
        rightPanel.setStyle("-fx-background-color: #0f172a;"); // Dark background

        // Card Pembungkus Form
        VBox formCard = new VBox(12);
        formCard.getStyleClass().add("card");
        formCard.setPrefWidth(500);
        formCard.setMaxWidth(500);
        formCard.setAlignment(Pos.CENTER_LEFT);

        Label lblTitle = new Label("Registrasi Akun");
        lblTitle.getStyleClass().add("label-title");

        Label lblSubtitle = new Label("Buat akun baru untuk mengelola keuangan Anda");
        lblSubtitle.getStyleClass().add("label-subtitle");
        VBox.setMargin(lblSubtitle, new Insets(0, 0, 10, 0));

        // Pengelompokan baris form menggunakan HBox agar rapi menyamping (Grid style)
        // Baris 1: Nama Lengkap & NIM
        HBox row1 = new HBox(15);
        row1.setAlignment(Pos.CENTER);
        VBox colNama = createInputField("Nama Lengkap", txtNama = new TextField(), "Masukkan nama");
        VBox colNim = createInputField("NIM", txtNim = new TextField(), "Masukkan NIM");
        HBox.setHgrow(colNama, Priority.ALWAYS);
        HBox.setHgrow(colNim, Priority.ALWAYS);
        row1.getChildren().addAll(colNama, colNim);

        // Baris 2: Email & Username
        HBox row2 = new HBox(15);
        row2.setAlignment(Pos.CENTER);
        VBox colEmail = createInputField("Email", txtEmail = new TextField(), "Masukkan email");
        VBox colUsername = createInputField("Username", txtUsername = new TextField(), "Masukkan username");
        HBox.setHgrow(colEmail, Priority.ALWAYS);
        HBox.setHgrow(colUsername, Priority.ALWAYS);
        row2.getChildren().addAll(colEmail, colUsername);

        // Baris 3: Password & Saldo Awal Dompet
        HBox row3 = new HBox(15);
        row3.setAlignment(Pos.CENTER);
        VBox colPassword = new VBox(5);
        Label lblPass = new Label("Password");
        lblPass.getStyleClass().add("form-label");
        
        // Buat field password tersembunyi dan field teks biasa untuk toggle lihat password
        txtPassword = new PasswordField();
        txtPassword.setPromptText("Masukkan password");
        TextField txtPasswordShow = new TextField();
        txtPasswordShow.setPromptText("Masukkan password");
        txtPasswordShow.setManaged(false);
        txtPasswordShow.setVisible(false);
        
        // Buat Tombol Mata untuk toggling
        Button btnTogglePass = new Button("👁");
        btnTogglePass.setStyle("-fx-background-color: transparent; -fx-text-fill: #94a3b8; -fx-cursor: hand; -fx-font-size: 14px; -fx-padding: 0 8 0 8;");
        
        // Posisikan tombol mata di ujung kanan field password
        javafx.scene.layout.StackPane passPane = new javafx.scene.layout.StackPane();
        passPane.setAlignment(Pos.CENTER_RIGHT);
        passPane.getChildren().addAll(txtPassword, txtPasswordShow, btnTogglePass);
        
        // Logika Toggling Tampilkan/Sembunyikan password
        btnTogglePass.setOnAction(ev -> {
            if (txtPassword.isVisible()) {
                txtPasswordShow.setText(txtPassword.getText());
                txtPasswordShow.setVisible(true);
                txtPasswordShow.setManaged(true);
                txtPassword.setVisible(false);
                txtPassword.setManaged(false);
                btnTogglePass.setText("🔒");
                btnTogglePass.setStyle("-fx-background-color: transparent; -fx-text-fill: #6366f1; -fx-cursor: hand; -fx-font-size: 14px; -fx-padding: 0 8 0 8;");
            } else {
                txtPassword.setText(txtPasswordShow.getText());
                txtPassword.setVisible(true);
                txtPassword.setManaged(true);
                txtPasswordShow.setVisible(false);
                txtPasswordShow.setManaged(false);
                btnTogglePass.setText("👁");
                btnTogglePass.setStyle("-fx-background-color: transparent; -fx-text-fill: #94a3b8; -fx-cursor: hand; -fx-font-size: 14px; -fx-padding: 0 8 0 8;");
            }
        });

        // Sinkronisasi teks agar ketika user mengetik di salah satu field, yang lain juga ikut terupdate
        txtPassword.textProperty().bindBidirectional(txtPasswordShow.textProperty());
        
        colPassword.getChildren().addAll(lblPass, passPane);
        
        VBox colSaldo = createInputField("Saldo Awal (Rp)", txtSaldoAwal = new TextField(), "Contoh: 50000");
        HBox.setHgrow(colPassword, Priority.ALWAYS);
        HBox.setHgrow(colSaldo, Priority.ALWAYS);
        row3.getChildren().addAll(colPassword, colSaldo);

        lblError = new Label("");
        lblError.getStyleClass().add("error-label");
        lblError.setWrapText(true);
        lblError.setVisible(false);

        lblSuccess = new Label("");
        lblSuccess.getStyleClass().add("success-label");
        lblSuccess.setWrapText(true);
        lblSuccess.setVisible(false);

        btnRegister = new Button("Daftar Sekarang");
        btnRegister.getStyleClass().add("button-primary");
        btnRegister.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(btnRegister, new Insets(10, 0, 5, 0));

        // Tautan navigasi kembali ke login jika user sudah memiliki akun
        HBox loginLinkContainer = new HBox(5);
        loginLinkContainer.setAlignment(Pos.CENTER);
        Label lblHaveAccount = new Label("Sudah punya akun?");
        lblHaveAccount.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 14px;");
        btnToLogin = new Button("Masuk di sini");
        btnToLogin.getStyleClass().add("link-button");
        loginLinkContainer.getChildren().addAll(lblHaveAccount, btnToLogin);

        formCard.getChildren().addAll(
            lblTitle, lblSubtitle,
            row1, row2, row3,
            lblError, lblSuccess,
            btnRegister, loginLinkContainer
        );

        rightPanel.getChildren().add(formCard);

        HBox.setHgrow(leftPanel, Priority.ALWAYS);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);
        root.getChildren().addAll(leftPanel, rightPanel);

        // --- CONTROLLER EVENTS (Event Handler Pendaftaran Akun) ---
        btnRegister.setOnAction(e -> {
            String name = txtNama.getText().trim();
            String nim = txtNim.getText().trim();
            String email = txtEmail.getText().trim();
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText();
            String saldoStr = txtSaldoAwal.getText().trim();

            // 1. Validasi apakah semua input wajib telah terisi
            if (name.isEmpty() || nim.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || saldoStr.isEmpty()) {
                lblError.setText("Semua field harus diisi!");
                lblError.setVisible(true);
                lblSuccess.setVisible(false);
                return;
            }

            // 2. Validasi NIM tidak boleh ada huruf (Hanya boleh angka saja)
            if (!nim.matches("\\d+")) {
                lblError.setText("NIM tidak valid! Hanya boleh berisi angka.");
                lblError.setVisible(true);
                lblSuccess.setVisible(false);
                txtNim.requestFocus();
                return;
            }

            // 3. Validasi Email wajib menggunakan domain @gmail.com
            if (!email.toLowerCase().endsWith("@gmail.com")) {
                lblError.setText("Email tidak valid! Harus berakhiran '@gmail.com'.");
                lblError.setVisible(true);
                lblSuccess.setVisible(false);
                txtEmail.requestFocus();
                return;
            }

            try {
                // 4. Validasi format angka untuk saldo awal
                double saldo = Double.parseDouble(saldoStr);
                if (saldo < 0) {
                    lblError.setText("Saldo awal tidak boleh negatif!");
                    lblError.setVisible(true);
                    lblSuccess.setVisible(false);
                    txtSaldoAwal.requestFocus();
                    return;
                }
        
                // 5. Enkripsi (Hashing) password menggunakan BCrypt untuk keamanan database
                String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());
        
                // 6. Instansiasi objek model (Dompet, Profile, User) secara berjenjang (Komposisi)
                Dompet dompet = new Dompet(saldo);
                Profile profile = new Profile(name, hashedPassword, nim, email);
                User user = new User(username, hashedPassword, profile, dompet);
        
                // 7. Menyimpan data akun pengguna baru ke MySQL melalui DatabaseManager
                DatabaseManager dbManager = new DatabaseManager("");
                boolean isSaved = dbManager.saveToStorage(user);
        
                if (isSaved) {
                    lblError.setVisible(false);
                    // Jika registrasi berhasil, navigasikan langsung ke halaman dashboard (HomeView)
                    ViewManager.showHomeView(username);
                } else {
                    // Jika registrasi gagal (misal karena username/NIM/email sudah terdaftar), tampilkan pesan error
                    lblError.setText("Pendaftaran gagal! Username/NIM/Email mungkin sudah terdaftar.");
                    lblError.setVisible(true);
                    lblSuccess.setVisible(false);
                }
        
            } catch (NumberFormatException ex) {
                // Jika input saldo awal tidak valid (bukan angka), tampilkan pesan error yang sesuai
                lblError.setText("Saldo awal harus berupa angka!");
                lblError.setVisible(true);
                lblSuccess.setVisible(false);
                txtSaldoAwal.requestFocus();
            }
        });

        // Event handler link kembali ke login
        btnToLogin.setOnAction(e -> ViewManager.showLoginView());
    }

    /**
     * Method helper privat untuk menyederhanakan pembuatan kolom input field berlabel.
     */
    private VBox createInputField(String labelText, TextField field, String prompt) {
        VBox wrapper = new VBox(5);
        Label lbl = new Label(labelText);
        lbl.getStyleClass().add("form-label");
        field.setPromptText(prompt);
        wrapper.getChildren().addAll(lbl, field);
        return wrapper;
    }

    /**
     * Mengambil root layout view ini untuk dipasang di scene.
     */
    public Parent getView() {
        return root;
    }
}
