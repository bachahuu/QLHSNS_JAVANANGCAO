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
}
