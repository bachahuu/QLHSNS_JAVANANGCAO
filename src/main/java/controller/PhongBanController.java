/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.ChucVuModel;
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
}
