/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;
import java.util.Date;
/**
 *
 * @author Windows
 */
public class ContractModel {
    private int maHopDong;
    private Integer maNhanVien;
    private LoaiHopDong loaiHopDong;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private Date ngayKy;
    private TrangThaiHopDong trangThai;
    private BigDecimal luongCoBan;

    public ContractModel() {
    }
    public ContractModel(int maHopDong, Integer maNhanVien, LoaiHopDong loaiHopDong, Date ngayBatDau, Date ngayKetThuc, Date ngayKy, TrangThaiHopDong trangThai, BigDecimal luongCoBan) {
        this.maHopDong = maHopDong;
        this.maNhanVien = maNhanVien;
        this.loaiHopDong = loaiHopDong;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.ngayKy = ngayKy;
        this.trangThai = trangThai;
        this.luongCoBan = luongCoBan;
    }
    public enum LoaiHopDong {
        Thu_viec,
        Chinh_thuc,
        Thoi_vu
    }
    public enum TrangThaiHopDong {
        Con_hieu_luc,
        Het_hieu_luc
    }

    public int getMaHopDong() {
        return maHopDong;
    }

    public Integer getMaNhanVien() {
        return maNhanVien;
    }

    public LoaiHopDong getLoaiHopDong() {
        return loaiHopDong;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public Date getNgayKy() {
        return ngayKy;
    }

    public TrangThaiHopDong getTrangThai() {
        return trangThai;
    }

    public BigDecimal getLuongCoBan() {
        return luongCoBan;
    }

    public void setMaHopDong(int maHopDong) {
        this.maHopDong = maHopDong;
    }

    public void setMaNhanVien(Integer maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public void setLoaiHopDong(LoaiHopDong loaiHopDong) {
        this.loaiHopDong = loaiHopDong;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public void setNgayKy(Date ngayKy) {
        this.ngayKy = ngayKy;
    }

    public void setTrangThai(TrangThaiHopDong trangThai) {
        this.trangThai = trangThai;
    }

    public void setLuongCoBan(BigDecimal luongCoBan) {
        this.luongCoBan = luongCoBan;
    }
    
    
}
