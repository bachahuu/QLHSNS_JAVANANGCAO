package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.ChucVuModel;
import model.ChucVuModel.TrangThaiChucVu;
import model.Connect;
import java.math.BigDecimal;

public class ChucVuController {

    public List<ChucVuModel> getAll() {
        List<ChucVuModel> list = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Connect mc = new Connect();
            conn = mc.getConnection();
            if (conn != null) {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT * FROM chuc_vu ORDER BY ma_chuc_vu");

                while (rs.next()) {
                    ChucVuModel chucvu = mapResultSetToChucVu(rs);
                    list.add(chucvu);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ChucVuModel findById(int maChucVu) {
        ChucVuModel cv = null;
        String sql = "SELECT * FROM chuc_vu WHERE ma_chuc_vu = ?";
        try (Connection conn = new Connect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maChucVu);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cv = mapResultSetToChucVu(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cv;
    }

    public List<ChucVuModel> searchByName(String keyword) {
        List<ChucVuModel> list = new ArrayList<>();
        String sql = "SELECT * FROM chuc_vu WHERE ten_chuc_vu LIKE ? ORDER BY ma_chuc_vu";
        try (Connection conn = new Connect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChucVuModel chucvu = mapResultSetToChucVu(rs);
                list.add(chucvu);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(ChucVuModel cv) {
        String sql = "INSERT INTO chuc_vu (ten_chuc_vu, mo_ta, cap_bac, phu_cap_mac_dinh, quyen_han, trang_thai) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = new Connect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, cv.getTenChucVu());
                ps.setString(2, cv.getMoTa());
                if (cv.getCapBac() == null) {
                    ps.setNull(3, java.sql.Types.INTEGER);
                } else {
                    ps.setInt(3, cv.getCapBac());
                }
                ps.setBigDecimal(4, cv.getPhuCapMacDinh());
                ps.setString(5, cv.getQuyenHan());
                ps.setString(6, cv.getTrangThai().name());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(ChucVuModel cv) {
        String sql = "UPDATE chuc_vu SET ten_chuc_vu=?, mo_ta=?, cap_bac=?, phu_cap_mac_dinh=?, quyen_han=?, trang_thai=? WHERE ma_chuc_vu=?";
        try (Connection conn = new Connect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cv.getTenChucVu());
            ps.setString(2, cv.getMoTa());
            if (cv.getCapBac() == null) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, cv.getCapBac());
            }
            ps.setBigDecimal(4, cv.getPhuCapMacDinh());
            ps.setString(5, cv.getQuyenHan());
            ps.setString(6, cv.getTrangThai().name());
            ps.setInt(7, cv.getMaChucVu());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int maChucVu) {
        String sql = "DELETE FROM chuc_vu WHERE ma_chuc_vu = ?";
        try (Connection conn = new Connect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maChucVu);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getChucVuDisplayList() {
        List<String> displayList = new ArrayList<>();
        List<ChucVuModel> chucVus = getAll();

        for (ChucVuModel chucVu : chucVus) {
            String display = chucVu.getMaChucVu() + " - " + chucVu.getTenChucVu();
            displayList.add(display);
        }

        return displayList;
    }

    // Hàm map ResultSet sang ChucVuModel để tránh viết lại nhiều lần
    private ChucVuModel mapResultSetToChucVu(ResultSet rs) throws Exception {
        ChucVuModel chucvu = new ChucVuModel();
        chucvu.setMaChucVu(rs.getInt("ma_chuc_vu"));
        chucvu.setTenChucVu(rs.getString("ten_chuc_vu"));
        chucvu.setMoTa(rs.getString("mo_ta"));
        chucvu.setCapBac(rs.getObject("cap_bac") != null ? rs.getInt("cap_bac") : null);
        chucvu.setPhuCapMacDinh(rs.getBigDecimal("phu_cap_mac_dinh"));
        chucvu.setQuyenHan(rs.getString("quyen_han"));

        String trangThaiStr = rs.getString("trang_thai");
        TrangThaiChucVu trangThai = TrangThaiChucVu.valueOf(trangThaiStr);
        chucvu.setTrangThai(trangThai);

        chucvu.setNgayTao(rs.getTimestamp("ngay_tao"));

        return chucvu;
    }
}
