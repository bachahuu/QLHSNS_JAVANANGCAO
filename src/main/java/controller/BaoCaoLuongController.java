/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.LuongModel;
import model.Connect;
import java.math.BigDecimal;

/**
 *
 * @author Admin
 */
public class BaoCaoLuongController {
    public List<LuongModel> getAll() {
        List<LuongModel> luongList = new ArrayList<>();
        String query = "SELECT l.*, nv.ho_ten FROM luong l LEFT JOIN nhan_vien nv ON l.ma_nhan_vien = nv.ma_nhan_vien";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Connect mc = new Connect();
            conn = mc.getConnection();
            if (conn == null) {
                System.out.println("Không thể kết nối tới cơ sở dữ liệu");
                return luongList;
            }

            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                LuongModel luong = new LuongModel();
                luong.setMaLuong(rs.getInt("ma_luong"));
                luong.setMaNhanVien(rs.getInt("ma_nhan_vien"));
                luong.setHoTen(rs.getString("ho_ten") != null ? rs.getString("ho_ten") : "Không có");
                luong.setNgayTinhLuong(rs.getDate("ngay_tinh_luong"));
                luong.setSoNgayCong(rs.getInt("so_ngay_cong"));
                luong.setSoGioTangCa(rs.getInt("so_gio_tang_ca"));
                luong.setTienThuong(rs.getBigDecimal("tien_thuong") != null ? rs.getBigDecimal("tien_thuong") : BigDecimal.ZERO);
                luong.setTongPhuCap(rs.getBigDecimal("tong_phu_cap") != null ? rs.getBigDecimal("tong_phu_cap") : BigDecimal.ZERO);
                luong.setTongKhauTru(rs.getBigDecimal("tong_khau_tru") != null ? rs.getBigDecimal("tong_khau_tru") : BigDecimal.ZERO);
                luong.setLuongThucNhan(rs.getBigDecimal("luong_thuc_nhan") != null ? rs.getBigDecimal("luong_thuc_nhan") : BigDecimal.ZERO);
                luongList.add(luong);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi lấy danh sách lương: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return luongList;
    }

    public List<LuongModel> searchByMaNhanVien(String searchText) {
        List<LuongModel> luongList = new ArrayList<>();
        String query = "SELECT l.*, nv.ho_ten FROM luong l LEFT JOIN nhan_vien nv ON l.ma_nhan_vien = nv.ma_nhan_vien WHERE l.ma_nhan_vien LIKE ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connect mc = new Connect();
            conn = mc.getConnection();
            if (conn == null) {
                System.out.println("Không thể kết nối tới cơ sở dữ liệu");
                return luongList;
            }

            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, "%" + searchText + "%");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                LuongModel luong = new LuongModel();
                luong.setMaLuong(rs.getInt("ma_luong"));
                luong.setMaNhanVien(rs.getInt("ma_nhan_vien"));
                luong.setHoTen(rs.getString("ho_ten") != null ? rs.getString("ho_ten") : "Không có");
                luong.setNgayTinhLuong(rs.getDate("ngay_tinh_luong"));
                luong.setSoNgayCong(rs.getInt("so_ngay_cong"));
                luong.setSoGioTangCa(rs.getInt("so_gio_tang_ca"));
                luong.setTienThuong(rs.getBigDecimal("tien_thuong") != null ? rs.getBigDecimal("tien_thuong") : BigDecimal.ZERO);
                luong.setTongPhuCap(rs.getBigDecimal("tong_phu_cap") != null ? rs.getBigDecimal("tong_phu_cap") : BigDecimal.ZERO);
                luong.setTongKhauTru(rs.getBigDecimal("tong_khau_tru") != null ? rs.getBigDecimal("tong_khau_tru") : BigDecimal.ZERO);
                luong.setLuongThucNhan(rs.getBigDecimal("luong_thuc_nhan") != null ? rs.getBigDecimal("luong_thuc_nhan") : BigDecimal.ZERO);
                luongList.add(luong);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi tìm kiếm lương theo mã nhân viên: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return luongList;
    }

    public List<LuongModel> getByMonthYear(int month, int year) {
        List<LuongModel> luongList = new ArrayList<>();
        String query = "SELECT l.*, nv.ho_ten FROM luong l LEFT JOIN nhan_vien nv ON l.ma_nhan_vien = nv.ma_nhan_vien WHERE (? = -1 OR MONTH(l.ngay_tinh_luong) = ?) AND (? = -1 OR YEAR(l.ngay_tinh_luong) = ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connect mc = new Connect();
            conn = mc.getConnection();
            if (conn == null) {
                System.out.println("Không thể kết nối tới cơ sở dữ liệu");
                return luongList;
            }

            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, month);
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);
            pstmt.setInt(4, year);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                LuongModel luong = new LuongModel();
                luong.setMaLuong(rs.getInt("ma_luong"));
                luong.setMaNhanVien(rs.getInt("ma_nhan_vien"));
                luong.setHoTen(rs.getString("ho_ten") != null ? rs.getString("ho_ten") : "Không có");
                luong.setNgayTinhLuong(rs.getDate("ngay_tinh_luong"));
                luong.setSoNgayCong(rs.getInt("so_ngay_cong"));
                luong.setSoGioTangCa(rs.getInt("so_gio_tang_ca"));
                luong.setTienThuong(rs.getBigDecimal("tien_thuong") != null ? rs.getBigDecimal("tien_thuong") : BigDecimal.ZERO);
                luong.setTongPhuCap(rs.getBigDecimal("tong_phu_cap") != null ? rs.getBigDecimal("tong_phu_cap") : BigDecimal.ZERO);
                luong.setTongKhauTru(rs.getBigDecimal("tong_khau_tru") != null ? rs.getBigDecimal("tong_khau_tru") : BigDecimal.ZERO);
                luong.setLuongThucNhan(rs.getBigDecimal("luong_thuc_nhan") != null ? rs.getBigDecimal("luong_thuc_nhan") : BigDecimal.ZERO);
                luongList.add(luong);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi lấy lương theo tháng/năm: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return luongList;
    }
}