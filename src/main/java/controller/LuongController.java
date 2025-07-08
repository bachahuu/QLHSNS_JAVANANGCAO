package controller;

import model.LuongModel;
import view_admin.LuongView;

import javax.swing.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import model.Connect;

public class LuongController {
    private LuongView view;
    private Connection conn;

    public LuongController(LuongView view, Connection conn) {
        this.view = view;
        this.conn = conn;
        initController();
        loadData();
    }

    public LuongController() {}

    // Tính lương thực nhận từ CSDL
    public BigDecimal calculateLuongThucNhan(int maLuong) {
        BigDecimal result = BigDecimal.ZERO;

        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM luong WHERE ma_luong = ?");
            ps.setInt(1, maLuong);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int maNhanVien = rs.getInt("ma_nhan_vien");
                int soNgayCong = rs.getInt("so_ngay_cong");
                int soGioTangCa = rs.getInt("so_gio_tang_ca");
                BigDecimal tienThuong = rs.getBigDecimal("tien_thuong");
                BigDecimal tongPhuCap = rs.getBigDecimal("tong_phu_cap");
                BigDecimal tongKhauTru = rs.getBigDecimal("tong_khau_tru");

                PreparedStatement ps2 = conn.prepareStatement("SELECT luong_co_ban FROM hop_dong WHERE ma_nhan_vien = ?");
                ps2.setInt(1, maNhanVien);
                ResultSet rs2 = ps2.executeQuery();

                BigDecimal luongCoBan = BigDecimal.ZERO;
                if (rs2.next()) {
                    luongCoBan = rs2.getBigDecimal("luong_co_ban");
                }

                rs2.close();
                ps2.close();

                BigDecimal luongNgay = luongCoBan.divide(new BigDecimal(26), RoundingMode.HALF_UP);
                BigDecimal tienTangCa = new BigDecimal(soGioTangCa * 40000);

                result = luongNgay.multiply(new BigDecimal(soNgayCong))
                        .add(tienTangCa)
                        .add(tienThuong)
                        .add(tongPhuCap)
                        .subtract(tongKhauTru);
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private void initController() {
        view.getBtnAdd().addActionListener(e -> insertLuong());
        view.getBtnUpdate().addActionListener(e -> updateLuong());
        view.getBtnDelete().addActionListener(e -> deleteLuong());
        view.getBtnSearch().addActionListener(e -> searchLuong());
        view.getBtnRefresh().addActionListener(e -> {
            view.clearFormFields();  // xóa trắng form
            loadData();              // load lại bảng
        });
        view.getTable().getSelectionModel().addListSelectionListener(e -> fillFormFromTable());
    }

    private void insertLuong() {
        try {
            int maNV = view.getMaNhanVien();
            java.sql.Date sqlDate = new java.sql.Date(view.getNgayTinhLuong().getTime());
            int ngayCong = view.getSoNgayCong();
            int gioTC = view.getSoGioTangCa();
            BigDecimal thuong = view.getTienThuong();
            BigDecimal phuCap = view.getTongPhuCap();
            BigDecimal khauTru = view.getTongKhauTru();

            // Kiểm tra trùng nhân viên + ngày tính lương
            PreparedStatement check = conn.prepareStatement(
                "SELECT COUNT(*) FROM luong WHERE ma_nhan_vien = ? AND ngay_tinh_luong = ?");
            check.setInt(1, maNV);
            check.setDate(2, sqlDate);
            ResultSet rsCheck = check.executeQuery();

            if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                JOptionPane.showMessageDialog(null, "❌ Nhân viên đã có bảng lương trong ngày này!");
                return;
            }

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO luong (ma_nhan_vien, ngay_tinh_luong, so_ngay_cong, so_gio_tang_ca, tien_thuong, tong_phu_cap, tong_khau_tru) VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, maNV);
            ps.setDate(2, sqlDate);
            ps.setInt(3, ngayCong);
            ps.setInt(4, gioTC);
            ps.setBigDecimal(5, thuong);
            ps.setBigDecimal(6, phuCap);
            ps.setBigDecimal(7, khauTru);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "✅ Thêm thành công");
            loadData();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateLuong() {
        try {
            int maLuong = view.getSelectedLuongId();
            int maNV = view.getMaNhanVien();
            java.sql.Date sqlDate = new java.sql.Date(view.getNgayTinhLuong().getTime());
            int ngayCong = view.getSoNgayCong();
            int gioTC = view.getSoGioTangCa();
            BigDecimal thuong = view.getTienThuong();
            BigDecimal phuCap = view.getTongPhuCap();
            BigDecimal khauTru = view.getTongKhauTru();

            PreparedStatement ps = conn.prepareStatement(
                "UPDATE luong SET ma_nhan_vien=?, ngay_tinh_luong=?, so_ngay_cong=?, so_gio_tang_ca=?, tien_thuong=?, tong_phu_cap=?, tong_khau_tru=? WHERE ma_luong=?");
            ps.setInt(1, maNV);
            ps.setDate(2, sqlDate);
            ps.setInt(3, ngayCong);
            ps.setInt(4, gioTC);
            ps.setBigDecimal(5, thuong);
            ps.setBigDecimal(6, phuCap);
            ps.setBigDecimal(7, khauTru);
            ps.setInt(8, maLuong);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "✅ Cập nhật thành công");
            loadData();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void deleteLuong() {
        try {
            int maLuong = view.getSelectedLuongId();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM luong WHERE ma_luong=?");
            ps.setInt(1, maLuong);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "✅ Xoá thành công");
            loadData();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void searchLuong() {
        try {
            String keyword = view.getSearchKeyword();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT l.ma_luong, n.ho_ten FROM luong l JOIN nhan_vien n ON l.ma_nhan_vien = n.ma_nhan_vien WHERE n.ho_ten LIKE ?");
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            Vector<String> column = new Vector<>();
            column.add("Mã Lương");
            column.add("Tên Nhân Viên");
            column.add("Lương Thực Nhận");

            Vector<Vector<Object>> data = new Vector<>();

            while (rs.next()) {
                int maLuong = rs.getInt("ma_luong");
                String tenNV = rs.getString("ho_ten");
                BigDecimal thucNhan = calculateLuongThucNhan(maLuong);

                Vector<Object> row = new Vector<>();
                row.add(maLuong);
                row.add(tenNV);
                row.add(thucNhan);
                data.add(row);
            }

            view.loadTable(data, column);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadData() {
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT l.ma_luong, n.ho_ten FROM luong l JOIN nhan_vien n ON l.ma_nhan_vien = n.ma_nhan_vien");
            ResultSet rs = ps.executeQuery();

            Vector<String> column = new Vector<>();
            column.add("Mã Lương");
            column.add("Tên Nhân Viên");
            column.add("Lương Thực Nhận");

            Vector<Vector<Object>> data = new Vector<>();

            while (rs.next()) {
                int maLuong = rs.getInt("ma_luong");
                String tenNV = rs.getString("ho_ten");
                BigDecimal thucNhan = calculateLuongThucNhan(maLuong);

                Vector<Object> row = new Vector<>();
                row.add(maLuong);
                row.add(tenNV);
                row.add(thucNhan);
                data.add(row);
            }

            view.loadTable(data, column);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void fillFormFromTable() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) return;

        int maLuong = view.getSelectedLuongId();

        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM luong WHERE ma_luong=?");
            ps.setInt(1, maLuong);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                view.setMaNVField(rs.getInt("ma_nhan_vien"));
                view.setNgayLuongField(rs.getDate("ngay_tinh_luong"));
                view.setNgayCongField(rs.getInt("so_ngay_cong"));
                view.setGioTCField(rs.getInt("so_gio_tang_ca"));
                view.setThuongField(rs.getBigDecimal("tien_thuong"));
                view.setPhuCapField(rs.getBigDecimal("tong_phu_cap"));
                view.setKhauTruField(rs.getBigDecimal("tong_khau_tru"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Nếu cần lấy danh sách lương theo nhân viên
    public List<LuongModel> getAllByNhanVien(int maNhanVien) {
        List<LuongModel> list = new ArrayList<>();
        Connection localConn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connect mc = new Connect();
            localConn = mc.getConnection();
            if (localConn == null) return list;

            String query = "SELECT * FROM luong WHERE ma_nhan_vien = ?";
            pstmt = localConn.prepareStatement(query);
            pstmt.setInt(1, maNhanVien);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                LuongModel luong = new LuongModel();
                luong.setMaLuong(rs.getInt("ma_luong"));
                luong.setMaNhanVien(rs.getInt("ma_nhan_vien"));
                luong.setNgayTinhLuong(rs.getDate("ngay_tinh_luong"));
                luong.setSoNgayCong(rs.getInt("so_ngay_cong"));
                luong.setSoGioTangCa(rs.getInt("so_gio_tang_ca"));
                luong.setTienThuong(rs.getBigDecimal("tien_thuong"));
                luong.setTongPhuCap(rs.getBigDecimal("tong_phu_cap"));
                luong.setTongKhauTru(rs.getBigDecimal("tong_khau_tru"));
                list.add(luong);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (localConn != null) localConn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
