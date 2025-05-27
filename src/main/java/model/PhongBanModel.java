/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.sql.Date;
import java.sql.Timestamp;
/**
 *
 * @author Windows
 */
public class PhongBanModel {
    private int maPhongBan;
    private String tenPhongBan;
    private String moTa;
    private int soNhanVien;
    private Date ngayThanhLap;
    private TrangThaiPhongBan trangThai;
    private Timestamp ngayTao;

    public PhongBanModel() {
    }

    public int getMaPhongBan() {
        return maPhongBan;
    }

    public void setMaPhongBan(int maPhongBan) {
        this.maPhongBan = maPhongBan;
    }

    public String getTenPhongBan() {
        return tenPhongBan;
    }

    public void setTenPhongBan(String tenPhongBan) {
        this.tenPhongBan = tenPhongBan;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getSoNhanVien() {
        return soNhanVien;
    }

    public void setSoNhanVien(int soNhanVien) {
        this.soNhanVien = soNhanVien;
    }

    public Date getNgayThanhLap() {
        return ngayThanhLap;
    }

    public void setNgayThanhLap(Date ngayThanhLap) {
        this.ngayThanhLap = ngayThanhLap;
    }

    public TrangThaiPhongBan getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiPhongBan trangThai) {
        this.trangThai = trangThai;
    }

    public Timestamp getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Timestamp ngayTao) {
        this.ngayTao = ngayTao;
    }
    
    public enum TrangThaiPhongBan {
    Hoat_dong,
    Ngung_hoat_dong
}

    @Override
    public String toString() {
        return maPhongBan + " - " + tenPhongBan;
    }
}
