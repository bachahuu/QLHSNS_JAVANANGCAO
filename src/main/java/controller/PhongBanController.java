/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.ChucVuModel;
import model.Connect;
import model.NhanSuModel;
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
