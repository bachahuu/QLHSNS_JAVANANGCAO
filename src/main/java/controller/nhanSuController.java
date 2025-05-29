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
import model.Connect;
import model.NhanSuModel;

/**
 *
 * @author Windows
 */
public class nhanSuController {
     public List<NhanSuModel> getAll(){
        List<NhanSuModel> list = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

         try {
            Connect mc = new Connect();
            conn = mc.getConnection();
                  if (conn != null) {
                System.out.println("Kết nối cơ sở dữ liệu thành công");
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT * FROM nhan_vien");

                while (rs.next()) {
                    NhanSuModel nhanSu = new NhanSuModel();
                    nhanSu.setMaNhanVien(rs.getInt("ma_nhan_vien"));
                    nhanSu.setMaSo(rs.getString("ma_so"));
                    nhanSu.setHoTen(rs.getString("ho_ten"));
                    nhanSu.setNgaySinh(rs.getDate("ngay_sinh"));
                    nhanSu.setGioiTinh(rs.getString("gioi_tinh"));
                    nhanSu.setDiaChi(rs.getString("dia_chi"));
                    nhanSu.setSoDienThoai(rs.getString("so_dien_thoai"));
                    nhanSu.setEmail(rs.getString("email"));
                    nhanSu.setTrinhDoHocVan(rs.getString("trinh_do_hoc_van"));
                    nhanSu.setMaPhongBan(rs.getInt("ma_phong_ban"));
                    nhanSu.setMaChucVu(rs.getInt("ma_chuc_vu"));
                    nhanSu.setNgayVaoLam(rs.getDate("ngay_vao_lam"));
                    nhanSu.setTinhTrang(rs.getString("tinh_trang"));
                    list.add(nhanSu);
                }
            } else {
                System.out.println("Kết nối cơ sở dữ liệu thất bại");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
         return list;
    }
     // tìm theo họ và tên
    public List<NhanSuModel> searchByHoTen(String hoTen) {
        List<NhanSuModel> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connect mc = new Connect();
            conn = mc.getConnection();
            if (conn != null) {
                String query = "SELECT * FROM nhan_vien WHERE ho_ten LIKE ?";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, "%" + hoTen + "%");
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    NhanSuModel nhanSu = new NhanSuModel();
                    nhanSu.setMaNhanVien(rs.getInt("ma_nhan_vien"));
                    nhanSu.setMaSo(rs.getString("ma_so"));
                    nhanSu.setHoTen(rs.getString("ho_ten"));
                    nhanSu.setNgaySinh(rs.getDate("ngay_sinh"));
                    nhanSu.setGioiTinh(rs.getString("gioi_tinh"));
                    nhanSu.setDiaChi(rs.getString("dia_chi"));
                    nhanSu.setSoDienThoai(rs.getString("so_dien_thoai"));
                    nhanSu.setEmail(rs.getString("email"));
                    nhanSu.setTrinhDoHocVan(rs.getString("trinh_do_hoc_van"));
                    nhanSu.setMaPhongBan(rs.getInt("ma_phong_ban"));
                    nhanSu.setMaChucVu(rs.getInt("ma_chuc_vu"));
                    nhanSu.setNgayVaoLam(rs.getDate("ngay_vao_lam"));
                    nhanSu.setTinhTrang(rs.getString("tinh_trang"));
                    list.add(nhanSu);
                }
            } else {
                System.out.println("Kết nối cơ sở dữ liệu thất bại");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
    // Phương thức mới để lấy danh sách nhân sự theo trạng thái
    public List<NhanSuModel> getByTinhTrang(String selectedStatus) {
        List<NhanSuModel> list = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Connect mc = new Connect();
            conn = mc.getConnection();
            if (conn != null) {
                stmt = conn.createStatement();
                String query;
                if (selectedStatus.equals("Tất cả trạng thái")) {
                    query = "SELECT * FROM nhan_vien";
                } else {
                    String tinhTrang = selectedStatus.equals("Đang làm việc") ? "Dang_lam" : "Da_nghi";
                    query = "SELECT * FROM nhan_vien WHERE tinh_trang = '" + tinhTrang + "'";
                }
                rs = stmt.executeQuery(query);

                while (rs.next()) {
                    NhanSuModel nhanSu = new NhanSuModel();
                    nhanSu.setMaNhanVien(rs.getInt("ma_nhan_vien"));
                    nhanSu.setMaSo(rs.getString("ma_so"));
                    nhanSu.setHoTen(rs.getString("ho_ten"));
                    nhanSu.setNgaySinh(rs.getDate("ngay_sinh"));
                    nhanSu.setGioiTinh(rs.getString("gioi_tinh"));
                    nhanSu.setDiaChi(rs.getString("dia_chi"));
                    nhanSu.setSoDienThoai(rs.getString("so_dien_thoai"));
                    nhanSu.setEmail(rs.getString("email"));
                    nhanSu.setTrinhDoHocVan(rs.getString("trinh_do_hoc_van"));
                    nhanSu.setMaPhongBan(rs.getInt("ma_phong_ban"));
                    nhanSu.setMaChucVu(rs.getInt("ma_chuc_vu"));
                    nhanSu.setNgayVaoLam(rs.getDate("ngay_vao_lam"));
                    nhanSu.setTinhTrang(rs.getString("tinh_trang"));
                    list.add(nhanSu);
                }
            } else {
                System.out.println("Kết nối cơ sở dữ liệu thất bại");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
     public boolean insertNhanVien(NhanSuModel nhanSu) {
         Connection conn = null;
        Statement stmt = null;
        try {
            Connect mc = new Connect();
            conn = mc.getConnection();
            if (conn != null) {
                // Chuẩn bị các giá trị, xử lý null
                String maSo = nhanSu.getMaSo() != null ? "'" + nhanSu.getMaSo() + "'" : "''";
                String hoTen = nhanSu.getHoTen() != null ? "'" + nhanSu.getHoTen() + "'" : "''";
                String gioiTinh = nhanSu.getGioiTinh() != null ? "'" + nhanSu.getGioiTinh() + "'" : "''";
                String ngaySinh = nhanSu.getNgaySinh() != null ? "'" + nhanSu.getNgaySinh().toString() + "'" : "NULL";
                String diaChi = nhanSu.getDiaChi() != null ? "'" + nhanSu.getDiaChi() + "'" : "''";
                String soDienThoai = nhanSu.getSoDienThoai() != null ? "'" + nhanSu.getSoDienThoai() + "'" : "''";
                String email = nhanSu.getEmail() != null ? "'" + nhanSu.getEmail() + "'" : "''";
                String trinhDoHocVan = nhanSu.getTrinhDoHocVan() != null ? "'" + nhanSu.getTrinhDoHocVan() + "'" : "''";
                String maPhongBan = String.valueOf(nhanSu.getMaPhongBan());
                String maChucVu = String.valueOf(nhanSu.getMaChucVu());
                String ngayVaoLam = nhanSu.getNgayVaoLam() != null ? "'" + nhanSu.getNgayVaoLam().toString() + "'" : "NULL";
                String tinhTrang = nhanSu.getTinhTrang() != null ? "'" + nhanSu.getTinhTrang() + "'" : "'Dang_lam'";

                // Câu lệnh SQL nối chuỗi
                String sql = "INSERT INTO nhan_vien (ma_so, ho_ten, ngay_sinh, gioi_tinh, dia_chi, so_dien_thoai, email, trinh_do_hoc_van, ma_phong_ban, ma_chuc_vu, ngay_vao_lam, tinh_trang) VALUES (" +
                             maSo + ", " +
                             hoTen + ", " +
                             ngaySinh + ", " +
                             gioiTinh + ", " +
                             diaChi + ", " +
                             soDienThoai + ", " +
                             email + ", " +
                             trinhDoHocVan + ", " +
                             maPhongBan + ", " +
                             maChucVu + ", " +
                             ngayVaoLam + ", " +
                             tinhTrang + ")";

                stmt = conn.createStatement();
                int rows = stmt.executeUpdate(sql);
                
                // Đóng kết nối và statement
                stmt.close();
                conn.close();
                
                return rows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
     public boolean updateNhanVien(NhanSuModel nhanSu) {
        try {
            Connect mc = new Connect();
            Connection conn = mc.getConnection();
            Statement stmt = conn.createStatement();

            String sql = "UPDATE nhan_vien SET " +
                        "ho_ten = '" + nhanSu.getHoTen() + "', " +
                        "ngay_sinh = '" + nhanSu.getNgaySinh() + "', " +
                        "gioi_tinh = '" + nhanSu.getGioiTinh() + "', " +
                        "dia_chi = '" + nhanSu.getDiaChi() + "', " +
                        "so_dien_thoai = '" + nhanSu.getSoDienThoai() + "', " +
                        "email = '" + nhanSu.getEmail() + "', " +
                        "trinh_do_hoc_van = '" + nhanSu.getTrinhDoHocVan() + "', " +
                        "ma_phong_ban = " + nhanSu.getMaPhongBan() + ", " +
                        "ma_chuc_vu = " + nhanSu.getMaChucVu() + ", " + 
                        "ngay_vao_lam = '" + nhanSu.getNgayVaoLam() + "', " +
                        "tinh_trang = '" + nhanSu.getTinhTrang() + "' " +
                        "WHERE ma_nhan_vien = " + nhanSu.getMaNhanVien();

            int result = stmt.executeUpdate(sql);
            conn.close();

            return result > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
     
    }
     public void delete(int id) {
        try {
           Connect mc = new Connect();
           Connection conn = mc.getConnection();
           Statement stmt = conn.createStatement();
             String sql = "DELETE FROM nhan_vien WHERE ma_nhan_vien = " + id;
             int result = stmt.executeUpdate(sql);
             if (result > 0) {
                    System.out.println("Xóa thành công user có id = " + id);
                } else {
                    System.out.println("Không tìm thấy user để xóa.");
                }
             conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
