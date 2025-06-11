-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 01, 2025 at 07:21 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `qlhsns`
--

-- --------------------------------------------------------

--
-- Table structure for table `bao_cao`
--

CREATE TABLE `bao_cao` (
  `ma_bao_cao` int(11) NOT NULL,
  `ten_bao_cao` varchar(150) DEFAULT NULL,
  `ngay_tao` date DEFAULT NULL,
  `thoi_gian_bao_cao` varchar(50) DEFAULT NULL,
  `noi_dung` longtext DEFAULT NULL,
  `nguoi_tao` int(11) DEFAULT NULL,
  `loai_bao_cao` enum('Nhan_su','Luong','Nghi_phep','Bien_dong','Khac') DEFAULT 'Khac',
  `ngay_tao_ban_ghi` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bao_cao`
--

INSERT INTO `bao_cao` (`ma_bao_cao`, `ten_bao_cao`, `ngay_tao`, `thoi_gian_bao_cao`, `noi_dung`, `nguoi_tao`, `loai_bao_cao`, `ngay_tao_ban_ghi`) VALUES
(7, 'Báo cáo nhân sự tháng 12/2024', '2024-12-31', 'Tháng 12/2024', 'Tổng số nhân viên: 15 người. Nhân viên mới: 2 người. Nhân viên nghỉ việc: 1 người. Tỷ lệ nghỉ việc: 6.7%', 2, 'Nhan_su', '2025-05-29 07:40:27'),
(8, 'Báo cáo lương tháng 12/2024', '2024-12-31', 'Tháng 12/2024', 'Tổng quỹ lương: 320,000,000 VNĐ. Lương trung bình: 21,333,333 VNĐ. Tăng 5% so với tháng trước.', 2, 'Luong', '2025-05-29 07:40:27'),
(9, 'Báo cáo nghỉ phép Q4/2024', '2024-12-31', 'Quý 4/2024', 'Tổng số ngày nghỉ phép: 45 ngày. Nghỉ ốm đau: 8 ngày. Nghỉ phép năm: 32 ngày. Nghỉ thai sản: 120 ngày.', 97, 'Nghi_phep', '2025-05-29 07:40:27'),
(10, 'Báo cáo biến động nhân sự 2024', '2024-12-31', 'Năm 2024', 'Tuyển mới: 5 người. Thôi việc: 3 người. Thăng chức: 2 người. Chuyển phòng ban: 1 người.', 2, 'Bien_dong', '2025-05-29 07:40:27'),
(11, 'Báo cáo hoạt động phòng Kỹ thuật', '2024-11-30', 'Tháng 11/2024', 'Hoàn thành 12/15 dự án. Tiến độ đạt 80%. Cần bổ sung thêm 2 nhân viên.', 97, 'Khac', '2025-05-29 07:40:27'),
(12, 'Báo cáo đánh giá hiệu suất', '2024-10-31', 'Quý 3/2024', 'Hiệu suất trung bình: 85%. Nhân viên xuất sắc: 3 người. Cần cải thiện: 2 người.', 98, 'Nhan_su', '2025-05-29 07:40:27');

-- --------------------------------------------------------

--
-- Table structure for table `chuc_vu`
--

CREATE TABLE `chuc_vu` (
  `ma_chuc_vu` int(11) NOT NULL,
  `ten_chuc_vu` varchar(100) NOT NULL,
  `mo_ta` varchar(255) DEFAULT NULL,
  `cap_bac` int(11) DEFAULT NULL,
  `luong_co_ban` decimal(15,2) DEFAULT NULL,
  `phu_cap_mac_dinh` decimal(15,2) DEFAULT NULL,
  `quyen_han` varchar(255) DEFAULT NULL,
  `trang_thai` enum('Hoat_dong','Ngung_hoat_dong') DEFAULT 'Hoat_dong',
  `ngay_tao` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `chuc_vu`
--

INSERT INTO `chuc_vu` (`ma_chuc_vu`, `ten_chuc_vu`, `mo_ta`, `cap_bac`, `luong_co_ban`, `phu_cap_mac_dinh`, `quyen_han`, `trang_thai`, `ngay_tao`) VALUES
(1, 'Chủ Tịch hội đồng quản trị', 'chỉ việc ký hợp đồng', 0, 500000000.00, 100000000.00, 'nắm trong tay mọi quyền', 'Hoat_dong', '2025-05-26 14:56:58'),
(2, 'giám đốc marketing', NULL, 1, 1000000.00, 500000.00, 'dưới chủ tịch hội đồng quản trị', 'Hoat_dong', '2025-05-27 08:13:33'),
(3, 'Giám đốc', 'Giám đốc công ty', 1, 50000000.00, 10000000.00, 'Toàn quyền', 'Hoat_dong', '2025-05-29 07:18:04'),
(4, 'Phó giám đốc', 'Phó giám đốc', 2, 35000000.00, 7000000.00, 'Quản lý cấp cao', 'Hoat_dong', '2025-05-29 07:18:04'),
(5, 'Trưởng phòng', 'Trưởng phòng ban', 3, 25000000.00, 5000000.00, 'Quản lý phòng ban', 'Hoat_dong', '2025-05-29 07:18:04'),
(6, 'Phó phòng', 'Phó phòng ban', 4, 20000000.00, 3000000.00, 'Hỗ trợ quản lý', 'Hoat_dong', '2025-05-29 07:18:04'),
(7, 'Nhân viên chính', 'Nhân viên chính thức', 5, 15000000.00, 2000000.00, 'Thực hiện công việc', 'Hoat_dong', '2025-05-29 07:18:04');

-- --------------------------------------------------------

--
-- Table structure for table `hop_dong`
--

CREATE TABLE `hop_dong` (
  `ma_hop_dong` int(11) NOT NULL,
  `ma_nhan_vien` int(11) DEFAULT NULL,
  `loai_hop_dong` enum('Thu_viec','Chinh_thuc','Thoi_vu') DEFAULT NULL,
  `ngay_bat_dau` date DEFAULT NULL,
  `ngay_ket_thuc` date DEFAULT NULL,
  `ngay_ky` date DEFAULT NULL,
  `trang_thai` enum('Con_hieu_luc','Het_hieu_luc') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hop_dong`
--

INSERT INTO `hop_dong` (`ma_hop_dong`, `ma_nhan_vien`, `loai_hop_dong`, `ngay_bat_dau`, `ngay_ket_thuc`, `ngay_ky`, `trang_thai`) VALUES
(16, 2, 'Chinh_thuc', '2020-01-15', NULL, '2020-01-10', 'Con_hieu_luc'),
(17, 97, 'Chinh_thuc', '2020-02-01', NULL, '2020-01-25', 'Con_hieu_luc'),
(18, 98, 'Chinh_thuc', '2020-02-15', NULL, '2020-02-10', 'Con_hieu_luc'),
(19, 99, 'Chinh_thuc', '2020-03-01', NULL, '2020-02-25', 'Con_hieu_luc'),
(20, 100, 'Chinh_thuc', '2020-04-15', NULL, '2020-04-10', 'Con_hieu_luc'),
(23, 103, 'Chinh_thuc', '2020-07-01', NULL, '2020-06-25', 'Con_hieu_luc'),
(24, 104, 'Thoi_vu', '2021-01-15', '2024-01-15', '2021-01-10', 'Con_hieu_luc'),
(26, 106, 'Chinh_thuc', '2021-03-15', NULL, '2021-03-10', 'Con_hieu_luc'),
(27, 107, 'Thoi_vu', '2021-04-01', '2024-04-01', '2021-03-25', 'Con_hieu_luc');

-- --------------------------------------------------------

--
-- Table structure for table `luong`
--

CREATE TABLE `luong` (
  `ma_luong` int(11) NOT NULL,
  `ma_nhan_vien` int(11) DEFAULT NULL,
  `ngay_tinh_luong` date DEFAULT NULL,
  `luong_co_ban` decimal(15,2) DEFAULT NULL,
  `so_ngay_cong` int(11) DEFAULT NULL,
  `so_gio_tang_ca` int(11) DEFAULT NULL,
  `tien_thuong` decimal(15,2) DEFAULT NULL,
  `tong_phu_cap` decimal(15,2) DEFAULT NULL,
  `tong_khau_tru` decimal(15,2) DEFAULT NULL,
  `luong_thuc_nhan` decimal(15,2) DEFAULT NULL,
  `ngay_tao` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `luong`
--

INSERT INTO `luong` (`ma_luong`, `ma_nhan_vien`, `ngay_tinh_luong`, `luong_co_ban`, `so_ngay_cong`, `so_gio_tang_ca`, `tien_thuong`, `tong_phu_cap`, `tong_khau_tru`, `luong_thuc_nhan`, `ngay_tao`) VALUES
(1, 2, '2024-12-31', 50000000.00, 22, 20, 5000000.00, 10000000.00, 8000000.00, 57000000.00, '2025-05-29 07:38:25'),
(2, 97, '2024-12-31', 25000000.00, 22, 15, 2000000.00, 5000000.00, 4000000.00, 28000000.00, '2025-05-29 07:38:25'),
(3, 98, '2024-12-31', 25000000.00, 22, 10, 1500000.00, 5000000.00, 3800000.00, 27700000.00, '2025-05-29 07:38:25'),
(4, 99, '2024-12-31', 25000000.00, 22, 8, 1000000.00, 5000000.00, 3700000.00, 27300000.00, '2025-05-29 07:38:25'),
(5, 100, '2024-12-31', 15000000.00, 22, 12, 800000.00, 2000000.00, 2200000.00, 15600000.00, '2025-05-29 07:38:25'),
(8, 103, '2024-12-31', 12000000.00, 22, 5, 500000.00, 1000000.00, 1800000.00, 11700000.00, '2025-05-29 07:38:25'),
(9, 104, '2024-12-31', 12000000.00, 20, 0, 0.00, 1000000.00, 1700000.00, 11300000.00, '2025-05-29 07:38:25'),
(11, 106, '2024-12-31', 20000000.00, 22, 12, 1200000.00, 3000000.00, 2800000.00, 21400000.00, '2025-05-29 07:38:25'),
(12, 107, '2024-12-31', 12000000.00, 22, 3, 300000.00, 1000000.00, 1700000.00, 11600000.00, '2025-05-29 07:38:25');

-- --------------------------------------------------------

--
-- Table structure for table `nghi_phep`
--

CREATE TABLE `nghi_phep` (
  `ma_nghi_phep` int(11) NOT NULL,
  `ma_nhan_vien` int(11) DEFAULT NULL,
  `loai_nghi` enum('Nghi_phep_nam','Nghi_om_dau','Nghi_khong_luong','Nghi_thai_san','Nghi_le') DEFAULT NULL,
  `ngay_bat_dau` date DEFAULT NULL,
  `ngay_ket_thuc` date DEFAULT NULL,
  `ly_do` varchar(255) DEFAULT NULL,
  `trang_thai` enum('Cho_duyet','Da_duyet','Tu_choi') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `nghi_phep`
--

INSERT INTO `nghi_phep` (`ma_nghi_phep`, `ma_nhan_vien`, `loai_nghi`, `ngay_bat_dau`, `ngay_ket_thuc`, `ly_do`, `trang_thai`) VALUES
(1, 2, 'Nghi_phep_nam', '2024-12-23', '2024-12-27', 'Nghỉ Tết Dương lịch', 'Da_duyet'),
(2, 97, 'Nghi_om_dau', '2024-11-15', '2024-11-16', 'Ốm sốt', 'Da_duyet'),
(3, 98, 'Nghi_phep_nam', '2024-10-01', '2024-10-03', 'Nghỉ lễ Quốc khánh', 'Da_duyet'),
(4, 99, 'Nghi_thai_san', '2024-08-01', '2024-11-30', 'Nghỉ sinh con', 'Da_duyet'),
(5, 100, 'Nghi_phep_nam', '2024-09-02', '2024-09-03', 'Nghỉ lễ Quốc khánh', 'Da_duyet'),
(8, 103, 'Nghi_le', '2024-04-30', '2024-05-01', 'Nghỉ lễ 30/4 - 1/5', 'Da_duyet'),
(9, 104, 'Nghi_khong_luong', '2024-03-10', '2024-03-12', 'Việc gia đình', 'Da_duyet'),
(11, 106, 'Nghi_om_dau', '2025-02-01', '2025-02-02', 'Khám sức khỏe', 'Cho_duyet'),
(12, 107, 'Nghi_phep_nam', '2025-03-08', '2025-03-08', 'Nghỉ lễ 8/3', 'Tu_choi');

-- --------------------------------------------------------

--
-- Table structure for table `nhan_vien`
--

CREATE TABLE `nhan_vien` (
  `ma_nhan_vien` int(11) NOT NULL,
  `ma_so` varchar(20) DEFAULT NULL,
  `ho_ten` varchar(100) DEFAULT NULL,
  `ngay_sinh` date DEFAULT NULL,
  `gioi_tinh` enum('Nam','Nu') DEFAULT NULL,
  `dia_chi` varchar(255) DEFAULT NULL,
  `so_dien_thoai` varchar(11) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `trinh_do_hoc_van` enum('Cao_dang','Dai_hoc','Tot_nghiep_cap_3') DEFAULT NULL,
  `ma_phong_ban` int(11) DEFAULT NULL,
  `ma_chuc_vu` int(11) DEFAULT NULL,
  `ngay_vao_lam` date DEFAULT NULL,
  `tinh_trang` enum('Dang_lam','Da_nghi') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `nhan_vien`
--

INSERT INTO `nhan_vien` (`ma_nhan_vien`, `ma_so`, `ho_ten`, `ngay_sinh`, `gioi_tinh`, `dia_chi`, `so_dien_thoai`, `email`, `trinh_do_hoc_van`, `ma_phong_ban`, `ma_chuc_vu`, `ngay_vao_lam`, `tinh_trang`) VALUES
(2, 'NV002', 'Hà Hữu Nam', '1996-01-01', 'Nam', 'Thái Bình', '0964092903', 'Nam@gmail.com', 'Dai_hoc', 9, 4, '2024-01-05', 'Da_nghi'),
(97, 'NV003', 'Nguyễn Văn An', '1985-03-15', 'Nam', '123 Đường ABC, Hà Nội', '0912345678', 'an.nguyen@company.com', 'Dai_hoc', 1, 1, '2020-01-15', 'Dang_lam'),
(98, 'NV004', 'Trần Thị Bình', '1990-07-20', 'Nu', '456 Đường XYZ, Hà Nội', '0923456789', 'binh.tran@company.com', 'Dai_hoc', 6, 3, '2020-02-01', 'Dang_lam'),
(99, 'NV005', 'Lê Văn Cường', '1988-12-10', 'Nam', '789 Đường DEF, Hà Nội', '0934567890', 'cuong.le@company.com', 'Dai_hoc', 7, 3, '2020-02-15', 'Dang_lam'),
(100, 'NV006', 'Phạm Thị Dung', '1992-05-25', 'Nu', '321 Đường GHI, Hà Nội', '0945678901', 'dung.pham@company.com', 'Dai_hoc', 8, 3, '2020-03-01', 'Dang_lam'),
(103, 'NV009', 'Đỗ Văn Giang', '1991-02-14', 'Nam', '147 Đường PQR, Hà Nội', '0978901234', 'giang.do@company.com', 'Dai_hoc', 7, 5, '2020-06-15', 'Dang_lam'),
(104, 'NV010', 'Ngô Thị Hạnh', '1994-08-22', 'Nu', '258 Đường STU, Hà Nội', '0989012345', 'hanh.ngo@company.com', 'Cao_dang', 1, 6, '2020-07-01', 'Dang_lam'),
(106, 'NV012', 'Đinh Thị Mai', '1992-04-28', 'Nu', '963 Đường DEF, Hà Nội', '0923456780', 'mai.dinh@company.com', 'Cao_dang', 6, 6, '2021-04-01', 'Dang_lam'),
(107, 'NV015', 'Võ Văn Phúc', '1987-01-09', 'Nam', '468 Đường MNO, Hà Nội', '0956789013', 'phuc.vo@company.com', 'Dai_hoc', 10, 4, '2020-08-16', 'Da_nghi'),
(109, 'NV020', 'Hồ Xuân Hương', '1993-01-01', 'Nu', 'Bồng Lai', '01082356487', 'huong@gmail.com', 'Dai_hoc', 1, 1, '2004-10-01', 'Da_nghi');

-- --------------------------------------------------------

--
-- Table structure for table `phong_ban`
--

CREATE TABLE `phong_ban` (
  `ma_phong_ban` int(11) NOT NULL,
  `ten_phong_ban` varchar(100) NOT NULL,
  `mo_ta` varchar(255) DEFAULT NULL,
  `so_nhan_vien` int(11) DEFAULT 0,
  `ngay_thanh_lap` date DEFAULT NULL,
  `trang_thai` enum('Hoat_dong','Ngung_hoat_dong') DEFAULT 'Hoat_dong',
  `ngay_tao` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `phong_ban`
--

INSERT INTO `phong_ban` (`ma_phong_ban`, `ten_phong_ban`, `mo_ta`, `so_nhan_vien`, `ngay_thanh_lap`, `trang_thai`, `ngay_tao`) VALUES
(1, 'Chỉ Tịch', 'Điều hành công ty nghìn tỷ', 10, '2015-04-02', 'Hoat_dong', '2025-05-26 14:55:00'),
(6, 'Phòng Nhân sự', 'Quản lý nhân sự và tuyển dụng', 8, '2020-01-15', 'Hoat_dong', '2025-05-29 07:18:04'),
(7, 'Phòng Kỹ thuật', 'Phát triển và bảo trì hệ thống', 15, '2020-02-01', 'Ngung_hoat_dong', '2025-05-29 07:18:04'),
(8, 'Phòng Kinh doanh', 'Bán hàng và chăm sóc khách hàng', 12, '2020-01-20', 'Hoat_dong', '2025-05-29 07:18:04'),
(9, 'Phòng Kế toán', 'Quản lý tài chính và kế toán', 6, '2020-01-10', 'Hoat_dong', '2025-05-29 07:18:04'),
(10, 'Phòng Marketing', 'Tiếp thị và quảng cáo', 10, '2020-03-01', 'Ngung_hoat_dong', '2025-05-29 07:18:04'),
(11, 'Phòng Hành chính', 'Quản lý hành chính tổng hợp', 5, '2020-01-05', 'Hoat_dong', '2025-05-29 07:18:04');

-- --------------------------------------------------------

--
-- Table structure for table `tai_khoan`
--

CREATE TABLE `tai_khoan` (
  `ma_tai_khoan` int(11) NOT NULL,
  `ma_nhan_vien` int(11) DEFAULT NULL,
  `ten_dang_nhap` varchar(50) DEFAULT NULL,
  `mat_khau` varchar(255) DEFAULT NULL,
  `vai_tro` enum('Quan_tri','Nhan_vien') DEFAULT NULL,
  `trang_thai` enum('Hoat_dong','Bi_khoa') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tai_khoan`
--

INSERT INTO `tai_khoan` (`ma_tai_khoan`, `ma_nhan_vien`, `ten_dang_nhap`, `mat_khau`, `vai_tro`, `trang_thai`) VALUES
(1, 2, 'admin', '$2y$10$abcdefghijklmnopqrstuvwxyz', 'Quan_tri', 'Hoat_dong'),
(2, 97, 'binh.tran', '$2y$10$bcdefghijklmnopqrstuvwxyza', 'Quan_tri', 'Hoat_dong'),
(3, 98, 'cuong.le', '$2y$10$cdefghijklmnopqrstuvwxyzab', 'Quan_tri', 'Hoat_dong'),
(4, 99, 'dung.pham', '$2y$10$defghijklmnopqrstuvwxyzabc', 'Quan_tri', 'Hoat_dong'),
(5, 100, 'em.hoang', '$2y$10$efghijklmnopqrstuvwxyzabcd', 'Nhan_vien', 'Hoat_dong'),
(8, 103, 'hanh.ngo', '$2y$10$hijklmnopqrstuvwxyzabcdefg', 'Nhan_vien', 'Hoat_dong'),
(9, 104, 'inh.bui', '$2y$10$ijklmnopqrstuvwxyzabcdefgh', 'Nhan_vien', 'Hoat_dong'),
(11, 106, 'long.truong', '$2y$10$klmnopqrstuvwxyzabcdefghij', 'Nhan_vien', 'Hoat_dong'),
(12, 107, 'mai.dinh', '$2y$10$lmnopqrstuvwxyzabcdefghijk', 'Nhan_vien', 'Hoat_dong');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bao_cao`
--
ALTER TABLE `bao_cao`
  ADD PRIMARY KEY (`ma_bao_cao`),
  ADD KEY `nguoi_tao` (`nguoi_tao`);

--
-- Indexes for table `chuc_vu`
--
ALTER TABLE `chuc_vu`
  ADD PRIMARY KEY (`ma_chuc_vu`);

--
-- Indexes for table `hop_dong`
--
ALTER TABLE `hop_dong`
  ADD PRIMARY KEY (`ma_hop_dong`),
  ADD KEY `ma_nhan_vien` (`ma_nhan_vien`);

--
-- Indexes for table `luong`
--
ALTER TABLE `luong`
  ADD PRIMARY KEY (`ma_luong`),
  ADD KEY `ma_nhan_vien` (`ma_nhan_vien`);

--
-- Indexes for table `nghi_phep`
--
ALTER TABLE `nghi_phep`
  ADD PRIMARY KEY (`ma_nghi_phep`),
  ADD KEY `ma_nhan_vien` (`ma_nhan_vien`);

--
-- Indexes for table `nhan_vien`
--
ALTER TABLE `nhan_vien`
  ADD PRIMARY KEY (`ma_nhan_vien`),
  ADD UNIQUE KEY `ma_so` (`ma_so`),
  ADD KEY `ma_phong_ban` (`ma_phong_ban`),
  ADD KEY `ma_chuc_vu` (`ma_chuc_vu`);

--
-- Indexes for table `phong_ban`
--
ALTER TABLE `phong_ban`
  ADD PRIMARY KEY (`ma_phong_ban`);

--
-- Indexes for table `tai_khoan`
--
ALTER TABLE `tai_khoan`
  ADD PRIMARY KEY (`ma_tai_khoan`),
  ADD UNIQUE KEY `ten_dang_nhap` (`ten_dang_nhap`),
  ADD KEY `ma_nhan_vien` (`ma_nhan_vien`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bao_cao`
--
ALTER TABLE `bao_cao`
  MODIFY `ma_bao_cao` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `chuc_vu`
--
ALTER TABLE `chuc_vu`
  MODIFY `ma_chuc_vu` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `hop_dong`
--
ALTER TABLE `hop_dong`
  MODIFY `ma_hop_dong` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT for table `luong`
--
ALTER TABLE `luong`
  MODIFY `ma_luong` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `nghi_phep`
--
ALTER TABLE `nghi_phep`
  MODIFY `ma_nghi_phep` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `nhan_vien`
--
ALTER TABLE `nhan_vien`
  MODIFY `ma_nhan_vien` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=110;

--
-- AUTO_INCREMENT for table `phong_ban`
--
ALTER TABLE `phong_ban`
  MODIFY `ma_phong_ban` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `tai_khoan`
--
ALTER TABLE `tai_khoan`
  MODIFY `ma_tai_khoan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bao_cao`
--
ALTER TABLE `bao_cao`
  ADD CONSTRAINT `bao_cao_ibfk_1` FOREIGN KEY (`nguoi_tao`) REFERENCES `nhan_vien` (`ma_nhan_vien`) ON DELETE CASCADE;

--
-- Constraints for table `hop_dong`
--
ALTER TABLE `hop_dong`
  ADD CONSTRAINT `hop_dong_ibfk_1` FOREIGN KEY (`ma_nhan_vien`) REFERENCES `nhan_vien` (`ma_nhan_vien`) ON DELETE CASCADE;

--
-- Constraints for table `luong`
--
ALTER TABLE `luong`
  ADD CONSTRAINT `luong_ibfk_1` FOREIGN KEY (`ma_nhan_vien`) REFERENCES `nhan_vien` (`ma_nhan_vien`) ON DELETE CASCADE;

--
-- Constraints for table `nghi_phep`
--
ALTER TABLE `nghi_phep`
  ADD CONSTRAINT `nghi_phep_ibfk_1` FOREIGN KEY (`ma_nhan_vien`) REFERENCES `nhan_vien` (`ma_nhan_vien`) ON DELETE CASCADE;

--
-- Constraints for table `nhan_vien`
--
ALTER TABLE `nhan_vien`
  ADD CONSTRAINT `nhan_vien_ibfk_1` FOREIGN KEY (`ma_phong_ban`) REFERENCES `phong_ban` (`ma_phong_ban`),
  ADD CONSTRAINT `nhan_vien_ibfk_2` FOREIGN KEY (`ma_chuc_vu`) REFERENCES `chuc_vu` (`ma_chuc_vu`);

--
-- Constraints for table `tai_khoan`
--
ALTER TABLE `tai_khoan`
  ADD CONSTRAINT `tai_khoan_ibfk_1` FOREIGN KEY (`ma_nhan_vien`) REFERENCES `nhan_vien` (`ma_nhan_vien`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
