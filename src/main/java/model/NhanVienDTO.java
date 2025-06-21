/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */
public class NhanVienDTO {
    private int maNhanVien;
    private String hoTen;

    public NhanVienDTO(int maNhanVien, String hoTen) {
        this.maNhanVien = maNhanVien;
        this.hoTen = hoTen;
    }

    public int getMaNhanVien() {
        return maNhanVien;
    }

    public String getHoTen() {
        return hoTen;
    }

    @Override
    public String toString() {
        return hoTen + " (Mã: " + maNhanVien + ")"; // Hiển thị thân thiện trong JComboBox
    }
}
