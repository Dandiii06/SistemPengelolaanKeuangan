package com.rizki.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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

public class HomeView {
    private BorderPane root;
    private String username;
    
    // Sidebar buttons
    private Button btnRingkasan;
    private Button btnTransaksi;
    private Button btnAnggaran;
    private Button btnLaporan;
    private Button btnProfil;
    private Button btnLogout;
    
    // Main dynamic content pane
    private StackPane contentArea;
    
    // Sub-panes
    private VBox paneRingkasan;
    private VBox paneTransaksi;
    private VBox paneAnggaran;
    private VBox paneLaporan;
    private VBox paneProfil;

    public HomeView(String username) {
        this.username = username;
        createView();
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
        Label lblGreeting = new Label("Halo, " + username + "!");
        lblGreeting.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        lblGreeting.setTextFill(Color.WHITE);
        Label lblGreetingDesc = new Label("Berikut adalah ringkasan keuangan pribadi Anda hari ini.");
        lblGreetingDesc.getStyleClass().add("label-subtitle");
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

        VBox cardSaldo = createMetricCard("TOTAL SALDO UTAMA", "Rp 12.500.000", "metric-card-primary", "Status: Aman & Terkendali");
        VBox cardPemasukan = createMetricCard("TOTAL PEMASUKAN", "Rp 15.000.000", "metric-card-success", "+ Rp 3.000.000 bulan ini");
        VBox cardPengeluaran = createMetricCard("TOTAL PENGELUARAN", "Rp 2.500.000", "metric-card-danger", "- Rp 250.000 minggu ini");

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

        VBox listTransactions = new VBox(10);
        listTransactions.getChildren().addAll(
            createTransactionRow("Gaji Bulanan", "Pemasukan", "12 Jun 2026", "Rp 15.000.000", true),
            createTransactionRow("Belanja Bulanan Supermarket", "Konsumsi", "13 Jun 2026", "- Rp 1.500.000", false),
            createTransactionRow("Buku Kuliah DPBO", "Pendidikan", "14 Jun 2026", "- Rp 400.000", false),
            createTransactionRow("Uang Jajan Tambahan", "Pemasukan", "14 Jun 2026", "Rp 500.000", true),
            createTransactionRow("Makan Malam & Kopi", "Konsumsi", "14 Jun 2026", "- Rp 100.000", false)
        );
        recentBox.getChildren().addAll(lblRecentTitle, listTransactions);

        // Right section: Budgets Status
        VBox budgetBox = new VBox(15);
        budgetBox.getStyleClass().add("card");
        budgetBox.setPrefWidth(350);

        Label lblBudgetTitle = new Label("Batas Anggaran");
        lblBudgetTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        lblBudgetTitle.setTextFill(Color.WHITE);

        VBox budgetList = new VBox(15);
        budgetList.getChildren().addAll(
            createBudgetProgressBar("Konsumsi Makanan", 0.65, "Rp 1.600.000 / Rp 2.500.000"),
            createBudgetProgressBar("Kebutuhan Kuliah", 0.40, "Rp 400.000 / Rp 1.000.000"),
            createBudgetProgressBar("Transportasi", 0.85, "Rp 425.000 / Rp 500.000")
        );
        budgetBox.getChildren().addAll(lblBudgetTitle, budgetList);

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

        VBox listMock = new VBox(8);
        listMock.getChildren().addAll(
            createTransactionRow("Gaji Bulanan", "Pemasukan", "12 Jun 2026", "Rp 15.000.000", true),
            createTransactionRow("Belanja Bulanan Supermarket", "Konsumsi", "13 Jun 2026", "- Rp 1.500.000", false),
            createTransactionRow("Buku Kuliah DPBO", "Pendidikan", "14 Jun 2026", "- Rp 400.000", false),
            createTransactionRow("Uang Jajan Tambahan", "Pemasukan", "14 Jun 2026", "Rp 500.000", true),
            createTransactionRow("Makan Malam & Kopi", "Konsumsi", "14 Jun 2026", "- Rp 100.000", false)
        );

        tableCard.getChildren().addAll(lblTableTitle, listMock);
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

        VBox activeList = new VBox(20);
        activeList.getChildren().addAll(
            createBudgetProgressBar("Konsumsi Makanan", 0.64, "Rp 1.600.000 Terpakai dari Batas Rp 2.500.000 (Bulanan)"),
            createBudgetProgressBar("Kebutuhan Kuliah", 0.40, "Rp 400.000 Terpakai dari Batas Rp 1.000.000 (Bulanan)"),
            createBudgetProgressBar("Transportasi", 0.85, "Rp 425.000 Terpakai dari Batas Rp 500.000 (Mingguan)"),
            createBudgetProgressBar("Hiburan & Kopi", 0.10, "Rp 50.000 Terpakai dari Batas Rp 500.000 (Mingguan)")
        );

        budgetCard.getChildren().addAll(lblActiveTitle, activeList);
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
        
        VBox stat1 = createStatCard("Rata-rata Pengeluaran Harian", "Rp 83.333");
        VBox stat2 = createStatCard("Kategori Pengeluaran Terbesar", "Konsumsi Makanan");
        VBox stat3 = createStatCard("Rasio Tabungan Bulan Ini", "83.33 %");

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

        VBox listDistribusi = new VBox(15);
        listDistribusi.getChildren().addAll(
            createDistributionRow("🍔  Konsumsi Makanan", "Rp 1.600.000", "64.0 %", 0.64, Color.web("#f43f5e")),
            createDistributionRow("📚  Kebutuhan Kuliah / Pendidikan", "Rp 400.000", "16.0 %", 0.16, Color.web("#3b82f6")),
            createDistributionRow("🚗  Transportasi", "Rp 425.000", "17.0 %", 0.17, Color.web("#eab308")),
            createDistributionRow("☕  Lainnya / Hiburan", "Rp 75.000", "3.0 %", 0.03, Color.web("#10b981"))
        );

        chartCard.getChildren().addAll(lblBreakdownTitle, listDistribusi);
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
        Label lblLetter = new Label(username.substring(0, 1).toUpperCase());
        lblLetter.setTextFill(Color.WHITE);
        lblLetter.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        StackPane avatarPane = new StackPane(avatarCircle, lblLetter);

        VBox userDetailBox = new VBox(5);
        Label lblName = new Label("Rizki Ramadhan"); // Default Mock
        lblName.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        lblName.setTextFill(Color.WHITE);
        Label lblNimText = new Label("NIM: 22012345"); // Default Mock
        lblNimText.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 14px;");
        userDetailBox.getChildren().addAll(lblName, lblNimText);

        avatarBox.getChildren().addAll(avatarPane, userDetailBox);

        // Details fields
        TextField txtNama = new TextField("Rizki Ramadhan");
        TextField txtNim = new TextField("22012345");
        TextField txtEmail = new TextField("rizki@student.upi.edu");
        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Masukkan password baru jika ingin diubah");

        Button btnUpdate = new Button("Simpan Perubahan");
        btnUpdate.getStyleClass().add("button-primary");
        btnUpdate.setMaxWidth(Double.MAX_VALUE);

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
}
