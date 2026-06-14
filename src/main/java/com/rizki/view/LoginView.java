package com.rizki.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.rizki.model.Database.DatabaseHelper;

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

public class LoginView {
    private HBox root;
    private TextField txtUsername;
    private PasswordField txtPassword;
    private Button btnLogin;
    private Button btnToRegister;
    private Label lblError;

    public LoginView() {
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

        // --- RIGHT PANEL (Login Form) ---
        VBox rightPanel = new VBox();
        rightPanel.setPrefWidth(600);
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setPadding(new Insets(40));
        rightPanel.setStyle("-fx-background-color: #0f172a;");

        // The Login Form Card
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

        // Form Fields
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

        HBox registerContainer = new HBox(5);
        registerContainer.setAlignment(Pos.CENTER);
        Label lblNoAccount = new Label("Belum punya akun?");
        lblNoAccount.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 14px;");
        btnToRegister = new Button("Daftar di sini");
        btnToRegister.getStyleClass().add("link-button");
        registerContainer.getChildren().addAll(lblNoAccount, btnToRegister);

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

        // --- CONTROLLER EVENTS (Temp Switch) ---
        btnLogin.setOnAction(e -> {
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();
            if (username.isEmpty() || password.isEmpty()) {
                lblError.setText("Username dan Password tidak boleh kosong!");
                lblError.setVisible(true);
            } else {
                String query = "SELECT nama FROM users WHERE username = ? AND password = ?";
                try (Connection conn = DatabaseHelper.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(query)) {
            
                    stmt.setString(1, username);
                    stmt.setString(2, password);
            
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        // Jika data ditemukan, ambil kolom 'nama' asli dari database
                        // String namaAsli = rs.getString("nama");
                        lblError.setVisible(false);
                        String namaAsli = username;
                        // Pindah ke dashboard dengan nama asli
                        ViewManager.showHomeView(namaAsli);
                    } else {
                        // Jika data tidak ditemukan
                        lblError.setText("Username atau Password salah!");
                        lblError.setVisible(true);
                    }
                } catch (SQLException ex) {
                    lblError.setText("Terjadi kesalahan database: " + ex.getMessage());
                    lblError.setVisible(true);
                    ex.printStackTrace();
                }
            }
        });

        btnToRegister.setOnAction(e -> ViewManager.showRegisterView());
    }

    public Parent getView() {
        return root;
    }
}
