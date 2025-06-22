/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 *
 * @author Windows
 */
public class NhanSuModel {
    private int maNhanVien;
    private String maSo; 
    private String hoTen; 
    private Date ngaySinh; 
    private String gioiTinh; 
    private String diaChi; 
    private String soDienThoai; 
    private String email; 
    private String trinhDoHocVan; 
    private int maPhongBan;
    private String tenPhongBan;//Sử dụng cho BaoCaoView
    private int maChucVu; 
    private Date ngayVaoLam; 
    private String tinhTrang; 

    public NhanSuModel() {
    }

    public NhanSuModel(int maNhanVien, String maSo, String hoTen, Date ngaySinh, String gioiTinh, String diaChi, String soDienThoai, String email, String trinhDoHocVan, int maPhongBan, int maChucVu, Date ngayVaoLam, String tinhTrang) {
        this.maNhanVien = maNhanVien;
        this.maSo = maSo;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.trinhDoHocVan = trinhDoHocVan;
        this.maPhongBan = maPhongBan;
        this.maChucVu = maChucVu;
        this.ngayVaoLam = ngayVaoLam;
        this.tinhTrang = tinhTrang;
    }
     // Getter và Setter
    public int getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(int maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getMaSo() {
        return maSo;
    }

    public void setMaSo(String maSo) {
        this.maSo = maSo;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTrinhDoHocVan() {
        return trinhDoHocVan;
    }

    public void setTrinhDoHocVan(String trinhDoHocVan) {
        this.trinhDoHocVan = trinhDoHocVan;
    }

    public int getMaPhongBan() {
        return maPhongBan;
    }

    public void setMaPhongBan(int maPhongBan) {
        this.maPhongBan = maPhongBan;
    }

    public int getMaChucVu() {
        return maChucVu;
    }

    public void setMaChucVu(int maChucVu) {
        this.maChucVu = maChucVu;
    }

    public Date getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setNgayVaoLam(Date ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    public String getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public String getTenPhongBan() {
        return tenPhongBan;
    }

    public void setTenPhongBan(String tenPhongBan) {
        this.tenPhongBan = tenPhongBan;
    }

    // Phương thức toString để hiển thị thông tin nhân viên
    @Override
    public String toString() {
        return "NhanSuModel{" +
                "maNhanVien=" + maNhanVien +
                ", maSo='" + maSo + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", ngaySinh=" + ngaySinh +
                ", gioiTinh='" + gioiTinh + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", email='" + email + '\'' +
                ", trinhDoHocVan='" + trinhDoHocVan + '\'' +
                ", maPhongBan=" + maPhongBan +
                ", maChucVu=" + maChucVu +
                ", ngayVaoLam=" + ngayVaoLam +
                ", tinhTrang='" + tinhTrang + '\'' +
                '}';
    }
}

