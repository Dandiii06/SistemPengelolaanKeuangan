-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Waktu pembuatan: 26 Jun 2026 pada 15.30
-- Versi server: 8.0.30
-- Versi PHP: 8.4.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sistem_keuangan`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `budgets`
--

CREATE TABLE `budgets` (
  `id` int NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `kategori` varchar(100) DEFAULT NULL,
  `batas_maksimal` double DEFAULT NULL,
  `periode` varchar(50) DEFAULT NULL,
  `total_terpakai` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data untuk tabel `budgets`
--

INSERT INTO `budgets` (`id`, `username`, `kategori`, `batas_maksimal`, `periode`, `total_terpakai`) VALUES
(1, 'damdam', 'Konsumsi Makanan', 1000000, 'Bulanan', 0),
(2, 'damdam', 'Transportasi', 500000, 'Bulanan', 0),
(3, 'damdam', 'Pendidikan', 350000, 'Bulanan', 0),
(6, 'damdam', 'Hiburan', 1000000, 'Bulanan', 0),
(9, 'edoo', 'Transportasi', 300000, 'Bulanan', 0),
(10, 'edoo', 'Konsumsi Makanan', 1000000, 'Bulanan', 0),
(11, 'edoo', 'Hiburan', 500000, 'Bulanan', 0),
(12, 'ray', 'Transportasi', 500000, 'Bulanan', 0),
(13, 'ray', 'Hiburan', 1000000, 'Bulanan', 0),
(14, 'ray', 'Konsumsi Makanan', 1500000, 'Bulanan', 0);

-- --------------------------------------------------------

--
-- Struktur dari tabel `transactions`
--

CREATE TABLE `transactions` (
  `id` varchar(50) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `jumlah` double DEFAULT NULL,
  `tanggal` varchar(20) DEFAULT NULL,
  `catatan` text,
  `tipe` varchar(20) DEFAULT NULL,
  `detail_source` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data untuk tabel `transactions`
--

INSERT INTO `transactions` (`id`, `username`, `jumlah`, `tanggal`, `catatan`, `tipe`, `detail_source`) VALUES
('TX-1781516802088', 'damdam', 35000, '2026-06-15', 'makan ayam mentai', 'Pengeluaran', 'Konsumsi Makanan'),
('TX-1781517606522', 'damdam', 100000, '2026-06-15', 'di tf mamah', 'Pemasukan', 'Pemasukan Gaji'),
('TX-1781518612982', 'damdam', 9000000, '2026-06-15', 'party bos', 'Pengeluaran', 'Hiburan'),
('TX-1781531017496', 'edoo', 500000, '2026-06-15', 'jalan jalan', 'Pengeluaran', 'Hiburan'),
('TX-1781531087460', 'edoo', 35000, '2026-06-15', 'makan sjabdsj', 'Pengeluaran', 'Konsumsi Makanan'),
('TX-1781531164990', 'Dandii911', 2000000, '2026-06-15', 'di tf mamah', 'Pemasukan', 'Pemasukan Gaji'),
('TX-1781970525425', 'edoo', 900000, '2026-06-20', 'maknn', 'Pengeluaran', 'Konsumsi Makanan'),
('TX-1781970578343', 'edoo', 280000, '2026-06-20', 'gojek ultra premium', 'Pengeluaran', 'Transportasi'),
('TX-1781970765552', 'edoo', 1000000, '2026-06-20', 'di tf', 'Pemasukan', 'Pemasukan Gaji'),
('TX-1782048743994', 'edoo', 100000, '2026-06-21', 'main', 'Pengeluaran', 'Hiburan'),
('TX-1782051137642', 'ray', 100000, '2026-06-21', 'makan ayam', 'Pengeluaran', 'Konsumsi Makanan'),
('TX-1782051170125', 'ray', 1000000, '2026-06-21', 'di tf mamah', 'Pemasukan', 'Pemasukan Gaji');

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `id` int NOT NULL,
  `nama` varchar(100) NOT NULL,
  `nim` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `saldo_awal` double DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`id`, `nama`, `nim`, `email`, `username`, `password`, `saldo_awal`, `created_at`) VALUES
(3, 'Rizki Dandi Saputra', '103022500114', 'saputrarizkidandi@gmail.com', 'Dandii911', 'DandiTel-u6', 2100000, '2026-06-14 10:55:08'),
(4, 'damnn', '12345', 'dummy@gmail.com', 'damdam', '$2a$10$oBsL2yTok3KAVDXBT4Tm.OT77MykCiKvaRyvaoiMiZKBmxS2TQ04y', 1065000, '2026-06-15 09:44:44'),
(6, 'alfredo', '1234567', 'alfredo@gmail.com', 'edoo', '$2a$10$ex0usZmC1F.40EZ4ed9J2OsFa1ZVR0TPCWAwBxM/dqWRYLpDtn9EK', 49185000, '2026-06-15 13:42:34'),
(7, 'Ray Prasetyo', '102801810', 'ray@gmail.com', 'ray', '$2a$10$78DYi2j82AYr7amCG8rJD./CE/WnU.uyOus0Qqxwlven0ehoERBTS', 100900000, '2026-06-21 14:11:13'),
(8, 'moning', '1234565', 'moning@gmail.com', 'mon', '$2a$10$UXV0iQVCb39pLHDvfJ5teOAEHXZl/prau2xkOKCOQe24zcv3bF2rK', 100000000, '2026-06-22 10:37:16');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `budgets`
--
ALTER TABLE `budgets`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nim` (`nim`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `budgets`
--
ALTER TABLE `budgets`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
