package com.rizki.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.rizki.model.Anggaran.Anggaran;
import com.rizki.model.Anggaran.Kategori;
import com.rizki.model.Anggaran.PeriodeAnggaran;
import com.rizki.model.Manajemen.DatabaseManager;
import com.rizki.model.Manajemen.Notifikasi;
import com.rizki.model.Manajemen.Validator;
import com.rizki.model.Pengguna.User;
import com.rizki.model.keuangan.Pemasukan;
import com.rizki.model.keuangan.Pengeluaran;
import com.rizki.model.keuangan.Transaksi;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Class HomeView merepresentasikan halaman dashboard utama aplikasi (Home View).
 * Tampilan ini dinamis dan memiliki 5 panel navigasi utama: Ringkasan, Transaksi, Anggaran, Laporan, dan Profil.
 * Di-render secara modular menggunakan JavaFX pane (BorderPane, StackPane, VBox, HBox).
 */
public class HomeView {
    private BorderPane root;              // Layout utama pembungkus (Sidebar di KIRI, ContentArea di TENGAH)
    private String username;              // Username pengguna aktif
    
    // Objek State Data (Model dan Database)
    private User userModel;               // Menyimpan data user (profil, dompet, transaksi)
    private DatabaseManager dbManager;    // Pengelola koneksi penyimpanan database
    private List<Anggaran> listAnggaran;  // Menyimpan daftar batas anggaran user
    
    // Komponen Navigasi Sidebar (Tombol menu)
    private Button btnRingkasan;
    private Button btnTransaksi;
    private Button btnAnggaran;
    private Button btnLaporan;
    private Button btnProfil;
    private Button btnLogout;
    
    // Panel Konten Dinamis utama (di tengah)
    private StackPane contentArea;
    
    // Sub-halaman (pane) yang akan ditukarkan di dalam contentArea
    private VBox paneRingkasan;
    private VBox paneTransaksi;
    private VBox paneAnggaran;
    private VBox paneLaporan;
    private VBox paneProfil;

    // Komponen UI Ringkasan (Overview)
    private Label lblGreeting;
    private Label lblTotalSaldoValue;
    private Label lblTotalPemasukanValue;
    private Label lblTotalPengeluaranValue;
    private VBox listRecentTransactions;
    private VBox listBudgetProgressBars;

    // Komponen UI Transaksi
    private VBox listAllTransactions;

    // Komponen UI Anggaran
    private VBox listActiveBudgets;
    private Label lblTotalAnggaranBulanan;
    private Label lblTotalAnggaranMingguan;

    // Komponen UI Laporan Analisis
    private Label lblDailyAverage;
    private Label lblMaxCategory;
    private Label lblSavingsRatio;
    private VBox listLaporanDistribution;

    // Komponen UI Profil Akun
    private Label lblAvatarName;
    private Label lblNimText;
    private Label lblLetter;

    /**
     * Constructor HomeView.
     * Mengambil data user ter-update dari database, memuat anggaran aktif,
     * serta membuat komponen-komponen UI awal (createView) dan menyegarkan data (refreshAllData).
     */
    public HomeView(String username) {
        this.username = username;
        this.dbManager = new DatabaseManager("");
        this.userModel = dbManager.loadUser(username);
        // Memuat anggaran dari database dan menghitung sisa anggaran rill
        if (this.userModel != null) {
            this.listAnggaran = dbManager.loadBudgets(username, getPengeluarans(userModel.getDompet().getDaftarTransaksi()));
        } else {
            this.listAnggaran = new ArrayList<>();
        }
        createView();         // Merakit layout & komponen
        refreshAllData();     // Membaca & menyinkronkan data model ke UI
    }

    private void createView() {
        root = new BorderPane();
        root.setPrefSize(1200, 750);

        // --- 1. SIDEBAR ---
        VBox sidebar = new VBox(10);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(240);
        sidebar.setPadding(new Insets(30, 15, 30, 15));

        // Brand Logo Header
        HBox brandHeader = new HBox(10);
        brandHeader.setAlignment(Pos.CENTER_LEFT);
        brandHeader.setPadding(new Insets(0, 0, 30, 10));
        
        Label lblLogo = new Label("💰");
        lblLogo.setStyle("-fx-font-size: 24px;");
        Label lblBrand = new Label("KeuanganKu");
        lblBrand.setTextFill(Color.WHITE);
        lblBrand.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        brandHeader.getChildren().addAll(lblLogo, lblBrand);

        // Navigation Buttons
        btnRingkasan = createSidebarButton("📊  Ringkasan");
        btnTransaksi = createSidebarButton("💸  Transaksi");
        btnAnggaran = createSidebarButton("🎯  Anggaran");
        btnLaporan = createSidebarButton("📈  Laporan");
        btnProfil = createSidebarButton("👤  Profil");
        
        // Active state initially
        setButtonActive(btnRingkasan);

        // Spacer
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Logout Button
        btnLogout = createSidebarButton("🚪  Keluar");
        btnLogout.setStyle("-fx-text-fill: #f43f5e; -fx-background-color: transparent;");
        btnLogout.setOnMouseEntered(e -> btnLogout.setStyle("-fx-background-color: #311b22; -fx-text-fill: #f43f5e; -fx-cursor: hand;"));
        btnLogout.setOnMouseExited(e -> btnLogout.setStyle("-fx-background-color: transparent; -fx-text-fill: #f43f5e;"));
        btnLogout.setOnAction(e -> ViewManager.showLoginView());

        sidebar.getChildren().addAll(brandHeader, btnRingkasan, btnTransaksi, btnAnggaran, btnLaporan, btnProfil, spacer, btnLogout);
        root.setLeft(sidebar);

        // --- 2. MAIN DYNAMIC CONTENT AREA ---
        contentArea = new StackPane();
        contentArea.getStyleClass().add("main-content");

        // Initialize all pages/views
        initRingkasanPane();
        initTransaksiPane();
        initAnggaranPane();
        initLaporanPane();
        initProfilPane();

        // Default view
        showPane(paneRingkasan);
        root.setCenter(contentArea);

        // --- 3. EVENT HANDLERS ---
        btnRingkasan.setOnAction(e -> {
            setButtonActive(btnRingkasan);
            showPane(paneRingkasan);
        });
        btnTransaksi.setOnAction(e -> {
            setButtonActive(btnTransaksi);
            showPane(paneTransaksi);
        });
        btnAnggaran.setOnAction(e -> {
            setButtonActive(btnAnggaran);
            showPane(paneAnggaran);
        });
        btnLaporan.setOnAction(e -> {
            setButtonActive(btnLaporan);
            showPane(paneLaporan);
        });
        btnProfil.setOnAction(e -> {
            setButtonActive(btnProfil);
            showPane(paneProfil);
        });
    }

