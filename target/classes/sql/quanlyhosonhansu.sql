-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th5 22, 2025 lúc 02:27 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `quanlyhosonhansu`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `bao_cao`
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

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chuc_vu`
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

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `hop_dong`
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

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `luong`
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

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nghi_phep`
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

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nhan_vien`
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

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `phong_ban`
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

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `tai_khoan`
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
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `bao_cao`
--
ALTER TABLE `bao_cao`
  ADD PRIMARY KEY (`ma_bao_cao`),
  ADD KEY `nguoi_tao` (`nguoi_tao`);

--
-- Chỉ mục cho bảng `chuc_vu`
--
ALTER TABLE `chuc_vu`
  ADD PRIMARY KEY (`ma_chuc_vu`);

--
-- Chỉ mục cho bảng `hop_dong`
--
ALTER TABLE `hop_dong`
  ADD PRIMARY KEY (`ma_hop_dong`),
  ADD KEY `ma_nhan_vien` (`ma_nhan_vien`);

--
-- Chỉ mục cho bảng `luong`
--
ALTER TABLE `luong`
  ADD PRIMARY KEY (`ma_luong`),
  ADD KEY `ma_nhan_vien` (`ma_nhan_vien`);

--
-- Chỉ mục cho bảng `nghi_phep`
--
ALTER TABLE `nghi_phep`
  ADD PRIMARY KEY (`ma_nghi_phep`),
  ADD KEY `ma_nhan_vien` (`ma_nhan_vien`);

--
-- Chỉ mục cho bảng `nhan_vien`
--
ALTER TABLE `nhan_vien`
  ADD PRIMARY KEY (`ma_nhan_vien`),
  ADD UNIQUE KEY `ma_so` (`ma_so`),
  ADD KEY `ma_phong_ban` (`ma_phong_ban`),
  ADD KEY `ma_chuc_vu` (`ma_chuc_vu`);

--
-- Chỉ mục cho bảng `phong_ban`
--
ALTER TABLE `phong_ban`
  ADD PRIMARY KEY (`ma_phong_ban`);

--
-- Chỉ mục cho bảng `tai_khoan`
--
ALTER TABLE `tai_khoan`
  ADD PRIMARY KEY (`ma_tai_khoan`),
  ADD UNIQUE KEY `ten_dang_nhap` (`ten_dang_nhap`),
  ADD KEY `ma_nhan_vien` (`ma_nhan_vien`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `bao_cao`
--
ALTER TABLE `bao_cao`
  MODIFY `ma_bao_cao` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `chuc_vu`
--
ALTER TABLE `chuc_vu`
  MODIFY `ma_chuc_vu` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `hop_dong`
--
ALTER TABLE `hop_dong`
  MODIFY `ma_hop_dong` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `luong`
--
ALTER TABLE `luong`
  MODIFY `ma_luong` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `nghi_phep`
--
ALTER TABLE `nghi_phep`
  MODIFY `ma_nghi_phep` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `nhan_vien`
--
ALTER TABLE `nhan_vien`
  MODIFY `ma_nhan_vien` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `phong_ban`
--
ALTER TABLE `phong_ban`
  MODIFY `ma_phong_ban` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `tai_khoan`
--
ALTER TABLE `tai_khoan`
  MODIFY `ma_tai_khoan` int(11) NOT NULL AUTO_INCREMENT;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `bao_cao`
--
ALTER TABLE `bao_cao`
  ADD CONSTRAINT `bao_cao_ibfk_1` FOREIGN KEY (`nguoi_tao`) REFERENCES `nhan_vien` (`ma_nhan_vien`);

--
-- Các ràng buộc cho bảng `hop_dong`
--
ALTER TABLE `hop_dong`
  ADD CONSTRAINT `hop_dong_ibfk_1` FOREIGN KEY (`ma_nhan_vien`) REFERENCES `nhan_vien` (`ma_nhan_vien`);

--
-- Các ràng buộc cho bảng `luong`
--
ALTER TABLE `luong`
  ADD CONSTRAINT `luong_ibfk_1` FOREIGN KEY (`ma_nhan_vien`) REFERENCES `nhan_vien` (`ma_nhan_vien`);

--
-- Các ràng buộc cho bảng `nghi_phep`
--
ALTER TABLE `nghi_phep`
  ADD CONSTRAINT `nghi_phep_ibfk_1` FOREIGN KEY (`ma_nhan_vien`) REFERENCES `nhan_vien` (`ma_nhan_vien`);

--
-- Các ràng buộc cho bảng `nhan_vien`
--
ALTER TABLE `nhan_vien`
  ADD CONSTRAINT `nhan_vien_ibfk_1` FOREIGN KEY (`ma_phong_ban`) REFERENCES `phong_ban` (`ma_phong_ban`),
  ADD CONSTRAINT `nhan_vien_ibfk_2` FOREIGN KEY (`ma_chuc_vu`) REFERENCES `chuc_vu` (`ma_chuc_vu`);

--
-- Các ràng buộc cho bảng `tai_khoan`
--
ALTER TABLE `tai_khoan`
  ADD CONSTRAINT `tai_khoan_ibfk_1` FOREIGN KEY (`ma_nhan_vien`) REFERENCES `nhan_vien` (`ma_nhan_vien`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
