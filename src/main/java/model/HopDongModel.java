/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author LAPTOP
 */
public class HopDongModel {
    public enum LoaiHopDong {
        Chinh_thuc("Chính thức"),
        Thoi_vu("Thời vụ"),
        Thu_viec("Thử việc");

        private final String displayName;

        LoaiHopDong(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public enum TrangThaiHopDong {
        Het_hieu_luc("Hết hiệu lực"),
        Con_hieu_luc("Còn hiệu lực");

        private final String displayName;

        TrangThaiHopDong(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    private int maHopDong;
    private int maNhanVien;
    private String hoten;
    private LoaiHopDong loaiHopDong;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private Date ngayKy;
    private TrangThaiHopDong trangThai;
    private double luongCoBan; // Thêm thuộc tính này

    public HopDongModel(int maHopDong, int maNhanVien,String hoten, LoaiHopDong loaiHopDong, Date ngayBatDau, Date ngayKetThuc, Date ngayKy, TrangThaiHopDong trangThai, double luongCoBan) {
        this.maHopDong = maHopDong;
        this.maNhanVien = maNhanVien;
        this.hoten=hoten;
        this.loaiHopDong = loaiHopDong;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.ngayKy = ngayKy;
        this.trangThai = trangThai;
        this.luongCoBan = luongCoBan; // Khởi tạo thuộc tính
    }

    public int getMaHopDong() {
        return maHopDong;
    }

    public void setMaHopDong(int maHopDong) {
        this.maHopDong = maHopDong;
    }

    public int getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(int maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }


    public LoaiHopDong getLoaiHopDong() {
        return loaiHopDong;
    }

    public void setLoaiHopDong(LoaiHopDong loaiHopDong) {
        this.loaiHopDong = loaiHopDong;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public Date getNgayKy() {
        return ngayKy;
    }

    public void setNgayKy(Date ngayKy) {
        this.ngayKy = ngayKy;
    }

    public TrangThaiHopDong getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiHopDong trangThai) {
        this.trangThai = trangThai;
    }

    public double getLuongCoBan() { // Getter mới
        return luongCoBan;
    }

    public void setLuongCoBan(double luongCoBan) { // Setter mới
        this.luongCoBan = luongCoBan;
    }
}