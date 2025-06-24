/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

/**
 *
 * @author LAPTOP
 */
public class NghiPhepModel {
    private int maNghiPhep;
    private int maNhanVien;
    private String loaiNghi;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String lyDo;
    private String trangThai;
    private String hoten;

    public NghiPhepModel(int maNghiPhep, int maNhanVien, String loaiNghi, Date ngayBatDau, Date ngayKetThuc, String lyDo, String trangThai, String hoten) {
        this.maNghiPhep = maNghiPhep;
        this.maNhanVien = maNhanVien;
        this.loaiNghi = loaiNghi;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.lyDo = lyDo;
        this.trangThai = trangThai;
        this.hoten = hoten;
    }

    public int getMaNghiPhep() {
        return maNghiPhep;
    }

    public void setMaNghiPhep(int maNghiPhep) {
        this.maNghiPhep = maNghiPhep;
    }

    public int getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(int maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getLoaiNghi() {
        return loaiNghi;
    }

    public void setLoaiNghi(String loaiNghi) {
        this.loaiNghi = loaiNghi;
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

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }
    
    
}
