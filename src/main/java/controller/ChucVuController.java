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
import model.ChucVuModel.TrangThaiChucVu;
import model.Connect;

/**
 *
 * @author Windows
 */
public class ChucVuController {
    public List<ChucVuModel> getAll(){
        List<ChucVuModel> list = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            Connect mc = new Connect();
            conn = mc.getConnection();
            if(conn !=null){
                System.out.println("kết nối Thành công");
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT * FROM chuc_vu");
                
                while(rs.next()){
                    ChucVuModel chucvu = new ChucVuModel();
                    chucvu.setMaChucVu(rs.getInt("ma_chuc_vu"));
                    chucvu.setTenChucVu(rs.getString("ten_chuc_vu"));
                    chucvu.setMoTa(rs.getString("mo_ta"));
                    chucvu.setCapBac(rs.getObject("cap_bac") != null ? rs.getInt("cap_bac") : null);
                    chucvu.setPhuCapMacDinh(rs.getBigDecimal("phu_cap_mac_dinh"));
                    chucvu.setQuyenHan(rs.getString("quyen_han"));
                    
                    // Xử lý enum
                    String trangThaiStr = rs.getString("trang_thai");
                    TrangThaiChucVu trangThai = TrangThaiChucVu.valueOf(trangThaiStr);
                    chucvu.setTrangThai(trangThai);
                    
                    chucvu.setNgayTao(rs.getTimestamp("ngay_tao"));

                    list.add(chucvu);
                }
            }
        } catch (Exception e) {
             e.printStackTrace(); // để dễ debug nếu có lỗi
        }
        return list;
    }
    public List<String> getChucVuDisplayList() {
    List<String> displayList = new ArrayList<>();
    List<ChucVuModel> chucVus = getAll(); // Tái sử dụng phương thức getAll()

    for (ChucVuModel chucVu : chucVus) {
        String display = chucVu.getMaChucVu() + " - " + chucVu.getTenChucVu();
        displayList.add(display);
    }

    return displayList;
}

}
