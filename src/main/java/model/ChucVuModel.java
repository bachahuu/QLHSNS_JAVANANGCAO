/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.math.BigDecimal;
import java.sql.Timestamp;
/**
 *
 * @author Windows
 */
public class ChucVuModel {
    private int maChucVu;
    private String tenChucVu;
    private String moTa;
    private Integer capBac;
    private BigDecimal phuCapMacDinh;
    private String quyenHan;
    private TrangThaiChucVu trangThai;
    private Timestamp ngayTao;

    public ChucVuModel() {
    }
    

<<<<<<< HEAD
    public ChucVuModel(int maChucVu, String tenChucVu, String moTa, Integer capBac,
=======
    public ChucVuModel(int maChucVu, String tenChucVu, String moTa, Integer capBac, 
>>>>>>> 34682488408a63a8d3a8ab5ddea0532a579817da
                       BigDecimal phuCapMacDinh, String quyenHan, TrangThaiChucVu trangThai, Timestamp ngayTao) {
        this.maChucVu = maChucVu;
        this.tenChucVu = tenChucVu;
        this.moTa = moTa;
        this.capBac = capBac;
        this.phuCapMacDinh = phuCapMacDinh;
        this.quyenHan = quyenHan;
        this.trangThai = trangThai;
        this.ngayTao = ngayTao;
    }

    // Getter/Setter
    public int getMaChucVu() {
        return maChucVu;
    }

    public void setMaChucVu(int maChucVu) {
        this.maChucVu = maChucVu;
    }

    public String getTenChucVu() {
        return tenChucVu;
    }

    public void setTenChucVu(String tenChucVu) {
        this.tenChucVu = tenChucVu;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public Integer getCapBac() {
        return capBac;
    }

    public void setCapBac(Integer capBac) {
        this.capBac = capBac;
    }
<<<<<<< HEAD

=======
>>>>>>> 34682488408a63a8d3a8ab5ddea0532a579817da
    public BigDecimal getPhuCapMacDinh() {
        return phuCapMacDinh;
    }

    public void setPhuCapMacDinh(BigDecimal phuCapMacDinh) {
        this.phuCapMacDinh = phuCapMacDinh;
    }

    public String getQuyenHan() {
        return quyenHan;
    }

    public void setQuyenHan(String quyenHan) {
        this.quyenHan = quyenHan;
    }

    public TrangThaiChucVu getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiChucVu trangThai) {
        this.trangThai = trangThai;
    }

    public Timestamp getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Timestamp ngayTao) {
        this.ngayTao = ngayTao;
    }
    public enum TrangThaiChucVu {
    Hoat_dong,
    Ngung_hoat_dong
    }
    
}

