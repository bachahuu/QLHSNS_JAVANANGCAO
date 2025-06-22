package controller;

import model.LuongModel;
import view_admin.LuongView;

import javax.swing.*;
import java.awt.event.*;
import java.math.BigDecimal;
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

    public LuongController() {
    }
    
   // Lấy tất cả lương của một nhân viên
    public List<LuongModel> getAllByNhanVien(int maNhanVien) {
        List<LuongModel> list = new ArrayList<>();
        Connection localConn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connect mc = new Connect();
            localConn = mc.getConnection(); // Tạo Connection
            if (localConn == null) {
                System.out.println("Kết nối cơ sở dữ liệu thất bại");
                return list;
            }
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
                luong.setLuongThucNhan(rs.getBigDecimal("luong_thuc_nhan"));
                list.add(luong);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (localConn != null) localConn.close(); // Đóng Connection
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
    // Lấy lương cơ bản của nhân viên đang còn hiệu lực
    public BigDecimal getLuongCoBanByNhanVien(int maNhanVien) {
        BigDecimal luongCoBan = BigDecimal.ZERO;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connect mc = new Connect();
            conn = mc.getConnection();
            String sql = "SELECT luong_co_ban FROM hop_dong WHERE ma_nhan_vien = ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, maNhanVien);
            rs = ps.executeQuery();
            if (rs.next()) {
                luongCoBan = rs.getBigDecimal("luong_co_ban");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return luongCoBan;
    }

    private void initController() {
        view.getBtnAdd().addActionListener(e -> insertLuong());
        view.getBtnUpdate().addActionListener(e -> updateLuong());
        view.getBtnDelete().addActionListener(e -> deleteLuong());
        view.getBtnSearch().addActionListener(e -> searchLuong());
        view.getBtnRefresh().addActionListener(e -> loadData());
        view.getTable().getSelectionModel().addListSelectionListener(e -> fillFormFromTable());
    }

    private BigDecimal fetchLuongCoBan(int maNhanVien) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT luong_co_ban FROM hop_dong WHERE ma_nhan_vien = ?");
        ps.setInt(1, maNhanVien);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getBigDecimal("luong_co_ban");
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal calculateLuong(int maNV, int ngayCong, int gioTC, BigDecimal thuong, BigDecimal phuCap, BigDecimal khauTru) throws SQLException {
        BigDecimal luongCoBan = fetchLuongCoBan(maNV);
        BigDecimal luongNgay = luongCoBan.divide(new BigDecimal(26), BigDecimal.ROUND_HALF_UP);
        BigDecimal tienTangCa = new BigDecimal(gioTC * 40000);
        return luongNgay.multiply(new BigDecimal(ngayCong))
                .add(tienTangCa)
                .add(thuong)
                .add(phuCap)
                .subtract(khauTru);
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
            BigDecimal thucNhan = calculateLuong(maNV, ngayCong, gioTC, thuong, phuCap, khauTru);

            PreparedStatement ps = conn.prepareStatement("INSERT INTO luong (ma_nhan_vien, ngay_tinh_luong, so_ngay_cong, so_gio_tang_ca, tien_thuong, tong_phu_cap, tong_khau_tru, luong_thuc_nhan) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, maNV);
            ps.setDate(2, sqlDate);
            ps.setInt(3, ngayCong);
            ps.setInt(4, gioTC);
            ps.setBigDecimal(5, thuong);
            ps.setBigDecimal(6, phuCap);
            ps.setBigDecimal(7, khauTru);
            ps.setBigDecimal(8, thucNhan);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Thêm thành công");
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
            BigDecimal thucNhan = calculateLuong(maNV, ngayCong, gioTC, thuong, phuCap, khauTru);

            PreparedStatement ps = conn.prepareStatement("UPDATE luong SET ma_nhan_vien=?, ngay_tinh_luong=?, so_ngay_cong=?, so_gio_tang_ca=?, tien_thuong=?, tong_phu_cap=?, tong_khau_tru=?, luong_thuc_nhan=? WHERE ma_luong=?");
            ps.setInt(1, maNV);
            ps.setDate(2, sqlDate);
            ps.setInt(3, ngayCong);
            ps.setInt(4, gioTC);
            ps.setBigDecimal(5, thuong);
            ps.setBigDecimal(6, phuCap);
            ps.setBigDecimal(7, khauTru);
            ps.setBigDecimal(8, thucNhan);
            ps.setInt(9, maLuong);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cập nhật thành công");
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
            JOptionPane.showMessageDialog(null, "Xoá thành công");
            loadData();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void searchLuong() {
        try {
            String keyword = view.getSearchKeyword();
            PreparedStatement ps = conn.prepareStatement("SELECT l.ma_luong, n.ho_ten, l.luong_thuc_nhan FROM luong l JOIN nhan_vien n ON l.ma_nhan_vien = n.ma_nhan_vien WHERE n.ho_ten LIKE ?");
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            Vector<String> column = new Vector<>();
            column.add("Mã Lương");
            column.add("Tên Nhân Viên");
            column.add("Lương Thực Nhận");
            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("ma_luong"));
                row.add(rs.getString("ho_ten"));
                row.add(rs.getBigDecimal("luong_thuc_nhan"));
                data.add(row);
            }
            view.loadTable(data, column);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadData() {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT l.ma_luong, n.ho_ten, l.luong_thuc_nhan FROM luong l JOIN nhan_vien n ON l.ma_nhan_vien = n.ma_nhan_vien");
            ResultSet rs = ps.executeQuery();
            Vector<String> column = new Vector<>();
            column.add("Mã Lương");
            column.add("Tên Nhân Viên");
            column.add("Lương Thực Nhận");
            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("ma_luong"));
                row.add(rs.getString("ho_ten"));
                row.add(rs.getBigDecimal("luong_thuc_nhan"));
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
}
