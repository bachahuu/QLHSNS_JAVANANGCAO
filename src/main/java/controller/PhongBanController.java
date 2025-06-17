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

import model.PhongBanModel;
import model.PhongBanModel.TrangThaiPhongBan;

/**
 *
 * @author Windows
 */
public class PhongBanController {
    public List<PhongBanModel> getAll(){
        List<PhongBanModel> list = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            Connect mc = new Connect();
            conn = mc.getConnection();
            if(conn !=null){
                System.out.println("kết nối Thành công");
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT * FROM phong_ban");
                
                while(rs.next()){
                   PhongBanModel phongban = new PhongBanModel();
                    phongban.setMaPhongBan(rs.getInt("ma_phong_ban"));
                    phongban.setTenPhongBan(rs.getString("ten_phong_ban"));
                    phongban.setMoTa(rs.getString("mo_ta"));
                    phongban.setSoNhanVien(rs.getInt("so_nhan_vien"));
                    phongban.setNgayThanhLap(rs.getDate("ngay_thanh_lap"));
                    
                    // Xử lý enum
                    String trangThaiStr = rs.getString("trang_thai");
                    TrangThaiPhongBan trangThai = TrangThaiPhongBan.valueOf(trangThaiStr);
                    phongban.setTrangThai(trangThai);

                    phongban.setNgayTao(rs.getTimestamp("ngay_tao"));

                    list.add(phongban);
                }
            }
        } catch (Exception e) {
             e.printStackTrace(); // để dễ debug nếu có lỗi
        }
        return list;
    }
    public List<String> getPhongBanDisplayList() {
    List<String> displayList = new ArrayList<>();
    List<PhongBanModel> phongBans = getAll(); // Tái sử dụng phương thức getAll()

    for (PhongBanModel phongban : phongBans) {
        String display = phongban.getMaPhongBan() + " - " + phongban.getTenPhongBan();
        displayList.add(display);
    }

    return displayList;
    }
    
     // tìm theo họ và tên
    public List<PhongBanModel> searchByTenPhongBan(String tenPhongBan) {
        List<PhongBanModel> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connect mc = new Connect();
            conn = mc.getConnection();
            if (conn != null) {
                String query = "SELECT * FROM phong_ban WHERE ten_phong_ban LIKE ?";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, "%" + tenPhongBan + "%");
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    PhongBanModel phongBan = new PhongBanModel();
                    phongBan.setMaPhongBan(rs.getInt("ma_phong_ban"));
                    phongBan.setTenPhongBan(rs.getString("ten_phong_ban"));
                    phongBan.setMoTa(rs.getString("mo_ta"));
                    phongBan.setSoNhanVien(rs.getInt("so_nhan_vien"));
                    phongBan.setNgayThanhLap(rs.getDate("ngay_thanh_lap"));
                    // Xử lý enum
                    String trangThaiStr = rs.getString("trang_thai");
                    TrangThaiPhongBan trangThai = TrangThaiPhongBan.valueOf(trangThaiStr);
                    phongBan.setTrangThai(trangThai);
                    phongBan.setNgayTao(rs.getTimestamp("ngay_tao"));
                    list.add(phongBan);
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
    public List<PhongBanModel> getByTinhTrang(String selectedStatus) {
        List<PhongBanModel> list = new ArrayList<>();
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
                    query = "SELECT * FROM phong_ban";
                } else {
                    String tinhTrang = selectedStatus.equals("Hoat_dong") ? "Hoat_dong" : "Ngung_hoat_dong";
                    query = "SELECT * FROM phong_ban WHERE trang_thai = '" + tinhTrang + "'";
                }
                rs = stmt.executeQuery(query);

                while (rs.next()) {
                    PhongBanModel phongBan = new PhongBanModel();
                    phongBan.setMaPhongBan(rs.getInt("ma_phong_ban"));
                    phongBan.setTenPhongBan(rs.getString("ten_phong_ban"));
                    phongBan.setMoTa(rs.getString("mo_ta"));
                    phongBan.setSoNhanVien(rs.getInt("so_nhan_vien"));
                    phongBan.setNgayThanhLap(rs.getDate("ngay_thanh_lap"));
                    // Xử lý enum
                    String trangThaiStr = rs.getString("trang_thai");
                    TrangThaiPhongBan trangThai = TrangThaiPhongBan.valueOf(trangThaiStr);
                    phongBan.setTrangThai(trangThai);
                    phongBan.setNgayTao(rs.getTimestamp("ngay_tao"));
                    list.add(phongBan);
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
    
    public boolean insertPhongBan(PhongBanModel phongBan) {
        Connection conn = null;
        Statement stmt = null;
        try {
            Connect mc = new Connect();
            conn = mc.getConnection();
            if (conn != null) {
             // Chuẩn bị các giá trị, xử lý null
            String tenPhongBan = phongBan.getTenPhongBan() != null ? "'" + phongBan.getTenPhongBan() + "'" : "''";
            String moTa = phongBan.getMoTa() != null ? "'" + phongBan.getMoTa() + "'" : "''";
            String soNhanVien = String.valueOf(phongBan.getSoNhanVien());
            String ngayThanhLap = phongBan.getNgayThanhLap() != null ? "'" + phongBan.getNgayThanhLap().toString() + "'" : "NULL";
            String trangThai = phongBan.getTrangThai() != null ? "'" + phongBan.getTrangThai().name() + "'" : "'Hoat_dong'";

                // Câu lệnh SQL nối chuỗi
            // Câu lệnh SQL nối chuỗi
            String sql = "INSERT INTO phong_ban (ten_phong_ban, mo_ta, so_nhan_vien, ngay_thanh_lap, trang_thai) VALUES (" +
                         tenPhongBan + ", " +
                         moTa + ", " +
                         soNhanVien + ", " +
                         ngayThanhLap + ", " +
                         trangThai + ")";

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
    public boolean updatePhongBan(PhongBanModel phongBan) {
        try {
            Connect mc = new Connect();
            Connection conn = mc.getConnection();
            Statement stmt = conn.createStatement();

            String sql = "UPDATE phong_ban SET " +
                        "ten_phong_ban = '" + phongBan.getTenPhongBan() + "', " +
                        "mo_ta = '" + phongBan.getMoTa() + "', " +
                        "so_nhan_vien = '" + phongBan.getSoNhanVien() + "', " +
                        "ngay_thanh_lap = '" + phongBan.getNgayThanhLap() + "', "+
                        "trang_thai = '" + phongBan.getTrangThai() + "', " +
                        "ngay_tao = '" + phongBan.getNgayTao() + "' " +
                        "WHERE ma_phong_ban = " + phongBan.getMaPhongBan();

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
             String sql = "DELETE FROM phong_ban WHERE ma_phong_ban = " + id;
             int result = stmt.executeUpdate(sql);
             if (result > 0) {
                    System.out.println("Xóa thành công Phòng Ban có id = " + id);
                } else {
                    System.out.println("Không tìm thấy Phòng Ban để xóa.");
                }
             conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