    private Button createSidebarButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("sidebar-button");
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    private void setButtonActive(Button activeBtn) {
        // Reset styles
        btnRingkasan.getStyleClass().removeAll("sidebar-button-active");
        btnTransaksi.getStyleClass().removeAll("sidebar-button-active");
        btnAnggaran.getStyleClass().removeAll("sidebar-button-active");
        btnLaporan.getStyleClass().removeAll("sidebar-button-active");
        btnProfil.getStyleClass().removeAll("sidebar-button-active");

        // Add class to active
        activeBtn.getStyleClass().add("sidebar-button-active");
    }

    private void showPane(VBox pane) {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(pane);
    }

    // ==========================================
    // 1. RINGKASAN PANE (Overview)
    // ==========================================
    private void initRingkasanPane() {
        paneRingkasan = new VBox(25);
        paneRingkasan.setAlignment(Pos.TOP_LEFT);

        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        VBox titleWrapper = new VBox(5);
        lblGreeting = new Label("Halo, " + (userModel != null ? userModel.getProfil().getNama() : username) + "!");
        lblGreeting.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
        lblGreeting.setTextFill(Color.WHITE);
        Label lblGreetingDesc = new Label("Berikut adalah ringkasan keuangan pribadi Anda hari ini.");
        titleWrapper.getChildren().addAll(lblGreeting, lblGreetingDesc);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnAddTransaction = new Button("➕ Transaksi Baru");
        btnAddTransaction.getStyleClass().add("button-primary");
        btnAddTransaction.setOnAction(e -> {
            setButtonActive(btnTransaksi);
            showPane(paneTransaksi);
        });
        header.getChildren().addAll(titleWrapper, spacer, btnAddTransaction);

        // Metric Cards (HBox)
        HBox metricContainer = new HBox(20);
        metricContainer.setAlignment(Pos.CENTER);

        VBox cardSaldo = createMetricCard("TOTAL SALDO UTAMA", "Rp 0", "metric-card-primary", "Status: Aman & Terkendali");
        lblTotalSaldoValue = (Label) cardSaldo.getChildren().get(1);

        VBox cardPemasukan = createMetricCard("TOTAL PEMASUKAN", "Rp 0", "metric-card-success", "Total Pemasukan Masuk");
        lblTotalPemasukanValue = (Label) cardPemasukan.getChildren().get(1);

        VBox cardPengeluaran = createMetricCard("TOTAL PENGELUARAN", "Rp 0", "metric-card-danger", "Total Pengeluaran Keluar");
        lblTotalPengeluaranValue = (Label) cardPengeluaran.getChildren().get(1);

        HBox.setHgrow(cardSaldo, Priority.ALWAYS);
        HBox.setHgrow(cardPemasukan, Priority.ALWAYS);
        HBox.setHgrow(cardPengeluaran, Priority.ALWAYS);
        metricContainer.getChildren().addAll(cardSaldo, cardPemasukan, cardPengeluaran);

        // Bottom Split Panel
        HBox bottomContainer = new HBox(25);
        bottomContainer.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(bottomContainer, Priority.ALWAYS);

        // Left section: Recent Transactions (VBox)
        VBox recentBox = new VBox(15);
        recentBox.getStyleClass().add("card");
        HBox.setHgrow(recentBox, Priority.ALWAYS);

        Label lblRecentTitle = new Label("Transaksi Terakhir");
        lblRecentTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        lblRecentTitle.setTextFill(Color.WHITE);

        listRecentTransactions = new VBox(10);
        recentBox.getChildren().addAll(lblRecentTitle, listRecentTransactions);

        // Right section: Budgets Status
        VBox budgetBox = new VBox(15);
        budgetBox.getStyleClass().add("card");
        budgetBox.setPrefWidth(370);

        Label lblBudgetTitle = new Label("Batas Anggaran");
        lblBudgetTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        lblBudgetTitle.setTextFill(Color.WHITE);

        listBudgetProgressBars = new VBox(15);
        budgetBox.getChildren().addAll(lblBudgetTitle, listBudgetProgressBars);

        bottomContainer.getChildren().addAll(recentBox, budgetBox);
        paneRingkasan.getChildren().addAll(header, metricContainer, bottomContainer);
    }

