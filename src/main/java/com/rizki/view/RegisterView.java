package com.rizki.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class RegisterView {
    private HBox root;
    private TextField txtNama;
    private TextField txtNim;
    private TextField txtEmail;
    private TextField txtUsername;
    private PasswordField txtPassword;
    private TextField txtSaldoAwal;
    private Button btnRegister;
    private Button btnToLogin;
    private Label lblError;
    private Label lblSuccess;

    public RegisterView() {
        createView();
    }

    private void createView() {
        root = new HBox();
        root.setPrefSize(1000, 650);

        // --- LEFT PANEL (Brand Presentation) ---
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

        // --- RIGHT PANEL (Register Form) ---
        VBox rightPanel = new VBox();
        rightPanel.setPrefWidth(600);
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setPadding(new Insets(30));
        rightPanel.setStyle("-fx-background-color: #0f172a;");

        // The Register Form Card
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

        // Rows of Fields (Grid Layout style using HBox)
        HBox row1 = new HBox(15);
        row1.setAlignment(Pos.CENTER);
        VBox colNama = createInputField("Nama Lengkap", txtNama = new TextField(), "Masukkan nama");
        VBox colNim = createInputField("NIM", txtNim = new TextField(), "Masukkan NIM");
        HBox.setHgrow(colNama, Priority.ALWAYS);
        HBox.setHgrow(colNim, Priority.ALWAYS);
        row1.getChildren().addAll(colNama, colNim);

        HBox row2 = new HBox(15);
        row2.setAlignment(Pos.CENTER);
        VBox colEmail = createInputField("Email", txtEmail = new TextField(), "Masukkan email");
        VBox colUsername = createInputField("Username", txtUsername = new TextField(), "Masukkan username");
        HBox.setHgrow(colEmail, Priority.ALWAYS);
        HBox.setHgrow(colUsername, Priority.ALWAYS);
        row2.getChildren().addAll(colEmail, colUsername);

        HBox row3 = new HBox(15);
        row3.setAlignment(Pos.CENTER);
        VBox colPassword = new VBox(5);
        Label lblPass = new Label("Password");
        lblPass.getStyleClass().add("form-label");
        txtPassword = new PasswordField();
        txtPassword.setPromptText("Masukkan password");
        colPassword.getChildren().addAll(lblPass, txtPassword);
        
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

        // --- CONTROLLER EVENTS (Temp Switch) ---
        btnRegister.setOnAction(e -> {
            String name = txtNama.getText().trim();
            String nim = txtNim.getText().trim();
            String email = txtEmail.getText().trim();
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();
            String saldoStr = txtSaldoAwal.getText().trim();

            if (name.isEmpty() || nim.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || saldoStr.isEmpty()) {
                lblError.setText("Semua field harus diisi!");
                lblError.setVisible(true);
                lblSuccess.setVisible(false);
            } else {
                try {
                    Double.parseDouble(saldoStr);
                    lblError.setVisible(false);
                    lblSuccess.setText("Registrasi berhasil! Silakan masuk.");
                    lblSuccess.setVisible(true);
                } catch (NumberFormatException ex) {
                    lblError.setText("Saldo awal harus berupa angka!");
                    lblError.setVisible(true);
                    lblSuccess.setVisible(false);
                }
            }
        });

        btnToLogin.setOnAction(e -> ViewManager.showLoginView());
    }

    private VBox createInputField(String labelText, TextField field, String prompt) {
        VBox wrapper = new VBox(5);
        Label lbl = new Label(labelText);
        lbl.getStyleClass().add("form-label");
        field.setPromptText(prompt);
        wrapper.getChildren().addAll(lbl, field);
        return wrapper;
    }

    public Parent getView() {
        return root;
    }
}
