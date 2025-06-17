package model;

import java.math.BigDecimal;
import java.util.Date;

public class LuongModel {
    private int maLuong;
    private int maNhanVien;
    private Date ngayTinhLuong;
    private int soNgayCong;
    private int soGioTangCa;
    private BigDecimal tienThuong;
    private BigDecimal tongPhuCap;
    private BigDecimal tongKhauTru;
    private BigDecimal luongThucNhan;

    public int getMaLuong() { return maLuong; }
    public void setMaLuong(int maLuong) { this.maLuong = maLuong; }

    public int getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(int maNhanVien) { this.maNhanVien = maNhanVien; }

    public Date getNgayTinhLuong() { return ngayTinhLuong; }
    public void setNgayTinhLuong(Date ngayTinhLuong) { this.ngayTinhLuong = ngayTinhLuong; }

    public int getSoNgayCong() { return soNgayCong; }
    public void setSoNgayCong(int soNgayCong) { this.soNgayCong = soNgayCong; }

    public int getSoGioTangCa() { return soGioTangCa; }
    public void setSoGioTangCa(int soGioTangCa) { this.soGioTangCa = soGioTangCa; }

    public BigDecimal getTienThuong() { return tienThuong; }
    public void setTienThuong(BigDecimal tienThuong) { this.tienThuong = tienThuong; }

    public BigDecimal getTongPhuCap() { return tongPhuCap; }
    public void setTongPhuCap(BigDecimal tongPhuCap) { this.tongPhuCap = tongPhuCap; }

    public BigDecimal getTongKhauTru() { return tongKhauTru; }
    public void setTongKhauTru(BigDecimal tongKhauTru) { this.tongKhauTru = tongKhauTru; }

    public BigDecimal getLuongThucNhan() { return luongThucNhan; }
    public void setLuongThucNhan(BigDecimal luongThucNhan) { this.luongThucNhan = luongThucNhan; }
}