    private VBox createMetricCard(String title, String value, String cssClass, String footerText) {
        VBox card = new VBox(10);
        card.getStyleClass().add(cssClass);
        
        Label lblTitle = new Label(title);
        lblTitle.getStyleClass().add("metric-title");

        Label lblVal = new Label(value);
        lblVal.getStyleClass().add("metric-value");

        Label lblFooter = new Label(footerText);
        lblFooter.setStyle("-fx-text-fill: rgba(255, 255, 255, 0.75); -fx-font-size: 12px;");

        card.getChildren().addAll(lblTitle, lblVal, lblFooter);
        return card;
    }

    private HBox createTransactionRow(String desc, String cat, String date, String amount, boolean isIncome) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10));
        row.setStyle("-fx-background-color: #0f172a; -fx-background-radius: 8px;");

        // Icon Circle
        Circle circle = new Circle(18);
        circle.setFill(isIncome ? Color.web("#059669") : Color.web("#dc2626"));
        Label lblIcon = new Label(isIncome ? "⬇" : "⬆");
        lblIcon.setTextFill(Color.WHITE);
        lblIcon.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        StackPane iconPane = new StackPane(circle, lblIcon);

        VBox descBox = new VBox(4);
        Label lblDesc = new Label(desc);
        lblDesc.setTextFill(Color.WHITE);
        lblDesc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        Label lblCatDate = new Label(cat + "  •  " + date);
        lblCatDate.setStyle("-fx-text-fill: #64748b; -fx-font-size: 12px;");
        descBox.getChildren().addAll(lblDesc, lblCatDate);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblAmount = new Label(amount);
        lblAmount.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        lblAmount.setTextFill(isIncome ? Color.web("#10b981") : Color.web("#f43f5e"));

        row.getChildren().addAll(iconPane, descBox, spacer, lblAmount);
        return row;
    }

    private VBox createBudgetProgressBar(String category, double progress, String detailText) {
        VBox box = new VBox(6);
        
        HBox labelRow = new HBox();
        Label lblCat = new Label(category);
        lblCat.setTextFill(Color.WHITE);
        lblCat.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label lblDetail = new Label(detailText);
        lblDetail.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 11px;");
        
        labelRow.getChildren().addAll(lblCat, spacer, lblDetail);

        ProgressBar pb = new ProgressBar(progress);
        pb.setMaxWidth(Double.MAX_VALUE);
        if (progress > 0.8) {
            pb.setStyle("-fx-accent: #f43f5e;"); // Red warning
        } else {
            pb.setStyle("-fx-accent: #6366f1;"); // Primary blue-purple
        }

        box.getChildren().addAll(labelRow, pb);
        return box;
    }

    // ==========================================
    // 2. TRANSAKSI PANE
    // ==========================================
    private void initTransaksiPane() {
        paneTransaksi = new VBox(25);
        paneTransaksi.setAlignment(Pos.TOP_LEFT);

        Label lblTitle = new Label("Riwayat Transaksi");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        lblTitle.setTextFill(Color.WHITE);

        HBox mainLayout = new HBox(25);
        VBox.setVgrow(mainLayout, Priority.ALWAYS);

        // Form Card (Left Column)
        VBox formCard = new VBox(15);
        formCard.getStyleClass().add("card");
        formCard.setPrefWidth(400);

        Label lblFormTitle = new Label("Tambah Transaksi Baru");
        lblFormTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        lblFormTitle.setTextFill(Color.WHITE);

        ComboBox<String> cmbJenis = new ComboBox<>();
        cmbJenis.getItems().addAll("Pemasukan", "Pengeluaran");
        cmbJenis.setValue("Pengeluaran");
        cmbJenis.setMaxWidth(Double.MAX_VALUE);

        TextField txtNominal = new TextField();
        txtNominal.setPromptText("Nominal (Contoh: 50000)");

        ComboBox<String> cmbKategori = new ComboBox<>();
        cmbKategori.getItems().addAll("Konsumsi Makanan", "Transportasi", "Pendidikan", "Pemasukan Gaji", "Tabungan", "Kesehatan", "Hiburan", "Lainnya");
        cmbKategori.setValue("Pilih Kategori...");
        cmbKategori.setMaxWidth(Double.MAX_VALUE);

        DatePicker dpTanggal = new DatePicker();
        dpTanggal.setMaxWidth(Double.MAX_VALUE);
        dpTanggal.setPromptText("Pilih tanggal");

        TextArea txtCatatan = new TextArea();
        txtCatatan.setPromptText("Tambahkan catatan...");
        txtCatatan.setPrefRowCount(3);

        Button btnSave = new Button("Simpan Transaksi");
        btnSave.getStyleClass().add("button-primary");
        btnSave.setMaxWidth(Double.MAX_VALUE);

        btnSave.setOnAction(e -> {
            try {
                String nominalStr = txtNominal.getText().trim();
                if (nominalStr.isEmpty()) return;
                
                double nominal = Double.parseDouble(nominalStr);
                String kategoriStr = cmbKategori.getValue();
                String jenis = cmbJenis.getValue();
                String catatan = txtCatatan.getText().trim();
                LocalDate tgl = dpTanggal.getValue();
                String tanggalStr = (tgl != null) ? tgl.toString() : LocalDate.now().toString();
                String id = "TX-" + System.currentTimeMillis();

                Validator validator = new Validator();
                if (!validator.cekInputSaldo(nominal) || kategoriStr == null || kategoriStr.equals("Pilih Kategori...")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Kesalahan Input");
                    alert.setHeaderText(null);
                    alert.setContentText("Input nominal harus lebih dari 0 dan kategori harus dipilih!");
                    alert.showAndWait();
                    return;
                }

                Transaksi tx;
                if ("Pemasukan".equals(jenis)) {
                    tx = new Pemasukan(id, nominal, tanggalStr, catatan, kategoriStr);
                    ((Pemasukan) tx).tambahSaldoDompet(userModel.getDompet());
                } else {
                    tx = new Pengeluaran(id, nominal, tanggalStr, catatan, new Kategori(kategoriStr));
                    ((Pengeluaran) tx).kurangiSaldoDompet(userModel.getDompet());
                }

                // Simpan transaksi di model & DB
                userModel.getDompet().tambahTransaksi(tx);
                dbManager.saveToStorage(tx);
                dbManager.saveToStorage(userModel);

                // Cek Notifikasi
                Notifikasi notif = new Notifikasi();
                notif.cekSaldoKritis(userModel.getDompet());
                if (tx instanceof Pengeluaran) {
                    Pengeluaran[] pengeluarans = getPengeluarans(userModel.getDompet().getDaftarTransaksi());
                    for (Anggaran ang : listAnggaran) {
                        if (ang.getKategori().getNamaKategori().equalsIgnoreCase(kategoriStr)) {
                            ang.hitungSisaLimit(pengeluarans);
                            notif.cekAmbangBatas(ang);
                        }
                    }
                }

                if (!notif.getPesanPeringatan().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Peringatan Keuangan");
                    alert.setHeaderText(null);
                    alert.setContentText(notif.getPesanPeringatan());
                    alert.showAndWait();
                }

                // Bersihkan form
                txtNominal.clear();
                txtCatatan.clear();
                dpTanggal.setValue(null);

                // Refresh seluruh tampilan
                refreshAllData();

            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Kesalahan Input");
                alert.setHeaderText(null);
                alert.setContentText("Nominal harus berupa angka!");
                alert.showAndWait();
            }
        });

        formCard.getChildren().addAll(
            lblFormTitle,
            new Label("Jenis Transaksi") {{ getStyleClass().add("form-label"); }}, cmbJenis,
            new Label("Nominal (Rp)") {{ getStyleClass().add("form-label"); }}, txtNominal,
            new Label("Kategori") {{ getStyleClass().add("form-label"); }}, cmbKategori,
            new Label("Tanggal") {{ getStyleClass().add("form-label"); }}, dpTanggal,
            new Label("Catatan") {{ getStyleClass().add("form-label"); }}, txtCatatan,
            btnSave
        );

        // Table List (Right Column)
        VBox tableCard = new VBox(15);
        tableCard.getStyleClass().add("card");
        HBox.setHgrow(tableCard, Priority.ALWAYS);

        Label lblTableTitle = new Label("Daftar Transaksi Anda");
        lblTableTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        lblTableTitle.setTextFill(Color.WHITE);

        listAllTransactions = new VBox(8);

        tableCard.getChildren().addAll(lblTableTitle, listAllTransactions);
        mainLayout.getChildren().addAll(formCard, tableCard);
        paneTransaksi.getChildren().addAll(lblTitle, mainLayout);
    }

    // ==========================================
    // 3. ANGGARAN PANE
    // ==========================================
    private void initAnggaranPane() {
        paneAnggaran = new VBox(25);
        paneAnggaran.setAlignment(Pos.TOP_LEFT);

        Label lblTitle = new Label("Manajemen Anggaran");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        lblTitle.setTextFill(Color.WHITE);

        HBox mainLayout = new HBox(25);
        VBox.setVgrow(mainLayout, Priority.ALWAYS);

        // Add budget form (Left Column)
        VBox formCard = new VBox(15);
        formCard.getStyleClass().add("card");
        formCard.setPrefWidth(400);

        Label lblFormTitle = new Label("Batasi Anggaran Baru");
        lblFormTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        lblFormTitle.setTextFill(Color.WHITE);

        ComboBox<String> cmbKategori = new ComboBox<>();
        cmbKategori.getItems().addAll("Konsumsi Makanan", "Transportasi", "Pendidikan", "Hiburan", "Lainnya");
        cmbKategori.setValue("Konsumsi Makanan");
        cmbKategori.setMaxWidth(Double.MAX_VALUE);

        TextField txtLimit = new TextField();
        txtLimit.setPromptText("Batas Maksimal (Rp) (Contoh: 1000000)");

        ComboBox<String> cmbPeriode = new ComboBox<>();
        cmbPeriode.getItems().addAll("Mingguan", "Bulanan");
        cmbPeriode.setValue("Bulanan");
        cmbPeriode.setMaxWidth(Double.MAX_VALUE);

        Button btnSaveBudget = new Button("Tentukan Anggaran");
        btnSaveBudget.getStyleClass().add("button-primary");
        btnSaveBudget.setMaxWidth(Double.MAX_VALUE);

        btnSaveBudget.setOnAction(e -> {
            try {
                String limitStr = txtLimit.getText().trim();
                if (limitStr.isEmpty()) return;
                
                double limit = Double.parseDouble(limitStr);
                String katStr = cmbKategori.getValue();
                String perStr = cmbPeriode.getValue();

                Validator validator = new Validator();
                if (!validator.cekInputSaldo(limit)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Kesalahan Input");
                    alert.setHeaderText(null);
                    alert.setContentText("Batas anggaran harus lebih dari 0!");
                    alert.showAndWait();
                    return;
                }

                Kategori kategoriObj = new Kategori(katStr);
                PeriodeAnggaran periodeObj = new PeriodeAnggaran(perStr);
                Anggaran budget = new Anggaran(limit, kategoriObj, periodeObj);

                // Simpan ke DB
                dbManager.saveToStorage(budget);

                // Reload budgets list
                listAnggaran = dbManager.loadBudgets(username, getPengeluarans(userModel.getDompet().getDaftarTransaksi()));

                // Bersihkan form
                txtLimit.clear();

                // Refresh data
                refreshAllData();

            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Kesalahan Input");
                alert.setHeaderText(null);
                alert.setContentText("Batas anggaran harus berupa angka!");
                alert.showAndWait();
            }
        });

        formCard.getChildren().addAll(
            lblFormTitle,
            new Label("Kategori Pengeluaran") {{ getStyleClass().add("form-label"); }}, cmbKategori,
            new Label("Batas Anggaran Maksimal (Rp)") {{ getStyleClass().add("form-label"); }}, txtLimit,
            new Label("Periode Anggaran") {{ getStyleClass().add("form-label"); }}, cmbPeriode,
            btnSaveBudget
        );

        // Active Budget List (Right Column)
        VBox budgetCard = new VBox(20);
        budgetCard.getStyleClass().add("card");
        HBox.setHgrow(budgetCard, Priority.ALWAYS);

        Label lblActiveTitle = new Label("Anggaran Aktif & Penggunaan");
        lblActiveTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        lblActiveTitle.setTextFill(Color.WHITE);

        // --- Kartu Total Anggaran ---
        HBox totalAnggaranBox = new HBox(20);
        totalAnggaranBox.setAlignment(Pos.CENTER_LEFT);
        totalAnggaranBox.setPadding(new Insets(12, 16, 12, 16));
        totalAnggaranBox.setStyle("-fx-background-color: #1e293b; -fx-background-radius: 10px; -fx-border-color: #334155; -fx-border-radius: 10px;");

        VBox bulananBox = new VBox(4);
        Label lblBulananTitle = new Label("📅  Total Anggaran Bulanan");
        lblBulananTitle.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 12px;");
        lblTotalAnggaranBulanan = new Label("Rp 0");
        lblTotalAnggaranBulanan.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        lblTotalAnggaranBulanan.setTextFill(Color.web("#6366f1"));
        bulananBox.getChildren().addAll(lblBulananTitle, lblTotalAnggaranBulanan);

        Region totalSpacer = new Region();
        HBox.setHgrow(totalSpacer, Priority.ALWAYS);

        VBox mingguanBox = new VBox(4);
        Label lblMingguanTitle = new Label("🗓  Total Anggaran Mingguan");
        lblMingguanTitle.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 12px;");
        lblTotalAnggaranMingguan = new Label("Rp 0");
        lblTotalAnggaranMingguan.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        lblTotalAnggaranMingguan.setTextFill(Color.web("#10b981"));
        mingguanBox.getChildren().addAll(lblMingguanTitle, lblTotalAnggaranMingguan);

        totalAnggaranBox.getChildren().addAll(bulananBox, totalSpacer, mingguanBox);

        listActiveBudgets = new VBox(20);

        budgetCard.getChildren().addAll(lblActiveTitle, totalAnggaranBox, listActiveBudgets);
        mainLayout.getChildren().addAll(formCard, budgetCard);
        paneAnggaran.getChildren().addAll(lblTitle, mainLayout);
    }

    // ==========================================
    // 4. LAPORAN PANE
    // ==========================================
    private void initLaporanPane() {
        paneLaporan = new VBox(25);
        paneLaporan.setAlignment(Pos.TOP_LEFT);

        Label lblTitle = new Label("Laporan Analisis Keuangan");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        lblTitle.setTextFill(Color.WHITE);

        HBox statsGrid = new HBox(20);
        
        VBox stat1 = createStatCard("Rata-rata Pengeluaran Harian", "Rp 0");
        lblDailyAverage = (Label) stat1.getChildren().get(1);

        VBox stat2 = createStatCard("Kategori Pengeluaran Terbesar", "-");
        lblMaxCategory = (Label) stat2.getChildren().get(1);

        VBox stat3 = createStatCard("Rasio Tabungan Bulan Ini", "0.00 %");
        lblSavingsRatio = (Label) stat3.getChildren().get(1);

        HBox.setHgrow(stat1, Priority.ALWAYS);
        HBox.setHgrow(stat2, Priority.ALWAYS);
        HBox.setHgrow(stat3, Priority.ALWAYS);
        statsGrid.getChildren().addAll(stat1, stat2, stat3);

        VBox chartCard = new VBox(20);
        chartCard.getStyleClass().add("card");
        VBox.setVgrow(chartCard, Priority.ALWAYS);

        Label lblBreakdownTitle = new Label("Analisis Distribusi Pengeluaran");
        lblBreakdownTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        lblBreakdownTitle.setTextFill(Color.WHITE);

        listLaporanDistribution = new VBox(15);

        chartCard.getChildren().addAll(lblBreakdownTitle, listLaporanDistribution);
        paneLaporan.getChildren().addAll(lblTitle, statsGrid, chartCard);
    }

    private VBox createStatCard(String title, String value) {
        VBox card = new VBox(10);
        card.getStyleClass().add("card");
        
        Label lblTitle = new Label(title);
        lblTitle.getStyleClass().add("label-subtitle");
        
        Label lblVal = new Label(value);
        lblVal.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        lblVal.setTextFill(Color.WHITE);

        card.getChildren().addAll(lblTitle, lblVal);
        return card;
    }

    private VBox createDistributionRow(String title, String val, String percentage, double ratio, Color barColor) {
        VBox box = new VBox(8);
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        
        Label lblTitle = new Label(title);
        lblTitle.setTextFill(Color.WHITE);
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblVal = new Label(val + " (" + percentage + ")");
        lblVal.setTextFill(Color.web("#94a3b8"));
        lblVal.setFont(Font.font("Segoe UI", 12));

        row.getChildren().addAll(lblTitle, spacer, lblVal);

        ProgressBar pb = new ProgressBar(ratio);
        pb.setMaxWidth(Double.MAX_VALUE);
        pb.setStyle("-fx-accent: " + toHex(barColor) + ";");

        box.getChildren().addAll(row, pb);
        return box;
    }

    private String toHex(Color color) {
        return String.format("#%02x%02x%02x", 
            (int)(color.getRed() * 255), 
            (int)(color.getGreen() * 255), 
            (int)(color.getBlue() * 255));
    }

    // ==========================================
    // 5. PROFIL PANE
    // ==========================================
    private void initProfilPane() {
        paneProfil = new VBox(25);
        paneProfil.setAlignment(Pos.TOP_LEFT);

        Label lblTitle = new Label("Profil Akun Pengguna");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        lblTitle.setTextFill(Color.WHITE);

        VBox formCard = new VBox(20);
        formCard.getStyleClass().add("card");
        formCard.setPrefWidth(500);
        formCard.setMaxWidth(500);

        // Header profil
        HBox avatarBox = new HBox(20);
        avatarBox.setAlignment(Pos.CENTER_LEFT);
        
        Circle avatarCircle = new Circle(40);
        avatarCircle.setFill(Color.web("#6366f1"));
        
        String initial = (userModel != null && !userModel.getProfil().getNama().isEmpty()) 
            ? userModel.getProfil().getNama().substring(0, 1).toUpperCase() 
            : username.substring(0, 1).toUpperCase();
            
        lblLetter = new Label(initial);
        lblLetter.setTextFill(Color.WHITE);
        lblLetter.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        StackPane avatarPane = new StackPane(avatarCircle, lblLetter);

        VBox userDetailBox = new VBox(5);
        lblAvatarName = new Label(userModel != null ? userModel.getProfil().getNama() : "Nama Pengguna");
        lblAvatarName.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        lblAvatarName.setTextFill(Color.WHITE);
        lblNimText = new Label("NIM: " + (userModel != null ? userModel.getProfil().getNim() : "-"));
        lblNimText.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 14px;");
        userDetailBox.getChildren().addAll(lblAvatarName, lblNimText);

        avatarBox.getChildren().addAll(avatarPane, userDetailBox);

        // Details fields
        TextField txtNama = new TextField(userModel != null ? userModel.getProfil().getNama() : "");
        TextField txtNim = new TextField(userModel != null ? userModel.getProfil().getNim() : "");
        txtNim.setEditable(false);
        TextField txtEmail = new TextField(userModel != null ? userModel.getProfil().getEmail() : "");
        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Masukkan password baru jika ingin diubah");

        Button btnUpdate = new Button("Simpan Perubahan");
        btnUpdate.getStyleClass().add("button-primary");
        btnUpdate.setMaxWidth(Double.MAX_VALUE);

        btnUpdate.setOnAction(e -> {
            String nama = txtNama.getText().trim();
            String email = txtEmail.getText().trim();
            String pass = txtPass.getText().trim();

            if (nama.isEmpty() || email.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Nama dan email tidak boleh kosong!");
                alert.showAndWait();
                return;
            }

            if (userModel != null) {
                userModel.getProfil().updateProfile(nama, email);
                if (!pass.isEmpty()) {
                    String hashed = org.mindrot.jbcrypt.BCrypt.hashpw(pass, org.mindrot.jbcrypt.BCrypt.gensalt());
                    userModel.setPassword(hashed);
                    userModel.getProfil().setPassword(hashed);
                }
                
                boolean saved = dbManager.saveToStorage(userModel);
                if (saved) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sukses");
                    alert.setHeaderText(null);
                    alert.setContentText("Profil berhasil disimpan!");
                    alert.showAndWait();
                    
                    txtPass.clear();
                    refreshAllData();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Gagal menyimpan profil ke database!");
                    alert.showAndWait();
                }
            }
        });

        formCard.getChildren().addAll(
            avatarBox,
            new Separator() {{ setStyle("-fx-background-color: #334155;"); }},
            new Label("Nama Lengkap") {{ getStyleClass().add("form-label"); }}, txtNama,
            new Label("NIM") {{ getStyleClass().add("form-label"); }}, txtNim,
            new Label("Email Student") {{ getStyleClass().add("form-label"); }}, txtEmail,
            new Label("Ganti Password") {{ getStyleClass().add("form-label"); }}, txtPass,
            btnUpdate
        );

        paneProfil.getChildren().addAll(lblTitle, formCard);
    }

    public Parent getView() {
        return root;
    }

    /**
     * Memperbarui seluruh data yang ditampilkan di UI dashboard dengan mengambil
     * data terbaru dari model Java. Dipanggil setelah transaksi/budget disimpan.
     */
    private void refreshAllData() {
        if (userModel == null) return;
        refreshRingkasanData(); // Segarkan kartu metric & transaksi terakhir di tab Ringkasan
        refreshTransaksiData(); // Segarkan tabel riwayat transaksi lengkap
        refreshAnggaranData();  // Segarkan progress bar batas anggaran aktif
        refreshLaporanData();   // Segarkan grafik analisis rata-rata & distribusi pengeluaran
    }

    /**
     * Menyegarkan panel Ringkasan (Overview):
     * menghitung total saldo, total masuk, total keluar, dan merender 5 transaksi teranyar.
     */
    private void refreshRingkasanData() {
        if (userModel == null) return;
        double saldo = userModel.getDompet().getSaldo();
        double totalIn = 0;
        double totalOut = 0;
        Transaksi[] txs = userModel.getDompet().getDaftarTransaksi();
        
        for (Transaksi t : txs) {
            if (t instanceof Pemasukan) {
                totalIn += t.getJumlah();
            } else if (t instanceof Pengeluaran) {
                totalOut += t.getJumlah();
            }
        }
        
        if (lblGreeting != null) {
            lblGreeting.setText("Halo, " + userModel.getProfil().getNama() + "!");
        }
        if (lblTotalSaldoValue != null) {
            lblTotalSaldoValue.setText(formatRupiah(saldo));
        }
        if (lblTotalPemasukanValue != null) {
            lblTotalPemasukanValue.setText(formatRupiah(totalIn));
        }
        if (lblTotalPengeluaranValue != null) {
            lblTotalPengeluaranValue.setText(formatRupiah(totalOut));
        }
        
        if (listRecentTransactions != null) {
            listRecentTransactions.getChildren().clear();
            int count = 0;
            for (int i = txs.length - 1; i >= 0 && count < 5; i--, count++) {
                Transaksi t = txs[i];
                boolean isIn = t instanceof Pemasukan;
                String label = t.getCatatan();
                String cat = isIn ? "Pemasukan" : ((Pengeluaran) t).getKategori().getNamaKategori();
                String amtStr = (isIn ? "Rp " : "- Rp ") + formatNumber(t.getJumlah());
                listRecentTransactions.getChildren().add(createTransactionRow(label, cat, t.getTanggal(), amtStr, isIn));
            }
            if (txs.length == 0) {
                listRecentTransactions.getChildren().add(new Label("Belum ada transaksi.") {{ setTextFill(Color.web("#94a3b8")); }});
            }
        }

        if (listBudgetProgressBars != null) {
            listBudgetProgressBars.getChildren().clear();
            Pengeluaran[] pengeluarans = getPengeluarans(txs);
            double totalBulananRingkasan = 0;
            double totalMingguanRingkasan = 0;
            for (Anggaran ang : listAnggaran) {
                ang.hitungSisaLimit(pengeluarans);
                double progress = ang.getBatasMaksimal() > 0 ? (ang.getTotalTerpakai() / ang.getBatasMaksimal()) : 0;
                String periode = ang.getPeriode() != null ? ang.getPeriode().getRentangWaktu() : "Bulanan";
                String detail = formatRupiah(ang.getTotalTerpakai()) + " / " + formatRupiah(ang.getBatasMaksimal()) + " (" + periode + ")";
                listBudgetProgressBars.getChildren().add(createBudgetProgressBar(
                    ang.getKategori().getNamaKategori(), progress, detail));
                if ("Mingguan".equalsIgnoreCase(periode)) {
                    totalMingguanRingkasan += ang.getBatasMaksimal();
                } else {
                    totalBulananRingkasan += ang.getBatasMaksimal();
                }
            }
            if (!listAnggaran.isEmpty()) {
                // Tampilkan ringkasan total di bawah progress bar
                Separator sep = new Separator();
                sep.setStyle("-fx-background-color: #334155;");
                HBox totalRow = new HBox(10);
                totalRow.setAlignment(Pos.CENTER_LEFT);
                Label lblTotalBln = new Label("Total Bulanan: " + formatRupiah(totalBulananRingkasan));
                lblTotalBln.setStyle("-fx-text-fill: #6366f1; -fx-font-size: 11px; -fx-font-weight: bold;");
                Region s = new Region(); HBox.setHgrow(s, Priority.ALWAYS);
                Label lblTotalMgg = new Label("Mingguan: " + formatRupiah(totalMingguanRingkasan));
                lblTotalMgg.setStyle("-fx-text-fill: #10b981; -fx-font-size: 11px; -fx-font-weight: bold;");
                totalRow.getChildren().addAll(lblTotalBln, s, lblTotalMgg);
                listBudgetProgressBars.getChildren().addAll(sep, totalRow);
            } else {
                listBudgetProgressBars.getChildren().add(new Label("Belum ada batas anggaran.") {{ setTextFill(Color.web("#94a3b8")); }});
            }
        }
    }

    private void refreshTransaksiData() {
        if (userModel == null) return;
        if (listAllTransactions != null) {
            listAllTransactions.getChildren().clear();
            Transaksi[] txs = userModel.getDompet().getDaftarTransaksi();
            for (int i = txs.length - 1; i >= 0; i--) {
                Transaksi t = txs[i];
                boolean isIn = t instanceof Pemasukan;
                String label = t.getCatatan();
                String cat = isIn ? "Pemasukan" : ((Pengeluaran) t).getKategori().getNamaKategori();
                String amtStr = (isIn ? "Rp " : "- Rp ") + formatNumber(t.getJumlah());
                listAllTransactions.getChildren().add(createTransactionRow(label, cat, t.getTanggal(), amtStr, isIn));
            }
            if (txs.length == 0) {
                listAllTransactions.getChildren().add(new Label("Belum ada transaksi.") {{ setTextFill(Color.web("#94a3b8")); }});
            }
        }
    }

    private void refreshAnggaranData() {
        if (userModel == null) return;
        if (listActiveBudgets != null) {
            listActiveBudgets.getChildren().clear();
            Pengeluaran[] pengeluarans = getPengeluarans(userModel.getDompet().getDaftarTransaksi());

            double totalBulanan = 0;
            double totalMingguan = 0;

            for (Anggaran ang : listAnggaran) {
                ang.hitungSisaLimit(pengeluarans);
                double progress = ang.getBatasMaksimal() > 0 ? (ang.getTotalTerpakai() / ang.getBatasMaksimal()) : 0;
                String periodeStr = ang.getPeriode() != null ? ang.getPeriode().getRentangWaktu() : "Bulanan";
                String detail = formatRupiah(ang.getTotalTerpakai()) + " / " + formatRupiah(ang.getBatasMaksimal())
                               + "  •  Sisa: " + formatRupiah(ang.getBatasMaksimal() - ang.getTotalTerpakai())
                               + "  (" + periodeStr + ")";
                listActiveBudgets.getChildren().add(createBudgetProgressBar(
                    ang.getKategori().getNamaKategori(), progress, detail));

                // Akumulasi total per periode
                if ("Mingguan".equalsIgnoreCase(periodeStr)) {
                    totalMingguan += ang.getBatasMaksimal();
                } else {
                    totalBulanan += ang.getBatasMaksimal();
                }
            }

            // Update label total anggaran di kartu ringkasan
            if (lblTotalAnggaranBulanan != null) {
                lblTotalAnggaranBulanan.setText(formatRupiah(totalBulanan));
            }
            if (lblTotalAnggaranMingguan != null) {
                lblTotalAnggaranMingguan.setText(formatRupiah(totalMingguan));
            }

            if (listAnggaran.isEmpty()) {
                listActiveBudgets.getChildren().add(new Label("Belum ada anggaran aktif.") {{ setTextFill(Color.web("#94a3b8")); }});
            }
        }
    }

    private void refreshLaporanData() {
        if (userModel == null) return;
        double totalIn = 0;
        double totalOut = 0;
        Transaksi[] txs = userModel.getDompet().getDaftarTransaksi();
        
        Map<String, Double> catMap = new HashMap<>();
        Set<String> uniqueDays = new HashSet<>();
        
        for (Transaksi t : txs) {
            if (t instanceof Pemasukan) {
                totalIn += t.getJumlah();
            } else if (t instanceof Pengeluaran) {
                double amt = t.getJumlah();
                totalOut += amt;
                Kategori cat = ((Pengeluaran) t).getKategori();
                String catName = cat != null ? cat.getNamaKategori() : "Lainnya";
                catMap.put(catName, catMap.getOrDefault(catName, 0.0) + amt);
                uniqueDays.add(t.getTanggal());
            }
        }
        
        double dailyAvg = uniqueDays.size() > 0 ? (totalOut / uniqueDays.size()) : 0;
        if (lblDailyAverage != null) {
            lblDailyAverage.setText(formatRupiah(dailyAvg));
        }
        
        String maxCat = "-";
        double maxAmt = 0;
        for (Map.Entry<String, Double> entry : catMap.entrySet()) {
            if (entry.getValue() > maxAmt) {
                maxAmt = entry.getValue();
                maxCat = entry.getKey();
            }
        }
        if (lblMaxCategory != null) {
            lblMaxCategory.setText(maxCat);
        }
        
        double savingsRatio = totalIn > 0 ? ((totalIn - totalOut) / totalIn * 100) : 0;
        if (lblSavingsRatio != null) {
            lblSavingsRatio.setText(String.format("%.2f %%", savingsRatio));
        }
        
        if (listLaporanDistribution != null) {
            listLaporanDistribution.getChildren().clear();
            for (Map.Entry<String, Double> entry : catMap.entrySet()) {
                double catAmt = entry.getValue();
                double ratio = totalOut > 0 ? (catAmt / totalOut) : 0;
                String pctStr = String.format("%.1f %%", ratio * 100);
                listLaporanDistribution.getChildren().add(
                    createDistributionRow("📁  " + entry.getKey(), formatRupiah(catAmt), pctStr, ratio, Color.web("#f43f5e"))
                );
            }
            if (catMap.isEmpty()) {
                listLaporanDistribution.getChildren().add(new Label("Belum ada pengeluaran terdaftar.") {{ setTextFill(Color.web("#94a3b8")); }});
            }
        }

        // Juga update avatar profil jika di halaman profil
        if (lblAvatarName != null) {
            lblAvatarName.setText(userModel.getProfil().getNama());
        }
        if (lblNimText != null) {
            lblNimText.setText("NIM: " + userModel.getProfil().getNim());
        }
        if (lblLetter != null && !userModel.getProfil().getNama().isEmpty()) {
            lblLetter.setText(userModel.getProfil().getNama().substring(0, 1).toUpperCase());
        }
    }

    private Pengeluaran[] getPengeluarans(Transaksi[] all) {
        if (all == null) return new Pengeluaran[0];
        List<Pengeluaran> res = new ArrayList<>();
        for (Transaksi t : all) {
            if (t instanceof Pengeluaran) {
                res.add((Pengeluaran) t);
            }
        }
        return res.toArray(new Pengeluaran[0]);
    }

    private String formatRupiah(double value) {
        return "Rp " + formatNumber(value);
    }
    
    private String formatNumber(double value) {
        return String.format("%,.0f", value).replace(",", ".");
    }
}
